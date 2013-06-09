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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.Dialog;
import com.google.gwt.user.client.ui.SimplePanel;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import org.evome.KaKsCalc.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.TreeStore;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.Portlet;
import org.evome.KaKsCalc.client.ui.events.TreeUpdateEvent;

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

    interface ProjectUtilsUiBinder extends UiBinder<Widget, ProjectUtils> {
    }
    
    @UiField
    SimplePanel panel;    
    
    private PortalLayoutContainer portal = initPortalContainer();

    public ProjectUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        panel.add(portal);
        this.setProject(Project.sampleData());

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
        final ProjectUtils pu = this;
        final ProjectAdd add = new ProjectAdd();
        add.setHeadingText("Add New Project");
        add.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (add.isUpdated()) {
                    Info.display("You have added", add.getMyTreeViewItem().getValue());
                    KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(add.getMyTreeViewItem(), TreeUpdateEvent.Action.ADD));
                    pu.setProject(add.getMyTreeViewItem());
                }
            }
        });
        add.show();
    }

    @UiHandler("btnProjectEdit")
    public void btnProjectEditClick(SelectEvent event) {
        final ProjectUtils pu = this;
        final ProjectEdit edit = new ProjectEdit(myproject);
        edit.setHeadingText("Edit Project : " + myproject.getName());
        edit.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (edit.isUpdated()) {
                    Info.display("You have editted", edit.getMyTreeViewItem().getValue());
                    KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(edit.getMyTreeViewItem(), TreeUpdateEvent.Action.UPDATE));
                    pu.setProject(edit.getMyTreeViewItem());
                }
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
        final CalculationAdd add = new CalculationAdd(myproject);
        add.setHeadingText("Add new Calculation");
        add.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                if (add.isUpdated()){
                    KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent(mytvi, add.getMyTreeViewItem()));                    
                    Info.display("You have added", add.getMyTreeViewItem().getValue());
                }
            }
        });        
        add.show();
    }
    


    public final void setProject(Project project) {
        this.myproject = project;
        this.mytvi = new TreeViewItem(TreeViewItem.Type.PROJECT, project.getId(), project.getName());

        //default : add Project Status Grid Portlet to portal
        //portal.clear();
        Portlet portlet = new Portlet();
        portlet.setHeadingText("Project Status");
        portlet.setCollapsible(true);
        portlet.setResize(true);
        //portlet.add(initStatusGrid(project));
        portal.add(portlet, 0);
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
        plc.setColumnWidth(0, .40);
        plc.setColumnWidth(1, .30);
        plc.setColumnWidth(2, .30);
        return plc;
    }
    
    private Grid initStatusGrid(Project p){
        Grid status = new Grid(6, 3);
        status.setText(0, 0, "Project ID");
        status.setText(0, 1, String.valueOf(p.getId()));
        status.setText(1, 0, "Project Name");
        status.setText(1, 1, p.getName());
        status.setText(2, 0, "Project Owner");
        status.setText(2, 1, p.getOwner().getFullName());
        status.setText(3, 0, "Created On");
        status.setText(3, 1, p.getCreateDate().toString());
        status.setText(4, 0, "Last Changed");
        status.setText(4, 1, p.getModifyDate().toString());
        status.setText(5, 0, "Description");
        status.setText(5, 1, p.getComment());
        return status;
    }
}
