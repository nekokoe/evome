/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.google.gwt.uibinder.client.UiHandler;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.evome.KaKsCalc.client.*;
import com.sencha.gxt.widget.core.client.box.ProgressMessageBox;
import com.sencha.gxt.data.shared.TreeStore;
/**
 *
 * @author nekoko
 */
public class ProjectEdit extends Window {
    
    private static ProjectEditUiBinder uiBinder = GWT.create(ProjectEditUiBinder.class);
    private static GWTServiceAsync rpc = Shared.getService();
    
    private TreeStore store = Workspace.getTreeView().getTreeStore(); //the tree store to update
    
    interface ProjectEditUiBinder extends UiBinder<Widget, ProjectEdit> {
    }
    
    public ProjectEdit(Project project){
        setWidget(uiBinder.createAndBindUi(this));
        init();
        //fill fields with given project
        txtProjectID.setValue(Integer.toString(project.getId()));
        txtProjectOwner.setValue(project.getOwner().getFullName());
        txtProjectName.setValue(project.getName());
        txtProjectComment.setValue(project.getComment());
        //set id and owner read only
        txtProjectID.setReadOnly(true);
        txtProjectOwner.setReadOnly(true);        
    }
    
    public ProjectEdit() {
        //this constructor means add new Project
        setWidget(uiBinder.createAndBindUi(this));
        init();
        fieldID.setVisible(false);
        fieldOwner.setVisible(false);
    }
    
    
    
    @UiField
    TextField txtProjectID, txtProjectOwner, txtProjectName, txtProjectComment;
    @UiField
    FieldLabel fieldID, fieldOwner;
    
    @UiHandler("btnSave")
    public void onSaveClick(SelectEvent event){
        this.hide();
        //process data update
        final Project p = new Project();
        p.setName(txtProjectName.getText());
        p.setOwner(KaKsCalc.getAccount());
        p.setComment(txtProjectComment.getText().replaceAll("'", "&#39"));
        
        final ProgressMessageBox pmb = new ProgressMessageBox("In progess", "Communicating with the server, please wait...");
        pmb.setModal(true);
        pmb.show();
        
        rpc.addNewProject(p, new AsyncCallback<Integer>(){
           @Override
           public void onSuccess(Integer pid){
               pmb.updateProgress(1,"Done.");
               store.add(new TreeViewItem("project",pid, p.getName()));
           }
           @Override
           public void onFailure(Throwable caught){
               
           }
        });
        pmb.hide();

    }
    
    @UiHandler("btnCancel")
    public void onCancelClick(SelectEvent event){
        //return
        this.hide();
    }
    
    
    private void init(){
        this.setModal(true);
        this.setMinWidth(360);
        this.setMinHeight(300);
    }
    
}
