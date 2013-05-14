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
import org.evome.KaKsCalc.client.Account;
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

    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Project getProject(int project_id){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        Project pj = new Project();
        String sql = "SELECT * FROM `project` WHERE id = " + project_id;
        //String sql = "SELECT * FROM `project` ";
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
        DBConnector dbconn = GWTServiceImpl.getDBConn();
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
        DBConnector dbconn = GWTServiceImpl.getDBConn();
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
        DBConnector dbconn = GWTServiceImpl.getDBConn();
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
        DBConnector dbconn = GWTServiceImpl.getDBConn();
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
        DBConnector dbconn = GWTServiceImpl.getDBConn();
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
    
    public static boolean editProject(Project proj){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        //id, owner, create can't be modified
        String sql = "UPDATE `task` SET ("
                + "name='" + proj.getName() + "',"
                + "comment='" + proj.getComment() + "',"
                + "modify='" + sdf.format(new Date()) + "'"
                + ") WHERE id = " + proj.getId();
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                return true;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean editCalculation(Calculation calc){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        //id, owner, create can't be modified
        String sql = "UPDATE `calculation` SET ("
                + "name='" + calc.getName() + "',"
                + "comment='" + calc.getComment() + "',"
                + "project='" + calc.getProject().getId() + "',"
                + "modify='" + sdf.format(new Date()) + "'"
                + ") WHERE id = " + calc.getId();
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                return true;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;            
    }
    
    public static boolean editTask(Task task){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        //id, owner, create, finish can't be modified
        String sql = "UPDATE `task` SET ("
                + "status='" + task.getStatus().ordinal() + "',"
                + "calc='" + task.getCalculation().getId() + "',"
                + "project='" + task.getProject().getId() + "',"
                + "qrank='" + task.getQueueRank() + "',"
                + "prank='" + task.getPriorityRank() + "',"
                + "comment='" + task.getComment() + "',"
                + "name='" + task.getName() + "',"
                + "modify='" + sdf.format(new Date()) + "',"
                + "kaks_c='" + task.getKaKsGeneticCode().ordinal() + "',"
                + "kaks_m='" + task.getKaKsMethod().name() + "'"
                + ") WHERE id = " + task.getId();
        try {
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                return true;
            }  
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean delProject(Project proj){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        String sql = "DELETE FROM `project` WHERE id = " + proj.getId();
        try {
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                return true;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean delCalculation(Calculation calc){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        String sql = "DELETE FROM `calculation` WHERE id = " + calc.getId();
        try {
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                return true;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;        
    }
    
    public static boolean delTask(Task task){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        String sql = "DELETE FROM `task` WHERE id = " + task.getId();
        try {
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                return true;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;               
    }
    
    public static ArrayList<Project> userProjects(Account account){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        ArrayList<Project> projects = new ArrayList<Project>();
        String sql = "SELECT * FROM `project` WHERE owner = " + account.getUserID();
        try{
            ResultSet rs = dbconn.execQuery(sql);
            while (rs.next()){
                Project p = new Project();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                p.setCreateDate(rs.getDate("create"));
                p.setModifyDate(rs.getDate("modify"));
                projects.add(p);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(projects.toString());
        return projects;
    }
    
    public static ArrayList<Calculation> subCalculations(Project project){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        ArrayList<Calculation> calcs = new ArrayList<Calculation>();
        String sql = "SELECT * FROM `calculation` WHERE project = " + project.getId();
        try{
            ResultSet rs = dbconn.execQuery(sql);
            while (rs.next()){
                Calculation c = new Calculation();
                c.setComment(rs.getString("comment"));
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                c.setProject(DatabaseManager.getProject(rs.getInt("project")));
                c.setCreateTime(rs.getDate("create"));
                c.setModifyTime(rs.getDate("modify"));
                calcs.add(c);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return calcs;        
    }
    
    public static ArrayList<Task> subTasks(Calculation calc){
        DBConnector dbconn = GWTServiceImpl.getDBConn();
        ArrayList<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM `task` WHERE calc = " + calc.getId();
        try{
            ResultSet rs = dbconn.execQuery(sql);
            while (rs.next()){
                Task t = new Task();
                t.setCalculation(DatabaseManager.getCalculation(rs.getInt("calc")));
                t.setComment(rs.getString("comment"));
                t.setCreateDate(rs.getDate("create"));
                t.setFinishDate(rs.getDate("finish"));
                t.setId(rs.getInt("id"));
                t.setKaKsGeneticCode(Task.Gencode.values()[rs.getInt("kaks_c")]);
                t.setKaKsMethod(Task.Method.valueOf(rs.getString("kaks_m")));
                t.setModifyDate(rs.getDate("modify"));
                t.setName(rs.getString("name"));
                t.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                t.setPriorityRank(Task.Priority.values()[rs.getInt("prank")]);
                t.setProjcet(DatabaseManager.getProject(rs.getInt("project")));
                t.setQueueRank(rs.getInt("qrank"));
                t.setStatus(Task.Status.values()[rs.getInt("status")]);
                tasks.add(t);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tasks;            
    }
}
