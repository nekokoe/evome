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
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import org.evome.KaKsCalc.client.Task;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import org.evome.KaKsCalc.client.*;

/**
 *
 * @author nekoko
 */
public class TaskUtils extends Composite {
    //inner data
    private Task mytask;
    private TreeViewItem mytvi;
    //rpc service
    private static GWTServiceAsync rpc = Shared.getService();
    //public widgets
    private PortalLayoutContainer portal = initPortalContainer();
    private Portlet statuslet = new Portlet();    
    
    //UI binder
    private static TaskUtilsUiBinder uiBinder = GWT.create(TaskUtilsUiBinder.class);    
    interface TaskUtilsUiBinder extends UiBinder<Widget, TaskUtils> {
    }
    
    @UiField
    SimplePanel panel;

    
    public TaskUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        portal.add(statuslet, 2);
        panel.add(portal);
    }
    
    public TaskUtils(Task task){
        this();
        setTask(task);
    }
    
    public TaskUtils(TreeViewItem tvi){
        this();
        setTask(tvi);
    }
    
    public final void setTask(Task task){
        this.mytask = task;
        this.mytvi = new TreeViewItem(TreeViewItem.Type.TASK, task.getId(), task.getName());
        statuslet.setHeadingText("Task Status");
        statuslet.setCollapsible(true);
        statuslet.setWidget(initStatusGrid(task));
    }
    
    public final void setTask(TreeViewItem tvi){
        final TaskUtils tu = this;
        rpc.getTask(tvi.getId(), new AsyncCallback<Task>(){
            @Override
            public void onSuccess(Task task){
                tu.setTask(task);
            }
            @Override
            public void onFailure(Throwable caught){}
        });        
    }

    @UiHandler("btnTaskAdd")
    public void onTaskAddClick(SelectEvent event){
        
    }
    
    @UiHandler("btnTaskEdit")
    public void onTaskEditClick(SelectEvent event){
        
    }
    
    @UiHandler("btnTaskDel")
    public void onTaskDelClick(SelectEvent event){
        
    }
    
    
    private PortalLayoutContainer initPortalContainer() {
        PortalLayoutContainer plc = new PortalLayoutContainer(3);
        plc.setColumnWidth(0, .50);
        plc.setColumnWidth(1, .25);
        plc.setColumnWidth(2, .25);
        return plc;
    }

    private Grid initStatusGrid(Task t) {
        Grid status = new Grid(13, 3);
        status.setText(0, 0, "ID");
        status.setText(0, 1, String.valueOf(t.getId()));
        status.setText(1, 0, "Name");
        status.setText(1, 1, t.getName());
        status.setText(2, 0, "Owner");
        status.setText(2, 1, t.getOwner().getFullName());
        status.setText(3, 0, "Calculation");
        status.setText(3, 1, t.getCalculation().getName());
        status.setText(4, 0, "Project");
        status.setText(4, 1, t.getProject().getName());
        status.setText(5, 0, "Status");
        status.setText(5, 1, t.getStatus().name());
        status.setText(6, 0, "Rank");
        status.setText(6, 1, Integer.toString(t.getQueueRank()));
        status.setText(7, 0, "Priority");
        status.setText(7, 1, t.getPriorityRank().name());
        status.setText(8, 0, "Created");
        status.setText(8, 1, t.getCreateDate().toString());
        status.setText(9, 0, "Modified");
        status.setText(9, 1, t.getModifyDate().toString());
        status.setText(10, 0, "Finished");
        status.setText(10, 1, t.getFinishDate().toString());
        status.setText(11, 0, "Parameters");
        status.setText(11, 1, "c=" + t.getKaKsGeneticCode() + "; m=" + t.getKaKsMethod());
        status.setText(12, 0, "Description");
        status.setText(12, 1, t.getComment());
        return status;
    }
    
    
}
