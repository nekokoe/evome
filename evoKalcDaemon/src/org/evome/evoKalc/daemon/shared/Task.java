/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon.shared;

/**
 *
 * @author nekoko
 */

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;


public class Task {
    //vars
    private int task_id, task_QR;
    private String task_comment, task_name;
    private String uuid, parent;
    private Date task_create, task_finish, task_modify, task_delete, submit;
    private Account owner;
    private Status status;
    private Priority priority;
    private Config config;
    private int cputime, mempeak;   //cputime in sec, mempeak in KB
    private String daemonUUID;
    
    
    //Define task status
    public static enum Status{
        NEW,EDIT,REMOVE,LOCK,READY,
        QUEUE,RUNNING,STOPPED,PAUSED,
        SUCCESS,FAIL,ERROR,
        START,STOP,PAUSE
    }
    //Define task priority rank
    public static enum Priority{
        URGENT,HIGH,NORMAL,LOW,RELAX
    }    
    
    public static enum Classify{
        ALL,
        IN_PROGRESS,COMPLETE,
        TODAY,THIS_WEEK,THIS_MONTH,THIS_YEAR,EARLIER,RECYCLE_BIN
    }
    //set default values
    //default values supress Null Pointer Excetion
    public Task(){
        //this.owner = Account.sampleData();
        this.task_comment = "";
        this.task_name = "";
        this.priority = Priority.LOW;
        this.task_QR = 0;
        this.status = Status.NEW;

    }
    //Override toString()
    @Override
    public String toString(){
        return this.task_name;
    }
    
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public void setQueueRank(int rank) {
        this.task_QR = rank;
    }

    public void setPriorityRank(Priority rank) {
        this.priority = rank;
    }

    public void setComment(String comment) {
        this.task_comment = comment;
    }

    public void setName(String name) {
        this.task_name = name;
    }
    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    
    public void setParentUUID(String uuid){
        this.parent = uuid;
    }
    


    //read methods
    public String getName() {
        return task_name;
    }

    public String getComment() {
        return task_comment;
    }

    public Priority getPriorityRank() {
        return priority;
    }

    public int getQueueRank() {
        return task_QR;
    }

    public Account getOwner() {
        return owner;
    }

    public Status getStatus() {
        return status;
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
    public String getUUID(){
        return this.uuid;
    }
    public String getParentUUID(){
        return this.parent;
    }
    
    public Config getKaKsConfig(){
        return this.config;
    }
    public void setKaKsConfig(Config conf){
        this.config = conf;
    }
    
    public int getCpuTime(){
        return this.cputime;
    }
    public void setCpuTime(int time){
        this.cputime = time;
    }
    
    public int getMemPeak(){
        return this.mempeak;
    }
    public void setMemPeak(int peak){
        this.mempeak = peak;
    }
    
    public Date getSubmitDate(){
        return this.submit;
    }
    public void setSubmitDate(Date d){
        this.submit = d;
    }
   
    public String getDaemonUUID(){
        return this.daemonUUID;
    }
    public void setDaemonUUID(String daemon){
        this.daemonUUID = daemon;
    }
    
    //code below for test purpose
    public static Task sampleData(){
        Task sample = new Task();
        sample.setComment("this is a sample task");
        sample.setCreateDate(new Date());
        sample.setFinishDate(new Date());
        sample.setId(1);

        sample.setModifyDate(new Date());
        sample.setName("test");
        sample.setOwner(Account.sampleData());
        sample.setPriorityRank(Priority.LOW);
        sample.setQueueRank(0);
        sample.setStatus(Status.NEW);
        return sample;
    }
    
}
