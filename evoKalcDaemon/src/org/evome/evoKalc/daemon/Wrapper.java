/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon;

import java.io.IOException;
import java.lang.reflect.Field;
import org.evome.evoKalc.daemon.utils.KaKsCalc;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.evome.evoKalc.daemon.shared.*;
import org.evome.evoKalc.daemon.server.*;

/**
 *
 * @author nekoko
 * 
 * 
 */
public class Wrapper{

    //thread container
    private static HashMap<String, Thread> threads = new HashMap<>();
    private static HashMap<String, KaKsCalc> calcs = new HashMap<>();
    
    
    public static void wrap(Task task){
        KaKsCalc kaks = new KaKsCalc(task);
        Thread thread = new Thread(kaks);
        thread.start();
        threads.put(task.getUUID(), thread);
        calcs.put(task.getUUID(), kaks);
    }
    
    public static void stop(Task task){
        threads.get(task.getUUID()).interrupt();
        while (threads.get(task.getUUID()).isAlive()){
            //wait until interrupted
            try{
                Thread.sleep(100);
            }catch(InterruptedException ex){
                
            }
        }
    }
    
    public static void remove(Task task){
        //to remove, stop first
        stop(task);
        threads.remove(task.getUUID());
        calcs.remove(task.getUUID());
    }
    
    public static void postFinish(Task t){
        //process post finish oprs
        //remove task from wrapper
        threads.remove(t.getUUID());
        calcs.remove(t.getUUID());
        //directly pass task status to db
        t.setFinishDate(new Date());
        DatabaseManager.editTask(t);    //maybe not good to operate it directly?        
    }
    
    public static boolean contains(Task t){
        return threads.containsKey(t.getUUID());
    }
    
    public static int count(){
        return threads.size();
    }
    
    public static int cpuUsed(){
        return threads.size();
    }
    
    public static int memUsed(){
        int mem_used = 0;
        for (Iterator<String> it = calcs.keySet().iterator(); it.hasNext(); ){
            mem_used += calcs.get(it.next()).getMemoryPeak();
        }
        return mem_used;
    }
    
    //to monitor mem peak usage
    public static void trackExtProcsMemPeak() {
        for (Iterator<String> it = calcs.keySet().iterator(); it.hasNext();) {
            String uuid = it.next();
            if (calcs.get(uuid).getCurrentExtProc() != null) {
                calcs.get(uuid).setMemoryPeak(getMemoryUse(getProcessPID(calcs.get(uuid).getCurrentExtProc())));
            }
        }
    }
    //to monitor task result
    public static void trackTaskStatus(){
        for (Iterator<String> it = threads.keySet().iterator(); it.hasNext();){
            String uuid = it.next();
            if (!threads.get(uuid).isAlive()){
                //threads is dead for some reasons, check if success and update status
                Task t = DatabaseManager.getTask(uuid);                
                if (calcs.get(uuid).hasError()){
                    Queue.updateTaskStatus(t, Task.Status.ERROR);
                }else if(calcs.get(uuid).isSuccess()){
                    Queue.updateTaskStatus(t, Task.Status.SUCCESS);
                }else{
                    Queue.updateTaskStatus(t, Task.Status.FAIL);                    
                }
                //call postFinish to clean up and set the finish date
                postFinish(t);
            }
        }
    }
    
    
    public static int getMemoryUse(int pid){
        //WARN: this only works on linux, and make sure you have permissions to execute pmap on the PID
        //use pmap to read the total mem usage
        int mem_used = 0;
        try{
            Process p = Runtime.getRuntime().exec("pmap " + pid);
            Scanner s = new Scanner(p.getInputStream());
            Pattern pattern = Pattern.compile("total\\s+(\\d+)K");
            while(s.hasNextLine()){
                String str = s.nextLine();
                Matcher m = pattern.matcher(str);
                if (m.find()){
                    mem_used = Integer.parseInt(m.group(1));
                }                
            }
        }catch (IOException ex) {
            //Logger.getLogger(Wrapper.class.getName()).log(Level.SEVERE, null, ex);
            //pmap not found, ignore
        }
        return mem_used;
    }
    
    public static int getProcessPID(Process process) {
        int pid = 0;
        if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
            /* get the PID on unix/linux systems */
            try {
                Field f = process.getClass().getDeclaredField("pid");
                f.setAccessible(true);
                pid = f.getInt(process);
            } catch (Throwable ex) {
                Logger.getLogger(Wrapper.class.getName()).log(Level.SEVERE, null, ex);                
            }
        }
        return pid;
    }    
}
