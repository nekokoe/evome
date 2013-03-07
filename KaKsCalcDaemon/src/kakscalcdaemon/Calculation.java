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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;

public class Calculation implements Runnable{
    //the workflow of the KaKs Calculation is :
    //1.align two seqs using muscle
    //2.extract alignments to axt format
    //3.export axt to KaKsCalculator

    private static SysConfig sysconf = new SysConfig();
    private static TaskManager taskman = new TaskManager();
    
    private Task task;
    
    public void init(Task task) {
        
    }
    
    @Override
    public void run(){
        
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

        //copy data from datadir to workdir
        //should be moved to outside?
        if (!taskman.copyTaskFile(task,
                TaskManager.OPS_COPY,
                TaskManager.FROM_DATA_TO_WORK,
                inputfile, inputfile)) {
            System.err.println(this.getClass().getName() + ": error while copying file from datadir to workdir");
            return false;
        }

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
            //process stopped, do clean
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
            //process stopped, do clean
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
