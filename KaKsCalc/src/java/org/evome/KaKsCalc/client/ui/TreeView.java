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
import org.evome.KaKsCalc.client.ui.events.TreeSelectChangeEvent;
import org.evome.KaKsCalc.client.ui.events.TreeUpdateEvent;

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
        initTree();
    }
    
    public TreeView(Session s){
        initWidget(uiBinder.createAndBindUi(this));
        
        Account account = new Account();    //tricky, only for test
        account.setUserID(s.getUserID());   //tricky, only for test
        
        initTree();
        
        addUserProjects(account);

    }
    
    private void initTree(){
        //set selection mode
        treeProject.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        //select the first 'project' at the first load
        treeProject.getSelectionModel().select(0, false);
        //expand all leaves
        treeProject.expandAll();
        //add select change handler
        treeProject.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<TreeViewItem>(){
            @Override
            public void onSelectionChanged(SelectionChangedEvent<TreeViewItem> event){
                TreeSelectChangeEvent tscEvent = new TreeSelectChangeEvent();
                tscEvent.setSelection(event.getSelection());
                KaKsCalc.EVENT_BUS.fireEvent(tscEvent);
            }
        });
        
        //add broadcast handler TreeUpdateEventHandler, to update the treeview when data changes
        KaKsCalc.EVENT_BUS.addHandler(TreeUpdateEvent.TYPE, new TreeUpdateEvent.TreeUpdateEventHandler() {
            @Override
            public void onUpdate(TreeUpdateEvent event) {
                switch(event.getAction()){
                    case ADD:
                        addNode(event.getParentTVI(),event.getTreeViewItem());
                        break;
                    case UPDATE:
                        updateNode(event.getTreeViewItem());
                        break;
                    case DELETE:
                        removeNode(event.getTreeViewItem());
                        break;
                }
            }
        });
    }
   
    @Deprecated
    public HandlerRegistration addSelectionChangedHandler(
                SelectionChangedEvent.SelectionChangedHandler<TreeViewItem> handler){
        return treeProject.getSelectionModel().addSelectionChangedHandler(handler);
    }
    
    public TreeStore<TreeViewItem> getTreeStore(){
        return this.store;
    }
    
    public Tree<TreeViewItem, String> getTree(){
        return this.treeProject;
    }
    
    
    private void addUserProjects(Account account){
        rpc.userProjects(account, new AsyncCallback<ArrayList<Project>>() {
            @Override
            public void onSuccess(ArrayList<Project> projects) {
                for (Iterator<Project> itp = projects.iterator(); itp.hasNext();) {
                    Project p = itp.next();
                    final TreeViewItem tvip = new TreeViewItem(TreeViewItem.Type.PROJECT, p.getId(), p.getName());
                    store.add(tvip);
                    //add sub calculations
                    addSubCalculations(tvip, p);
                }
            }
            @Override
            public void onFailure(Throwable caught){
                Window.alert(caught.getMessage()); //pending delete, test
            }
        });        
    }
    
    private void addSubCalculations(TreeViewItem tvi, Project p) {
        final TreeViewItem tvip = tvi;
        rpc.subCalculations(p, new AsyncCallback<ArrayList<Calculation>>() {
            @Override
            public void onSuccess(ArrayList<Calculation> calcs) {
                for (Iterator<Calculation> itc = calcs.iterator(); itc.hasNext();) {
                    Calculation c = itc.next();
                    TreeViewItem tvic = new TreeViewItem(TreeViewItem.Type.CALCULATION, c.getId(), c.getName());
                    store.add(tvip, tvic);
                    //add sub tasks
                    addSubTasks(tvic, c);
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage()); //pending delete, test                            
            }
        });
    }
    
    private void addSubTasks(TreeViewItem tvi, Calculation c){
        final TreeViewItem tvic = tvi;
        rpc.subTasks(c, new AsyncCallback<ArrayList<Task>>() {
            @Override
            public void onSuccess(ArrayList<Task> tasks) {
                for (Iterator<Task> itt = tasks.iterator(); itt.hasNext();) {
                    Task t = itt.next();
                    TreeViewItem tvit = new TreeViewItem(TreeViewItem.Type.TASK, t.getId(), t.getName());
                    store.add(tvic, tvit);
                }
            }
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage()); //pending delete, test                                        
            }
        });   
    }
    
    private void addNode(TreeViewItem parent, TreeViewItem node){
        if (parent == null){
            store.add(node);    //null parent means adding a project node
        }else{
            store.add(parent, node);
        }
    }
    
    private void updateNode(TreeViewItem node) {
        //depends on leaf type
        switch (node.getType()) {
            case PROJECT:
                store.update(node);
                store.removeChildren(node);
                Project p = new Project();
                p.setId(node.getId());   //tricky
                addSubCalculations(node, p);
                break;
            case CALCULATION:
                store.update(node);
                store.removeChildren(node);
                Calculation c = new Calculation();
                c.setId(node.getId());   //tricky
                addSubTasks(node, c);
                break;
            case TASK:
                store.update(node);
                break;
        }
    }
    
    private void removeNode(TreeViewItem node) {
        TreeViewItem parent = store.getParent(node);
        parent = (parent == null)? store.getChild(0) : parent;
        KaKsCalc.EVENT_BUS.fireEvent(new TreeSelectChangeEvent(parent));
        treeProject.getSelectionModel().select(node, true);
        store.removeChildren(node);
        store.remove(node);
    }

    
    private void sampleData(){
        //set treeview
        TreeViewItem p1 = new TreeViewItem(TreeViewItem.Type.PROJECT,1,"project 1");
        store.add(p1);
        TreeViewItem c1 = new TreeViewItem(TreeViewItem.Type.CALCULATION,1,"calculation 1");
        TreeViewItem c2 = new TreeViewItem(TreeViewItem.Type.CALCULATION,2,"calculation 2");
        store.add(p1, c1);
        store.add(p1, c2);
        TreeViewItem t1 = new TreeViewItem(TreeViewItem.Type.TASK,1,"task 1");
        TreeViewItem t2 = new TreeViewItem(TreeViewItem.Type.TASK,2,"task 2");
        store.add(c1, t1);
        store.add(c1, t2);
        
        TreeViewItem p2 = new TreeViewItem(TreeViewItem.Type.PROJECT,2,"project 2");
        store.add(p2);
        store.add(p2, new TreeViewItem(TreeViewItem.Type.CALCULATION,3,"calculation 3"));
        store.add(p2, new TreeViewItem(TreeViewItem.Type.CALCULATION,4,"calculation 4"));             
    }
}
