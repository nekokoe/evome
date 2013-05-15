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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.Dialog;
import com.google.gwt.user.client.ui.SimplePanel;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import org.evome.KaKsCalc.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.info.Info;
/**
 *
 * @author nekoko
 * 
 * ProjectUtils provide tools for project management
 * 
 */
public class ProjectUtils extends Composite {
    
    private Project myproject = new Project();
    private TreeStore store = Workspace.getTreeView().getTreeStore(); //the tree store to update
    
    private static GWTServiceAsync rpc = Shared.getService();
    private static ProjectUtilsUiBinder uiBinder = GWT.create(ProjectUtilsUiBinder.class);
    
    interface ProjectUtilsUiBinder extends UiBinder<Widget, ProjectUtils> {
    }
    
    public ProjectUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        this.setProject(Project.sampleData());
    }
    
    public ProjectUtils(Project project){
        initWidget(uiBinder.createAndBindUi(this));
        this.setProject(project);
    }
    
    public ProjectUtils(TreeViewItem tvi){
        initWidget(uiBinder.createAndBindUi(this));
        final ProjectUtils projutil = this;
        rpc.getProject(tvi.getId(), new AsyncCallback<Project>(){
            @Override
            public void onSuccess(Project project){
                projutil.setProject(project);
            }
            @Override
            public void onFailure(Throwable caught){}
        });        
    }
    
    @UiHandler("btnProjectAdd")
    public void btnProjectAddClick(SelectEvent event){
        ProjectEdit add = new ProjectEdit();
        add.setHeadingText("Add New Project");
        add.show();
    }
    
    @UiHandler("btnProjectEdit")
    public void btnProjectEditClick(SelectEvent event){
        ProjectEdit edit = new ProjectEdit(myproject);
        edit.setHeadingText("Edit Project : " + myproject.getName());
        edit.show();
    }
    
    @UiHandler("btnProjectDel")
    public void btnProjectDelClick(SelectEvent event){
        ConfirmMessageBox confirm = 
                new ConfirmMessageBox("Confirm", "Are you sure want to delete : " + myproject.getName() + " ? <br>"
                + "All data under this project will be deleted!");
        confirm.addHideHandler(new HideEvent.HideHandler(){
            @Override
            public void onHide(HideEvent event){
                MessageBox source = (MessageBox)event.getSource();
                if (source.getHideButton() == source.getButtonById(Dialog.PredefinedButton.YES.name())){
                    rpc.delProject(myproject, new AsyncCallback<Boolean>(){
                        @Override
                        public void onSuccess(Boolean b){
                            if (b){
                                store.remove(new TreeViewItem("project",myproject.getId(),myproject.getName()));
                            }else{
                                Info.display("Error", "Failed to delete " + myproject.getName());
                            }
                        }
                        @Override
                        public void onFailure(Throwable caught){
                            Info.display("Error", "Communication with server failed.");
                        }
                        
                    });
                }
            }
        });
        confirm.show();
    }
    
    @UiHandler("btnCalcAdd")
    public void btnCalcAddClick(SelectEvent event){
        CalculationAdd add = new CalculationAdd(myproject);
        add.show();        
    }
    
    
    
    @UiField
    SimplePanel panel;
    
    
    public final void setProject(Project project){
        this.myproject = project;
        panel.clear();
        panel.add(new ProjectStatus(project));
    }
    
}
