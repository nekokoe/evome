/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.evome.KaKsCalc.client.shared.UploadInfo;
import java.util.ArrayList;

/**
 *
 * @author nekoko
 */
public interface GWTServiceAsync {
    //public void start(AsyncCallback<?> callback);
    
    public void getServerTime(AsyncCallback<Long> callback);
    
    //===========================ACCOUNT SERVICES===============================
    public void createSession(Account account, AsyncCallback<Session> callback);
    public void authenticValidation(Account a, Session s, String valkey, AsyncCallback<Boolean> callback);
    public void createAccount(Account account, AsyncCallback<Integer> callback);
    public void getAccount(int account_id, AsyncCallback<Account> callback);    
    public void editAccount(Account account, AsyncCallback<Boolean> callback);
    public void delAccount(Account account, AsyncCallback<Boolean> callback);  
    
    //===========================DATABASE SERVICES==============================
    public void addNewProject(Project project, AsyncCallback<Integer> callback);
    public void addNewCalculation(Calculation calc, AsyncCallback<Integer> callback);
    public void addNewTask(Task task, AsyncCallback<Task> callback);
    public void addResource(Resource res, AsyncCallback<Integer> callback);
    
    public void editProject(Project project, AsyncCallback<Boolean> callback);
    public void editCalculation(Calculation calc, AsyncCallback<Boolean> callback);
    public void editTask(Task task, AsyncCallback<Boolean> callback);
    public void editResource(Resource res, AsyncCallback<Boolean> callback);    
    
    public void delProject(Project project, AsyncCallback<Boolean> callback);
    public void delCalculation(Calculation calc, AsyncCallback<Boolean> callback);
    public void delTask(Task task, AsyncCallback<Boolean> callback); 
    public void delResource(Resource res, AsyncCallback<Boolean> callback);
    
    public void getProject(int project_id, AsyncCallback<Project> callback);
    public void getCalculation(int calc_id, AsyncCallback<Calculation> callback);
    public void getTask(int task_id, AsyncCallback<Task> callback);
    public void getResource(int res_id, AsyncCallback<Resource> callback);    
    
    //get child for TreeView
    public void userProjects(Account account, AsyncCallback<ArrayList<Project>> callback);
    public void subCalculations(Project proj, AsyncCallback<ArrayList<Calculation>> callback);
    public void subTasks(Calculation calc, AsyncCallback<ArrayList<Task>> callback);
    
    //get parsed fasta seq id
    public void parseSeqIDs(Resource res, AsyncCallback<ArrayList<String>> callback);
    public void childResources(String uuid, AsyncCallback<ArrayList<Resource>> callback);    
    public void uploadAsResource(UploadInfo info, AsyncCallback<Resource> callback);
}
