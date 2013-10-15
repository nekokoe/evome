/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
import org.evome.evoKalc.client.shared.*;

/**
 *
 * @author nekoko
 */

@RemoteServiceRelativePath("gwtservice")
public interface GWTService extends RemoteService {

    //public void start();//rpc init: set sysconf and dbconn into GWTServiceImpl on server side.
    
    public long getServerTime(); //server time in second from 1970-1-1
    
    
    //==============================ACCOUNT SERVICES============================
    //to take effect, all operations should be bound with Session, as this
    //    public boolean editAccount(Session s, Account a);
    
    //session create and active; p.s. session is destoryed automatically
    public Session createSession();
    public Session getSession(String uuid);
    public boolean activeSession(Session s);
    
    
    //sign in and out
    public Session signInAndBindSession(Account a, Session s, String accountKey);    
    public Session signOutAndDetachSession(Session s, String accountKey);
    
    public Account anonymousAccount();
    public Account createAccount(Account a);
    public Account getAccount(int account_id);
    public Account findAccount(String email);
    public boolean editAccount(Session s, Account a);
    public boolean delAccount(Session s, Account a);    
    
    
    //==============================DATABASE SERVICES===========================
    //Database services of KaKsCalc
    //add operations returns the final instance on server side, null if failed
    //task manipulation
    public Task addTask(Session s, Task t);
    public boolean editTask(Session s, Task t);
    public boolean delTask(Session s, Task t);
    public Task getTask(Session s, int task_id);    
    
    //resource manipulation
    public int addResource(Session s, Resource res);
    public boolean editResource(Session s, Resource res);
    public boolean delResource(Session s, Resource res);
    public Resource getResource(Session s, int res_id);
    
    //show user tasks
    public ArrayList<Task> userTasks(Session s, Account a, Task.Classify c);
    
    //=============================RESOURCES OPERATION==========================
    //get parsed fasta seq id
    public ArrayList<Sequence> parseSeqIDs(Session s, Resource res);
    public ArrayList<Resource> childResources(Session s, String uuid);
    public ArrayList<Resource> userResources(Session s, Account a, Resource.Type type);
    public Resource uploadAsResource(UploadInfo info); 
    public Resource savePairList(Session s, ArrayList<Pair> pairlist);
    //=============================miscellaneous================================
    public boolean saveConfig(Session s, Config c);
    
    public Task createTaskByWizard(Session s, Task t, ArrayList<Pair> l, Config c);
    public Task editTaskByWizard(Session s, Task t, ArrayList<Pair> l, Config c);
}
