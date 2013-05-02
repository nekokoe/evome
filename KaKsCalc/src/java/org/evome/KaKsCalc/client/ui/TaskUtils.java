/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.evome.KaKsCalc.client.Task;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;

/**
 *
 * @author nekoko
 */
public class TaskUtils extends Composite {
    
    private Task current;
    
    private static TaskUtilsUiBinder uiBinder = GWT.create(TaskUtilsUiBinder.class);
    
    interface TaskUtilsUiBinder extends UiBinder<Widget, TaskUtils> {
    }
    
    public TaskUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        Task task = Task.sampleData();
        panel.add(new TaskStatus(task));
    }
    
    @UiField
    VerticalPanel panel;
    
    @UiHandler("btnTaskEdit")
    public void onTaskEditClick(SelectEvent event){
        panel.clear();
        panel.add(new TaskEdit(current));
    }
}
