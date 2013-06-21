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
import org.evome.KaKsCalc.client.Resource;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

/**
 *
 * @author nekoko
 */
public class DatabaseManager {

    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private static DBConnector dbconn = GWTServiceImpl.getDBConn();
    
    private static DBConnector conn(){
        return GWTServiceImpl.getDBConn();
    }
    
    public static Project getProject(int project_id){
        Project pj = new Project();
        String sql = "SELECT * FROM `project` WHERE project.id = " + project_id;
        //String sql = "SELECT * FROM `project` ";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                pj.setId(project_id);
                pj.setUUID(rs.getString("uuid"));
                pj.setParentUUID(rs.getString("parent"));
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
    
    public static Project getProjectByUUID(String uuid){
        Project p = new Project();
        String sql = "SELECT * FROM `project` WHERE project.uuid='" + uuid + "'";
        //String sql = "SELECT * FROM `project` ";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                p.setId(rs.getInt("id"));
                p.setUUID(rs.getString("uuid"));
                p.setParentUUID(rs.getString("parent"));                
                p.setName(rs.getString("name"));
                p.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                p.setCreateDate(rs.getDate("create"));
                p.setModifyDate(rs.getDate("modify"));
                p.setComment(rs.getString("comment"));
            }else{
                p = null;    //return null if no this project or else
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
            p = null;
        }
        return p;        
    }
    
