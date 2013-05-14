/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.ArrayList;
import org.evome.KaKsCalc.client.GWTService;
import org.evome.KaKsCalc.client.Session;
import java.util.Date;
import java.util.UUID;
import org.evome.KaKsCalc.client.Account;
import org.evome.KaKsCalc.client.Calculation;
import org.evome.KaKsCalc.client.Project;
import org.evome.KaKsCalc.client.Task;

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
        return dbconn;
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
    
    @Override
    public String randomUUID(){
        return UUID.randomUUID().toString();
    }
    
    //==============================ACCOUNT SERVICES============================
    
    @Override
    public Session createSession(Account account){
        return SessionManager.createSession(account);
    }
    @Override
    public boolean authenticValidation(Account a, Session s, String valkey){
        return SessionManager.authenticValidation(s, a, valkey);
    }
    @Override
    public int createAccount(Account account){
        return AccountManager.createAccount(account);
    }
    @Override
    public Account getAccount(int account_id){
        return AccountManager.getAccount(account_id);
    }
    @Override
    public boolean editAccount(Account account){
        return AccountManager.editAccount(account);
    }
    @Override
    public boolean delAccount(Account account){
        return AccountManager.removeAccount(account);
    }
    
    //==============================DATABASE SERVICES===========================
    //add operations returns the generated ID, failed if 0 returned
    @Override
    public int addNewProject(Project project){
        return DatabaseManager.addProject(project);
    }
    
    @Override
    public int addNewCalculation(Calculation calc){
        return DatabaseManager.addCalculation(calc);
    }
    @Override
    public int addNewTask(Task task){
        return DatabaseManager.addTask(task);
    }
    
    //edit functions return if succeed
    @Override    
    public boolean editProject(Project project){
        return DatabaseManager.editProject(project);
    }
    @Override
    public boolean editCalculation(Calculation calc){
        return DatabaseManager.editCalculation(calc);
    }
    @Override
    public boolean editTask(Task task){
        return DatabaseManager.editTask(task);
    }
    
    @Override
    public boolean delProject(Project project){
        return DatabaseManager.delProject(project);
    }
    @Override
    public boolean delCalculation(Calculation calc){
        return DatabaseManager.delCalculation(calc);
    }
    @Override
    public boolean delTask(Task task){
        return DatabaseManager.delTask(task);
    }
    
    //get database instances by passing IDs
    @Override
    public Project getProject(int project_id){
        return DatabaseManager.getProject(project_id);
    }
    @Override
    public Calculation getCalculation(int calc_id){
        return DatabaseManager.getCalculation(calc_id);
    }
    @Override
    public Task getTask(int task_id){
        return DatabaseManager.getTask(task_id);
    }
    
    @Override
    public ArrayList<Project> userProjects(Account account){
        return DatabaseManager.userProjects(account);
    }
    @Override
    public ArrayList<Calculation> subCalculations(Project proj){
        return DatabaseManager.subCalculations(proj);
    }
    @Override
    public ArrayList<Task> subTasks(Calculation calc){
        return DatabaseManager.subTasks(calc);
    }
}
