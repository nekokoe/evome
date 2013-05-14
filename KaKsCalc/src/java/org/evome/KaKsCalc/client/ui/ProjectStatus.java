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
import com.google.gwt.user.client.ui.Label;

import org.evome.KaKsCalc.client.Project;
/**
 *
 * @author nekoko
 */
public class ProjectStatus extends Composite {
    
    private static ProjectStatusUiBinder uiBinder = GWT.create(ProjectStatusUiBinder.class);
    
    interface ProjectStatusUiBinder extends UiBinder<Widget, ProjectStatus> {
    }
    
    public ProjectStatus() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    public ProjectStatus(Project p){
        initWidget(uiBinder.createAndBindUi(this));
        lblID.setText(String.valueOf(p.getId()));
        lblName.setText(p.getName());
        lblOwner.setText(p.getOwner().getFullName());
        lblCreate.setText(p.getCreateDate().toString());
        lblModify.setText(p.getModifyDate().toString());
        lblComment.setText(p.getComment());
    }
    
    @UiField
    Label lblID, lblName, lblOwner, lblCreate, lblModify, lblComment;
    
}
