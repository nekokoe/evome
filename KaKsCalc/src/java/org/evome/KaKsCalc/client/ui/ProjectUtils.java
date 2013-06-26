/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import org.evome.KaKsCalc.client.shared.TreeViewItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import com.sencha.gxt.widget.core.client.Dialog;
import com.google.gwt.user.client.ui.SimplePanel;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import org.evome.KaKsCalc.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.Portlet;
import org.evome.KaKsCalc.client.ui.events.TreeUpdateEvent;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.button.TextButton;
import org.evome.KaKsCalc.client.widget.resources.ExampleImages;

/**
 *
 * @author nekoko
 *
 * ProjectUtils provide tools for project management
 *
 */
public class ProjectUtils extends Composite {

    private Project myproject;
    private TreeViewItem mytvi;
    private static GWTServiceAsync rpc = Shared.getService();
    private static ProjectUtilsUiBinder uiBinder = GWT.create(ProjectUtilsUiBinder.class);
    private static ExampleImages images = ExampleImages.INSTANCE;
    //declare public widgets
    private PortalLayoutContainer portal = initPortalContainer();
    private Portlet statuslet = new Portlet();

    //uibinder
    interface ProjectUtilsUiBinder extends UiBinder<Widget, ProjectUtils> {
    }
    @UiField
    SimplePanel panel;

