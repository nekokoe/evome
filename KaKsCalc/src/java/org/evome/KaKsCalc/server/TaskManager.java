/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

/**
 *
 * @author nekoko
 */
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Task;
import org.evome.KaKsCalc.client.Job;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TaskManager {

    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public TaskManager() {
    }
    
    public Task get(Task task){
        return this.get(task.getId());
    }

    public Task get(int task_id) {
        Task task = null;
        //send sql query
        String sql = "SELECT * FROM `task` WHERE id = " + task_id;
        try {
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()) {
                task = new Task();
                task.setComment(rs.getString("comment"));
                task.setCreateDate(rs.getDate("create"));
                task.setFinishDate(rs.getDate("finish"));
                task.setModifyDate(rs.getDate("modify"));
                task.setDeleteDate(rs.getDate("delete"));
                task.setId(rs.getInt("id"));
                task.setName(rs.getString("name"));
                task.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                task.setCalculation(DatabaseManager.getCalculation(rs.getInt("calculation")));
                task.setProjcet(rs.getInt("project"));
                task.setPriorityRank(rs.getInt("prank"));
                task.setQueueRank(rs.getInt("qrank"));
                task.setStatus(rs.getInt("status"));
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return task;
    }

    public int add(Task task) {
        int task_id = 0;
        String sql = "INSERT INTO `task` SET ("
                + "create = '" + dbconn.getSqlTime() + "',"
                + "finish = '" + "" + "',"
                + "modify = '" + dbconn.getSqlTime() + "',"
                + "delete = '" + "" + "',"
                + "status = " + task.getStatus() + ","
                + "owner = " + task.getOwner() + ","
                + "calculation = " + task.getCalculation() + ","
                + "project = " + task.getProject() + ","                
                + "qrank = " + task.getQueueRank() + ","
                + "prank = " + task.getPriorityRank() + ","
                + "comment = '" + task.getComment() + "',"
                + "name = '" + task.getName() + "'"
                + ")";
        try {
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            if (rs != null){
                rs.next();
                task_id = rs.getInt(1);
            }            
            rs.close();
        } catch (Exception ex) { //debug out output this way
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return task_id;
    }

    public boolean modify(Task task) {
        String sql = "UPDATE `task` SET ("
                + "modify = '" + dbconn.getSqlTime() + "',"
                + "finish = '" + task.getFinishDate() + "',"
                + "status = " + task.getStatus() + ","
                + "owner = " + task.getOwner() + ","
                + "calculation = " + task.getCalculation() + ","
                + "project = " + task.getProject() + ","                
                + "qrank = " + task.getQueueRank() + ","
                + "prank = " + task.getPriorityRank() + ","
                + "comment = '" + task.getComment() + "',"
                + "name = '" + task.getName() + "'"
                + ") WHERE id = " + task.getId();
        ResultSet rs = dbconn.execQuery(sql);
        if (rs != null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean delete(Task task) {
        //deleting ops are async. tag it as TASK_REMOVED and wait recycler to remove it
        //1.terminate its job in queue
        this.terminate(task);
        //2.mark it removed
        String sql = "UPDATE `task` SET("
                +"status = " + Task.TASK_REMOVED
                +") WHERE id = " + task.getId();
        ResultSet rs = dbconn.execQuery(sql);
        if (rs != null) {
            return true;
        } else {
            return false;
        }
    }

    public int submit(Task task) {
        //submit task to queue
        int job_id = 0;
        String sql = "INSERT INTO `job` SET("
                +"task = '" + task.getId() + "',"
                +"name = 'task_" + task.getId() + "',"
                +"submit = '" + dbconn.getSqlTime() + "',"
                +"queue = '" + "1" + "',"
                +"status = " + Job.JOB_QUEUE + ","
                +"prank = " + task.getPriorityRank() + ","
                +"qrank = " + task.getQueueRank() + ""
                +")";
        try{
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            if (rs != null){
                rs.next();
                job_id = rs.getInt(1);
            }
            rs.close();
        }catch(Exception ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);            
        }
        return job_id;
    }
    
    public void terminate(Task task){
        //terminate task in queue
        String sql = "UPDATE `job` SET ("
                + "status = " + Job.JOB_KILL
                + ") WHERE task = " + task.getId();
        ResultSet rs = dbconn.execQuery(sql);        
    }
    
    public void recycle(){
        //recycle tasks marked as REMOVED
        String sql = "SELECT * FROM `task` WHERE status = " + Task.TASK_REMOVED;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            while (rs.next()){
                sql = "SELECT 1 FROM `job` WHERE task = " + rs.getInt("id");
                ResultSet rs2 = dbconn.execQuery(sql);
                if (rs2.next()){
                    int job_status = rs2.getInt("status");
                    if (job_status == Job.JOB_RUN 
                            || job_status == Job.JOB_KILL 
                            || job_status == Job.JOB_HOLD){
                        continue;
                    }    
                }
                rs2.close();
                sql = "DELETE FROM `task` WHERE id = " + rs.getInt("id");
                dbconn.execQuery(sql);
            }
        }catch(Exception ex){
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);              
        }        
    }


    
}
