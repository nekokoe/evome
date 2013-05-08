/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.evome.KaKsCalc.client.Calculation;
import org.evome.KaKsCalc.client.Project;
import org.evome.KaKsCalc.client.Task;
import org.evome.KaKsCalc.client.rpc.GWTServiceDatabase;
import org.evome.KaKsCalc.server.DatabaseManager;

/**
 *
 * @author nekoko
 */
public class GWTServiceDatabaseImpl extends RemoteServiceServlet implements GWTServiceDatabase{
    //add operations returns the generated ID, failed if 0 returned
    @Override
    public int addNewProject(Project project){
        return DatabaseManager.addProject(project);
    };
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
}