    public ProjectUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        portal.add(statuslet, 2);
        panel.add(portal);
    }

    public ProjectUtils(Project project) {
        this();
        this.setProject(project);
    }

    public ProjectUtils(TreeViewItem tvi) {
        this();
        this.setProject(tvi);
    }

    @UiHandler("btnProjectAdd")
    public void btnProjectAddClick(SelectEvent event) {
        final AddProject add = new AddProject();
        add.setHeadingText("Add New Project");
        add.show();
    }

    @UiHandler("btnProjectEdit")
    public void btnProjectEditClick(SelectEvent event) {
        final ProjectUtils pu = this;
        final EditProject edit = new EditProject();
        edit.setHeadingText("Edit Project : " + myproject.getName());
        edit.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                pu.refresh();
            }
        });
        edit.show();
    }

    @UiHandler("btnProjectDel")
    public void btnProjectDelClick(SelectEvent event) {
        final ProjectUtils pu = this;
        ConfirmMessageBox confirm =
                new ConfirmMessageBox("Confirm", "Are you sure want to delete : " + myproject.getName() + " ? <br>"
                + "All data under this project will be deleted!");
        confirm.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                MessageBox source = (MessageBox) event.getSource();
                if (source.getHideButton() == source.getButtonById(Dialog.PredefinedButton.YES.name())) {
                    rpc.delProject(myproject, new AsyncCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean b) {
                            if (b) {
                                KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(mytvi, TreeUpdateEvent.Action.DELETE));
                            } else {
                                Info.display("Error", "Failed to delete " + myproject.getName());
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

    @UiHandler("btnCalcAdd")
    public void btnCalcAddClick(SelectEvent event) {
        final CalculationUtils cu = new CalculationUtils();
        final CalculationUtils.AddCalculation addcalc = cu.new AddCalculation(myproject);
        addcalc.setHeadingText("Add new Calculation");
        addcalc.show();
    }

    public final void setProject(Project project) {
        this.myproject = project;
        this.mytvi = new TreeViewItem(TreeViewItem.Type.PROJECT, project.getId(), project.getName());

        //default : add Project Status Grid Portlet to portal
        //portal.clear();
        statuslet.setHeadingText("Project Status");
        statuslet.setCollapsible(true);
        statuslet.setResize(true);
        statuslet.setWidget(initStatusGrid(project));

    }

    public final void setProject(TreeViewItem tvi) {
        final ProjectUtils pu = this;
        rpc.getProject(tvi.getId(), new AsyncCallback<Project>() {
            @Override
            public void onSuccess(Project project) {
                pu.setProject(project);
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });
    }

    private PortalLayoutContainer initPortalContainer() {
        PortalLayoutContainer plc = new PortalLayoutContainer(3);
        plc.setColumnWidth(0, .50);
        plc.setColumnWidth(1, .25);
        plc.setColumnWidth(2, .25);
        return plc;
    }

    private Grid initStatusGrid(Project p) {
        Grid status = new Grid(7, 3);
        status.setText(0, 0, "ID");
        status.setText(0, 1, String.valueOf(p.getId()));
        status.setText(1, 0, "UUID");
        status.setText(1, 1, p.getUUID());
        status.setText(2, 0, "Name");
        status.setText(2, 1, p.getName());
        status.setText(3, 0, "Owner");
        status.setText(3, 1, p.getOwner().getFullName());
        status.setText(4, 0, "Created");
        status.setText(4, 1, p.getCreateDate().toString());
        status.setText(5, 0, "Modified");
        status.setText(5, 1, p.getModifyDate().toString());
        status.setText(6, 0, "Description");
        status.setText(6, 1, p.getComment());
        return status;
    }

    private void refresh() {
        //this method reload the project from database
        this.setProject(mytvi); //tricky, setProject(tvi) will request the db
    }

    public class AddProject extends Window {

        private TextField projectName = new TextField();
        private TextArea projectComment = new TextArea();

        public AddProject() {
            this.setModal(true);
            this.setWidth(400);
            this.setHeight(300);
            VerticalLayoutContainer container = new VerticalLayoutContainer();
            VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
            VerticalLayoutContainer.VerticalLayoutData layout2 = new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10));
            projectName.setAllowBlank(false);
            FieldLabel lblName = new FieldLabel();
            lblName.setText("Project Name");
            lblName.setWidget(projectName);
            FieldLabel lblComment = new FieldLabel();
            lblComment.setText("Project Details");
            lblComment.setWidget(projectComment);
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
            final AddProject me = this;
            //save
            btnSave.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    final Project p = new Project();
                    p.setName(projectName.getText());
                    p.setOwner(KaKsCalc.getAccount());
                    p.setComment(projectComment.getText());
                    rpc.addNewProject(p, new AsyncCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer pid) {
                            TreeViewItem additem = new TreeViewItem(TreeViewItem.Type.PROJECT, pid, p.getName());
                            Info.display("You have added", additem.getValue());
                            KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(additem, TreeUpdateEvent.Action.ADD));
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

    public class EditProject extends Window {

        private Label projectID = new Label(), projectUUID = new Label(), projectOwner = new Label();
        private TextField projectName = new TextField();
        private TextArea projectComment = new TextArea();

        public EditProject() {
            this.setModal(true);
            this.setMinWidth(400);
            this.setMinHeight(400);
            this.setWidth(400);
            this.setHeight(400);
            VerticalLayoutContainer container = new VerticalLayoutContainer();
            VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
            VerticalLayoutContainer.VerticalLayoutData layout2 = new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10));
            //form
            projectName.setAllowBlank(false);
            FieldLabel fldID = new FieldLabel();
            fldID.setText("Project ID");
            fldID.setWidget(projectID);
//            FieldLabel fldUUID = new FieldLabel();
//            fldUUID.setText("UUID");
//            fldUUID.setWidget(projectUUID);            
            FieldLabel fldOwner = new FieldLabel();
            fldOwner.setText("Project Owner");
            fldOwner.setWidget(projectOwner);
            FieldLabel fldName = new FieldLabel();
            fldName.setText("Project Name");
            fldName.setWidget(projectName);
            FieldLabel fldComment = new FieldLabel();
            fldComment.setText("Project Details");
            fldComment.setWidget(projectComment);
            container.add(fldID, layout);
//            container.add(fldUUID, layout);
            container.add(fldOwner, layout);
            container.add(fldName, layout);
            container.add(fldComment, layout2);
            this.add(container);
            //init current values
            projectID.setText(Integer.toString(myproject.getId()));
//            projectUUID.setText(myproject.getUUID());
            projectOwner.setText(myproject.getOwner().getFullName());
            projectName.setValue(myproject.getName());
            projectComment.setValue(myproject.getComment());
            //buttons
            TextButton btnSave = new TextButton("Save");
            btnSave.setIcon(images.add());
            TextButton btnCancel = new TextButton("Cancel");
            btnCancel.setIcon(images.delete());
            this.addButton(btnSave);
            this.addButton(btnCancel);
            //button events
            final EditProject me = this;
            //save
            btnSave.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    final Project p = new Project();
                    p.setId(myproject.getId());
                    p.setName(projectName.getText());
                    p.setOwner(myproject.getOwner());
                    p.setComment(projectComment.getText());
                    rpc.editProject(p, new AsyncCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean b) {
                            TreeViewItem item = new TreeViewItem(TreeViewItem.Type.PROJECT, p.getId(), p.getName());
                            Info.display("You have edited", item.getValue());
                            KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(item, TreeUpdateEvent.Action.UPDATE));
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
