/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon.utils;

/**
 *
 * @author nekoko
 */
import org.evome.evoKalc.daemon.shared.*;
import org.evome.evoKalc.daemon.server.ResourceManager;
import org.evome.evoKalc.daemon.server.SysConfig;
import org.evome.evoKalc.daemon.server.DatabaseManager;
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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.template.AbstractSequence;
import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.FastaWriterHelper;

public class KaKsCalc implements Runnable{
    //the workflow of the KaKs Calculation is :
    //1.align two seqs using muscle
    //2.extract alignments to axt format
    //3.export axt to KaKsCalculator

    private static SysConfig sysconf = new SysConfig();
    
    private Task mytask;
    private String workDir;
    private boolean isSuccess = false;
    private boolean hasError = false;
    private int memPeak = 0;
    private Process currentExtProc;
    
    
    private String binBase = sysconf.evomeConfigPath + File.separator + ".." + File.separator + "bin";
    private String muscleExecutable = "";
    private String kaksExecutable = "";
   
    public KaKsCalc(Task task){
        mytask =  task;
        workDir = ResourceManager.getWorkPath(task);
        
        if (System.getProperty("os.name").startsWith("Linux")){
            muscleExecutable = binBase + File.separator + "muscle";
            kaksExecutable = binBase + File.separator + "KaKs_Calculator";
        }else if (System.getProperty("os.name").startsWith("Windows")){
            muscleExecutable = binBase + File.separator + "muscle_win32.exe";
            kaksExecutable = binBase + File.separator + "KaKs_Calculator.exe";            
        }
    }
    
    public int getMemoryPeak(){
        return this.memPeak;        
    }
    public void setMemoryPeak(int memuse){
        memPeak = (memuse > memPeak) ? memuse : memPeak;
    }
    
    public Process getCurrentExtProc(){
        return currentExtProc ;
    }
    
