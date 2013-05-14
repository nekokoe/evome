/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import java.util.Iterator;
import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.google.gwt.user.client.Event;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import org.evome.KaKsCalc.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author nekoko
 */
public class TreeView extends Composite {
    
    private static GWTServiceAsync rpc = Shared.getService();
    
    @UiField(provided=true)
    TreeStore<TreeViewItem> store = new TreeStore<TreeViewItem>(new ModelKeyProvider<TreeViewItem>(){
        @Override
        public String getKey(TreeViewItem tvp){
            return tvp.getKey();
        }
    });
    
    @UiField(provided=true)
    ValueProvider<TreeViewItem, String> valueProvider = new ValueProvider<TreeViewItem, String>(){
        @Override
        public String getValue(TreeViewItem tvp){
            return tvp.getValue();
        }
        @Override
        public void setValue(TreeViewItem tvp, String value){
            
        }
        @Override
        public String getPath(){
            return "key";
        }
    };
    
    @UiField
    Tree<TreeViewItem, String> treeProject;
    
    private static TreeViewUiBinder uiBinder = GWT.create(TreeViewUiBinder.class);
    
    interface TreeViewUiBinder extends UiBinder<Widget, TreeView> {
    }   
    
    public TreeView() {
        initWidget(uiBinder.createAndBindUi(this));
        //apply sample data
        sampleData();        
        //set selection mode
        treeProject.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        //select the first 'project' at the first load
        treeProject.getSelectionModel().select(0, false);
        //expand all leaves
        treeProject.expandAll();
    }
    
    public TreeView(Session s){
        initWidget(uiBinder.createAndBindUi(this));
        
        //warn: this is a non-stand shortcut
        Account account = new Account();
        account.setUserID(s.getUserID());
        
        //final RpcDataBasket<ArrayList<Project>> rdbp = new RpcDataBasket<ArrayList<Project>>();
        final TreeStore ts = store;
        rpc.userProjects(account, new AsyncCallback<ArrayList<Project>>() {
            @Override
            public void onSuccess(ArrayList<Project> projects) {
                //Window.alert(projects.toString());
                for (Iterator<Project> itp = projects.iterator(); itp.hasNext();) {
                    Project p = itp.next();
                    final TreeViewItem tvip = new TreeViewItem("project", p.getId(), p.getName());
                    ts.add(tvip);
                    //add sub calculations
                    rpc.subCalculations(p, new AsyncCallback<ArrayList<Calculation>>() {
                        @Override
                        public void onSuccess(ArrayList<Calculation> calcs) {
                            for (Iterator<Calculation> itc = calcs.iterator(); itc.hasNext();) {
                                Calculation c = itc.next();
                                final TreeViewItem tvic = new TreeViewItem("calculation", c.getId(), c.getName());
                                ts.add(tvip, tvic);
                                //add sub tasks
                                rpc.subTasks(c, new AsyncCallback<ArrayList<Task>>() {
                                    @Override
                                    public void onSuccess(ArrayList<Task> tasks) {
                                        for (Iterator<Task> itt = tasks.iterator(); itt.hasNext();) {
                                            Task t = itt.next();
                                            TreeViewItem tvit = new TreeViewItem("task", t.getId(), t.getName());
                                            store.add(tvic, tvit);
                                        }
                                    }
                                    @Override
                                    public void onFailure(Throwable caught) {
                                        Window.alert(caught.getMessage()); //pending delete, test                                        
                                    }
                                });
                            }
                        }
                        @Override
                        public void onFailure(Throwable caught) {
                            Window.alert(caught.getMessage()); //pending delete, test                            
                        }
                    });
                }
            }
            @Override
            public void onFailure(Throwable caught){
                Window.alert(caught.getMessage()); //pending delete, test
            }
        });
        
        //set selection mode
        treeProject.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        //select the first 'project' at the first load
        treeProject.getSelectionModel().select(0, false);
        //expand all leaves
        treeProject.expandAll();
    }
   
    public HandlerRegistration addSelectionChangedHandler(
                SelectionChangedEvent.SelectionChangedHandler<TreeViewItem> handler){
        return treeProject.getSelectionModel().addSelectionChangedHandler(handler);
    }
    
    public TreeStore<TreeViewItem> getTreeStore(){
        return this.store;
    }
    
    private void sampleData(){
        //set treeview
        TreeViewItem p1 = new TreeViewItem("project",1,"project 1");
        store.add(p1);
        TreeViewItem c1 = new TreeViewItem("calculation",1,"calculation 1");
        TreeViewItem c2 = new TreeViewItem("calculation",2,"calculation 2");
        store.add(p1, c1);
        store.add(p1, c2);
        TreeViewItem t1 = new TreeViewItem("task",1,"task 1");
        TreeViewItem t2 = new TreeViewItem("task",2,"task 2");
        store.add(c1, t1);
        store.add(c1, t2);
        
        TreeViewItem p2 = new TreeViewItem("project",2,"project 2");
        store.add(p2);
        store.add(p2, new TreeViewItem("calculation",3,"calculation 3"));
        store.add(p2, new TreeViewItem("calculation",4,"calculation 4"));             
    }
}
