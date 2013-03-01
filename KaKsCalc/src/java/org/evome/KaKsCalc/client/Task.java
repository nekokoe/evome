/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

/**
 *
 * @author nekoko
 */
import com.google.gwt.user.client.rpc.IsSerializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements IsSerializable {

    private int task_id, task_status, task_owner, task_QR, task_PR;
    private String task_comment, task_name;
    private Date task_create, task_finish, task_modify, task_delete;
    //Define task status
    public static final int TASK_NEW = 0,
            TASK_READY = 1,
            TASK_RUNNING = 2,
            TASK_SUCCESS = 3,
            TASK_ERROR = 4,
            TASK_STOPPED = 5,
            TASK_PAUSED = 6,
            TASK_REMOVED = 7;
    //Define task priority rank
    public static final int TASK_PR_RELAX = 20,
            TASK_PR_LOW = 15,
            TASK_PR_NORMAL = 10,
            TASK_PR_HIGH = 5,
            TASK_PR_INTENSE = 0;
    //define queue job status
    public static final int JOB_QUEUE = 1,
            JOB_RUN = 2,
            JOB_HOLD = 3,
            JOB_ERR = 4,
            JOB_KILL = 5,
            JOB_FINISH = 6;
    
    //set methods
    public void setCreateDate(Date create) {
        this.task_create = create;
    }

    public void setFinishDate(Date finish) {
        this.task_finish = finish;
    }

    public void setModifyDate(Date modify) {
        this.task_modify = modify;
    }

    public void setDeleteDate(Date delete) {
        this.task_delete = delete;
    }

    public void setId(int id) {
        this.task_id = id;
    }

    public void setStatus(int status) {
        this.task_status = status;
    }

    public void setOwner(int owner) {
        this.task_owner = owner;
    }

    public void setQueueRank(int rank) {
        this.task_QR = rank;
    }

    public void setPriorityRank(int rank) {
        this.task_PR = rank;
    }

    public void setComment(String comment) {
        this.task_comment = comment;
    }

    public void setName(String name) {
        this.task_name = name;
    }

    //read methods
    public String getName() {
        return task_name;
    }

    public String getComment() {
        return task_comment;
    }

    public int getPriorityRank() {
        return task_PR;
    }

    public int getQueueRank() {
        return task_QR;
    }

    public int getOwner() {
        return task_owner;
    }

    public int getStatus() {
        return task_status;
    }

    public int getId() {
        return task_id;
    }

    public Date getCreateDate() {
        return task_create;
    }

    public Date getFinishDate() {
        return task_finish;
    }

    public Date getModifyDate() {
        return this.task_modify;
    }

    public Date getDeleteDate() {
        return this.task_delete;
    }

    public String getCreateDateSimpleFormat() {
        return getSimpleDateFormat(task_create);
    }

    public String getFinishDateSimpleFormat() {
        return getSimpleDateFormat(task_finish);
    }

    private String getSimpleDateFormat(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(d);
        return dateStr;
    }
}
