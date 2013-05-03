/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;


import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Task;
import org.evome.KaKsCalc.client.Job;
import org.evome.KaKsCalc.client.Project;
import org.evome.KaKsCalc.client.Calculation;
import java.util.Date;

/**
 *
 * @author nekoko
 */
public class DatabaseManager {
    
    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Project getProject(int project_id){
        Project pj = new Project();
        String sql = "SELECT * FROM `project` WHERE id = " + project_id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                pj.setId(project_id);
                pj.setName(rs.getString("name"));
                pj.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                pj.setCreateDate(rs.getDate("create"));
                pj.setModifyDate(rs.getDate("modify"));                
            }else{
                pj = null;    //return null if no this project or else
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
            pj = null;
        }
        return pj;
    }
    
    public static Calculation getCalculation(int calc_id){
        Calculation calc = new Calculation();
        String sql = "SELECT * FROM `calculation` WHERE id = " + calc_id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                calc.setComment(rs.getString("comment"));
                calc.setId(rs.getInt("id"));
                calc.setName(rs.getString("name"));
                calc.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                calc.setProject(DatabaseManager.getProject(rs.getInt("project")));
                calc.setCreateTime(rs.getDate("create"));
                calc.setModifyTime(rs.getDate("modify"));
            }else{
                calc = null;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            calc = null;
        }
        return calc;
    }
    
    public static Task getTask(int task_id){
        Task task = new Task();
        String sql = "SELECT * FROM `task` WHERE id = " + task_id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                task.setCalculation(DatabaseManager.getCalculation(rs.getInt("calc")));
                task.setComment(rs.getString("comment"));
                task.setCreateDate(rs.getDate("create"));
                task.setFinishDate(rs.getDate("finish"));
                task.setId(rs.getInt("id"));
                task.setKaKsGeneticCode(Task.Gencode.values()[rs.getInt("kaks_c")]);
                task.setKaKsMethod(Task.Method.valueOf(rs.getString("kaks_m")));
                task.setModifyDate(rs.getDate("modify"));
                task.setName(rs.getString("name"));
                task.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                task.setPriorityRank(Task.Priority.values()[rs.getInt("prank")]);
                task.setProjcet(DatabaseManager.getProject(rs.getInt("project")));
                task.setQueueRank(rs.getInt("qrank"));
                task.setStatus(Task.Status.values()[rs.getInt("status")]);
            }else{
                task = null;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            task = null;
        }
        return task;
    }
    
    public static int addProject(Project proj){
        int proj_id;
        String sql = "INSERT INTO `project` SET ("
                + "owner='"  + proj.getOwner().getUserID() + "',"
                + "name='" + proj.getName() + "',"
                + "comment='" + proj.getComment() + "',"
                + "create='" + sdf.format(new Date()) + "',"
                + "modify='" + sdf.format(new Date()) + "'"
                + ")";
        try{
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            if (rs.next()){
                proj_id = rs.getInt(1);
            }else{
                proj_id = 0;
            }                
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            proj_id = 0;
        }
        return proj_id;
    }
    
    public static int addCalculation(Calculation calc){
        int calc_id;
        String sql = "INSERT INTO `calculation` SET ("
                + "project='" + calc.getProject().getId() + "',"
                + "owner='" + calc.getOwner().getUserID() + "',"
                + "name='" + calc.getName() + "',"
                + "comment='" + calc.getComment() + "',"
                + "create='" + sdf.format(new Date()) + "',"
                + "modify='" + sdf.format(new Date()) + "'"
                +")";
        try{
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            if (rs.next()){
                calc_id = rs.getInt(1);
            }else{
                calc_id = 0;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            calc_id = 0;
        }
        return calc_id;
    }
    
    public static int addTask(Task task){
        int task_id;
        String sql = "INSERT INTO `task` SET ("
                + "status='" + task.getStatus().ordinal() + "',"
                + "owner='" + task.getOwner().getUserID() + "',"
                + "calc='" + task.getCalculation().getId() + "',"
                + "project='" + task.getProject().getId() + "',"
                + "qrank='" + task.getQueueRank() + "',"
                + "prank='" + task.getPriorityRank().ordinal() + "',"
                + "comment='" + task.getComment() + "',"
                + "name='" + task.getName() + "',"
                + "create='" + sdf.format(new Date()) + "',"
                + "modify='" + sdf.format(new Date()) + "',"
                + "kaks_c='" + task.getKaKsGeneticCode().ordinal() + "',"
                + "kaks_m='" + task.getKaKsMethod().name() + "'"
                + ")";
        try{
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            if (rs.next()){
                task_id = rs.getInt(1);
            }else{
                task_id = 0;
            }            
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            task_id = 0;
        }
        return task_id;
    }
    
    public static void editProject(Project proj){
        String 
    }
}
