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
import org.evome.KaKsCalc.client.*;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import org.evome.KaKsCalc.client.GWTServiceAsync;
import com.sencha.gxt.widget.core.client.info.Info;
/**
 *
 * @author nekoko
 */
public class CalculationUtils extends Composite {
    
    //store current calculation
    private Calculation mycalc;
    private TreeViewItem mytvi;
    private TreeStore<TreeViewItem> store = Workspace.getTreeView().getTreeStore(); //the tree store to update
    
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
        this.mytvi = new TreeViewItem(mycalc.getClassType(), mycalc.getId(), mycalc.getName());
    }
    
    public CalculationUtils(TreeViewItem tvi){
        initWidget(uiBinder.createAndBindUi(this));
        this.setCalculation(tvi);
        this.mytvi = tvi;
    }
    
    public final void setCalculation(Calculation calc){
        this.mycalc = calc;
        Workspace.getContentPanel().setHeadingText(calc.getName());
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
                if (add.isUpdated()){
                    Info.display("You have added", add.getMyTreeViewItem().getValue());
                    Project p = mycalc.getProject();
                    cu.store.add(new TreeViewItem(p.getClassType(), p.getId(), p.getName()),
                            add.getMyTreeViewItem());
                    cu.setCalculation(add.getMyTreeViewItem());
                }
            }
        });
        add.show();
    }    
    
    @UiHandler("btnCalcEdit")
    public void onCalcEditClick(SelectEvent event){
        final CalculationUtils cu = this;
        final CalculationEdit edit = new CalculationEdit(mycalc);
        edit.setHeadingText("Edit Calculation : " + mycalc.getName());
        edit.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (edit.isUpdated()){
                    Info.display("You have editted", edit.getMyTreeViewItem().getValue());
                    cu.store.update(edit.getMyTreeViewItem());
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
                                cu.store.remove(mytvi);
                                Workspace.getContentPanel().clear();
                                Workspace.getContentPanel().add(new ProjectUtils(cu.store.getParent(mytvi)));
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
    }
    @UiHandler("btnTaskAdd")
    public void onCalcResultClick(SelectEvent event){
        
    }
    
    @UiField
    SimplePanel panel;

}
