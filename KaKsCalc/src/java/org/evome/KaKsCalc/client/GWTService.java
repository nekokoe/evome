/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
import org.evome.KaKsCalc.client.shared.UploadInfo;

/**
 *
 * @author nekoko
 */
@RemoteServiceRelativePath("gwtservice")
public interface GWTService extends RemoteService {

    //public void start();//rpc init: set sysconf and dbconn into GWTServiceImpl on server side.
    
    public long getServerTime(); //server time in second from 1970-1-1
    
    
    //==============================ACCOUNT SERVICES============================
    //note: non-safety rpc call, for authentic validation is not enforced on every rpc call
    //could be enforced with requiring an authToken with authenticValidation
    //all operations should provide a authToken to take effect, just like
    //authToken = authenticValidation(...);
    //editAccount(account, authToken);
    public Session createSession(Account account);
    public boolean authenticValidation(Account a, Session s, String valkey);
    public int createAccount(Account account);
    public Account getAccount(int account_id);    
    public boolean editAccount(Account account);
    public boolean delAccount(Account account);    
    
    
    //==============================DATABASE SERVICES===========================
    //Database services of KaKsCalc
    //add operations returns the generated ID, failed if 0 returned
    public int addNewProject(Project project);
    public int addNewCalculation(Calculation calc);
    public int addNewTask(Task task);
    public int addResource(Resource res);
    
    public boolean editProject(Project project);
    public boolean editCalculation(Calculation calc);
    public boolean editTask(Task task);
    public boolean editResource(Resource res);
    
    public boolean delProject(Project project);
    public boolean delCalculation(Calculation calc);
    public boolean delTask(Task task);
    public boolean delResource(Resource res);
    
    //get database instances by passing IDs
    public Project getProject(int project_id);
    public Calculation getCalculation(int calc_id);
    public Task getTask(int task_id);
    public Resource getResource(int res_id);
    
    //get sub nodes for TreeView
    public ArrayList<Project> userProjects(Account account);
    public ArrayList<Calculation> subCalculations(Project proj);
    public ArrayList<Task> subTasks(Calculation calc);
    
    
    //=============================RESOURCES OPERATION==========================
    //get parsed fasta seq id
    public ArrayList<String> parseFastaIDs(String filename);
    public ArrayList<Resource> childResources(String uuid);
    public Resource uploadAsResource(UploadInfo info); 
}
