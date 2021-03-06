/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author nekoko
 */
public class Job implements IsSerializable {

    //sql data
    private int job_id, job_task, job_queue, job_status, job_prank, job_qrank;
    private int job_req_cpu, job_req_mem;
    private String job_name;
    private String job_submit;
    private String uuid, parent;
    //runtime data
    private Calculation job_calc;
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

    public void setSubmitDate(String submit) {
        this.job_submit = submit;
    }

    public void setReqCPU(int cpu) {
        this.job_req_cpu = cpu;
    }

    public void setReqMem(int mem) {
        this.job_req_mem = mem;
    }

    public void setCalculation(Calculation calc) {
        this.job_calc = calc;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public void setParentUUID(String uuid) {
        this.parent = uuid;
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

    public String getSubmitDate() {
        return this.job_submit;
    }

    public int getReqCPU() {
        return this.job_req_cpu;
    }

    public int getReqMem() {
        return this.job_req_mem;
    }

    public Calculation getCalculation() {
        return this.job_calc;
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getParentUUID() {
        return this.parent;
    }
}
