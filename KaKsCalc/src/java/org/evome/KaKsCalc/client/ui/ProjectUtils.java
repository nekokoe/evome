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
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.Dialog;
import com.google.gwt.user.client.ui.SimplePanel;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import org.evome.KaKsCalc.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author nekoko
 * 
 * ProjectUtils provide tools for project management
 * 
 */
public class ProjectUtils extends Composite {
    
    private Project myproject = new Project();

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
    
    public ProjectUtils(int proj_id){
        initWidget(uiBinder.createAndBindUi(this));
        final ProjectUtils projutil = this;
        rpc.getProject(proj_id, new AsyncCallback<Project>(){
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
        ProjectEdit edit = new ProjectEdit();
        edit.show();
    }
    
    @UiHandler("btnProjectEdit")
    public void btnProjectEditClick(SelectEvent event){
        ProjectEdit edit = new ProjectEdit(myproject);
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
                    //YES clicked
                    //perform project deletion
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
