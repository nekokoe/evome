/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.data.shared.ListStore;
import org.evome.KaKsCalc.client.Task;

/**
 *
 * @author nekoko
 */
public class TaskEdit extends Window {
    
    private static TaskEditUiBinder uiBinder = GWT.create(TaskEditUiBinder.class);
    
    interface TaskEditUiBinder extends UiBinder<Widget, TaskEdit> {
    }
    
    public TaskEdit() {
        setWidget(uiBinder.createAndBindUi(this));
    }
    
    public TaskEdit(Task task){
        setWidget(uiBinder.createAndBindUi(this));
        
    }
    
    @UiField
    FieldLabel fieldID, fieldOwner, fieldProject, fieldCalc;
    @UiField
    TextField txtID, txtOwner, txtProject, txtCalc, txtName, txtComment;
//    @UiField
//    ComboBox comboCodon;
    
    
            
}
