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
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.box.ProgressMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import org.evome.KaKsCalc.client.KaKsCalc;
import org.evome.KaKsCalc.client.Project;
import com.sencha.gxt.widget.core.client.Window;
import org.evome.KaKsCalc.client.GWTServiceAsync;
import org.evome.KaKsCalc.client.Shared;

import com.google.gwt.user.client.Timer;

/**
 *
 * @author nekoko
 */
public class ProjectAdd extends Window {
    
    private static ProjectAddUiBinder uiBinder = GWT.create(ProjectAddUiBinder.class);
    private static GWTServiceAsync rpc = Shared.getService();
    
    private TreeStore<TreeViewItem> store = Workspace.getTreeView().getTreeStore(); //the tree store to update
    
    interface ProjectAddUiBinder extends UiBinder<Widget, ProjectAdd> {
    }
    
    public ProjectAdd() {
        setWidget(uiBinder.createAndBindUi(this));
        init();
    }
    
    
    @UiField
    TextField txtProjectName;
    @UiField
    TextArea txtProjectComment;
    
    @UiHandler("btnSave")
    public void onSaveClick(SelectEvent event){
        //process data update
        final Project p = new Project();
        p.setName(txtProjectName.getText().replaceAll("'", "&#39"));
        p.setOwner(KaKsCalc.getAccount());
        p.setComment(txtProjectComment.getText().replaceAll("'", "&#39"));
        
        final ProgressMessageBox pmb = new ProgressMessageBox("In progess", "Communicating with the server, please wait...");
        pmb.setModal(true);
        pmb.show();
        
        rpc.addNewProject(p, new AsyncCallback<Integer>(){
           @Override
           public void onSuccess(Integer pid){
               pmb.updateProgress(1,"Done.");
               store.add(new TreeViewItem("project",pid, p.getName()));
           }
           @Override
           public void onFailure(Throwable caught){
               
           }
        });
        Timer t = new Timer(){
            @Override
            public void run(){
                pmb.hide();
            }
        };
        t.schedule(1000);
        this.hide();
    }
    
    @UiHandler("btnCancel")
    public void onCancelClick(SelectEvent event){
        //return
        this.hide();
    }
    
    
    private void init(){
        this.setModal(true);
        this.setMinWidth(360);
        this.setMinHeight(300);
        txtProjectComment.setHeight(150);
    }
    
    
}
