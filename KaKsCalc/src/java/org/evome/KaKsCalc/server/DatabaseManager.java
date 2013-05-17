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
import java.util.Iterator;

/**
 *
 * @author nekoko
 */
public class DatabaseManager {

    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DBConnector dbconn = GWTServiceImpl.getDBConn();
    
    public static Project getProject(int project_id){
        Project pj = new Project();
        String sql = "SELECT * FROM `project` WHERE project.id = " + project_id;
        //String sql = "SELECT * FROM `project` ";
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                pj.setId(project_id);
                pj.setName(rs.getString("name"));
                pj.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                pj.setCreateDate(rs.getDate("create"));
                pj.setModifyDate(rs.getDate("modify"));
                pj.setComment(rs.getString("comment"));
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
        String sql = "SELECT * FROM `calculation` WHERE calculation.id = " + calc_id;
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
        String sql = "SELECT * FROM `task` WHERE task.id = " + task_id;
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
        String sql = "INSERT INTO `project` SET "
                + "project.owner='"  + proj.getOwner().getUserID() + "',"
                + "project.name='" + proj.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "project.comment='" + proj.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "project.create='" + sdf.format(new Date()) + "',"
                + "project.modify='" + sdf.format(new Date()) + "'";
        try{
            ResultSet rs = dbconn.execUpdateReturnGeneratedKeys(sql);
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
        String sql = "INSERT INTO `calculation` SET "
                + "calculation.project='" + calc.getProject().getId() + "',"
                + "calculation.owner='" + calc.getOwner().getUserID() + "',"
                + "calculation.name='" + calc.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "calculation.comment='" + calc.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "calculation.create='" + sdf.format(new Date()) + "',"
                + "calculation.modify='" + sdf.format(new Date()) + "'";                
        try{
            ResultSet rs = dbconn.execUpdateReturnGeneratedKeys(sql);
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
        String sql = "INSERT INTO `task` SET "
                + "task.status='" + task.getStatus().ordinal() + "',"
                + "task.owner='" + task.getOwner().getUserID() + "',"
                + "task.calc='" + task.getCalculation().getId() + "',"
                + "task.project='" + task.getProject().getId() + "',"
                + "task.qrank='" + task.getQueueRank() + "',"
                + "task.prank='" + task.getPriorityRank().ordinal() + "',"
                + "task.comment='" + task.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "task.name='" + task.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "task.create='" + sdf.format(new Date()) + "',"
                + "task.modify='" + sdf.format(new Date()) + "',"
                + "task.kaks_c='" + task.getKaKsGeneticCode().ordinal() + "',"
                + "task.kaks_m='" + task.getKaKsMethod().name() + "'";
        try{
            ResultSet rs = dbconn.execUpdateReturnGeneratedKeys(sql);
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
        //id, owner, create can't be modified
        String sql = "UPDATE `project` SET "
                + "project.name='" + proj.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "project.comment='" + proj.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "project.modify='" + sdf.format(new Date()) + "'"
                + " WHERE project.id = " + proj.getId();
        return (dbconn.execUpdate(sql) > 0) ? true : false;   
    }
    
    public static boolean editCalculation(Calculation calc){
        //id, owner, create can't be modified
        String sql = "UPDATE `calculation` SET "
                + "calculation.name='" + calc.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "calculation.comment='" + calc.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "calculation.project='" + calc.getProject().getId() + "',"
                + "calculation.modify='" + sdf.format(new Date()) + "'"
                + " WHERE calculation.id = " + calc.getId();
        return (dbconn.execUpdate(sql) > 0) ? true : false;             
    }
    
    public static boolean editTask(Task task) {
        //id, owner, create, finish can't be modified
        String sql = "UPDATE `task` SET "
                + "task.status='" + task.getStatus().ordinal() + "',"
                + "task.calc='" + task.getCalculation().getId() + "',"
                + "task.project='" + task.getProject().getId() + "',"
                + "task.qrank='" + task.getQueueRank() + "',"
                + "task.prank='" + task.getPriorityRank() + "',"
                + "task.comment='" + task.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "task.name='" + task.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "task.modify='" + sdf.format(new Date()) + "',"
                + "task.kaks_c='" + task.getKaKsGeneticCode().ordinal() + "',"
                + "task.kaks_m='" + task.getKaKsMethod().name() + "'"
                + " WHERE task.id = " + task.getId();
        return (dbconn.execUpdate(sql) > 0) ? true : false;        
    }
    
    public static boolean delProject(Project proj){
        boolean isSuccess = true;
        //delete all sub calculations
        ArrayList<Calculation> calcs = subCalculations(proj);
        for (Iterator<Calculation> it = calcs.iterator(); it.hasNext();){
            isSuccess &= delCalculation(it.next());
        }
        if (isSuccess) { //has deleted all subs
            String sql = "DELETE FROM `project` WHERE project.id = " + proj.getId();
            if (dbconn.execUpdate(sql) > 0) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        }
        return isSuccess;
    }
    
    public static boolean delCalculation(Calculation calc) {
        boolean isSuccess = true;
        //delete all sub tasks
        ArrayList<Task> tasks = subTasks(calc);
        for (Iterator<Task> it = tasks.iterator(); it.hasNext();) {
            isSuccess &= delTask(it.next());
        }
        if (isSuccess) {
            String sql = "DELETE FROM `calculation` WHERE calculation.id = " + calc.getId();
            if (dbconn.execUpdate(sql) > 0) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        }
        return isSuccess;
    }
    
    public static boolean delTask(Task task) {
        boolean isSuccess;
        String sql = "DELETE FROM `task` WHERE task.id = " + task.getId();
        if (dbconn.execUpdate(sql) > 0) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return isSuccess;
    }
    
    public static ArrayList<Project> userProjects(Account account){

        ArrayList<Project> projects = new ArrayList<Project>();
        String sql = "SELECT * FROM `project` WHERE project.owner = " + account.getUserID();
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
        return projects;
    }
    
    public static ArrayList<Calculation> subCalculations(Project project){
        ArrayList<Calculation> calcs = new ArrayList<Calculation>();
        String sql = "SELECT * FROM `calculation` WHERE calculation.project = " + project.getId();
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
        ArrayList<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM `task` WHERE task.calc = " + calc.getId();
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
