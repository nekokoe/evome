/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.ProgressMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.evome.KaKsCalc.client.Calculation;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import org.evome.KaKsCalc.client.GWTServiceAsync;
import org.evome.KaKsCalc.client.Shared;
import org.evome.KaKsCalc.client.KaKsCalc;

/**
 *
 * @author nekoko
 */
public class CalculationEdit extends Window {
    
    private static CalculationEditUiBinder uiBinder = GWT.create(CalculationEditUiBinder.class);
    private static GWTServiceAsync rpc = Shared.getService();
    
    private Calculation mycalc;
    private boolean isUpdated = false;
    private TreeViewItem mytvi;    
    
    interface CalculationEditUiBinder extends UiBinder<Widget, CalculationEdit> {
    }
    
    public CalculationEdit() {
        setWidget(uiBinder.createAndBindUi(this));
        init();
    }
    
    public CalculationEdit(Calculation calc){
        setWidget(uiBinder.createAndBindUi(this));
        init();
        this.mycalc = calc;
        txtCalcID.setValue(Integer.toString(calc.getId()));
        txtCalcOwner.setValue(calc.getOwner().getFullName());
        txtCalcProject.setValue(calc.getProject().getName());
        txtCalcName.setValue(calc.getName());
        txtCalcComment.setValue(calc.getComment());
    }
    
    @UiField
    TextField txtCalcID, txtCalcOwner, txtCalcProject, txtCalcName;
    @UiField    
    TextArea txtCalcComment;
    
    
    @UiHandler("btnSave")
    public void onSaveClick(SelectEvent event){
        //process data update
        final CalculationEdit ce = this;
        final Calculation c = new Calculation();
        c.setComment(txtCalcComment.getText());
        c.setId(mycalc.getId());
        c.setName(txtCalcName.getText());
        c.setOwner(mycalc.getOwner());
        c.setProject(mycalc.getProject());
        
        final ProgressMessageBox pmb = new ProgressMessageBox("In progess", "Communicating with the server, please wait...");
        pmb.setModal(true);
        pmb.show();
        
        rpc.editCalculation(c, new AsyncCallback<Boolean>(){
            @Override
            public void onSuccess(Boolean b) {
                pmb.updateProgress(1, "Done.");
                ce.isUpdated = b;
                ce.mytvi = new TreeViewItem(TreeViewItem.Type.CALCULATION, c.getId(), c.getName());
                pmb.hide();
                ce.hide();
            }
            @Override
            public void onFailure(Throwable caught) {
                pmb.hide();
                ce.hide();                
            }
        });        
    }
    
    @UiHandler("btnCancel")
    public void onCancelClick(SelectEvent event){
        //return
        this.hide();
    }

    public boolean isUpdated(){
        return this.isUpdated;
    }
    
    public TreeViewItem getMyTreeViewItem(){
        return this.mytvi;
    }
    
    private void init(){
        this.setModal(true);
        this.setMinWidth(400);
        this.setMinHeight(300);
        this.setWidth(400);
        this.setHeight(300);
        txtCalcID.setReadOnly(true);
        txtCalcOwner.setReadOnly(true);
        txtCalcProject.setReadOnly(true);
    }
}
