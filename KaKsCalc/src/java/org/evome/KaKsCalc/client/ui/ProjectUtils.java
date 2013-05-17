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
import com.sencha.gxt.widget.core.client.info.Info;

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
    private TreeStore<TreeViewItem> store = Workspace.getTreeView().getTreeStore(); //the tree store to update
    private static GWTServiceAsync rpc = Shared.getService();
    private static ProjectUtilsUiBinder uiBinder = GWT.create(ProjectUtilsUiBinder.class);

    interface ProjectUtilsUiBinder extends UiBinder<Widget, ProjectUtils> {
    }

    public ProjectUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        this.setProject(Project.sampleData());
    }

    public ProjectUtils(Project project) {
        initWidget(uiBinder.createAndBindUi(this));
        this.setProject(project);
        this.mytvi = new TreeViewItem(myproject.getClassType(), myproject.getId(), myproject.getName());
    }

    public ProjectUtils(TreeViewItem tvi) {
        initWidget(uiBinder.createAndBindUi(this));
        this.setProject(tvi);
        this.mytvi = tvi;
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
                    pu.store.add(add.getMyTreeViewItem());
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
                    pu.store.update(edit.getMyTreeViewItem());
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
                                pu.store.remove(mytvi);
                                pu.setProject(Workspace.getTreeView().getTreeStore().getChild(0));
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
                    Info.display("You have added", add.getMyTreeViewItem().getValue());
                    store.add(mytvi, add.getMyTreeViewItem());
                }
            }
        });        
        add.show();
    }
    
    @UiField
    SimplePanel panel;

    public final void setProject(Project project) {
        this.myproject = project;
        Workspace.getContentPanel().setHeadingText(project.getName());
        panel.clear();
        panel.add(new ProjectStatus(project));
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
}
