/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kakscalcdaemon;

/**
 *
 * @author nekoko
 */
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {

    private int task_id, task_status, task_owner, task_calc, task_project, task_QR, task_PR;
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
    
    public void setCalc(int calc){
        this.task_calc = calc;        
    }
    
    public void setProjcet(int proj){
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
    public int getCalc(){
        return task_calc;
    }
    public int getProject(){
        return task_project;
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
    
    
    private int job_id, job_queue, job_status, job_prank, job_qrank;
    private String job_name;
    private Date job_submit;
      
    //define queue job status
    public static final int JOB_QUEUE = 1,
            JOB_RUN = 2,
            JOB_HOLD = 3,
            JOB_ERR = 4,
            JOB_KILL = 5,
            JOB_FINISH = 6;
    
    public void setJobId(int id){
        this.job_id = id;
    }
    public void setJobQueue(int queue){
        this.job_queue = queue;
    }
    public void setJobStatus(int status){
        this.job_status = status;
    }
    public void setJobPriorityRank(int prank){
        this.job_prank = prank;
    }
    public void setJobQueueRank(int qrank){
        this.job_qrank = qrank;
    }
    public void setJobName(String name){
        this.job_name = name;
    }
    public void setJobSubmitDate(Date submit){
        this.job_submit = submit;
    }
    
    public int getJobId(){
        return this.job_id;
    }
    public int getJobQueu(){
        return this.job_queue;
    }
    public int getJobStatus(){
        return this.job_status;
    }
    public int getJobPriorityRank(){
        return this.job_prank;
    }
    public int getJobQueueRank(){
        return this.job_qrank;
    }
    public String getJobName(){
        return this.job_name;
    }
    public Date getJobSubmitDate(){
        return this.job_submit;
    }
    
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
}
