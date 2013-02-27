/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

/**
 *
 * @author nekoko
 */

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Task;

public class TaskManager {
    private DBConnector dbconn = new DBConnector();
    private SysConfig sysconf = new SysConfig();
    
    public TaskManager(){

    }
    
    public Task get(int task_id){
        Task task = null;
        //send sql query
        String sql = "SELECT * FROM `task` WHERE task_id = " + task_id;
        try {
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()) {
                task = new Task();
                task.setComment(rs.getString("task_comment"));
                task.setCreateDate(rs.getDate("task_create"));
                task.setFinishDate(rs.getDate("task_finish"));
                task.setId(rs.getInt("task_id"));
                task.setName(rs.getString("task_name"));
                task.setOwner(rs.getInt("task_owner"));
                task.setPriorityRank(rs.getInt("task_PR"));
                task.setQueueRank(rs.getInt("task_QR"));
                task.setStatus(rs.getInt("task_status"));
            }
        } catch (Exception ex) {
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return task;
    }
    
    public int add(Task task){
        int task_id = 0;
        String sql = "INSERT INTO `task` SET ("
                + "task_create = '" + task.getCreateDateSimpleFormat() + "',"
                + "task_finish = '" + task.getFinishDateSimpleFormat() + "',"
                + "task_status = " + task.getStatus() + ","
                + "task_owner = " + task.getOwner() + ","
                + "task_QR = " + task.getQueueRank() + ","
                + "task_PR = " + task.getPriorityRank() + ","
                + "task_comment = '" + task.getComment() + "',"
                + "task_name = '" + task.getName() + "'"
                + ")";
        try{
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            rs.next();
            task_id = rs.getInt(1);
            rs.close();
        }catch(Exception ex) { //debug out output this way
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return task_id;
    }
    
    public boolean modify(Task task) {
        String sql = "UPDATE `task` SET ("
                + "task_create = '" + task.getCreateDateSimpleFormat() + "',"
                + "task_finish = '" + task.getFinishDateSimpleFormat() + "',"
                + "task_status = " + task.getStatus() + ","
                + "task_owner = " + task.getOwner() + ","
                + "task_QR = " + task.getQueueRank() + ","
                + "task_PR = " + task.getPriorityRank() + ","
                + "task_comment = '" + task.getComment() + "',"
                + "task_name = '" + task.getName() + "'"
                + ") WHERE task_id = " + task.getId();
        ResultSet rs = dbconn.execQuery(sql);
        if (rs == null) {
            return false;
        } else {
            return true;
        }

    }

    public boolean delete(Task task) {
        String sql = "DELETE FROM `task` WHERE task_id = " + task.getId();
        ResultSet rs = dbconn.execQuery(sql);
        if (rs == null) {
            return false;
        } else {
            return true;
        }
    }
    
}
