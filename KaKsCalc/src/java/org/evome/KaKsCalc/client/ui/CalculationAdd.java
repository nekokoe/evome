/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiField;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

import org.evome.KaKsCalc.client.Calculation;
import org.evome.KaKsCalc.client.Project;
/**
 *
 * @author nekoko
 */
public class CalculationAdd extends Window {
    
    private Project project;
    
    private static CalculationAddUiBinder uiBinder = GWT.create(CalculationAddUiBinder.class);
    
    interface CalculationAddUiBinder extends UiBinder<Widget, CalculationAdd> {
    }
    
    public CalculationAdd(Project project) {
        setWidget(uiBinder.createAndBindUi(this));
        this.project = project;
    }
    
    
    
    @UiField
    TextField txtName, txtComment;
    
    @UiHandler("btnSave")
    public void btnSaveClick(SelectEvent event){
        Calculation c = new Calculation();
        c.setProject(this.project.getId());
        c.setName(txtName.getValue());
        c.setComment(txtComment.getValue());
        this.hide();
    }
    
    @UiHandler("btnCancel")
    public void btnCancelClick(SelectEvent event){
        this.hide();
    }
}
