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
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.google.gwt.user.client.ui.SimplePanel;
import org.evome.KaKsCalc.client.Calculation;
/**
 *
 * @author nekoko
 */
public class CalculationUtils extends Composite {
    
    //store current calculation
    private Calculation current = new Calculation();
    
    private static CalculationUtilsUiBinder uiBinder = GWT.create(CalculationUtilsUiBinder.class);
    
    interface CalculationUtilsUiBinder extends UiBinder<Widget, CalculationUtils> {
    }
    
    public CalculationUtils() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    public CalculationUtils(Calculation calc){
        this.current = calc;
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @UiHandler("btnCalcStart")
    public void onCalcStartClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcStop")
    public void onCalcStopClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcEdit")
    public void onCalcEditClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcDelete")
    public void onCalcDeleteClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcResult")
    public void onCalcResultClick(SelectEvent event){
        
    }
    
    @UiField
    SimplePanel panel;
    
}
