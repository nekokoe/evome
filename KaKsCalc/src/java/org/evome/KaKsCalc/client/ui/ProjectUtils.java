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
import com.sencha.gxt.widget.core.client.Window;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.Dialog;
/**
 *
 * @author nekoko
 * 
 * ProjectUtils provide tools for project management
 * 
 */
public class ProjectUtils extends Composite {
    
    private static ProjectUtilsUiBinder uiBinder = GWT.create(ProjectUtilsUiBinder.class);

    final Dialog dlgEditProject = new Dialog();
    
    interface ProjectUtilsUiBinder extends UiBinder<Widget, ProjectUtils> {
    }
    
    public ProjectUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        dlgEditProject.add(new ProjectEdit());
        dlgEditProject.setModal(true);
    }
    
    @UiHandler("btnProjectAdd")
    public void btnProjectAddClick(SelectEvent event){
        dlgEditProject.show();
        dlgEditProject.center();
    }
}
