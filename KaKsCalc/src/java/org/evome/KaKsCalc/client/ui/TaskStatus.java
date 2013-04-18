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
/**
 *
 * @author nekoko
 */
public class TaskStatus extends Composite {
    
    private static TaskStatusUiBinder uiBinder = GWT.create(TaskStatusUiBinder.class);
    
    interface TaskStatusUiBinder extends UiBinder<Widget, TaskStatus> {
    }
    
    public TaskStatus() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiField
    Label lblID, lblName, lblStatus, lblOwner, lblCalc, lblProject, lblQueueRank, lblPriority, lblCreate, lblModify, lblFinish, lblParam, lblComment;
    
    
}
