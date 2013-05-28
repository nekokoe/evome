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
import com.sencha.gxt.widget.core.client.form.TextArea;
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
    
    private Project myproject; //store the project passed in
    private boolean isUpdated = false;
    private TreeViewItem mytvi;
    
    interface ProjectEditUiBinder extends UiBinder<Widget, ProjectEdit> {
    }
    
    public ProjectEdit(Project project){
        setWidget(uiBinder.createAndBindUi(this));
        init();
        this.myproject = project;
        //fill fields with given project
        txtProjectID.setValue(Integer.toString(project.getId()));
        txtProjectOwner.setValue(project.getOwner().getFullName());
        txtProjectName.setValue(project.getName());
        txtProjectComment.setValue(project.getComment());
        //set id and owner read only
        txtProjectID.setReadOnly(true);
        txtProjectOwner.setReadOnly(true);                
    }
    
    public ProjectEdit(){
        setWidget(uiBinder.createAndBindUi(this));        
    }
    
    @UiField
    TextField txtProjectID, txtProjectOwner, txtProjectName;
    @UiField
    TextArea txtProjectComment;
    
    @UiHandler("btnSave")
    public void onSaveClick(SelectEvent event){
        final ProjectEdit pe = this;
        final Project p = new Project();        
        p.setId(myproject.getId());
        p.setName(txtProjectName.getText());
        p.setOwner(myproject.getOwner());
        p.setComment(txtProjectComment.getText());
        
        final ProgressMessageBox pmb = new ProgressMessageBox("In progess", "Communicating with the server, please wait...");
        pmb.setModal(true);
        pmb.show();
        
        rpc.editProject(p, new AsyncCallback<Boolean>(){
            @Override
            public void onSuccess(Boolean b) {
                pmb.updateProgress(1, "Done.");
                pe.isUpdated = b;
                pe.mytvi = new TreeViewItem("project", p.getId(), p.getName());
                pmb.hide();
                pe.hide();
            }
            @Override
            public void onFailure(Throwable caught) {
                pmb.hide();
                pe.hide();                
            }
        });
    }
    
    @UiHandler("btnCancel")
    public void onCancelClick(SelectEvent event){
        //return
        this.hide();
    }
    
    
    private void init(){
        this.setModal(true);
        this.setMinWidth(400);
        this.setMinHeight(400);
        this.setWidth(400);
        this.setHeight(400);
    }
    
    public boolean isUpdated(){
        return this.isUpdated;
    }
    
    public TreeViewItem getMyTreeViewItem(){
        return this.mytvi;
    }
    
}
