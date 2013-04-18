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
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.Dialog;
import com.google.gwt.user.client.ui.SimplePanel;
import org.evome.KaKsCalc.client.Project;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
/**
 *
 * @author nekoko
 * 
 * ProjectUtils provide tools for project management
 * 
 */
public class ProjectUtils extends Composite {
    
    private static ProjectUtilsUiBinder uiBinder = GWT.create(ProjectUtilsUiBinder.class);
    private Project current = new Project();
    
    interface ProjectUtilsUiBinder extends UiBinder<Widget, ProjectUtils> {
    }
    
    public ProjectUtils() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiHandler("btnProjectAdd")
    public void btnProjectAddClick(SelectEvent event){
        ProjectEdit edit = new ProjectEdit();
        edit.show();
    }
    
    @UiHandler("btnProjectEdit")
    public void btnProjectEditClick(SelectEvent event){
        ProjectEdit edit = new ProjectEdit(current);
        edit.show();
    }
    
    @UiHandler("btnProjectDel")
    public void btnProjectDelCLick(SelectEvent event){
        ConfirmMessageBox confirm = 
                new ConfirmMessageBox("Confirm", "Are you sure want to delete : " + current.getName() + " ? <br>"
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
    
    
    @UiField
    SimplePanel panel;
    
    
    public void setCurrentProject(Project project){
        this.current = project;
        panel.clear();
        panel.add(new ProjectStatus(current));
    }
}
