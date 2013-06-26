/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import org.evome.KaKsCalc.client.*;
import org.evome.KaKsCalc.client.shared.TreeViewItem;
import org.evome.KaKsCalc.client.shared.ListViewItem;
import org.evome.KaKsCalc.client.shared.UploadInfo;
import org.evome.KaKsCalc.client.ui.events.TreeUpdateEvent;
import org.evome.KaKsCalc.client.widget.resources.ExampleImages;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author nekoko
 */
public class CalculationUtils extends Composite {

    //store current calculation
    private Calculation mycalc;
    private TreeViewItem mytvi;
    //public widgets
    private PortalLayoutContainer portal = initPortalContainer();
    private Portlet statuslet = new Portlet();    
    private Portlet uploadlet = new Portlet();
    //service
    private static GWTServiceAsync rpc = Shared.getService();
    //uibinder
    private static CalculationUtilsUiBinder uiBinder = GWT.create(CalculationUtilsUiBinder.class);
    private static ExampleImages images = ExampleImages.INSTANCE;
    interface CalculationUtilsUiBinder extends UiBinder<Widget, CalculationUtils> {
    }
    @UiField
    SimplePanel panel;

    public CalculationUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        portal.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        portal.add(statuslet,2);
        portal.add(uploadlet, 0);
        panel.add(portal);
    }

    public CalculationUtils(Calculation calc) {
        this();
        this.setCalculation(calc);
    }

    public CalculationUtils(TreeViewItem tvi) {
        this();
        this.setCalculation(tvi);
    }

    public final void setCalculation(Calculation calc) {
        this.mycalc = calc;
        this.mytvi = new TreeViewItem(TreeViewItem.Type.CALCULATION, calc.getId(), calc.getName());
        statuslet.setHeadingText("Calculation Status");
        statuslet.setCollapsible(true);
        statuslet.setResize(true);
        statuslet.setWidget(initStatusGrid(calc));
        uploadlet.setHeadingText("Sequence Uploader");
        uploadlet.setWidget(initFileUploader(calc));
    }

    public final void setCalculation(TreeViewItem tvi) {
        final CalculationUtils cu = this;
        rpc.getCalculation(tvi.getId(), new AsyncCallback<Calculation>() {
            @Override
            public void onSuccess(Calculation calc) {
                cu.setCalculation(calc);
            }

            @Override
            public void onFailure(Throwable caught) {
                Info.display("Error", caught.getMessage());
            }
        });
    }
    
    private void refresh(){
        this.setCalculation(mytvi); //tricky, setCalculation will request db and reset UI
    }

    @UiHandler("btnCalcStartAll")
    public void onCalcStartAllClick(SelectEvent event) {
    }

    @UiHandler("btnCalcStopAll")
    public void onCalcStopAllClick(SelectEvent event) {
    }

    @UiHandler("btnCalcAdd")
    public void onCalcAddClick(SelectEvent event) {
        final AddCalculation add = new AddCalculation();
        add.setHeadingText("Add new Calculation");
        add.show();
    }

    @UiHandler("btnCalcEdit")
    public void onCalcEditClick(SelectEvent event) {
        final CalculationUtils cu = this;
        final EditCalculation edit = new EditCalculation();
        edit.setHeadingText("Edit Calculation : " + mycalc.getName());
        edit.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                cu.refresh();
            }
        });
        edit.show();
    }

    @UiHandler("btnCalcDelete")
    public void onCalcDeleteClick(SelectEvent event) {
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
    public void onTaskAddClick(SelectEvent event) {
        final Portlet taskwiz = new Portlet();
        taskwiz.setHeadingText("Create a new Task");
        taskwiz.setWidget(new TaskWizard(mycalc));
        taskwiz.getHeader().addTool(new ToolButton(ToolButton.CLOSE, new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                taskwiz.removeFromParent();
            }
        }));
        portal.add(taskwiz, 0);
    }

    private PortalLayoutContainer initPortalContainer() {
        PortalLayoutContainer plc = new PortalLayoutContainer(3);
        plc.setColumnWidth(0, .50);
        plc.setColumnWidth(1, .25);
        plc.setColumnWidth(2, .25);
        return plc;
    }
    
    //planar portlet
    //status grid
    private Grid initStatusGrid(Calculation c) {
        Grid status = new Grid(7, 3);
        status.setText(0, 0, "ID");
        status.setText(0, 1, String.valueOf(c.getId()));
        status.setText(1, 0, "Name");
        status.setText(1, 1, c.getName());
        status.setText(2, 0, "Owner");
        status.setText(2, 1, c.getOwner().getFullName());
        status.setText(3, 0, "Project");
        status.setText(3, 1, c.getProject().getName());        
        status.setText(4, 0, "Created");
        status.setText(4, 1, c.getCreateTime().toString());
        status.setText(5, 0, "Modified");
        status.setText(5, 1, c.getModifyTime().toString());
        status.setText(6, 0, "Description");
        status.setText(6, 1, c.getComment());
        return status;
    }
    
    //file uploader
    private VerticalLayoutContainer initFileUploader(Calculation calc) {
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
        rpc.childResources(mycalc.getUUID(), new AsyncCallback<ArrayList<Resource>>() {
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
                    rpc.uploadAsResource(new UploadInfo(info.message, info.name, mycalc.getUUID(), mycalc.getOwner()), new AsyncCallback<Resource>() {
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
    
    
    
    //floating windows
    public class AddCalculation extends Window{
        private TextField calcName = new TextField();
        private TextArea calcComment = new TextArea();

        public AddCalculation() {
            this.setModal(true);
            this.setWidth(400);
            this.setHeight(300);
            VerticalLayoutContainer container = new VerticalLayoutContainer();
            VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
            VerticalLayoutContainer.VerticalLayoutData layout2 = new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10));
            calcName.setAllowBlank(false);
            FieldLabel lblName = new FieldLabel();
            lblName.setText("Calculation Name");
            lblName.setWidget(calcName);
            FieldLabel lblComment = new FieldLabel();
            lblComment.setText("Calculation Details");
            lblComment.setWidget(calcComment);
            container.add(lblName, layout);
            container.add(lblComment, layout2);
            this.add(container);
            //buttons
            TextButton btnSave = new TextButton("Save");
            btnSave.setIcon(images.add());
            TextButton btnCancel = new TextButton("Cancel");
            btnCancel.setIcon(images.delete());
            this.addButton(btnSave);
            this.addButton(btnCancel);

            //button events
            final CalculationUtils.AddCalculation me = this;
            //save
            btnSave.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    final Calculation c = new Calculation();
                    c.setName(calcName.getText());
                    c.setOwner(KaKsCalc.getAccount());
                    c.setComment(calcComment.getText());
                    c.setProject(mycalc.getProject());
//                    final ProgressMessageBox pmb = new ProgressMessageBox("In progess", "Communicating with the server, please wait...");
//                    pmb.setModal(true);
//                    pmb.show();
                    rpc.addNewCalculation(c, new AsyncCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer cid) {
                            Info.display("You have added", c.getName());
//                          pmb.updateProgress(1, "Done.");                            
                            TreeViewItem additem = new TreeViewItem(TreeViewItem.Type.CALCULATION, cid, c.getName());
                            TreeViewItem parent = new TreeViewItem(TreeViewItem.Type.PROJECT, c.getProject().getId(), c.getProject().getName());
                            KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(parent, additem));
//                          pmb.hide();
                            me.hide();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
//                          pmb.hide();
                            me.hide();
                        }
                    });
                }
            });
            //cancel
            btnCancel.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    me.hide();
                }
            });
        }
        
        public AddCalculation(Project project){
            this();
            mycalc.setProject(project);
        }
    }
    
    public class EditCalculation extends Window {

        private Label calcID = new Label(), calcOwner = new Label(), calcProj = new Label();
        private TextField calcName = new TextField();
        private TextArea calcComment = new TextArea();

        public EditCalculation() {
            this.setModal(true);
            this.setMinWidth(400);
            this.setMinHeight(400);
            this.setWidth(400);
            this.setHeight(400);
            VerticalLayoutContainer container = new VerticalLayoutContainer();
            VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
            VerticalLayoutContainer.VerticalLayoutData layout2 = new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10));
            //form
            calcName.setAllowBlank(false);
            FieldLabel fldID = new FieldLabel();
            fldID.setText("Calculation ID");
            fldID.setWidget(calcID);
            FieldLabel fldOwner = new FieldLabel();
            fldOwner.setText("Calculation Owner");
            fldOwner.setWidget(calcOwner);
            FieldLabel fldProj = new FieldLabel();
            fldProj.setText("Related Project");
            fldProj.setWidget(calcProj);
            FieldLabel fldName = new FieldLabel();
            fldName.setText("Calculation Name");
            fldName.setWidget(calcName);
            FieldLabel fldComment = new FieldLabel();
            fldComment.setText("Calculation Details");
            fldComment.setWidget(calcComment);
            container.add(fldID, layout);
            container.add(fldOwner, layout);
            container.add(fldName, layout);
            container.add(fldComment, layout2);
            this.add(container);
            //init current values
            calcID.setText(Integer.toString(mycalc.getId()));
            calcOwner.setText(mycalc.getOwner().getFullName());
            calcProj.setText(mycalc.getProject().getName());
            calcName.setValue(mycalc.getName());
            calcComment.setValue(mycalc.getComment());            
            //buttons
            TextButton btnSave = new TextButton("Save");
            btnSave.setIcon(images.add());
            TextButton btnCancel = new TextButton("Cancel");
            btnCancel.setIcon(images.delete());
            this.addButton(btnSave);
            this.addButton(btnCancel);
            //button events
            final CalculationUtils.EditCalculation me = this;
            //save
            btnSave.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    final Calculation c = new Calculation();
                    c.setId(mycalc.getId());
                    c.setName(calcName.getText());
                    c.setOwner(mycalc.getOwner());
                    c.setComment(calcComment.getText());
                    c.setProject(mycalc.getProject());
                    rpc.editCalculation(c, new AsyncCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean b) {
                            if (b) {
                                Info.display("You have edited", c.getName());
                                TreeViewItem item = new TreeViewItem(TreeViewItem.Type.CALCULATION, c.getId(), c.getName());
                                KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(item, TreeUpdateEvent.Action.UPDATE));
                            } else {
                                Info.display("Failed Editing", c.getName());
                            }
                            me.hide();
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            me.hide();
                        }
                    });
                }
            });
            //cancel
            btnCancel.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    me.hide();
                }
            });
        }
    }    
}
