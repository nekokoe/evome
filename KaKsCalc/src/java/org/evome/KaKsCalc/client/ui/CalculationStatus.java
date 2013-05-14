/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import org.evome.KaKsCalc.client.*;


/**
 *
 * @author nekoko
 */
public class CalculationStatus extends Composite {
    
    private static CalculationStatusUiBinder uiBinder = GWT.create(CalculationStatusUiBinder.class);
    
    interface CalculationStatusUiBinder extends UiBinder<Widget, CalculationStatus> {
    }
    
    public CalculationStatus() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    public CalculationStatus(Calculation calc){
        initWidget(uiBinder.createAndBindUi(this));
        lblID.setText(String.valueOf(calc.getId()));
        lblProject.setText(calc.getProject().getName());
        lblOwner.setText(calc.getOwner().getFullName());
        lblName.setText(calc.getName());
        lblComment.setText(calc.getComment());
        lblCreate.setText(calc.getCreateTime().toString());
        lblModify.setText(calc.getModifyTime().toString());
    }
    
    @UiField
    Label lblID, lblProject, lblOwner, lblName, lblComment, lblCreate, lblModify;
    
}
