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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.evome.KaKsCalc.client.Task;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import org.evome.KaKsCalc.client.*;

/**
 *
 * @author nekoko
 */
public class TaskUtils extends Composite {
    
    private Task mytask;
    
    private static TaskUtilsUiBinder uiBinder = GWT.create(TaskUtilsUiBinder.class);
    private static GWTServiceAsync rpc = Shared.getService();
    
    interface TaskUtilsUiBinder extends UiBinder<Widget, TaskUtils> {
    }
    
    public TaskUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        setCurrentTask(Task.sampleData());
    }
    
    public TaskUtils(Task task){
        initWidget(uiBinder.createAndBindUi(this));
        setCurrentTask(task);
    }
    
    public TaskUtils(TreeViewItem tvi){
        initWidget(uiBinder.createAndBindUi(this));
        final TaskUtils tu = this;
        rpc.getTask(tvi.getId(), new AsyncCallback<Task>(){
            @Override
            public void onSuccess(Task task){
                tu.setCurrentTask(task);
            }
            @Override
            public void onFailure(Throwable caught){}
        });
        
    }
    
    public final void setCurrentTask(Task task){
        this.mytask = task;
        panel.clear();
        panel.add(new TaskStatus(task));
    }
    
    public Task getCurrentTask(){
        return this.mytask;
    }
    
    @UiField
    VerticalPanel panel;
    
    @UiHandler("btnTaskEdit")
    public void onTaskEditClick(SelectEvent event){
        panel.clear();
        panel.add(new TaskEdit(mytask));
    }
}
