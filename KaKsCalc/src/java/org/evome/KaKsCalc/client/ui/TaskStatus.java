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
    
    private Task current;
    
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
        this.current = task;
        lblID.setText(Integer.toString(task.getId()));
        lblName.setText(task.getName());
        lblStatus.setText(task.getStatus().name());
        lblOwner.setText(task.getOwner().getFullName());
        lblCalc.setText(task.getCalculation().getName());
        lblProject.setText(task.getProject().getName());
        lblQueueRank.setText(Integer.toString(task.getQueueRank()));
        lblPriority.setText(task.getPriorityRank().name());
        lblCreate.setText(task.getCreateDate().toString());
        lblModify.setText(task.getModifyDate().toString());
        lblFinish.setText(task.getFinishDate().toString());
        lblParam.setText("c=" + task.getKaKsGeneticCode() + "; m=" + task.getKaKsMethod());
        lblComment.setText(task.getComment());
    }
    
}
