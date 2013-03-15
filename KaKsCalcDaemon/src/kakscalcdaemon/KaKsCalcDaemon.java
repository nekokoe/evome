/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

import java.io.FileOutputStream;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 *
 * @author nekoko
 */
public class KaKsCalcDaemon {

    /**
     * @param args the command line arguments
     */

    private static SysConfig sysconf = new SysConfig();
    private static int retryCount = 0;
    
    public static void main(String[] args) {
        // TODO code application logic here

        Date starttime = new Date();
        System.out.println("KaKsCalcDaemon started at " + starttime.toString());
        if (isSingleInstance()) {
            Queue.resetBrokenJobs();
            startDaemon();
        } else {
            System.err.println("Another instance of the app is running, now exit...");
            System.exit(1);
        }
    }
    
    public static void startDaemon(){
        //keep refreshing DB every intervals
        while(true){
            //sleep 
            try{
                Thread.sleep(sysconf.DAEMON_INTERVAL);
            }catch (InterruptedException ex){
                //caught sig kill while sleeping, trying to continue
                if (retryCount++ > 10){
                    System.exit(-1);
                }
            }
            
            //auto submit tasks with status TASK_READY to queue
            ArrayList<Task> tasks_ready = TaskManager.getAll(Task.TASK_READY);
            for (Iterator<Task> task = tasks_ready.iterator(); task.hasNext();){
                if (Queue.canSubmitNext()){
                    Queue.submit(task.next());
                }
            }
            //start queued jobs
            ArrayList<Job> jobs_queue = Queue.getAll(Job.JOB_QUEUE);
            for (Iterator<Job> job = jobs_queue.iterator(); job.hasNext();){
                if (Queue.canStartNext()){
                    Queue.start(job.next());
                }
            }
            //stop killed jobs
            Queue.stopKilled();
            //remove finished jobs
            Queue.removeFinished();
        }
    }
    
    private static boolean isSingleInstance(){
        //check if multi instance running
        try{
            FileLock lock = new FileOutputStream(sysconf.DAEMON_CLASS_PATH + "/app_lock").getChannel().tryLock();
            if (lock == null){
                return false;
            }
        }catch(Exception ex){
            System.err.println("WARNING: Unable to create/access app_lock under " + sysconf.DAEMON_CLASS_PATH + ", will continue allowing 2 or more instance, but at high risk...");
        }
        return true;
    }
}
