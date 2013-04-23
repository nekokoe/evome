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

public class Task implements IsSerializable {

    private int task_id, task_status, task_owner, task_calc, task_project, task_QR, task_PR;
    private String ownerText;
    private String task_comment, task_name;
    private String task_create, task_finish, task_modify, task_delete;
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

    //set methods
    public void setCreateDate(String create) {
        this.task_create = create;
    }

    public void setFinishDate(String finish) {
        this.task_finish = finish;
    }

    public void setModifyDate(String modify) {
        this.task_modify = modify;
    }

    public void setDeleteDate(String delete) {
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
    public void setOwnerText(String owner){
        this.ownerText = owner;
    }
    public void setCalculation(int calc) {
        this.task_calc = calc;
    }

    public void setProjcet(int proj) {
        this.task_project = proj;
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
    public String getOwnerText(){
        return this.ownerText;
    }
    public int getCalculation() {
        return task_calc;
    }

    public int getProject() {
        return task_project;
    }

    public int getStatus() {
        return task_status;
    }

    public int getId() {
        return task_id;
    }

    public String getCreateDate() {
        return task_create;
    }

    public String getFinishDate() {
        return task_finish;
    }

    public String getModifyDate() {
        return this.task_modify;
    }

    public String getDeleteDate() {
        return this.task_delete;
    }
    
    //kaks calculation params
    private int kaks_c;
    private String kaks_m;
    
    public void setKaKsGeneticCode(int c){
        this.kaks_c = c;
    }
    public void setKaKsMethod(String m){
        this.kaks_m = m;
    }
    public int getKaKsGeneticCode(){
        return this.kaks_c;
    }
    public String getKaKsMethod(){
        return this.kaks_m;
    }
    
    //text description of status, used for UI
    public static String statusText(int status) {
        switch (status) {
            case TASK_NEW:
                return "TASK_NEW";
            case TASK_READY:
                return "TASK_READY";
            case TASK_RUNNING:
                return "TASK_RUNNING";
            case TASK_SUCCESS:
                return "TASK_SUCCESS";
            case TASK_ERROR:
                return "TASK_ERROR";
            case TASK_STOPPED:
                return "TASK_STOPPED";
            case TASK_PAUSED:
                return "TASK_PAUSED";
            case TASK_REMOVED:
                return "TASK_REMOVED";
            default:
                return "unknown status";
        }
    }
    
    public static String priorityText(int priority){
        switch(priority){
            case TASK_PR_RELAX:
                return "TASK_PR_RELAX";
            case TASK_PR_LOW:
                return "TASK_PR_LOW";
            case TASK_PR_NORMAL:
                return "TASK_PR_NORMAL";
            case TASK_PR_HIGH:
                return "TASK_PR_HIGH";
            case TASK_PR_INTENSE:
                return "TASK_PR_INTENSE";
            default:
                return "unknown priority class";
        }
    }
    
    public void applySampleData(){
        this.kaks_c = 1;
        this.kaks_m = "GYN";
        this.ownerText = "test";
        this.task_PR = 1;
        this.task_QR = 1;
        this.task_calc = 1;
        this.task_comment = "this is a sample data";
        this.task_create = "2000-01-01 12:34:56";
        this.task_finish = "";
        this.task_id = 1;
        this.task_modify = "";
        this.task_name = "test";
        this.task_owner = 1;
        this.task_project = 1;
        this.task_status = 1;
    }
    
}
