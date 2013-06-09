/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import org.evome.KaKsCalc.client.*;
import org.evome.KaKsCalc.client.ui.events.TreeUpdateEvent;
/**
 *
 * @author nekoko
 */
public class CalculationUtils extends Composite {
    
    //store current calculation
    private Calculation mycalc;
    private TreeViewItem mytvi;
    
    private static CalculationUtilsUiBinder uiBinder = GWT.create(CalculationUtilsUiBinder.class);
    private static GWTServiceAsync rpc = Shared.getService();
    
    interface CalculationUtilsUiBinder extends UiBinder<Widget, CalculationUtils> {
    }
    
    public CalculationUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        this.setCalculation(Calculation.sampleData());
    }
    
    public CalculationUtils(Calculation calc){
        initWidget(uiBinder.createAndBindUi(this));
        this.setCalculation(calc);

    }
    
    public CalculationUtils(TreeViewItem tvi){
        initWidget(uiBinder.createAndBindUi(this));
        this.setCalculation(tvi);
    }
    
    public final void setCalculation(Calculation calc){
        this.mycalc = calc;
        this.mytvi = new TreeViewItem(TreeViewItem.Type.CALCULATION, calc.getId(),calc.getName());        
        panel.clear();
        panel.add(new CalculationStatus(calc));
    }    
    
    public final void setCalculation(TreeViewItem tvi){
        final CalculationUtils cu = this;
        rpc.getCalculation(tvi.getId(), new AsyncCallback<Calculation>(){
            @Override
            public void onSuccess(Calculation calc){
                cu.setCalculation(calc);
            }
            @Override
            public void onFailure(Throwable caught){
                Info.display("Error",caught.getMessage());
            }
        });  
    }
    
    @UiHandler("btnCalcStart")
    public void onCalcStartClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcStop")
    public void onCalcStopClick(SelectEvent event){
        
    }
    @UiHandler("btnCalcAdd")
    public void onCalcAddClick(SelectEvent event){
        final CalculationUtils cu = this;
        final CalculationAdd add = new CalculationAdd(mycalc.getProject());
        add.setHeadingText("Add new Calculation");
        add.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (add.isUpdated()) {
                    Info.display("You have added", add.getMyTreeViewItem().getValue());
                    Project p = mycalc.getProject();
                    KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(
                            new TreeViewItem(TreeViewItem.Type.PROJECT, p.getId(), p.getName()), add.getMyTreeViewItem()));
                    cu.setCalculation(add.getMyTreeViewItem());
                }
            }
        });
        add.show();
    }    
    
    @UiHandler("btnCalcEdit")
    public void onCalcEditClick(SelectEvent event) {
        final CalculationUtils cu = this;
        final CalculationEdit edit = new CalculationEdit(mycalc);
        edit.setHeadingText("Edit Calculation : " + mycalc.getName());
        edit.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (edit.isUpdated()) {
                    Info.display("You have editted", edit.getMyTreeViewItem().getValue());
                    KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(edit.getMyTreeViewItem(), TreeUpdateEvent.Action.UPDATE));
                    cu.setCalculation(edit.getMyTreeViewItem());
                }
            }
        });
        edit.show();
    }
    
    @UiHandler("btnCalcDelete")
    public void onCalcDeleteClick(SelectEvent event){
        final CalculationUtils cu = this;
        ConfirmMessageBox confirm =
                new ConfirmMessageBox("Confirm", "Are you sure want to delete : " + mycalc.getName() + " ? <br>"
                + "All data under this calculation will be deleted!");
        confirm.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                MessageBox source = (MessageBox) event.getSource();
                if (source.getHideButton() == source.getButtonById(Dialog.PredefinedButton.YES.name())) {
                    rpc.delCalculation(mycalc, new AsyncCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean b) {
                            if (b) {
                                KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(mytvi, TreeUpdateEvent.Action.DELETE));
                            } else {
                                Info.display("Error", "Failed to delete " + mycalc.getName());
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", "Communication with server failed.");
                        }
                    });
                }
            }
        });
        confirm.show();
    }
    
    @UiHandler("btnTaskAdd")
    public void onTaskAddClick(SelectEvent event){
        panel.clear();        
        panel.add(new TaskWizard(mycalc));
    }
    
    @UiField
    SimplePanel panel;

}
