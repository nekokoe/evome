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
import java.util.Date;

public class Task implements IsSerializable {
    //vars
    private int task_id, task_QR;
    private String task_comment, task_name;
    private String uuid, parent;
    private Date task_create, task_finish, task_modify, task_delete;
    private Project project;
    private Calculation calc;
    private Account owner;
    private Status status;
    private Priority priority;
    //kaks calculation params
    private Gencode kaks_code;
    private Method kaks_method;
    //Define task status
    public enum Status{
        TASK_NEW,TASK_READY,TASK_RUNNING,TASK_SUCCESS,TASK_ERROR,TASK_STOPPED,TASK_PAUSED,TASK_REMOVED
    }
    //Define task priority rank
    public enum Priority{
        TASK_PR_INTENSE,TASK_PR_HIGH,TASK_PR_NORMAL,TASK_PR_LOW,TASK_PR_RELAX
    }
    //define KaKs method
    public enum Method{
        NG,LWL,LPB,MLWL,MLPB,YN,MYN,GY,MS,MA,GNG,GLWL,GMLWL,GLPB,GMLPB,GYN,GMYN,ALL
    }
    //define genetic code
    //refer to: http://www.ncbi.nlm.nih.gov/Taxonomy/Utils/wprintgc.cgi?mode=c
    public enum Gencode{
        u0,Standard,VM,YM,MPCM,IM,CDHN,u7,u8,EFM,EN,BAPP,AYN,AM,AFM,BN,CM,u17,u18,u19,u20,TM,SOM,TM2,PM
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

    public void setCalculation(Calculation calc) {
        this.calc = calc;
    }

    public void setProjcet(Project proj) {
        this.project = proj;
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

    public Calculation getCalculation() {
        return calc;
    }

    public Project getProject() {
        return project;
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
    
    public void setKaKsGeneticCode(Gencode code){
        this.kaks_code = code;
    }
    public void setKaKsMethod(Method m){
        this.kaks_method = m;
    }
    public Gencode getKaKsGeneticCode(){
        return this.kaks_code;
    }
    public Method getKaKsMethod(){
        return this.kaks_method;
    }
    
    //code below for test purpose
    public static Task sampleData(){
        Task sample = new Task();
        sample.setCalculation(Calculation.sampleData());
        sample.setComment("this is a sample task");
        sample.setCreateDate(new Date());
        sample.setFinishDate(new Date());
        sample.setId(1);
        sample.setKaKsGeneticCode(Gencode.Standard);
        sample.setKaKsMethod(Method.MA);
        sample.setModifyDate(new Date());
        sample.setName("test");
        sample.setOwner(Account.sampleData());
        sample.setPriorityRank(Priority.TASK_PR_LOW);
        sample.setProjcet(Project.sampleData());
        sample.setQueueRank(0);
        sample.setStatus(Status.TASK_NEW);
        return sample;
    }
    
}
