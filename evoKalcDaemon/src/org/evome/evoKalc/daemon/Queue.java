/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon;

import org.evome.evoKalc.daemon.shared.*;
import org.evome.evoKalc.daemon.server.SysConfig;
import org.evome.evoKalc.daemon.server.DBConnector;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import org.evome.evoKalc.daemon.server.AccountManager;
import org.evome.evoKalc.daemon.server.DatabaseManager;


/**
 *  Queue manages jobs in queue
 *  Queue belongs to daemon instance
 * @author nekoko
 */
public class Queue {
    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();
    
    private static DBConnector conn(){
        return Shared.dbconn();
    }

    
    public static ArrayList<Task> getTaskByStatus(Task.Status status){
        ArrayList<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM `task` WHERE task.status = " + status.ordinal() + " ORDER BY task.prank, task.qrank, task.submit DESC";
        try{
            ResultSet rs = conn().execQuery(sql);
            while (rs.next()){
                Task t = new Task();
                t.setComment(rs.getString("comment"));
                t.setCreateDate(rs.getDate("create"));
                t.setFinishDate(rs.getDate("finish"));
                t.setId(rs.getInt("id"));
                t.setUUID(rs.getString("uuid"));
                t.setParentUUID(rs.getString("parent"));                
                t.setModifyDate(rs.getDate("modify"));
                t.setSubmitDate(rs.getDate("submit"));                
                t.setName(rs.getString("name"));
                t.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                t.setPriorityRank(Task.Priority.values()[rs.getInt("prank")]);
                t.setQueueRank(rs.getInt("qrank"));
                t.setStatus(Task.Status.values()[rs.getInt("status")]);
                t.setKaKsConfig(DatabaseManager.getConfig(UUID.fromString(rs.getString("config"))));
                t.setCpuTime(rs.getInt("cputime"));
                t.setMemPeak(rs.getInt("mempeak"));
                t.setDaemonUUID(rs.getString("daemon"));                 
                tasks.add(t);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tasks;         
    }
    
    public static boolean updateTaskStatus(Task t, Task.Status status){
        t.setStatus(status);
        return DatabaseManager.editTask(t);
    }
    
    public static void start(Task task){
        Wrapper.wrap(task);
        updateTaskStatus(task, Task.Status.RUNNING);
    }
    
    public static void stop(Task task){
        //kill task if running
        //Wrapper.stop(task);
        //remove job from wrapper, NOTE: remove includes stop action
        Wrapper.remove(task);
        updateTaskStatus(task, Task.Status.STOPPED);
    }
    
    public static void doClientAction(Task t){
        switch (t.getStatus()){
            case STOP:
                stop(t);
                break;
            case START:
            case PAUSE:
            default:
        }
    }
    
    public static boolean submit(Task task) {
        Task t = DatabaseManager.getTask(task.getId()); //request again to ensure the status is confirmed as READY
        if (t.getStatus() == Task.Status.READY){
            t.setStatus(Task.Status.QUEUE);
            t.setSubmitDate(new Date());
            return DatabaseManager.editTask(t);
        }
        return false;
    }
    
    
    public static boolean canStartNext(){
        if ((Wrapper.cpuUsed() < sysconf.MAX_CPU) 
                && (Wrapper.memUsed() < sysconf.MAX_MEM)){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean canSubmitNext(){
        String sql = "SELECT COUNT(*) AS job_count FROM `task` WHERE"
                + " task.status = " + Task.Status.QUEUE.ordinal() 
                + " OR task.status = " + Task.Status.RUNNING.ordinal()
                + " OR task.status = " + Task.Status.PAUSED.ordinal(); 
        try{
            ResultSet rs = dbconn.execQuery(sql);
            rs.next();
            int job_count = rs.getInt("job_count");
            if (job_count < 100){ //manually set this to 100, to be customized in future
                return true;
            }
        }catch(Exception ex){
        }        
        return false;
    }
    
    public static void resetBrokenTasks(){
        //reset 'running jobs' to JOB_QUEUE
        //this should be call at the start up time, if some tasks is marked as RUNNING, but not found in the wrapper
        ArrayList<Task> tasks = getTaskByStatus(Task.Status.RUNNING);
        for (Iterator<Task> it = tasks.iterator(); it.hasNext();){
            Task t = it.next();
            if (!Wrapper.contains(t)){
                updateTaskStatus(t, Task.Status.READY);
            }
        }
    }
}
