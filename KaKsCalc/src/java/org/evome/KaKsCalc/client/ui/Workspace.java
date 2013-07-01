/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import org.evome.KaKsCalc.client.shared.TreeViewItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
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
import org.evome.KaKsCalc.client.ui.events.TreeSelectChangeEvent;
import org.evome.KaKsCalc.client.ui.events.TreeUpdateEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.widget.core.client.info.Info;



/**
 *
 * @author nekoko
 */
public class Workspace extends Composite {
    
    private static WorkspaceUiBinder uiBinder = GWT.create(WorkspaceUiBinder.class);

    interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {}
    

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
        conRoot.setHeight(Window.getClientHeight());
        
        //experimental: add a TreeSelectChangeHandelr to EVENTBUS, see if events could be broadcast and listened
        //OK!
        KaKsCalc.EVENT_BUS.addHandler(TreeSelectChangeEvent.TYPE, new TreeSelectChangeEvent.TreeSelectChangeHandler() {
            @Override
            public void onTreeSelectChange(TreeSelectChangeEvent event) {
                for (Iterator<TreeViewItem> it = event.getSelection().iterator(); it.hasNext();) {
                    TreeViewItem item = it.next();
                    pnlWorkSpace.clear();
                    //set pnlWorkSpace heading text
                    pnlWorkSpace.setHeadingText(item.getValue());
                    //apply right panel according to item type
                    switch(item.getType()){
                        case PROJECT:
                            pnlWorkSpace.add(new ProjectUtils(item));
                            break;
                        case CALCULATION:
                            pnlWorkSpace.add(new CalculationUtils(item));
                            break;
                        case TASK:
                            pnlWorkSpace.add(new TaskUtils(item));
                            break;
                    }
                }
            }
        });
        

        
    }
    
    public final void setTreeView(TreeView tv){
        //in workspace, only 1 treeview implemented
        //set it to static variable to make it visible to other widgets
        pnlTreeView.clear();
        pnlTreeView.add(tv);        
        tv.treeProject.getSelectionModel().select(0, false);
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
