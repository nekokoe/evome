/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

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
    
    public ProjectStatus(Project project){
        initWidget(uiBinder.createAndBindUi(this));        
    }
}
