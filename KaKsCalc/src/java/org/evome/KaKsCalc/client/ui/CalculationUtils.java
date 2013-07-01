/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import org.evome.KaKsCalc.client.widget.PortletWizard;
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
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
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
import org.evome.KaKsCalc.client.widget.PortletFileUploader;

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
    private PortletFileUploader uploadlet = new PortletFileUploader();
    //service
    private static GWTServiceAsync rpc = Shared.getService();
    //uibinder
    private static CalculationUtilsUiBinder uiBinder = GWT.create(CalculationUtilsUiBinder.class);
    private static ExampleImages images = ExampleImages.INSTANCE;
    interface CalculationUtilsUiBinder extends UiBinder<Widget, CalculationUtils> {
    }
    @UiField
    SimpleContainer container;
    @UiField
    ToolBar toolbar;

    public CalculationUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        portal.add(statuslet,2);
        portal.add(uploadlet, 0);
        container.add(portal);
        container.getElement().setAttribute("style", "overflow:auto; height:95%;");        
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
        //status wig
        statuslet.setHeadingText("Calculation Status");
        statuslet.setCollapsible(true);
        statuslet.setResize(true);
        statuslet.setWidget(initStatusGrid(calc));
        //uploader
        uploadlet.setCalculation(calc);
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
        final TaskWizard wizard = new TaskWizard(mycalc);
        //wizard.setHeadingText("Create a new Task");
        wizard.getHeader().addTool(new ToolButton(ToolButton.CLOSE, new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                wizard.removeFromParent();
            }
        }));
        portal.add(wizard, 0);
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
