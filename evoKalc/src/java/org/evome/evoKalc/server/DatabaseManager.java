/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.server;


import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.evome.evoKalc.client.shared.*;

/**
 *
 * @author nekoko
 */
public class DatabaseManager {

    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    private static DBConnector dbconn = GWTServiceImpl.getDBConn();
    
    private static DBConnector conn() {
        return GWTServiceImpl.getDBConn();
    }

    public static String sdformat(Date date) {
        if (date == null) {
            return "1970-1-1 00:00:00";
        }
        return sdf.format(date);
    }
    
    public static Task getTask(int task_id){
        Task task = new Task();
        String sql = "SELECT * FROM `task` WHERE task.id = " + task_id;
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                task.setComment(rs.getString("comment"));
                task.setCreateDate(rs.getDate("create"));
                task.setFinishDate(rs.getDate("finish"));
                task.setId(rs.getInt("id"));
                task.setUUID(rs.getString("uuid"));
                task.setParentUUID(rs.getString("parent"));                
                task.setModifyDate(rs.getDate("modify"));
                task.setSubmitDate(rs.getDate("submit"));
                task.setName(rs.getString("name"));
                task.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                task.setPriorityRank(Task.Priority.values()[rs.getInt("prank")]);
                task.setQueueRank(rs.getInt("qrank"));
                task.setStatus(Task.Status.values()[rs.getInt("status")]);
                task.setKaKsConfig(getConfig(UUID.fromString(rs.getString("config"))));
                task.setCpuTime(rs.getInt("cputime"));
                task.setMemPeak(rs.getInt("mempeak"));
                task.setDaemonUUID(rs.getString("daemon"));                
            }else{
                task = null;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            task = null;
        }
        return task;
    }
    public static Task getTask(String uuid){
        Task task = new Task();
        String sql = "SELECT * FROM `task` WHERE task.uuid='" + uuid + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                task.setComment(rs.getString("comment"));
                task.setCreateDate(rs.getDate("create"));
                task.setFinishDate(rs.getDate("finish"));
                task.setId(rs.getInt("id"));
                task.setUUID(rs.getString("uuid"));
                task.setParentUUID(rs.getString("parent"));
                task.setModifyDate(rs.getDate("modify"));
                task.setSubmitDate(rs.getDate("submit"));                
                task.setName(rs.getString("name"));
                task.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                task.setPriorityRank(Task.Priority.values()[rs.getInt("prank")]);
                task.setQueueRank(rs.getInt("qrank"));
                task.setStatus(Task.Status.values()[rs.getInt("status")]);
                task.setKaKsConfig(getConfig(UUID.fromString(rs.getString("config"))));
                task.setCpuTime(rs.getInt("cputime"));
                task.setMemPeak(rs.getInt("mempeak"));
                task.setDaemonUUID(rs.getString("daemon"));
            }else{
                task = null;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            task = null;
        }
        return task;
    }    
    public static Task addTask(Task task){
        int task_id;
        String sql = "INSERT INTO `task` SET "
                + "task.status='" + task.getStatus().ordinal() + "',"
                + "task.owner='" + task.getOwner().getUserID() + "',"
                + "task.qrank='" + task.getQueueRank() + "',"
                + "task.prank='" + task.getPriorityRank().ordinal() + "',"
                + "task.comment='" + task.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "task.name='" + task.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "task.create='" + sdformat(new Date()) + "',"
                + "task.modify='" + sdformat(new Date()) + "',"
                + "task.uuid='" + UUID.randomUUID().toString() + "',"
                + "task.parent='" + task.getParentUUID() + "',"
                + "task.config='" + task.getKaKsConfig().getUUID() + "'";
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
        //return task_id;
        return getTask(task_id);
    }
    public static boolean editTask(Task task) {
        //id, owner, create, finish can't be modified
        String sql = "UPDATE `task` SET "
                + "task.status='" + task.getStatus().ordinal() + "',"
                + "task.owner='" + task.getOwner().getUserID() + "',"
                + "task.qrank='" + task.getQueueRank() + "',"
                + "task.prank='" + task.getPriorityRank().ordinal() + "',"
                + "task.comment='" + task.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "task.name='" + task.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "task.modify='" + sdformat(new Date()) + "',"
                + "task.submit='" + sdformat(task.getSubmitDate()) + "',"
                + "task.parent='" + task.getParentUUID() + "',"
                + "task.config='" + task.getKaKsConfig().getUUID() + "',"
                + "task.cputime='" + task.getCpuTime() + "',"
                + "task.mempeak='" + task.getMemPeak() + "',"
                + "task.daemon='" + task.getDaemonUUID() + "'"
                + " WHERE task.id=" + task.getId() + " OR task.uuid='" + task.getUUID()+"'";
        return (conn().execUpdate(sql) > 0) ? true : false;        
    }    
    public static boolean delTask(Task task) {
        boolean isSuccess;
        //delete childResources
        ArrayList<Resource> resources = childResources(UUID.fromString(task.getUUID()));
        for (Iterator<Resource> it = resources.iterator(); it.hasNext();) {
            delResource(it.next());
        }
        //delete task resource folder
        String sql = "DELETE FROM `task` WHERE task.id=" + task.getId() + " OR task.uuid='" + task.getUUID() + "'";
        isSuccess = (conn().execUpdate(sql) > 0) ? true : false;
        return isSuccess;
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
                res.setType(Resource.Type.values()[rs.getInt("type")]);
                res.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                res.setGroup(rs.getInt("group"));
                res.setUUID(rs.getString("uuid"));
                res.setCreateDate(rs.getDate("create"));
                res.setModifyDate(rs.getDate("modify"));
                res.setComment(rs.getString("comment"));
                res.setPermission(rs.getInt("permission"));                
            }else{
                res = null;    //return null if no this project or else
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
            res = null;
        }
        return res;        
    }
    public static Resource getResource(String uuid){
        Resource res = new Resource();
        String sql = "SELECT * FROM `resource` WHERE resource.uuid='" + uuid + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                res.setId(rs.getInt("id"));
                res.setUUID(rs.getString("uuid"));
                res.setParentUUID(rs.getString("parent"));
                res.setName(rs.getString("name"));
                res.setType(Resource.Type.values()[rs.getInt("type")]);
                res.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                res.setGroup(rs.getInt("group"));
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
    public static int addResource(Resource res){
        int id;
        String randomUUID = UUID.randomUUID().toString();
        if (res.getName().isEmpty()){
            res.setName(randomUUID);            //if no name, using the UUID
        }
        String sql = "INSERT INTO `resource` SET "
                + "resource.name='" + res.getName().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "resource.type='" + res.getType().ordinal() + "',"
                + "resource.owner='"  + res.getOwner().getUserID() + "',"
                + "resource.group=1,"  /* to be continued*/
                + "resource.create='" + sdf.format(new Date()) + "',"
                + "resource.modify='" + sdf.format(new Date()) + "',"
                + "resource.uuid='" + randomUUID + "',"
                + "resource.parent='" + res.getParentUUID() + "',"      //this field must be set on client if you need to associate resource to any elements
                + "resource.permission=0,"  /* to be continued*/
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
    public static boolean editResource(Resource res){
        //id, owner, create can't be modified
        String sql = "UPDATE `resource` SET "
                + "resource.name='" + res.getName() + "',"
                + "resource.type='" + res.getType().ordinal() + "',"
                + "resource.owner='" + res.getOwner().getUserID() + "',"
                + "resource.group='" + res.getGroup() + "',"
                + "resource.comment='" + res.getComment().replaceAll("[\\\\]*'", "\\\\'") + "',"
                + "resource.owner='" + res.getOwner().getUserID() + "',"
                + "resource.permission='" + res.getPermission() + "',"
                + "resource.parent='" + res.getParentUUID() + "',"
                + "resource.modify='" + sdf.format(new Date()) + "'"
                + " WHERE resource.id=" + res.getId() + " OR resource.uuid='" + res.getUUID() + "'";
        return (conn().execUpdate(sql) > 0) ? true : false;   
    }    
    
    public static boolean delResource(Resource res) {
        res = getResource(res.getUUID());
        ResourceManager.delResourceFile(res);   //remove file first
        String sql = "DELETE FROM `resource` WHERE resource.id=" + res.getId() + " OR resource.uuid='" + res.getUUID() + "'";
        return (conn().execUpdate(sql) > 0) ? true : false;
    }

    public static Config addConfig(Config config){
        String sql = "INSERT INTO `kaksconfig` SET "
                + "kaksconfig.uuid='" + UUID.randomUUID().toString() + "',"
                + "kaksconfig.owner='" + config.getOwner().getUUID() + "',"
                + "kaksconfig.pairmode='" + config.getPairMode().ordinal() + "',"
                + "kaksconfig.method='" + config.methods2String() + "',"
                + "kaksconfig.code='" + config.getKaKsGeneticCode().ordinal() + "',"
                + "kaksconfig.create='" + sdf.format(new Date()) + "',"
                + "kaksconfig.modify='" + sdf.format(new Date()) + "',"
                + "kaksconfig.name='" + config.getName() + "',"
                + "kaksconfig.comment='" + config.getComment() + "',"
                + "kaksconfig.pairlist='" + config.getPairlist().getUUID() + "'"
                + "";
        ResultSet rs = conn().execUpdateReturnGeneratedKeys(sql);
        try{
            if (rs.next()){
                int id = rs.getInt(1);
                return getConfig(id);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return null;        
    }
    
    public static Config getConfig(int id){
        //get kaks config by id
        Config c = null;
        String sql = "SELECT * FROM `kaksconfig` WHERE kaksconfig.id = " + id;
        ResultSet rs = conn().execQuery(sql);
        try{
            if (rs.next()){
                c = new Config();
                c.setId(rs.getInt("id"));
                c.setUUID(rs.getString("uuid"));
                c.setOwner(AccountManager.getAccount(UUID.fromString(rs.getString("owner"))));
                c.setPairMode(Config.PairMode.values()[rs.getInt("pairmode")]);
                c.setKaKsGeneticCode(Config.Gencode.values()[rs.getInt("code")]);
                c.string2Methods(rs.getString("method"));
                c.setCreateDate(rs.getDate("create"));
                c.setModifyDate(rs.getDate("modify"));
                c.setName(rs.getString("name"));
                c.setComment(rs.getString("comment"));
                c.setPairlist(getResource(rs.getString("pairlist")));
                c.setResult(getResource(rs.getString("result")));                 
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);              
        }
        return c;    
    }
    
    public static Config getConfig(UUID uuid){
        //get kaks config by id
        Config c = null;
        String sql = "SELECT * FROM `kaksconfig` WHERE kaksconfig.uuid = '" + uuid.toString() + "'";
        ResultSet rs = conn().execQuery(sql);
        try{
            if (rs.next()){
                c = new Config();
                c.setId(rs.getInt("id"));
                c.setUUID(rs.getString("uuid"));
                c.setOwner(AccountManager.getAccount(UUID.fromString(rs.getString("owner"))));
                c.setPairMode(Config.PairMode.values()[rs.getInt("pairmode")]);
                c.setKaKsGeneticCode(Config.Gencode.values()[rs.getInt("code")]);
                c.string2Methods(rs.getString("method"));
                c.setCreateDate(rs.getDate("create"));
                c.setModifyDate(rs.getDate("modify"));
                c.setName(rs.getString("name"));
                c.setComment(rs.getString("comment"));
                c.setPairlist(getResource(rs.getString("pairlist")));
                c.setResult(getResource(rs.getString("result"))); 
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);              
        }
        return c;    
    }
    
    public static Config editConfig(Config c){
        String sql = "UPDATE `kaksconfig` SET "
                + "kaksconfig.pairmode = '" + c.getPairMode().ordinal() + "',"
                + "kaksconfig.method = '" + c.methods2String() + "',"
                + "kaksconfig.code = '" + c.getKaKsGeneticCode().ordinal() + "',"
                + "kaksconfig.modify = '" + sdf.format(new Date()) + "',"
                + "kaksconfig.name = '" + c.getName() + "',"
                + "kaksconfig.comment = '" + c.getComment() + "',"
                + "kaksconfig.pairlist = '" + c.getPairlist().getUUID() + "',"
                + "kaksconfig.result = '" + c.getResult().getUUID() + "'"                
                + " WHERE kaksconfig.id = " + c.getId() + "";
        if (conn().execUpdate(sql) > 0){
            //update success
            return getConfig(c.getId());            
        }else{
            return null;
        }
    }
    
    public static boolean delConfig(Config c){
        String sql = "DELETE FROM `kaksconfig` WHERE kaksconfig.id = " + c.getId() + "";
        if (conn().execUpdate(sql) > 0){
            //success
            return true;
        }else{
            return false;
        }              
    }
    
    
    public static ArrayList<Task> userTasks(Account account, Task.Classify c){
        ArrayList<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM `task` WHERE task.owner = " + account.getUserID();
        //append classify conditions
        switch(c){
            case ALL:
                break;
            case IN_PROGRESS:
                sql = sql + " AND ("
                        + "task.status=" + Task.Status.RUNNING.ordinal() 
                        + " OR task.status=" + Task.Status.QUEUE.ordinal() 
                        + ")";
                break;
            case TODAY:
                sql = sql + " AND TIME_TO_SEC(TIMEDIFF(NOW(), task.create)) < 86400";
                break;
            case THIS_WEEK:
                sql = sql + " AND TIME_TO_SEC(TIMEDIFF(NOW(), task.create)) < 604800";
                break;
            case THIS_MONTH:
                sql = sql + " AND TIME_TO_SEC(TIMEDIFF(NOW(), task.create)) < 18144000";
                break;
            case THIS_YEAR:
                sql = sql + " AND TIME_TO_SEC(TIMEDIFF(NOW(), task.create)) < 6622560000";
                break;
            case EARLIER:
                sql = sql + " AND TIME_TO_SEC(TIMEDIFF(NOW(), task.create)) > 6622560000";
                break;
            case RECYCLE_BIN:
                sql = sql + " AND task.status=" + Task.Status.REMOVE.ordinal();
        }
        
        
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
                t.setName(rs.getString("name"));
                t.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                t.setPriorityRank(Task.Priority.values()[rs.getInt("prank")]);
                t.setQueueRank(rs.getInt("qrank"));
                t.setStatus(Task.Status.values()[rs.getInt("status")]);
                tasks.add(t);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tasks;            
    }
    
    public static ArrayList<Resource> userResource(Account a, Resource.Type type){
        ArrayList<Resource> reslist = new ArrayList<Resource>();
        String sql = "SELECT * FROM `resource` WHERE resource.owner='" + a.getUserID() + "'";
        //filter the type
        switch(type){
            case Sequence:
            case Pairlist: 
            case Config:
            case Result:
            case Default:
                sql = sql + " AND resource.type = " + type.ordinal();
                break;
            case All:
            default:
        }
        sql = sql + " ORDER BY resource.modify DESC";
        
        ResultSet rs = conn().execQuery(sql);
        try{
            while (rs.next()){
                Resource r = new Resource();
                r.setId(rs.getInt("id"));
                r.setUUID(rs.getString("uuid"));
                r.setParentUUID(rs.getString("parent"));
                r.setName(rs.getString("name"));
                r.setType(Resource.Type.values()[rs.getInt("type")]);
                r.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                r.setGroup(rs.getInt("group"));
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
                r.setType(Resource.Type.values()[rs.getInt("type")]);
                r.setOwner(AccountManager.getAccount(rs.getInt("owner")));
                r.setGroup(rs.getInt("group"));
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
    

}
