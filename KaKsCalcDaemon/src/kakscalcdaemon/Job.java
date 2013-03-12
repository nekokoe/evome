/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author nekoko
 */
public class Job {

    private int job_id, job_task, job_queue, job_status, job_prank, job_qrank;
    private String job_name;
    private Date job_submit;
    //define queue job status
    public static final int JOB_QUEUE = 1,
            JOB_RUN = 2,
            JOB_HOLD = 3,
            JOB_ERR = 4,
            JOB_KILL = 5,
            JOB_FINISH = 6;

    public void setId(int id) {
        this.job_id = id;
    }

    public void setTask(int task) {
        this.job_task = task;
    }

    public void setQueue(int queue) {
        this.job_queue = queue;
    }

    public void setStatus(int status) {
        this.job_status = status;
    }

    public void setPriorityRank(int prank) {
        this.job_prank = prank;
    }

    public void setQueueRank(int qrank) {
        this.job_qrank = qrank;
    }

    public void setName(String name) {
        this.job_name = name;
    }

    public void setSubmitDate(Date submit) {
        this.job_submit = submit;
    }

    public int getId() {
        return this.job_id;
    }

    public int getTask() {
        return this.job_task;
    }

    public int getQueue() {
        return this.job_queue;
    }

    public int getStatus() {
        return this.job_status;
    }

    public int getPriorityRank() {
        return this.job_prank;
    }

    public int getQueueRank() {
        return this.job_qrank;
    }

    public String getName() {
        return this.job_name;
    }

    public Date getSubmitDate() {
        return this.job_submit;
    }

    public String getSubmitDateSimpleFormat() {
        return getSimpleDateFormat(this.job_submit);
    }

    private String getSimpleDateFormat(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(d);
        return dateStr;
    }
}
