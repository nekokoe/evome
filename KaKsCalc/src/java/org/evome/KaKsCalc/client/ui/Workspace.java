/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 *
 * @author nekoko
 */
public class Workspace extends Composite {

    @UiField(provided = true)
    MarginData outerData = new MarginData();
    @UiField(provided = true)
    BorderLayoutData northData = new BorderLayoutData(100);
    @UiField(provided = true)
    BorderLayoutData westData = new BorderLayoutData(150);
    @UiField(provided = true)
    MarginData centerData = new MarginData();
    @UiField(provided = true)
    BorderLayoutData eastData = new BorderLayoutData(150);
    @UiField(provided = true)
    BorderLayoutData southData = new BorderLayoutData(100);

    @UiField(provided=true)
    TreeStore<TreeViewProperties> store = new TreeStore<TreeViewProperties>(new ModelKeyProvider<TreeViewProperties>(){
        @Override
        public String getKey(TreeViewProperties tvp){
            return tvp.getKey();
        }
    });
    
    @UiField(provided=true)
    ValueProvider<TreeViewProperties, String> valueProvider = new ValueProvider<TreeViewProperties, String>(){
        @Override
        public String getValue(TreeViewProperties tvp){
            return tvp.getValue();
        }
        @Override
        public void setValue(TreeViewProperties tvp, String value){
            
        }
        @Override
        public String getPath(){
            return "key";
        }
    };
    
    @UiField
    Tree<TreeViewProperties, String> treeProject;
    @UiField
    ContentPanel pnlWorkSpace;
    
    private static WorkspaceUiBinder uiBinder = GWT.create(WorkspaceUiBinder.class);

    interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {}
    
    public Workspace() {
        //workspace margins
        northData.setMargins(new Margins(5));
        westData.setMargins(new Margins(0, 0, 5, 5));
        westData.setCollapsible(true);
        westData.setSplit(true);
        centerData.setMargins(new Margins(0, 5, 5, 5));
        //set autoselect active
        treeProject.setAutoSelect(true);        
        //set treeview
        TreeViewProperties p1 = new TreeViewProperties("1","project 1");
        store.add(p1);
        store.add(p1, new TreeViewProperties("1.1","calculation 1"));
        store.add(p1, new TreeViewProperties("1.2","calculation 2"));        
        TreeViewProperties p2 = new TreeViewProperties("2","project 2");
        store.add(p2);
        store.add(p2, new TreeViewProperties("2.1","calculation 1"));
        store.add(p2, new TreeViewProperties("2.2","calculation 2"));          

        
        
        //bind UI
        initWidget(uiBinder.createAndBindUi(this));
        
        //add project utils
        pnlWorkSpace.add(new ProjectUtils());
    }
    
    
}
