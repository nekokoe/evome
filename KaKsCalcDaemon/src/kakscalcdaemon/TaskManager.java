/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

/**
 *
 * @author nekoko
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskManager {

    public static final int FROM_DATA_TO_WORK = 1,
            FROM_WORK_TO_DATA = 2,
            FROM_DATA_TO_DATA = 3,
            FROM_WORK_TO_WORK = 4,
            OPS_COPY = 1,
            OPS_MOVE = 2;
    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();

    public TaskManager() {
    }

    public static Task get(Task task) {
        return get(task.getId());
    }

    public static Task get(int task_id) {
        Task task = null;
        //send sql query
        try {
            String sql = "SELECT * FROM `task` WHERE id = " + task_id;
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
                task.setOwner(rs.getInt("owner"));
                task.setCalc(rs.getInt("calc"));
                task.setProjcet(rs.getInt("project"));
                task.setPriorityRank(rs.getInt("prank"));
                task.setQueueRank(rs.getInt("qrank"));
                task.setStatus(rs.getInt("status"));
                task.setKaKsGeneticCode(rs.getInt("kaks_c"));
                task.setKaKsMethod(rs.getString("kaks_m"));
            }
        } catch (Exception ex) {
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return task;
    }

    public static int add(Task task) {
        int task_id = 0;
        String sql = "INSERT INTO `task` SET ("
                + "create = '" + dbconn.getSqlTime() + "',"
                + "finish = '" + "" + "',"
                + "modify = '" + dbconn.getSqlTime() + "',"
                + "delete = '" + "" + "',"
                + "status = " + task.getStatus() + ","
                + "owner = " + task.getOwner() + ","
                + "calc = " + task.getCalc() + ","
                + "project = " + task.getProject() + ","
                + "qrank = " + task.getQueueRank() + ","
                + "prank = " + task.getPriorityRank() + ","
                + "comment = '" + task.getComment() + "',"
                + "name = '" + task.getName() + "',"
                + "kaks_c = '" + task.getKaKsGeneticCode() + "',"
                + "kaks_m = '" + task.getKaKsMethod() + "'"                
                + ")";
        try {
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            if (rs != null) {
                rs.next();
                task_id = rs.getInt(1);
            }
            rs.close();
        } catch (Exception ex) { //debug out output this way
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return task_id;
    }

    public static boolean update(Task task) {
        String sql = "UPDATE `task` SET ("
                + "modify = '" + dbconn.getSqlTime() + "',"
                + "finish = '" + task.getFinishDateSimpleFormat() + "',"
                + "status = " + task.getStatus() + ","
                + "owner = " + task.getOwner() + ","
                + "calculation = " + task.getCalc() + ","
                + "project = " + task.getProject() + ","
                + "qrank = " + task.getQueueRank() + ","
                + "prank = " + task.getPriorityRank() + ","
                + "comment = '" + task.getComment() + "',"
                + "name = '" + task.getName() + "',"
                + "kaks_c = '" + task.getKaKsGeneticCode() + "',"
                + "kaks_m = '" + task.getKaKsMethod() + "'"
                + ") WHERE id = " + task.getId();
        ResultSet rs = dbconn.execQuery(sql);
        if (rs != null) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean updateStatus(Task task, int status) {
        Task dbtask = get(task);//must get db here, avoid changing other data
        task.setStatus(status);
        dbtask.setStatus(status); //sychronize caller task
        return update(dbtask);
    }

    public static void recycle(){
        //stop task marked as TASK_KILL and TASK_REMOVED
        String sql = "SELECT * FROM `task` WHERE status = " + Task.TASK_KILL + "OR status = " + Task.TASK_REMOVE;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            while (rs.next()){
                sql = "SELECT * FROM `job` WHERE task = " + rs.getInt("id");
                ResultSet rs2= dbconn.execQuery(sql);
                while (rs2.next()){
                    Queue.stop(Queue.get(rs2.getInt("id")));
                }
            }
        }catch(Exception ex){
            
        }
    }
    
    public static String getSubDir(String prefix, Task task) {
        return prefix
                + "/" + task.getOwner()
                + "/" + task.getProject()
                + "/" + task.getId();
    }

    private static boolean initSubDir(String prefix, Task task) {
        String path = getSubDir(prefix, task);
        try {
            File dir = new File(path);
            if (dir.exists()) {
                //task dir exists, check if empty
                if (dir.isDirectory()) {
                    File[] list = dir.listFiles();
                    if (list.length > 0) {
                        //cannot use this dir
                        return false;
                    }
                } else {
                    //is here, but regular file
                    return false;
                }
                //now safe to remove it
                dir.delete();
            }
            dir.mkdirs();
            System.out.println(TaskManager.class.getName() + ", create task dir : " + path);
            return true;
        } catch (Exception ex) {
            System.err.println(TaskManager.class.getName() + ", Cannot create dir: " + path);
            Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static boolean initDataDir(Task task) {
        return initSubDir(sysconf.DATA_ROOT_PATH, task);
    }

    public static boolean initWorkDir(Task task) {
        return initSubDir(sysconf.WORK_ROOT_PATH, task);
    }

    public static boolean copyTaskFile(Task task, int ops, int direct, String s, String d) {
        File src, dest;
        //init src and dest
        if (direct == TaskManager.FROM_DATA_TO_WORK
                || direct == TaskManager.FROM_DATA_TO_DATA) {
            src = new File(getSubDir(sysconf.DATA_ROOT_PATH, task) + "/" + new File(s).getName()); //get basename to avoid redirecting
        } else {
            src = new File(getSubDir(sysconf.WORK_ROOT_PATH, task) + "/" + new File(s).getName());
        }
        if (direct == TaskManager.FROM_DATA_TO_DATA
                || direct == TaskManager.FROM_WORK_TO_DATA) {
            dest = new File(getSubDir(sysconf.DATA_ROOT_PATH, task) + "/" + new File(d).getName());
        } else {
            dest = new File(getSubDir(sysconf.WORK_ROOT_PATH, task) + "/" + new File(d).getName());
        }
        //check
        if (s.equals(d)
                && (direct == TaskManager.FROM_WORK_TO_WORK || direct == TaskManager.FROM_DATA_TO_DATA)) {
            System.err.println(TaskManager.class.getName() + ":" + s + " and " + d + " are the same file!");
            return false;
        }
        //move or copy
        if (ops == TaskManager.OPS_MOVE) {
            src.renameTo(dest);
        } else if (ops == TaskManager.OPS_COPY) {
            try {
                FileChannel fcin = new FileInputStream(src).getChannel();
                FileChannel fcout = new FileOutputStream(dest).getChannel();
                long size = fcin.size();
                long bytesCopied = fcin.transferTo(0, size, fcout);
                System.out.println(bytesCopied + "/" + size + " bytes copied");
                fcin.close();
                fcout.close();
                return true;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (IOException ex) {
                Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            System.err.println(TaskManager.class.getName() + ": copy ops " + ops + " not defined?");
            return false;
        }
        return true; // all success
    }
}
