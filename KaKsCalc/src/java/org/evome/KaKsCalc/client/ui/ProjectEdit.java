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
import org.evome.KaKsCalc.client.Project;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.google.gwt.uibinder.client.UiHandler;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.Window;
/**
 *
 * @author nekoko
 */
public class ProjectEdit extends Window {
    
    private static ProjectEditUiBinder uiBinder = GWT.create(ProjectEditUiBinder.class);
    
    interface ProjectEditUiBinder extends UiBinder<Widget, ProjectEdit> {
    }
    
    public ProjectEdit(Project project){
        super();
        setWidget(uiBinder.createAndBindUi(this));
        init();
        //fill fields with given project
        txtProjectID.setValue(Integer.toString(project.getId()));
        txtProjectOwner.setValue(project.getOwner().getFullName());
        txtProjectName.setValue(project.getName());
        txtProjectComment.setValue(project.getComment());
//        //set id and owner read only
        txtProjectID.setReadOnly(true);
        txtProjectOwner.setReadOnly(true);        
    }
    
    public ProjectEdit() {
        super();
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
        //process data update
        this.hide();
    }
    
    @UiHandler("btnCancel")
    public void onCancelClick(SelectEvent event){
        //return
        this.hide();
    }
    
    
    private void init(){
        this.setModal(true);
        this.setMinWidth(300);
        this.setMinHeight(200);
        
    }
    
}
