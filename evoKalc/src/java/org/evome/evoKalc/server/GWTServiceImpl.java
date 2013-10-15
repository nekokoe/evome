/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.evome.evoKalc.client.shared.*;
import org.evome.evoKalc.client.GWTService;


/**
 *
 * @author nekoko
 */
public class GWTServiceImpl extends RemoteServiceServlet implements GWTService {
    
    private static SysConfig sysconf;
    private static DBConnector dbconn;
    
    public static SysConfig getSysConfig(){
        return sysconf;
    }
    
    public static DBConnector getDBConn() {
        if (dbconn.isAlive()){
            return dbconn;
        }else{
            return dbconn = new DBConnector(sysconf);
        }
    }
    
    @Override
    public void init(){
        sysconf = new SysConfig(this.getServletContext().getRealPath("/"));
        dbconn = new DBConnector(sysconf);        
    }
    
   
    @Override
    public long getServerTime(){
        Date date = new Date();
        return date.getTime();
    }
    
    //==============================ACCOUNT SERVICES============================
    
    @Override
    public Session createSession(){
        return SessionManager.createSession();
    }
    @Override 
    public Session getSession(String uuid){
        return SessionManager.getSession(UUID.fromString(uuid));
    }
    
    @Override
    public boolean activeSession(Session s){
        return SessionManager.activeSession(s);
    }
    
    @Override
    public Session signInAndBindSession(Account a, Session s, String accountKey){
        String authKey = SessionManager.updateAuthKey(s, a, accountKey);
        if (authKey != null){
            s.setAuthKey(authKey);
            return SessionManager.bindSession(s, a);
        }else{
            return null;
        }
    }  
    @Override
    public Session signOutAndDetachSession(Session s, String accountKey) {
        if (SessionManager.detachSession(s)) {
            //and delete account if an anonymous account
            if (s.getAccount().isAnonymous()) {
                s.getAccount().setAccountKey(accountKey);   //rememver to set accountKey for it is not auto filled 
                AccountManager.removeAccount(s.getAccount());
            }
            s.setAccount(null);
            s.setAuthKey("");
            return s;
        }else{
            return null;
        }
    }
    
    @Override
    public Account anonymousAccount(){
        return AccountManager.anonymousAccount();
    }
    
    @Override    
    public Account createAccount(Account account){
        //to create a new account, find if account already exsists
        if (AccountManager.findAccount(account.getEmail()) == null){
            return AccountManager.createAccount(account);            
        }else{
            System.err.println("create account failed: account already exists!");
            return null;
        }
    }
    @Override
    public Account getAccount(int account_id){
        return AccountManager.getAccount(account_id);
    }
    @Override
    public boolean editAccount(Session s, Account account){
        if (SessionManager.isSessionValid(s)){
            return AccountManager.editAccount(account);
        }else{
            return false;
        }
    }
    @Override
    public boolean delAccount(Session s, Account account){
        return AccountManager.removeAccount(account);
    }
    @Override
    public Account findAccount(String email){
        return AccountManager.findAccount(email);
    }
    
    //==============================DATABASE SERVICES===========================
    //add operations returns the generated ID, failed if 0 returned
    @Override
    public Task addTask(Session s, Task t){
        return DatabaseManager.addTask(t);
    }    
    @Override
    public boolean editTask(Session s, Task task){
        return DatabaseManager.editTask(task);
    }
    @Override
    public boolean delTask(Session s, Task task){
        return DatabaseManager.delTask(task);
    }
    @Override
    public Task getTask(Session s, int task_id){
        return DatabaseManager.getTask(task_id);
    }
    @Override
    public ArrayList<Task> userTasks(Session s, Account a, Task.Classify c){
        return DatabaseManager.userTasks(a, c);
    }
    
    @Override
    public int addResource(Session s, Resource res){
        return DatabaseManager.addResource(res);
    }
    @Override
    public boolean editResource(Session s, Resource res){
        return DatabaseManager.editResource(res);
    }
    @Override
    public boolean delResource(Session s, Resource res){
        if (!SessionManager.isSessionValid(s)){
           return false; 
        }
        return DatabaseManager.delResource(res);
    }
    @Override
    public Resource getResource(Session s, int res_id){
        return DatabaseManager.getResource(res_id);
    }    
    
    //=======================RESOURCES SERVICES=================================
    @Override
    public ArrayList<Sequence> parseSeqIDs(Session s, Resource res) {
        return ResourceManager.parseSeqIDs(res);
    }

    @Override
    public ArrayList<Resource> childResources(Session s, String uuid){
        return DatabaseManager.childResources(UUID.fromString(uuid));
    }
    
    @Override
    public ArrayList<Resource> userResources(Session s, Account a, Resource.Type type){
        if (SessionManager.isSessionValid(s)){
            return DatabaseManager.userResource(a, type);
        }else{
            return null;
        }
    }
    
    @Override
    public Resource uploadAsResource(UploadInfo info){
        if (!SessionManager.isSessionValid(info.getSession())){
            return null;
        }
        switch (info.getType()){
            case TEXT:
                return ResourceManager.textAsResource(info);
            case SEQLIST:
                
            case FILE:
            default: 
                return ResourceManager.fileAsResource(info);                   
        }     
    }
    
    @Override
    public Resource savePairList(Session s, ArrayList<Pair> pairlist){
        return ResourceManager.savePairList(s.getAccount(), pairlist);
    }
    
    
    //===========================OTHERS=========================================
    
    @Override
    public boolean saveConfig(Session s, Config c){
        if (!SessionManager.isSessionValid(s) || c == null){
            return false;
        }
        Config conf = DatabaseManager.getConfig(c.getId());
        if (conf == null){
            //new
            if (DatabaseManager.addConfig(c) != null){
                return true;
            }
        }else{
            //edit
            if (DatabaseManager.editConfig(c) != null){
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Task createTaskByWizard(Session s, Task t, ArrayList<Pair> l, Config c){
        if (!SessionManager.isSessionValid(s) || t == null || l == null || c == null){
            return null;
        }
        //save pairlist
        Resource pairlist = ResourceManager.savePairList(s.getAccount(), l);
        if (pairlist == null){
            return null;
        }        
        //save config
        c.setPairlist(pairlist);
        Config conf = DatabaseManager.addConfig(c);
        if (conf == null){
            return null;
        }

        //create task
        t.setKaKsConfig(conf);
        Task task = DatabaseManager.addTask(t);
        
        if (task != null){
            task.setStatus(Task.Status.READY);
            DatabaseManager.editTask(task);
        }
        
        return task;
    }
    
    
    @Override
    public Task editTaskByWizard(Session s, Task t, ArrayList<Pair> l, Config c){
        if (!SessionManager.isSessionValid(s) || t == null || l == null || c == null){
            return null;
        }
        //save pairlist
        Resource pairlist = ResourceManager.savePairList(s.getAccount(), l);
        if (pairlist == null){
            return null;
        }        
        //delete old pairlist
        DatabaseManager.delResource(c.getPairlist());
        //save config
        c.setPairlist(pairlist);
        Config conf = DatabaseManager.editConfig(c);
        if (conf == null){
            return null;
        }

        //create task
        t.setKaKsConfig(conf);
        Task task = null;
        if (DatabaseManager.editTask(t)){
            task = DatabaseManager.getTask(t.getId());
        }
        
        if (task != null){
            task.setStatus(Task.Status.READY);
            DatabaseManager.editTask(task);
        }
        
        return task;        
    
    }
     
    
}
