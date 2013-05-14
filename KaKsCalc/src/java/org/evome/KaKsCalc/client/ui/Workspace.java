/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;
import java.util.Iterator;
import org.evome.KaKsCalc.client.*;
/**
 *
 * @author nekoko
 */
public class Workspace extends Composite {
    
    private static WorkspaceUiBinder uiBinder = GWT.create(WorkspaceUiBinder.class);

    interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {}
    
    private TreeView treeView;
    
    public Workspace(Session s){
        initWidget(uiBinder.createAndBindUi(this));        
        //workspace binding to a session
        init();
        setTreeView(new TreeView(s));
    }
    
    public Workspace() {
        initWidget(uiBinder.createAndBindUi(this));        
        init();
        setTreeView(new TreeView());
    }
    
    private void init(){
        //set workspace margins
        outerData.setMargins(new Margins(0, 0, 0, 0));
        northData.setMargins(new Margins(5));
        westData.setMargins(new Margins(0, 0, 5, 5));
        westData.setCollapsible(true);
        westData.setSplit(true);
        centerData.setMargins(new Margins(0, 5, 5, 5));
        //set root container width        
        conRoot.setWidth(Window.getClientWidth());        
    }
    
    public final void setTreeView(TreeView tv){
        this.treeView = tv;

        //add selection handler
        tv.addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<TreeViewItem>(){
            @Override
            public void onSelectionChanged(SelectionChangedEvent<TreeViewItem> event){
                for(Iterator<TreeViewItem> it = event.getSelection().iterator(); it.hasNext();){
                    TreeViewItem item = it.next();
                    pnlWorkSpace.clear();
                    pnlWorkSpace.setHeadingText(item.getValue());
                    if (item.getType().equalsIgnoreCase("project")){
                        pnlWorkSpace.add(new ProjectUtils(item.getId()));
                    }else if(item.getType().equalsIgnoreCase("calculation")){
                        pnlWorkSpace.add(new CalculationUtils(item.getId()));
                    }else if(item.getType().equalsIgnoreCase("task")){
                        pnlWorkSpace.add(new TaskUtils(item.getId()));
                    }
                }
            }
        });
        pnlTreeView.clear();
        pnlTreeView.add(tv);        
        tv.treeProject.getSelectionModel().select(0, false);
    }
    
    public TreeView getTreeView(){
        return this.treeView;
    }    
    
    @UiField(provided = true)
    MarginData outerData = new MarginData();
    @UiField(provided = true)
    BorderLayoutData northData = new BorderLayoutData(50);
    @UiField(provided = true)
    BorderLayoutData westData = new BorderLayoutData(150);
    @UiField(provided = true)
    MarginData centerData = new MarginData();
    @UiField(provided = true)
    BorderLayoutData eastData = new BorderLayoutData(150);
    @UiField(provided = true)
    BorderLayoutData southData = new BorderLayoutData(100);


    @UiField
    public ContentPanel pnlTreeView;
    
    @UiField
    public ContentPanel pnlWorkSpace;
    
    @UiField
    SimpleContainer conRoot;
}
