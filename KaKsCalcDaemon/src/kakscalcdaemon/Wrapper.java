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
public class Wrapper implements Runnable{
    private String cmdline;
    private int pid;
    private boolean wantPID, isInitialized;
    private Task dbtask;
    
    //database managers
    private static TaskManager taskman = new TaskManager();
    
    //thread container
    private static HashMap<Integer, Thread> threads = new HashMap<>();

    public Wrapper() {
        this.isInitialized = false;
        this.wantPID = false;
        this.pid = 0;
    }
    
    public void init(Task task, boolean wantPID) {
        this.wantPID = wantPID;
        dbtask = taskman.get(task);
        String cmd = taskman.toCommand(dbtask);
        //append stout, stderr redirect
        cmd = cmd + " 1>" + resman.get(task.getTaskOutput()).getPath();
        cmd = cmd + " 2>" + resman.get(task.getTaskError()).getPath();
        this.cmdline = (wantPID) ? this.replaceQuote(cmd) : cmd;        
    }
    
    private String replaceQuote(String str){
        return str.replaceAll("\"", "\\\"");
    }
    
    public void wrap(){
        Thread thread = new Thread(this);
        Wrapper.threads.put(dbtask.getId(), thread);
        thread.start();
    }
    
    public static void stop(Task task){
        Wrapper.threads.get(task.getId()).interrupt();
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
