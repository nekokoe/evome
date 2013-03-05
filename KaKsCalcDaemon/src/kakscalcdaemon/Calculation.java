/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

/**
 *
 * @author nekoko
 */
import java.util.HashMap;


public class Calculation {
    //the workflow of the KaKs Calculation is :
    //1.align two seqs using muscle
    //2.extract alignments to axt format
    //3.export axt to KaKsCalculator
    private static SysConfig sysconf = new SysConfig();
    private static TaskManager taskman = new TaskManager();
    
    
    public void start(){
        
    }
    
    public boolean doMuscleAlignment(Task task, HashMap<String, String> filemapper){
        //task indicates working path
        //filemapper is designed for non-default input filename
        String muscle = sysconf.DAEMON_CLASS_PATH + "/" + "bin" + "/" + "muscle";
        String datadir = taskman.getSubDir(sysconf.DATA_ROOT_PATH, task);
        String workdir = taskman.getSubDir(sysconf.WORK_ROOT_PATH, task);
        String inputfile = filemapper.get("input");
        String outputfile = filemapper.get("output");
        String logfile = filemapper.get("stdout");
        String errfile = filemapper.get("stderr");
        
        //copy data from datadir to workdir
        if (!taskman.copyTaskFile(task, 
                TaskManager.OPS_COPY, 
                TaskManager.FROM_DATA_TO_WORK, 
                inputfile, inputfile)){
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
                + " 2> " + errfile;
        
        //execute
    }
    
    public boolean convertMuscleToAxt(Task task, HashMap<String, String> filemapper){
        
    }
    
    public boolean doKaKsCalculation(Task task, HashMap<String, String> filmemapper){
        
    }
    
}
