/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import java.util.Iterator;
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
/**
 *
 * @author nekoko
 */
public class TreeView extends Composite {
    
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
    
    private static TreeViewUiBinder uiBinder = GWT.create(TreeViewUiBinder.class);
    
    interface TreeViewUiBinder extends UiBinder<Widget, TreeView> {
    }   
    
    public TreeView() {
        initWidget(uiBinder.createAndBindUi(this));
        //set autoselect active
        
        //set treeview
        TreeViewProperties p1 = new TreeViewProperties("1","project 1");
        store.add(p1);
        TreeViewProperties c1 = new TreeViewProperties("1.1","calculation 1");
        TreeViewProperties c2 = new TreeViewProperties("1.2","calculation 2");
        store.add(p1, c1);
        store.add(p1, c2);
        TreeViewProperties t1 = new TreeViewProperties("1.1.1","task 1");
        TreeViewProperties t2 = new TreeViewProperties("1.1.2","task 2");
        store.add(c1, t1);
        store.add(c1, t2);
        
        TreeViewProperties p2 = new TreeViewProperties("2","project 2");
        store.add(p2);
        store.add(p2, new TreeViewProperties("2.1","calculation 1"));
        store.add(p2, new TreeViewProperties("2.2","calculation 2"));      
        

        //set selection mode
        treeProject.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        //defined selection change handler
//        treeProject.getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<TreeViewProperties>(){
//            @Override
//            public void onSelectionChanged(SelectionChangedEvent<TreeViewProperties> event){
//                for(Iterator<TreeViewProperties> it = event.getSelection().iterator(); it.hasNext();){
//                    Window.alert("you have selected: " + it.next().getValue());
//                }
//            }
//        });
    }
    
    public HandlerRegistration addSelectionChangedHandler(
                SelectionChangedEvent.SelectionChangedHandler<TreeViewProperties> handler){
        return treeProject.getSelectionModel().addSelectionChangedHandler(handler);
    }
}
