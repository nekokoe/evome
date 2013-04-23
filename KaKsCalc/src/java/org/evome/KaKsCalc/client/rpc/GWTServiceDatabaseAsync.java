/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.evome.KaKsCalc.client.*;

/**
 *
 * @author nekoko
 */
public interface GWTServiceDatabaseAsync {
    public void addNewProject(Project project, AsyncCallback<Integer> callback);
    public void addNewCalculation(Calculation calc, AsyncCallback<Integer> callback);
    public void addNewTask(Task task, AsyncCallback<Integer> callback);
    
    public void editProject(Project project, AsyncCallback<Boolean> callback);
    public void editCalculation(Calculation calc, AsyncCallback<Boolean> callback);
    public void editTask(Task task, AsyncCallback<Boolean> callback);
    
    public void delProject(Project project, AsyncCallback<Boolean> callback);
    public void delCalculation(Calculation calc, AsyncCallback<Boolean> callback);
    public void delTask(Task task, AsyncCallback<Boolean> callback); 
    
    public Project getProject(int project_id, AsyncCallback<Project> callback);
    public Calculation getCalculation(int calc_id, AsyncCallback<Calculation> callback);
    public Task getTask(int task_id, AsyncCallback<Task> callback);
}
