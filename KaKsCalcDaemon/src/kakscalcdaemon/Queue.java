/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;

/**
 *  Queue manages jobs in queue
 * @author nekoko
 */
public class Queue {
    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();
    
    private int id;
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
                + "qrank = '" + job.getQueueRank() + "'"
                + ") WHERE id = " + job.getId();
        ResultSet rs = dbconn.execQuery(sql);
        return (rs != null) ? true : false;
    }
   
    public static void delete(Job job){
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
                + "queue = '" + "1" + "',"
                + "status = " + Job.JOB_QUEUE + ","
                + "prank = " + task.getPriorityRank() + ","
                + "qrank = " + task.getQueueRank() + ""
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
    
    public boolean canSubmit(){
        
    }
    
}
