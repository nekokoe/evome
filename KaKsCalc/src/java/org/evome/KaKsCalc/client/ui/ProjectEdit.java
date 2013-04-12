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

/**
 *
 * @author nekoko
 */
public class ProjectEdit extends Composite {
    
    private static ProjectEditUiBinder uiBinder = GWT.create(ProjectEditUiBinder.class);
    
    interface ProjectEditUiBinder extends UiBinder<Widget, ProjectEdit> {
    }
    
    public ProjectEdit(Project project){
        initWidget(uiBinder.createAndBindUi(this));
        //set id and owner read only
        txtProjectID.setReadOnly(true);
        txtProjectOwner.setReadOnly(true);
        //fill fields with given project
        txtProjectID.setText(Integer.toString(project.getId()));
        txtProjectOwner.setText(project.getOwnerName());
        txtProjectName.setText(project.getName());
        txtProjectComment.setText(project.getComment());
    }
    
    public ProjectEdit() {
        initWidget(uiBinder.createAndBindUi(this));
        txtProjectID.setVisible(false);
        txtProjectOwner.setVisible(false);
    }
    
    @UiField
    TextField txtProjectID, txtProjectOwner, txtProjectName, txtProjectComment;
}