    public static Calculation getCalculation(int calc_id){
        Calculation calc = new Calculation();
        String sql = "SELECT * FROM `calculation` WHERE calculation.id = " + calc_id;
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                calc.setComment(rs.getString("comment"));
                calc.setId(rs.getInt("id"));
                calc.setUUID(rs.getString("uuid"));
                calc.setParentUUID(rs.getString("parent"));                
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
    public static Calculation getCalculationByUUID(String uuid){
        Calculation calc = new Calculation();
        String sql = "SELECT * FROM `calculation` WHERE calculation.uuid='" + uuid + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                calc.setComment(rs.getString("comment"));
                calc.setId(rs.getInt("id"));
                calc.setUUID(rs.getString("uuid"));
                calc.setParentUUID(rs.getString("parent"));
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
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                task.setCalculation(DatabaseManager.getCalculation(rs.getInt("calc")));
                task.setComment(rs.getString("comment"));
                task.setCreateDate(rs.getDate("create"));
                task.setFinishDate(rs.getDate("finish"));
                task.setId(rs.getInt("id"));
                task.setUUID(rs.getString("uuid"));
                task.setParentUUID(rs.getString("parent"));                
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
    public static Task getTaskByUUID(String uuid){
        Task task = new Task();
        String sql = "SELECT * FROM `task` WHERE task.uuid='" + uuid + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                task.setCalculation(DatabaseManager.getCalculation(rs.getInt("calc")));
                task.setComment(rs.getString("comment"));
                task.setCreateDate(rs.getDate("create"));
                task.setFinishDate(rs.getDate("finish"));
                task.setId(rs.getInt("id"));
                task.setUUID(rs.getString("uuid"));
                task.setParentUUID(rs.getString("parent"));
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

    public static Resource getResource(int id){
        Resource res = new Resource();
        String sql = "SELECT * FROM `resource` WHERE resource.id = " + id;
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                res.setId(rs.getInt("id"));
                res.setUUID(rs.getString("uuid"));
                res.setParentUUID(rs.getString("parent"));
                res.setName(rs.getString("name"));
                res.setType(Resource.ResType.values()[rs.getInt("type")]);
                res.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                res.setGroup(rs.getInt("group"));
                res.setTask(DatabaseManager.getTask(rs.getInt("task")));
                res.setUUID(rs.getString("uuid"));
                res.setCreateDate(rs.getDate("create"));
                res.setModifyDate(rs.getDate("modify"));
                res.setComment(rs.getString("comment"));
            }else{
                res = null;    //return null if no this project or else
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
            res = null;
        }
        return res;        
    }
    public static Resource getResourceByUUID(String uuid){
        Resource res = new Resource();
        String sql = "SELECT * FROM `resource` WHERE resource.uuid='" + uuid + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                res.setId(rs.getInt("id"));
                res.setUUID(rs.getString("uuid"));
                res.setParentUUID(rs.getString("parent"));
                res.setName(rs.getString("name"));
                res.setType(Resource.ResType.values()[rs.getInt("type")]);
                res.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                res.setGroup(rs.getInt("group"));
                res.setTask(DatabaseManager.getTask(rs.getInt("task")));
                res.setCreateDate(rs.getDate("create"));
                res.setModifyDate(rs.getDate("modify"));
                res.setComment(rs.getString("comment"));
            }else{
                res = null;    //return null if no this project or else
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
            res = null;
        }
        return res;        
    }     
    
    public static int addProject(Project proj){
        int proj_id;
        String sql = "INSERT INTO `project` SET "
                + "project.owner='"  + proj.getOwner().getUserID() + "',"
                + "project.name='" + proj.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "project.comment='" + proj.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "project.create='" + sdf.format(new Date()) + "',"
                + "project.uuid='" + UUID.randomUUID().toString() + "',"
                + "project.parent='" + proj.getOwner().getUUID() + "',"
                + "project.modify='" + sdf.format(new Date()) + "'";
        try{
            ResultSet rs = conn().execUpdateReturnGeneratedKeys(sql);
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
                + "calculation.uuid='" + UUID.randomUUID().toString() + "',"
                + "calculation.parent='" + calc.getProject().getUUID() + "',"
                + "calculation.modify='" + sdf.format(new Date()) + "'";                
        try{
            ResultSet rs = conn().execUpdateReturnGeneratedKeys(sql);
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
                + "task.uuid='" + UUID.randomUUID().toString() + "',"
                + "task.parent='" + task.getCalculation().getUUID() + "',"
                + "task.kaks_c='" + task.getKaKsGeneticCode().ordinal() + "',"
                + "task.kaks_m='" + task.getKaKsMethod().name() + "'";
        try{
            ResultSet rs = conn().execUpdateReturnGeneratedKeys(sql);
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

    public static int addResource(Resource res){
        int id;
        String sql = "INSERT INTO `resource` SET "
                + "resource.name='" + res.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "resource.type='" + res.getType().ordinal() + "',"
                + "resource.owner='"  + res.getOwner().getUserID() + "',"
                + "resource.group=1"  /* to be continued*/
                + "resource.task='" + res.getTask().getId() + "',"
                + "resource.uuid='" + res.getUUID() + "',"
                + "resource.create='" + sdf.format(new Date()) + "',"
                + "resource.modify='" + sdf.format(new Date()) + "',"
                + "resource.uuid='" + UUID.randomUUID().toString() + "',"
                + "resource.parent='" + res.getParentUUID() + "',"      //this field must be set on client if you need to associate resource to any elements
                + "resource.permission=0"  /* to be continued*/
                + "resource.comment='" + res.getComment().replaceAll("[\\\\]*'", "\\\\'") + "'";
        try{
            ResultSet rs = conn().execUpdateReturnGeneratedKeys(sql);
            if (rs.next()){
                id = rs.getInt(1);
            }else{
                id = 0;
            }                
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            id = 0;
        }
        return id;        
    }
    
    public static boolean editProject(Project proj){
        //id, owner, create can't be modified
        String sql = "UPDATE `project` SET "
                + "project.name='" + proj.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "project.comment='" + proj.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "project.modify='" + sdf.format(new Date()) + "'"
                + " WHERE project.id=" + proj.getId() + " OR project.uuid='" + proj.getUUID() + "";
        return (conn().execUpdate(sql) > 0) ? true : false;   
    }
    
    public static boolean editCalculation(Calculation calc){
        //id, owner, create can't be modified
        String sql = "UPDATE `calculation` SET "
                + "calculation.name='" + calc.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "calculation.comment='" + calc.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "calculation.project='" + calc.getProject().getId() + "',"
                + "calculation.modify='" + sdf.format(new Date()) + "'"
                + " WHERE calculation.id=" + calc.getId() + " OR calculation.uuid='" + calc.getUUID() + "'";
        return (conn().execUpdate(sql) > 0) ? true : false;             
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
                + " WHERE task.id=" + task.getId() + " OR task.uuid='" + task.getUUID()+"'";
        return (conn().execUpdate(sql) > 0) ? true : false;        
    }
    
    public static boolean editResource(Resource res){
        //id, owner, create can't be modified
        String sql = "UPDATE `resource` SET "
                + "resource.comment='" + res.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "resource.modify='" + sdf.format(new Date()) + "'"
                + " WHERE resource.id=" + res.getId() + " OR resource.uuid='" + res.getUUID() + "'";
        return (conn().execUpdate(sql) > 0) ? true : false;   
    }    
    
    public static boolean delProject(Project proj){
        boolean isSuccess = true;
        //delete all sub calculations
        ArrayList<Calculation> calcs = subCalculations(proj);
        for (Iterator<Calculation> it = calcs.iterator(); it.hasNext();){
            isSuccess &= delCalculation(it.next());
        }
        if (isSuccess) { //has deleted all subs
            String sql = "DELETE FROM `project` WHERE project.id=" + proj.getId() + " OR project.uuid='" + proj.getUUID() + "'";
            if (conn().execUpdate(sql) > 0) {
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
            String sql = "DELETE FROM `calculation` WHERE calculation.id=" + calc.getId() + " OR calculation.uuid='" + calc.getUUID() + "'";
            if (conn().execUpdate(sql) > 0) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
        }
        return isSuccess;
    }
    
    public static boolean delTask(Task task) {
        boolean isSuccess;
        String sql = "DELETE FROM `task` WHERE task.id=" + task.getId() + " OR task.uuid='" + task.getUUID() + "'";
        if (conn().execUpdate(sql) > 0) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return isSuccess;
    }
    
    public static boolean delResource(Resource res){
        boolean isSuccess;
        String sql = "DELETE FROM `resource` WHERE resource.id=" + res.getId() + " OR resource.uuid='" + res.getUUID() + "'";
        if (conn().execUpdate(sql) > 0) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return isSuccess;        
    }
    
    public static ArrayList<Project> userProjects(Account account){

        ArrayList<Project> projects = new ArrayList<Project>();
        String sql = "SELECT * FROM `project` WHERE project.owner=" + account.getUserID() + " OR project.parent='" + account.getUUID() + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            while (rs.next()){
                Project p = new Project();
                p.setId(rs.getInt("id"));
                p.setUUID(rs.getString("uuid"));
                p.setParentUUID(rs.getString("parent"));
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
            ResultSet rs = conn().execQuery(sql);
            while (rs.next()){
                Calculation c = new Calculation();
                c.setComment(rs.getString("comment"));
                c.setId(rs.getInt("id"));
                c.setUUID(rs.getString("uuid"));
                c.setParentUUID(rs.getString("parent"));                
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
            ResultSet rs = conn().execQuery(sql);
            while (rs.next()){
                Task t = new Task();
                t.setCalculation(DatabaseManager.getCalculation(rs.getInt("calc")));
                t.setComment(rs.getString("comment"));
                t.setCreateDate(rs.getDate("create"));
                t.setFinishDate(rs.getDate("finish"));
                t.setId(rs.getInt("id"));
                t.setUUID(rs.getString("uuid"));
                t.setParentUUID(rs.getString("parent"));                
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
    
    public static ArrayList<Resource> childResources(UUID uuid){
        ArrayList<Resource> reslist = new ArrayList<Resource>();
        String sql = "SELECT * FROM `resource` WHERE resource.parent='" + uuid.toString() + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            while (rs.next()){
                Resource r = new Resource();
                r.setId(rs.getInt("id"));
                r.setUUID(rs.getString("uuid"));
                r.setParentUUID(rs.getString("parent"));
                r.setName(rs.getString("name"));
                r.setType(Resource.ResType.values()[rs.getInt("type")]);
                r.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                r.setGroup(rs.getInt("group"));
                r.setTask(DatabaseManager.getTask(rs.getInt("task")));
                r.setCreateDate(rs.getDate("create"));
                r.setModifyDate(rs.getDate("modify"));
                r.setComment(rs.getString("comment"));
                reslist.add(r);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reslist;           
    }
    
    public static UUID getParentUUID(UUID uuid){
        //return null if no parent or error
        String sql = "SELECT * FROM 'account','calculation','project','task' WHERE uuid='" + uuid.toString() + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                return UUID.fromString(rs.getString("parent"));
            }else{
                return null;
            }            
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
            return null;        
        }
        
    }
}
