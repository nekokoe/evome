/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import java.util.ArrayList;
import java.util.Iterator;
import org.evome.evoKalc.client.shared.*;
import org.evome.evoKalc.client.event.*;
import org.evome.evoKalc.client.GWTServiceAsync;



/**
 *
 * @author nekoko
 */
public class FileUploader extends Composite{
    private static GWTServiceAsync rpc = Shared.RPC;
    private static Session session = Shared.MY_SESSION;
    private static ResourceProps resprops = GWT.create(ResourceProps.class);
    private static Resource.Type typefilter = Resource.Type.All;
    
    
    public FileUploader(){
        this.initWidget(initFileUploader());
    }
    
    public static void setTypeFilter(Resource.Type type){
        typefilter = type;
    }
    
    //file uploader
    private VerticalLayoutContainer initFileUploader() {
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        
        //set file list
        
        
        final ListView<Resource, String> fileListView = new ListView<Resource, String>(new ListStore<Resource>(resprops.key()), resprops.name());
        
        //set listview mode
        fileListView.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        
        //set listview context menu
        Menu contextMenu = new Menu();
        MenuItem delete = new MenuItem();
        delete.setText("Delete This File");
        delete.addSelectionHandler(new SelectionHandler<Item>(){
           @Override
            public void onSelection(SelectionEvent<Item> event) {
                final Resource select = fileListView.getSelectionModel().getSelectedItem();
                ConfirmMessageBox confirm =
                        new ConfirmMessageBox("Confirm", "Are you sure want to delete : " + select.getName() + " ?");
                confirm.addHideHandler(new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        MessageBox source = (MessageBox) event.getSource();
                        if (source.getHideButton() == source.getButtonById(Dialog.PredefinedButton.YES.name())) {
                            rpc.delResource(session, select, new AsyncCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean b) {
                                    if (b) {
                                        fileListView.getStore().remove(select);
                                        Info.display("Success", select.getName() + " has been deleted.");
                                    } else {
                                        Info.display("Error", "Failed to delete " + select.getName());
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
        });
        contextMenu.add(delete);
        fileListView.setContextMenu(contextMenu);
        
        //get existing file list for user
        //disabled on 2013/10/15, uplodaer cares only the files uploaded by itself
//        rpc.userResources(session, session.getAccount(), Resource.Type.All, new AsyncCallback<ArrayList<Resource>>() {
//            @Override
//            public void onSuccess(ArrayList<Resource> list) {
//                if (list == null){
//                    Info.display("Error", "Your session is not valid, please login again");
//                    Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGN_OUT));
//                }
//                for (Iterator<Resource> it = list.iterator(); it.hasNext();){
//                    Resource res = it.next();
//                    if (typefilter == Resource.Type.All || typefilter == res.getType()){
//                        fileListView.getStore().add(res);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable caught) {
//                Info.display("Error", "Communication with server failed");
//            }
//        });
 
        //set uploader
        SingleUploader uploader = new SingleUploader();
        uploader.avoidRepeatFiles(true);
        uploader.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
            @Override
            public void onFinish(IUploader uploader) {
                if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {
                    IUploader.UploadedInfo info = uploader.getServerInfo();
                    Info.display("Upload Success", "Finished uploading file " + info.name);
                    //register upload as resource
                    UploadInfo upload = new UploadInfo(session, info.name, UploadInfo.Type.FILE);
                    upload.setPath(info.message);
                    rpc.uploadAsResource(upload, new AsyncCallback<Resource>() {
                        @Override
                        public void onSuccess(Resource res) {
                            if (res == null){
                                Info.display("Error", "Failed to register file as resource");
                            }
                            fileListView.getStore().add(0, res);
                            Info.display("Resource", "uploaded file registered as " +  res.getUUID());
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", "Communication with server failed.");
                        }
                    });
                }
            }
        });
        
        //add uploader and filelist to container
        vertical.add(new Label("Select and upload local file:"), layout);
        vertical.add(uploader, layout);
        vertical.add(new Label("Existing Files:"), layout);
        vertical.add(fileListView, layout);
        return vertical;
    }    
}
