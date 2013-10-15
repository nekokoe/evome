/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import org.evome.evoKalc.client.shared.*;


/**
 *
 * @author nekoko
 */
public interface GWTServiceAsync {
    //public void start(AsyncCallback<?> callback);
    
    public void getServerTime(AsyncCallback<Long> callback);
    
    //===========================ACCOUNT SERVICES===============================
    public void createSession(AsyncCallback<Session> callback);
    public void getSession(String uuid, AsyncCallback<Session> callback);
    public void activeSession(Session s, AsyncCallback<Boolean> callback);
    
    public void signInAndBindSession(Account a, Session s, String accountKey, AsyncCallback<Session> callback); 
    public void signOutAndDetachSession(Session s, String accountKey, AsyncCallback<Session> callback);    
    
    public void anonymousAccount(AsyncCallback<Account> callback);
    public void createAccount(Account a, AsyncCallback<Account> callback);
    public void getAccount(int account_id, AsyncCallback<Account> callback);
    public void findAccount(String email, AsyncCallback<Account> callback);
    public void editAccount(Session s, Account a, AsyncCallback<Boolean> callback);
    public void delAccount(Session s, Account a, AsyncCallback<Boolean> callback);  
    
    //===========================DATABASE SERVICES==============================
    //task manipulation
    public void addTask(Session s, Task t, AsyncCallback<Task> callback);
    public void editTask(Session s, Task t, AsyncCallback<Boolean> callback);
    public void delTask(Session s, Task t, AsyncCallback<Boolean> callback); 
    public void getTask(Session s, int task_id, AsyncCallback<Task> callback);
    
    
    public void addResource(Session s, Resource res, AsyncCallback<Integer> callback);
    public void editResource(Session s, Resource res, AsyncCallback<Boolean> callback);    
    public void delResource(Session s, Resource res, AsyncCallback<Boolean> callback);
    public void getResource(Session s, int res_id, AsyncCallback<Resource> callback);    
    
    //get child for TreeView
    public void userTasks(Session s, Account a, Task.Classify c, AsyncCallback<ArrayList<Task>> callback);
    
    
    //===========================RESOURCE OPERATIONS============================
    //get parsed fasta seq id
    public void parseSeqIDs(Session s, Resource res, AsyncCallback<ArrayList<Sequence>> callback);
    public void childResources(Session s, String uuid, AsyncCallback<ArrayList<Resource>> callback);    
    public void userResources(Session s, Account a, Resource.Type type, AsyncCallback<ArrayList<Resource>> callback);
    public void uploadAsResource(UploadInfo info, AsyncCallback<Resource> callback);
    public void savePairList(Session s, ArrayList<Pair> pairlist, AsyncCallback<Resource> callback);    
    
    //==========================OTHERS==========================================
    public void saveConfig(Session s, Config c, AsyncCallback<Boolean> callback);
    public void createTaskByWizard(Session s, Task t, ArrayList<Pair> l, Config c, AsyncCallback<Task> callback);
    public void editTaskByWizard(Session s, Task t, ArrayList<Pair> l, Config c, AsyncCallback<Task> callback);
}
