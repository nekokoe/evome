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
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.ProgressMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.evome.KaKsCalc.client.Calculation;
import org.evome.KaKsCalc.client.GWTServiceAsync;
import org.evome.KaKsCalc.client.KaKsCalc;
import org.evome.KaKsCalc.client.Project;
import org.evome.KaKsCalc.client.Shared;
/**
 *
 * @author nekoko
 */
public class CalculationAdd extends Window {
    

    
    private static CalculationAddUiBinder uiBinder = GWT.create(CalculationAddUiBinder.class);
    private static GWTServiceAsync rpc = Shared.getService();
    
    private Project myproject; //calculation add to this project
    private boolean isUpdated = false; //caller read this to determine if rpc is called
    private TreeViewItem mytvi; //to update the treeview after adding calc
    
    
    interface CalculationAddUiBinder extends UiBinder<Widget, CalculationAdd> {}
    
    public CalculationAdd(Project project) {
        setWidget(uiBinder.createAndBindUi(this));
        this.myproject = project;
        init();
    }
    
    @UiField
    TextField txtName;
    @UiField
    TextArea txtComment;
    
    @UiHandler("btnSave")
    public void btnSaveClick(SelectEvent event){
        final CalculationAdd ca = this;
        final Calculation c = new Calculation();
        c.setProject(this.myproject);
        c.setOwner(KaKsCalc.getAccount());
        c.setName(txtName.getText());
        c.setComment(txtComment.getText());
        
        final ProgressMessageBox pmb = new ProgressMessageBox("In progess", "Communicating with the server, please wait...");
        pmb.setModal(true);
        pmb.show();
        
        rpc.addNewCalculation(c, new AsyncCallback<Integer>(){
            @Override
            public void onSuccess(Integer cid){
                pmb.updateProgress(1, "Done.");
                ca.isUpdated = true;
                ca.mytvi = new TreeViewItem(c.getClassType(), cid, c.getName());
                pmb.hide();
                ca.hide();
            }
            @Override
            public void onFailure(Throwable caught){
               pmb.hide();
               ca.hide();                
            }
        });
    }
    
    @UiHandler("btnCancel")
    public void btnCancelClick(SelectEvent event){
        this.hide();
    }
    
    private void init(){
        this.setModal(true);
        this.setMinWidth(360);
        this.setMinHeight(300);
        this.setWidth(360);
        this.setHeight(300);
    }
    
    public boolean isUpdated(){
        return this.isUpdated;
    }
    
    public TreeViewItem getMyTreeViewItem(){
        return this.mytvi;
    }   
}
