/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.evoKalc.daemon.server.DBConnector;
import org.evome.evoKalc.daemon.server.DatabaseManager;
import org.evome.evoKalc.daemon.server.SysConfig;
import org.evome.evoKalc.daemon.shared.Shared;
import org.evome.evoKalc.daemon.shared.Task;

/**
 *
 * @author nekoko
 */
public class EvoKalcDaemon {


    /**
     * @param args the command line arguments
     */

    private static SysConfig sysconf = Shared.sysconf;
    private static DBConnector conn = Shared.dbconn();
    
    private static int retryCount = 0;
    public static UUID uuid;
    
    private static Thread watchDogDaemon;
    
    public static void main(String[] args) {
        if (isSingleInstance()) {
            Shared.daemonUUID = UUID.randomUUID();
            Shared.starttime = new Date();
            System.out.println("KaKsCalcDaemon started at " + Shared.starttime.toString());
            Queue.resetBrokenTasks();
            startWatchDogDaemon();
            startQueueDaemon();
        } else {
            System.err.println("Another instance of the app is running, now exit...");
            System.exit(1);
        }
    }
    
    public static void startQueueDaemon(){
        //keep refreshing DB every intervals
        while(true){
            //sleep 
            try{
                Thread.sleep(sysconf.DAEMON_INTERVAL);
            }catch (InterruptedException ex){
                //caught sig kill while sleeping, trying to continue
                if (retryCount++ > 10){
                    System.err.println("Main thread has been interrupted for over 10 times, now exit...");
                    System.exit(-1);
                }
            }
            
            //auto submit tasks with status TASK_READY to queue
            ArrayList<Task> tasks = Queue.getTaskByStatus(Task.Status.READY);
            for (Iterator<Task> task = tasks.iterator(); task.hasNext();){
                if (Queue.canSubmitNext()){
                    Queue.submit(task.next());
                }
            }
            //start queued jobs
            tasks = Queue.getTaskByStatus(Task.Status.QUEUE);
            for (Iterator<Task> task = tasks.iterator(); task.hasNext();){
                if (Queue.canStartNext()){
                    Queue.start(task.next());
                }
            }
            //do client actions
            //stop
            tasks = Queue.getTaskByStatus(Task.Status.STOP);
            for (Iterator<Task> task = tasks.iterator(); task.hasNext();){
                Queue.doClientAction(task.next());
            }            

        }
    }
    
    private static void startWatchDogDaemon(){
        watchDogDaemon = new Thread(new Runnable(){
            @Override
            public void run(){
                while(true){
                    Wrapper.trackExtProcsMemPeak();
                    Wrapper.trackTaskStatus();
                    try{
                        Thread.sleep(100);
                    }catch(InterruptedException ex){
                    }                    

                }
            }
        });
        watchDogDaemon.start();
    }
    
    private static boolean isSingleInstance(){
        //check if multi instance running
        try{
            FileLock lock = new FileOutputStream(sysconf.evomeConfigPath + File.separator + "applock").getChannel().tryLock();
            if (lock != null){
                return true;
            }
        }catch(Exception ex){
            Logger.getLogger(EvoKalcDaemon.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("WARNING: Unable to create/access app_lock under " + sysconf.evomeConfigPath + ", please check the filesystem permissions, now exit.");
        }
        return false;
    }
    

    
}
