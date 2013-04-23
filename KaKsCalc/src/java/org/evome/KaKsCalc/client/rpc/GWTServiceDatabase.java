/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.rpc;

import org.evome.KaKsCalc.client.*;

/**
 *
 * @author nekoko
 */
public interface GWTServiceDatabase {
    //Database services of KaKsCalc
    //add operations returns the generated ID, failed if 0 returned
    public int addNewProject(Project project);
    public int addNewCalculation(Calculation calc);
    public int addNewTask(Task task);
    
    public boolean editProject(Project project);
    public boolean editCalculation(Calculation calc);
    public boolean editTask(Task task);
    
    public boolean delProject(Project project);
    public boolean delCalculation(Calculation calc);
    public boolean delTask(Task task);
    
    //get database instances by passing IDs
    public Project getProject(int project_id);
    public Calculation getCalculation(int calc_id);
    public Task getTask(int task_id);
}
