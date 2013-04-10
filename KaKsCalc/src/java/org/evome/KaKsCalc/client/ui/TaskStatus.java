/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

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
}
