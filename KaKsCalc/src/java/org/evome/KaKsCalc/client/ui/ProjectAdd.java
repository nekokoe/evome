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

//import com.google.gwt.user.client.Timer;
//import com.google.gwt.event.shared.HandlerRegistration;
//import com.google.gwt.event.shared.EventHandler;
//import com.google.gwt.event.shared.GwtEvent;
/**
 *
 * @author nekoko
 */
public class ProjectAdd extends Window {
    
    private static ProjectAddUiBinder uiBinder = GWT.create(ProjectAddUiBinder.class);
    private static GWTServiceAsync rpc = Shared.getService();
    
    private boolean isUpdated = false;
    private TreeViewItem mytvi; 
    
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
        final ProjectAdd pa = this;
        p.setName(txtProjectName.getText());
        p.setOwner(KaKsCalc.getAccount());
        p.setComment(txtProjectComment.getText());
        
        final ProgressMessageBox pmb = new ProgressMessageBox("In progess", "Communicating with the server, please wait...");
        pmb.setModal(true);
        pmb.show();
        
        rpc.addNewProject(p, new AsyncCallback<Integer>(){
           @Override
           public void onSuccess(Integer pid){
               pmb.updateProgress(1,"Done.");
               //store.add(new TreeViewItem("project",pid, p.getName())); //deprecated, this opr. should be handled in ProjectUtils
               pa.isUpdated = true;
               pa.mytvi = new TreeViewItem(p.getClassType(), pid, p.getName());
               pmb.hide();
               pa.hide();
           }
           @Override
           public void onFailure(Throwable caught){
               pmb.hide();
               pa.hide();
           }
        });
    }
    
    @UiHandler("btnCancel")
    public void onCancelClick(SelectEvent event){
        //return
        this.hide();
    }
    
    
    
    private void init(){
        this.setModal(true);
        this.setMinWidth(400);
        this.setMinHeight(400);
        this.setWidth(400);
        this.setHeight(300);
    }
    
    public boolean isUpdated(){
        return this.isUpdated;
    }
    
    public TreeViewItem getMyTreeViewItem(){
        return this.mytvi;
    }
    
//    public interface UpdateHandler extends EventHandler{
//        void onUpdate(UpdateEvent event);
//    };
//    
//    public class UpdateEvent extends GwtEvent<UpdateHandler>{
//        Type<UpdateHandler> TYPE = new Type<UpdateHandler>();
//        @Override
//        protected void dispatch(UpdateHandler handler) {
//            handler.onUpdate(this);
//        }
//        @Override
//        @SuppressWarnings({"unchecked", "rawtypes"})
//        public Type<UpdateHandler> getAssociatedType() {
//            return (Type) TYPE;
//        }
//        @Override
//        public Window getSource(){
//            return (Window) super.getSource();
//        }
//    }
//    
//    public HandlerRegistration addUpdateHandler(UpdateHandler handler){
//        return this.addHandler(handler, new GwtEvent.Type<UpdateHandler>());
//    }
}
