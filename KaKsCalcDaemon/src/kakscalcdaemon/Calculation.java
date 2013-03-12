/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

/**
 *
 * @author nekoko
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.FastaWriterHelper;

public class Calculation implements Runnable{
    //the workflow of the KaKs Calculation is :
    //1.align two seqs using muscle
    //2.extract alignments to axt format
    //3.export axt to KaKsCalculator

    private static SysConfig sysconf = new SysConfig();
    private static TaskManager taskman = new TaskManager();
    
    private Task task;
    private boolean isInitialized = false;
    private boolean isSuccess = false;
    
    public void init(Task t) {
        ArrayList<String> filelist = new ArrayList<>();
        filelist.add("sequence.fasta");
        filelist.add("pair.list");
        this.init(t, filelist);
    }
    
    public void init(Task t, ArrayList<String> filelist) {
        //t is task to be executed
        //filelist is file need to be copy from datadir to workdir
        //set task
        this.task = taskman.get(t);
        //prepare workdir
        if (!taskman.initWorkDir(task)){
            System.err.println("Failed init workdir for task:" + task.getId());
            return;
        }
        //copy data from datadir to workdir
        for (Iterator<String> file = filelist.iterator(); file.hasNext();){
            String filename = file.next();
            if (!taskman.copyTaskFile(task,TaskManager.OPS_COPY,TaskManager.FROM_DATA_TO_WORK,
                    filename, filename)) {
                System.err.println(this.getClass().getName() + ": error while copying file from datadir to workdir");
                return;
            }
        }
        this.isInitialized = true;
    }
    
    @Override
    public void run(){
        if (!this.isInitialized){
            System.err.println("Unable to run task which is not initialized, do nothing...");
            return;
        }
        try {
            HashMap<String, String> mapper = new HashMap<>();
            String workdir = taskman.getSubDir(sysconf.WORK_ROOT_PATH, this.task);
            //update status
            taskman.updateTaskStauts(task, Task.TASK_RUNNING);
            taskman.updateJobStatus(task, Job.JOB_RUN);
            //get DNApair
            mapper.clear();
            mapper.put("sequence", "sequence.fasta");
            mapper.put("pairlist", "pair.list");
            LinkedHashMap<String, DNAPair> dnaPair = splitDNAPair(this.task, mapper);
            //do muscle alignment on each pair
            ArrayList<String> axtlist = new ArrayList<>();
            for (Iterator<String> pair = dnaPair.keySet().iterator(); pair.hasNext();){
                String pairname = pair.next();
                try{
                    FastaWriterHelper.writeNucleotideSequence(new File(workdir + pairname + ".fasta"), dnaPair.get(pairname).all());
                    //do muscle alignment
                    mapper.clear();
                    mapper.put("input", pairname + ".fasta");
                    mapper.put("output", pairname + ".muscle");
                    mapper.put("stdout", pairname + ".log");
                    mapper.put("stderr", pairname + ".stderr");
                    if (!this.doMuscleAlignment(this.task, mapper)){
                        throw new Exception("muscle alignment failed on task " + this.task.getId() + ", gene pair " + pairname);
                    }
                    //convert muscle to axt
                    mapper.clear();
                    mapper.put("pairname", pairname);
                    mapper.put("muscle", pairname + ".muscle");
                    mapper.put("axt", pairname + ".axt");
                    if (!this.convertMuscleToAxt(this.task, mapper)){
                        throw new Exception("muscle 2 axt conversion failed on task " + this.task.getId() + ", gene pair " + pairname);                        
                    }
                }catch(Exception ex){
                    throw ex;
                }
                axtlist.add(pairname + ".axt");
            }
            //cat axt files
            if (!this.catAxtFiles(this.task, axtlist, task.getId() + ".kaks.in")){
                throw new Exception("can't cat axt files to kaks on task " + this.task.getId());
            }
            //do kaks calc
            mapper.clear();
            mapper.put("input", task.getId() + ".kaks.in");
            mapper.put("output", task.getId() + ".kaks.out");
            mapper.put("stdout", task.getId() + ".kaks.stdout");
            mapper.put("stderr", task.getId() + ".kaks.stderr");
            if (!this.doKaKsCalculation(task, mapper)){
                throw new Exception("error running kaks calculation on task " + this.task.getId());     
            }
            //seems to be ok, mark it success
            this.isSuccess = true;
            this.finish();
        }catch(Exception ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);            
            this.isSuccess = false;
            this.finish();
        }
    }
    
    public void finish(){
        //do cleaning after calculation
        if (this.isSuccess){
            //
            2
        }else{
            
        }
    }
    
    private LinkedHashMap<String, DNAPair> splitDNAPair(Task task, HashMap<String, String> mapper){
        LinkedHashMap<String, DNAPair> dnaPair = new LinkedHashMap<>();
        String workdir = taskman.getSubDir(sysconf.WORK_ROOT_PATH, task);
        String seqfile = mapper.get("sequnece");
        String pairlist = mapper.get("pairlist");
        //read DNA sequence
        LinkedHashMap<String, DNASequence> sequence = null;
        try{
            sequence = FastaReaderHelper.readFastaDNASequence(new File(workdir + "/" + new File(seqfile).getName()));
        }catch(Exception ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            System.err.println("faile to read sequence file: " + seqfile + "for task " + task.getId());
        }
        //read parlist and put pair
        try{
            Scanner scanner = new Scanner(new FileInputStream(workdir + "/" + new File(pairlist).getName()));
            while (scanner.hasNextLine()){
                String[] line = scanner.nextLine().split("\\t");
                String pairname = line[0];
                String pair1 = line[1];
                String pair2 = line[2];
                if (sequence.containsKey(pair1) && sequence.containsKey(pair2)){
                    pairname = pairname.replaceAll("[^\\w]", "_"); //only \w is allowed for pairname
                    DNAPair pair = new DNAPair(pairname, sequence.get(pair1), sequence.get(pair2));
                    dnaPair.put(pairname, pair);
                }
            }
        }catch(Exception ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            System.err.println("faile to read pair list: " + pairlist + "for task " + task.getId());            
        }
        return dnaPair;
    }
    
    public boolean doMuscleAlignment(Task task, HashMap<String, String> mapper) {
        //task indicates working path
        //filemapper is designed for non-default input filename
        String muscle = sysconf.DAEMON_CLASS_PATH + "/" + "bin" + "/" + "muscle";
        String datadir = taskman.getSubDir(sysconf.DATA_ROOT_PATH, task);
        String workdir = taskman.getSubDir(sysconf.WORK_ROOT_PATH, task);
        String inputfile = mapper.get("input");
        String outputfile = mapper.get("output");
        String logfile = mapper.get("stdout");
        String errfile = mapper.get("stderr");

        //build cmdline
        String cmdline = muscle
                + " -in " + workdir + "/" + inputfile
                + " -out " + workdir + "/" + outputfile
                + " -maxiters 100"
                + " -maxhours 1"
                + " -log " + workdir + "/" + logfile
                + " -quite"
                + " 2> " + workdir + "/" + errfile;

        //execute
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmdline);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int ret = process.waitFor();
            return (ret == 0) ? true : false;
        } catch (InterruptedException ex) {
            //thread interrupted, destory process
            process.destroy();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean convertMuscleToAxt(Task task, HashMap<String, String> mapper) {
        //filemapper is designed for non-default input filename
        String workdir = taskman.getSubDir(sysconf.WORK_ROOT_PATH, task);
        String muscle = mapper.get("muscle");
        String axt = mapper.get("axt");

        String pairname = mapper.get("pairname");
        //read muscle result and write to axt
        //demo of axt:
        //NP_000006.1
        //ATGGACATTGAAGCATATTTTGAAAGAATT
        //ATGGACATCGAAGCATACTTTGAAAGGATT
        //
        try {
            LinkedHashMap<String, DNASequence> muscleResult =
                    FastaReaderHelper.readFastaDNASequence(new File(workdir + "/" + new File(muscle).getName()));
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(workdir + "/" + new File(axt).getName())));
            bw.write(pairname + "\\n");
            for (Iterator<String> id = muscleResult.keySet().iterator(); id.hasNext();) {
                DNASequence seq = muscleResult.get(id.next());
                bw.write(seq.getSequenceAsString() + "\\n");
            }
            bw.write("\\n");
            bw.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean catAxtFiles(Task task, ArrayList<String> axtlist, String filename) {
        String workdir = taskman.getSubDir(sysconf.WORK_ROOT_PATH, task);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(workdir + "/" + new File(filename).getName())));
            for (Iterator<String> axt = axtlist.iterator(); axt.hasNext();) {
                BufferedReader br = new BufferedReader(new FileReader(new File(workdir + "/" + new File(axt.next()).getName())));
                String line;
                while ((line = br.readLine()) != null) {
                    bw.write(line);
                }
                br.close();
            }
            bw.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean doKaKsCalculation(Task task, HashMap<String, String> mapper) {
        //filemapper is designed for non-default input filename
        String kakscalc = sysconf.DAEMON_CLASS_PATH + "/" + "bin" + "/" + "KaKs_Calculator";
        String workdir = taskman.getSubDir(sysconf.WORK_ROOT_PATH, task);
        String inputfile = mapper.get("input");
        String outputfile = mapper.get("output");
        String logfile = mapper.get("stdout");
        String errfile = mapper.get("stderr");
        int kaks_code = task.getKaKsGeneticCode();
        String kaks_method = task.getKaKsMethod().replaceAll(",", " -m ");

        //build command line
        String cmdline = kakscalc
                + " -i " + workdir + "/" + new File(inputfile).getName()
                + " -o " + workdir + "/" + new File(outputfile).getName()
                + " -c " + kaks_code
                + " -m " + kaks_method
                + " 1> " + workdir + "/" + new File(logfile).getName()
                + " 2> " + workdir + "/" + new File(errfile).getName();
        //execute
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmdline);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int ret = process.waitFor();
            return (ret == 0) ? true : false;
        } catch (InterruptedException ex) {
            //thread interrupted, destory process
            process.destroy();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private class DNAPair {

        private String name;
        private DNASequence a, b;

        public DNAPair(){}
        
        public DNAPair(String name, DNASequence a, DNASequence b){
            this.name = name;
            this.a = a;
            this.b = b;
        }
        
        public void name(String name) {
            this.name = name;
        }

        public void a(DNASequence a) {
            this.a = a;
        }

        public void b(DNASequence b) {
            this.b = b;
        }

        public String name() {
            return this.name;
        }

        public DNASequence a() {
            return a;
        }

        public DNASequence b() {
            return b;
        }
        
        public ArrayList<DNASequence> all(){
            ArrayList<DNASequence> all = new ArrayList<>();
            all.add(a);
            all.add(b);
            return all;
        }
    }   
}
