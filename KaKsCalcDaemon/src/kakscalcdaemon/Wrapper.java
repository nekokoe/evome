/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nekoko
 * 
 * Wrapper starts the command in Task instance, and waits for the return code
 */
public class Wrapper{
    private String cmdline;
    private int pid;
    private boolean wantPID, isInitialized;
    private Task dbtask;
    
    //database managers
    private static TaskManager taskman = new TaskManager();
    
    //thread container
    public static HashMap<Integer, Thread> threads = new HashMap<>();
    public static HashMap<Integer, Calculation> calculations = new HashMap<>(); 

    public static void wrap(Task task){
        Calculation calculation = new Calculation();
        calculation.init(task);
        Thread thread = new Thread(calculation);
        calculations.put(task.getId(), calculation);
        threads.put(task.getId(), thread);        
        thread.start();
    }
    
    public static void stop(Task task){
        threads.get(task.getId()).interrupt();
        calculations.get(task.getId()).finish();
    }
    
    @Override
    public void run(){
        Process process = null;
        if (!this.isInitialized) {
            System.err.println("Wrapper started without being init(), do noting");
        }else{
            //if (this.wantPID){
            // to be implemented...    
            //}
            try{
                process = Runtime.getRuntime().exec(this.cmdline);
                //update task status in db
                taskman.updateStatus(dbtask, Task.TASK_RUNNING);
                taskman.updateStartDate(dbtask);
                //wait to exit
                process.waitFor();
                int exitcode = process.exitValue();
                //call finish
                int fin_status = taskman.finish(dbtask,exitcode);
                taskman.updateStatus(dbtask, fin_status);
                //remove thread from wrapper;
                Wrapper.threads.remove(dbtask.getId());
            }catch(IOException ex){
                Logger.getLogger(Wrapper.class.getName()).log(Level.SEVERE, null, ex);                     
            }catch(InterruptedException ex){
                Logger.getLogger(Wrapper.class.getName()).log(Level.WARNING, null, ex);     
                //recieve this = thread is killed by user request
                process.destroy();
                taskman.updateStatus(dbtask, Task.TASK_STOPPED);
                //remove thread from wrapper;
                Wrapper.threads.remove(dbtask.getId());                
            }
        }        
    }
}
