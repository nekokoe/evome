/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import org.evome.KaKsCalc.client.Task;
/**
 *
 * @author nekoko
 */
public class TaskStatus extends Composite {
    
    private Task task;
    
    private static TaskStatusUiBinder uiBinder = GWT.create(TaskStatusUiBinder.class);
    
    interface TaskStatusUiBinder extends UiBinder<Widget, TaskStatus> {
    }
    
    public TaskStatus() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    public TaskStatus(Task task){
        initWidget(uiBinder.createAndBindUi(this));
        this.setTask(task);        
    }
    
    @UiField
    Label lblID, lblName, lblStatus, lblOwner, lblCalc, lblProject, lblQueueRank, lblPriority, lblCreate, lblModify, lblFinish, lblParam, lblComment;
    
    
    public final void setTask(Task task){
        this.task = task;
        lblID.setText(Integer.toString(task.getId()));
        lblName.setText(task.getName());
        lblStatus.setText(Task.statusText(task.getStatus()));
        lblOwner.setText(task.getOwnerText());
        lblCalc.setText(Integer.toString(task.getCalculation()));
        lblProject.setText(Integer.toString(task.getProject()));
        lblQueueRank.setText(Integer.toString(task.getQueueRank()));
        lblPriority.setText(Integer.toString(task.getPriorityRank()));
        lblCreate.setText(task.getCreateDate());
        lblModify.setText(task.getModifyDate());
        lblFinish.setText(task.getFinishDate());
        lblParam.setText("c=" + task.getKaKsGeneticCode() + "; m=" + task.getKaKsMethod());
        lblComment.setText(task.getComment());
    }
    
}
