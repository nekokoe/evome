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
import com.sencha.gxt.widget.core.client.Window;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
/**
 *
 * @author nekoko
 * 
 * ProjectUtils provide tools for project management
 * 
 */
public class ProjectUtils extends Composite {
    
    private static ProjectUtilsUiBinder uiBinder = GWT.create(ProjectUtilsUiBinder.class);
    
    interface ProjectUtilsUiBinder extends UiBinder<Widget, ProjectUtils> {
    }
    
    public ProjectUtils() {
        initWidget(uiBinder.createAndBindUi(this));
        initListData();
    }
    
    ListStore<ListViewProperties> listStore = new ListStore<ListViewProperties>(new ModelKeyProvider<ListViewProperties>(){
        @Override
        public String getKey(ListViewProperties lvp){
            return lvp.getKey();
        } 
    });
    
    ValueProvider<ListViewProperties, String> valueProvider = new ValueProvider<ListViewProperties, String>(){
        @Override
        public String getValue(ListViewProperties lvp){
            return lvp.getValue();
        }
        @Override
        public void setValue(ListViewProperties tvp, String value){
            
        }
        @Override
        public String getPath(){
            return "list";
        }  
    };
    
//    @UiField(provided=true)
//    ListView<ListViewProperties, String> listStatus = new ListView<ListViewProperties, String>(listStore, valueProvider);
    
    private void initListData(){
        listStore.add(new ListViewProperties("1","Project ID"));
        listStore.add(new ListViewProperties("2","Project Name"));
        listStore.add(new ListViewProperties("3","Project Owner"));
        listStore.add(new ListViewProperties("4","Project Description"));
        
    }
}
