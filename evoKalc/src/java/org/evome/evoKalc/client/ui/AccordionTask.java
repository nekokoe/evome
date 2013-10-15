/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;


import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.info.Info;
import org.evome.evoKalc.client.shared.*;
import org.evome.evoKalc.client.event.ViewChangeEvent;


import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.evome.evoKalc.client.event.TaskChangeEvent;

/**
 *
 * @author nekoko
 */
public class AccordionTask extends Composite{
    TreeViewItemProps props = GWT.create(TreeViewItemProps.class);
    
    TreeStore<TreeViewItem> treeStore = new TreeStore<TreeViewItem>(props.key());
    Tree<TreeViewItem, String> tree = new Tree<TreeViewItem, String>(treeStore, props.name());
    
    public AccordionTask(){
        this.initWidget(tree);
        tree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        initFolder();
        
        //response to UIRefreshEvent
        Shared.EVENT_BUS.addHandler(TaskChangeEvent.TYPE, new TaskChangeEvent.UIRefreshHandler() {
            @Override
            public void onRefresh(TaskChangeEvent event) {
                List<TreeViewItem> list = tree.getSelectionModel().getSelectedItems();
                //manully fire an fake event in order to update the task list
                tree.getSelectionModel().fireEvent(new SelectionChangedEvent<TreeViewItem>(list));
            }
        });
    }
    
    private void initFolder(){
        TreeViewItem<Folder> tviInProgress = new TreeViewItem<Folder>(new Folder("In Progress", Task.Classify.IN_PROGRESS));
        TreeViewItem<Folder> tviToday = new TreeViewItem(new Folder("Today", Task.Classify.TODAY));
        TreeViewItem<Folder> tviThisWeek = new TreeViewItem(new Folder("This Week", Task.Classify.THIS_WEEK));
        TreeViewItem<Folder> tviThisMonth = new TreeViewItem(new Folder("This Month", Task.Classify.THIS_MONTH));
        TreeViewItem<Folder> tviThisYear = new TreeViewItem(new Folder("This Year", Task.Classify.THIS_YEAR));
        TreeViewItem<Folder> tviEarlier = new TreeViewItem(new Folder("Earlier", Task.Classify.EARLIER));
        TreeViewItem<Folder> tviDelete = new TreeViewItem(new Folder("Recycle Bin", Task.Classify.RECYCLE_BIN));
        TreeViewItem<Folder> tviAll = new TreeViewItem(new Folder("All Tasks", Task.Classify.ALL));
        treeStore.add(tviInProgress);
        treeStore.add(tviToday);
        treeStore.add(tviThisWeek);
        treeStore.add(tviThisMonth);
        treeStore.add(tviThisYear);
        treeStore.add(tviEarlier);
        treeStore.add(tviDelete);
        treeStore.add(tviAll);
        
        tree.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<TreeViewItem>() {

            @Override
            public void onSelectionChanged(SelectionChangedEvent<TreeViewItem> event) {
                TreeViewItem item = event.getSelection().get(0);    //for a single selection mode
                if (item.getItem() instanceof Folder){
                    Folder f = (Folder)item.getItem();                          //feel not good, revising in the future
                    Task.Classify c = (Task.Classify) f.getClassify();          //feel not good, revising in the future
                    
                    if (c == null){
                        c = Task.Classify.ALL;      //force default as ALL
                    }
                    //request the tasklist, fire change event
                    Shared.RPC.userTasks(Shared.MY_SESSION, Shared.MY_SESSION.getAccount(), c, new AsyncCallback<ArrayList<Task>>(){

                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", "Fail to request the task list from server");
                        }

                        @Override
                        public void onSuccess(ArrayList<Task> result) {
                            Shared.EVENT_BUS.fireEvent(new ViewChangeEvent(new TaskGridList(result)));
                        }
                        
                    });
                }
            }
        });
    }

    
}
