/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.evome.KaKsCalc.client.Calculation;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.google.gwt.uibinder.client.UiField;

/**
 *
 * @author nekoko
 */
public class CalculationEdit extends Window {
    
    private static CalculationEditUiBinder uiBinder = GWT.create(CalculationEditUiBinder.class);
    
    interface CalculationEditUiBinder extends UiBinder<Widget, CalculationEdit> {
    }
    
    public CalculationEdit() {
        setWidget(uiBinder.createAndBindUi(this));
        init();
    }
    
    public CalculationEdit(Calculation calc){
        setWidget(uiBinder.createAndBindUi(this));
        init();
    }
    
    @UiField
    TextField txtCalcID, txtCalcOwner, txtCalcProject, txtCalcName, txtCalcComment;
    @UiField    
    FieldLabel fieldID, fieldOwner, fieldProject;
    
    
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
