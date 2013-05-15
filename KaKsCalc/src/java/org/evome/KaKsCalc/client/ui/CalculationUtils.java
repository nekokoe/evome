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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.google.gwt.user.client.ui.SimplePanel;
import org.evome.KaKsCalc.client.Calculation;
import org.evome.KaKsCalc.client.RpcDataBasket;
import org.evome.KaKsCalc.client.Shared;
import com.google.gwt.user.client.Window;
import org.evome.KaKsCalc.client.GWTServiceAsync;
/**
 *
 * @author nekoko
 */
public class CalculationUtils extends Composite {
    
    //store current calculation
    private Calculation calc;
    private TreeViewItem tvi;
    
    private static CalculationUtilsUiBinder uiBinder = GWT.create(CalculationUtilsUiBinder.class);
    private static GWTServiceAsync rpc = Shared.getService();
    
    interface CalculationUtilsUiBinder extends UiBinder<Widget, CalculationUtils> {
    }
    
    public CalculationUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        this.setCurrentCalculation(Calculation.sampleData());
    }
    
    public CalculationUtils(Calculation calc){
        initWidget(uiBinder.createAndBindUi(this));
        this.setCurrentCalculation(calc);
    }
    
    public CalculationUtils(TreeViewItem tvi){
        initWidget(uiBinder.createAndBindUi(this));
        final CalculationUtils cu = this;
        rpc.getCalculation(tvi.getId(), new AsyncCallback<Calculation>(){
            @Override
            public void onSuccess(Calculation calc){
                cu.setCurrentCalculation(calc);
            }
            @Override
            public void onFailure(Throwable caught){
                Window.alert(caught.getMessage());
            }
        });        
    }
    
    public final void setCurrentCalculation(Calculation calc){
        this.calc = calc;
        panel.clear();
        panel.add(new CalculationStatus(calc));
    }    
    
    
    @UiHandler("btnCalcStart")
    public void onCalcStartClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcStop")
    public void onCalcStopClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcAdd")
    public void onCalcAddClick(SelectEvent event){
        
    }    
    @UiHandler("btnCalcEdit")
    public void onCalcEditClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcDelete")
    public void onCalcDeleteClick(SelectEvent event){
        
    }
    @UiHandler("btnTaskAdd")
    public void onCalcResultClick(SelectEvent event){
        
    }
    
    @UiField
    SimplePanel panel;

}
