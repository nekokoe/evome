/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Queue manages jobs in queue
 * @author nekoko
 */
public class Queue {
    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();
    
    private int id = 1;
    private int size = 100;
    private String name;
    
    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    
    public static Job get(Task task){
        Job job = new Job();
        try{
            String sql = "SELECT * FROM `job` WHERE task = " + task.getId();
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                job.setId(rs.getInt("id"));
                job.setTask(rs.getInt("task"));
                job.setName(rs.getString("name"));
                job.setSubmitDate(rs.getDate("submit"));
                job.setQueue(rs.getInt("queue"));
                job.setStatus(rs.getInt("status"));
                job.setPriorityRank(rs.getInt("prank"));
                job.setQueueRank(rs.getInt("qrank"));
                job.setReqCPU(rs.getInt("cpu"));
                job.setReqMem(rs.getInt("mem"));
            }
        }catch(Exception ex){
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return job;        
    }
    
    public static Job get(Job job){
        return get(job.getId());
    }
    
    public static Job get(int job_id){
        Job job = new Job();
        try{
            String sql = "SELECT * FROM `job` WHERE id = " + job_id;
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                job.setId(rs.getInt("id"));
                job.setTask(rs.getInt("task"));
                job.setName(rs.getString("name"));
                job.setSubmitDate(rs.getDate("submit"));
                job.setQueue(rs.getInt("queue"));
                job.setStatus(rs.getInt("status"));
                job.setPriorityRank(rs.getInt("prank"));
                job.setQueueRank(rs.getInt("qrank"));
                job.setReqCPU(rs.getInt("cpu"));
                job.setReqMem(rs.getInt("mem"));
            }
        }catch(Exception ex){
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return job;
    }
    
    public static boolean update(Job job) {
        String sql = "UPDATE `job` SET ("
                + "name = '" + job.getName() + "',"
                + "submit = '" + job.getSubmitDateSimpleFormat() + "',"
                + "queue = '" + job.getQueue() + "',"
                + "status = '" + job.getStatus() + "',"
                + "prank = '" + job.getPriorityRank() + "',"
                + "qrank = '" + job.getQueueRank() + "',"
                + "cpu = '" + job.getReqCPU() + "',"
                + "mem = '" + job.getReqMem() + "'"
                + ") WHERE id = " + job.getId();
        ResultSet rs = dbconn.execQuery(sql);
        return (rs != null) ? true : false;
    }
    
    public static boolean delete(Job job){
        String sql = "DELETE FROM `job` WHERE id = " + job.getId();
        ResultSet rs = dbconn.execQuery(sql);
        return (rs != null) ? true : false;        
    }
    
    public static boolean updateStatus(Job job, int status){
        Job dbjob = Queue.get(job.getId());
        dbjob.setStatus(status);
        job.setStatus(status);
        return Queue.update(dbjob);        
    }

    public static void start(Job job){
        Wrapper.wrap(job);
        //update status
        Queue.updateStatus(job, Job.JOB_RUN);
    }
    
    public static void stop(Job job){
        //kill task if running
        Wrapper.stop(job);
        //remove job from wrapper
        Wrapper.remove(job);
    }

    public static int submit(Task task) {
        //submit task to queue
        int job_id = 0;
        String sql = "INSERT INTO `job` SET("
                + "task = '" + task.getId() + "',"
                + "name = 'task_" + task.getId() + "',"
                + "submit = '" + dbconn.getSqlTime() + "',"
                + "queue = 1," //fixed now
                + "status = " + Job.JOB_QUEUE + ","
                + "prank = " + task.getPriorityRank() + ","
                + "qrank = " + task.getQueueRank() + ","
                + "cpu = 1," //fixed now
                + "mem = 1048576" //fixed now
                + ")";
        try {
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            if (rs != null) {
                rs.next();
                job_id = rs.getInt(1);
            }
            rs.close();
        } catch (Exception ex) {
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return job_id;
    }
    
    public static Job fetch(){
        //fetch one job out of the top of the queue
        //ordering by PR, QR and submit-time
        Job job = null;
        String sql = "SELECT TOP 1 * FROM `job` WHERE status = " + Job.JOB_QUEUE + " ORDER BY prank, qrank, submit";
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
               job = Queue.get(rs.getInt("id")); 
            }
        }catch (Exception ex){
            Logger.getLogger(Queue.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return job;
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
        String sql = "SELECT COUNT(*) AS job_count FROM `job` WHERE queue = 1";
        try{
            ResultSet rs = dbconn.execQuery(sql);
            rs.next();
            int job_count = rs.getInt("job_count");
            if (job_count < 100){
                return true;
            }
        }catch(Exception ex){
        }        
        return false;
    }
    
   
}
