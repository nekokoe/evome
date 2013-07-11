/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.widget;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
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
import org.evome.KaKsCalc.client.Calculation;
import org.evome.KaKsCalc.client.GWTServiceAsync;
import org.evome.KaKsCalc.client.Resource;
import org.evome.KaKsCalc.client.Shared;
import org.evome.KaKsCalc.client.Account;
import org.evome.KaKsCalc.client.shared.ListViewItem;
import org.evome.KaKsCalc.client.shared.UploadInfo;
import org.evome.KaKsCalc.client.widget.resources.ExampleImages;
/**
 *
 * @author nekoko
 */
public class PortletFileUploader extends Portlet{
    private static GWTServiceAsync rpc = Shared.getService();    
    private static ExampleImages images = ExampleImages.INSTANCE;
    
    private String uuid;
    private Account owner;
    
    public PortletFileUploader(){
     
    }
    
    public void setCalculation(Calculation calc){
        this.uuid = calc.getUUID();
        this.owner = calc.getOwner();
        this.setHeadingText("Sequence File Uploader");
        this.setWidget(initFileUploader());           //NOTE: may not be safe, uuid/owner should be passed by params
    }
    
    //file uploader
    private VerticalLayoutContainer initFileUploader() {
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        
        //set file list
        final ListView<ListViewItem, String> fileListView = new ListView<ListViewItem, String>(new ListStore<ListViewItem>(new ModelKeyProvider<ListViewItem>(){
            @Override
            public String getKey(ListViewItem item){
                return item.getUUID();
            }
        }), new ValueProvider<ListViewItem, String>(){
            @Override
            public String getValue(ListViewItem item){
                return item.getValue();
            }
            @Override
            public void setValue(ListViewItem item, String value){
                item.setValue(value);
            }
            @Override
            public String getPath(){
                return "key";
            }
        });
        
        //set listview mode
        fileListView.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        
        //set listview context menu
        Menu contextMenu = new Menu();
        MenuItem delete = new MenuItem();
        delete.setText("Delete This File");
        delete.setIcon(images.delete());
        delete.addSelectionHandler(new SelectionHandler<Item>(){
           @Override
            public void onSelection(SelectionEvent<Item> event) {
                final ListViewItem select = fileListView.getSelectionModel().getSelectedItem();
                ConfirmMessageBox confirm =
                        new ConfirmMessageBox("Confirm", "Are you sure want to delete : " + select.getValue() + " ?");
                confirm.addHideHandler(new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        MessageBox source = (MessageBox) event.getSource();
                        if (source.getHideButton() == source.getButtonById(Dialog.PredefinedButton.YES.name())) {
                            rpc.delResource(new Resource(select.getUUID()), new AsyncCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean b) {
                                    if (b) {
                                        fileListView.getStore().remove(select);
                                        Info.display("Success", select.getValue() + " has been deleted.");
                                    } else {
                                        Info.display("Error", "Failed to delete " + select.getValue());
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
        
        //get existing file list for calculation
        rpc.childResources(uuid, new AsyncCallback<ArrayList<Resource>>() {
            @Override
            public void onSuccess(ArrayList<Resource> list) {
                for (Iterator<Resource> it = list.iterator(); it.hasNext();){
                    Resource res = it.next();
                    fileListView.getStore().add(new ListViewItem(ListViewItem.Type.RES, res.getUUID(), res.getName()));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                Info.display("Error", "Communication with server failed");
            }
        });
 
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
                    rpc.uploadAsResource(new UploadInfo(info.message, info.name, uuid, owner, UploadInfo.Type.FILE), new AsyncCallback<Resource>() {
                        @Override
                        public void onSuccess(Resource res) {
                            if (res == null){
                                Info.display("Error", "Failed to register file as resource");
                            }
                            fileListView.getStore().add(new ListViewItem(ListViewItem.Type.RES, res.getUUID(), res.getName()));                            
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