    @Override
    public void run(){

        try {
            //get Sequence Pair
            ArrayList<Pair> pairs = this.readPairList(mytask);
            //do muscle alignment on each pair
            File axtfile = new File(workDir + File.separator +  mytask.getUUID() + ".axt");
            BufferedWriter axtwriter = new BufferedWriter(new FileWriter(axtfile));
            for (Iterator<Pair> it = pairs.iterator(); it.hasNext();){
                Pair pair = it.next();
                File input = new File(workDir + File.separator + pair.getName() + ".fasta");
                try{
                    //write pair to file
                    BufferedWriter writer = new BufferedWriter(new FileWriter(input));
                    writer.write(Shared.toFasta(pair.getA()));
                    writer.write(Shared.toFasta(pair.getB()));
                    writer.flush();
                    writer.close();
                    //do muscle alignment
                    if (!this.doMuscleAlignment(pair)){
                        throw new Exception("muscle alignment failed on task " + mytask.getUUID() + ", gene pair " + pair.getName());
                    }
                    //convert muscle to axt
                    if (!this.convertMuscleToAxt(pair)){
                        throw new Exception("muscle 2 axt conversion failed on task " + mytask.getUUID() + ", gene pair " + pair.getName());                        
                    }
                    //combine this axt to total axtfile
                    File pairaxt = new File(workDir + File.separator + pair.getName() + ".axt");
                    BufferedReader reader = new BufferedReader(new FileReader(pairaxt));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        axtwriter.write(line + System.lineSeparator());
                    }
                    reader.close();                    
                }catch(Exception ex){
                    throw ex;
                }                
            }
            axtwriter.close();

            //do kaks calc
            if (!this.doKaKsCalculation(mytask)){
                throw new Exception("error running kaks calculation on task " + mytask);     
            }
            
            //Seems everything is OK? check result if OK
            this.isSuccess = checkResult();
            
        }catch(Exception ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            this.hasError = true;
        }
    }
    
    public boolean checkResult(){
        //to do validation on results
        
        /*
         to be implemented, always return true for the momoment
         */
        
        return true;
    }
    
    public boolean isSuccess(){
        return this.isSuccess;
    }
    
    public boolean hasError(){
        return this.hasError;
    }
    
    
    
    private ArrayList<Pair> readPairList(Task task){
        Resource pairlist = task.getKaKsConfig().getPairlist();
        ArrayList<Pair> pairs = new ArrayList<Pair>();
        Path file = Paths.get(ResourceManager.getResourcePath(sysconf.DATA_ROOT_PATH, pairlist));
        try{
            Scanner s = new Scanner(new FileInputStream(file.toFile()));
            while (s.hasNextLine()){
                Pair p = new Pair();
                String[] line = s.nextLine().split("\\t");
                p.setGroup(line[0]);
                p.setName(line[1]);
                p.setA(ResourceManager.getSequence(line[2], line[3]));
                p.setB(ResourceManager.getSequence(line[4], line[5]));
                pairs.add(p);
            }
        }catch(IOException ex){
            System.err.println("Fail to read and parse pairlist : " + pairlist.getUUID());
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);            
        }
        return pairs;
    }
    
    
    
    
    private boolean doMuscleAlignment(Pair pair) {
        //build cmdline
        File input = new File(workDir + File.separator + pair.getName() + ".fasta");
        String cmdline = muscleExecutable
                + " -in " + workDir + File.separator + input.getName()
                + " -out " + workDir + File.separator + pair.getName() + ".muscle.out"
                + " -maxiters 100"
                + " -maxhours 1"
                + " -log " + workDir + File.separator + pair.getName() + ".muscle.log";
                //+ " "
                //+ " 2> " + workDir + File.separator + pair.getName() + ".muscle.err";

        //execute
        Process process = null;
        try {
            System.out.println("exec : " + cmdline);
            process = Runtime.getRuntime().exec(cmdline);
            this.currentExtProc = process;
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int ret = process.waitFor();
            System.out.println("exit code : " + ret);
            return (ret == 0) ? true : false;       //...muscle return 1 when success />_</
        } catch (InterruptedException ex) {
            //thread interrupted, destory process
            process.destroy();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean convertMuscleToAxt(Pair pair) {
        //filemapper is designed for non-default input filename
        //read muscle result and write to axt
        //demo of axt:
        //NP_000006.1
        //ATGGACATTGAAGCATATTTTGAAAGAATT
        //ATGGACATCGAAGCATACTTTGAAAGGATT
        //
        LinkedHashMap<String, ? extends AbstractSequence> muscleResult = new LinkedHashMap<>();       
        File input = new File(workDir + File.separator + pair.getName() + ".muscle.out");
        try {
            if (ResourceManager.isFastaDNA(input)){
                muscleResult = FastaReaderHelper.readFastaDNASequence(input);
            }
            if (ResourceManager.isFastaProtein(input)){
                muscleResult = FastaReaderHelper.readFastaProteinSequence(input);
            }            
            File output = new File(workDir + File.separator + pair.getName() + ".axt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));
            bw.write(pair.getName() + "\n");
            for (Iterator<String> id = muscleResult.keySet().iterator(); id.hasNext();) {
                bw.write(muscleResult.get(id.next()).getSequenceAsString() + "\n");
            }
            bw.write("\n");
            bw.flush();
            bw.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private boolean doKaKsCalculation(Task task) {
        int kaks_code = task.getKaKsConfig().getKaKsGeneticCode().ordinal();
        String kaks_method = task.getKaKsConfig().methods2String().replaceAll(",", " -m ");

        //build command line
        String cmdline = kaksExecutable
                + " -i " + workDir + File.separator +  mytask.getUUID() + ".axt"
                + " -o " + workDir + File.separator +  mytask.getUUID() + ".kaks.out"
                + " -c " + kaks_code
                + " -m " + kaks_method;
                //+ " 1> " + workDir + File.separator +  mytask.getUUID() + ".kaks.log"
                //+ " 2> " + workDir + File.separator +  mytask.getUUID() + ".kaks.err";
        //execute
        Process process = null;
        try {
            System.out.println("exec : " + cmdline);
            process = Runtime.getRuntime().exec(cmdline);
            this.currentExtProc = process;
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        try {
            int ret = process.waitFor();
            System.out.println("exit code : " + ret);            
            return (ret == 0) ? true : false;
        } catch (InterruptedException ex) {
            //thread interrupted, destory process
            process.destroy();
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}
