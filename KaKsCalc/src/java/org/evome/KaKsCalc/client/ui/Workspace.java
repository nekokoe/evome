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
import org.evome.KaKsCalc.client.Project;
/**
 *
 * @author nekoko
 */
public class Workspace extends Composite {

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
    ContentPanel pnlTreeView;
    
    @UiField
    ContentPanel pnlWorkSpace;
    
    @UiField
    SimpleContainer conRoot;
    
    private static WorkspaceUiBinder uiBinder = GWT.create(WorkspaceUiBinder.class);

    interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {}
    
    public Workspace() {
        outerData.setMargins(new Margins(0, 0, 0, 0));
        //workspace margins
        northData.setMargins(new Margins(5));
        westData.setMargins(new Margins(0, 0, 5, 5));
        westData.setCollapsible(true);
        westData.setSplit(true);
        centerData.setMargins(new Margins(0, 5, 5, 5));
        
        //bind UI
        initWidget(uiBinder.createAndBindUi(this));
        //set root container width        
        conRoot.setWidth(Window.getClientWidth());
        
        //declare workspace widget
        final ProjectUtils project = new ProjectUtils();
        final CalculationUtils calculation = new CalculationUtils();
        final TaskUtils task = new TaskUtils();
        //add Tree View
        TreeView treeView = new TreeView();
        
        treeView.addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<TreeViewProperties>(){
            @Override
            public void onSelectionChanged(SelectionChangedEvent<TreeViewProperties> event){
                for(Iterator<TreeViewProperties> it = event.getSelection().iterator(); it.hasNext();){
                    String itemValue = it.next().getValue();
                    pnlWorkSpace.clear();
                    pnlWorkSpace.setHeadingText(itemValue);
                    if (itemValue.startsWith("project")){
                        pnlWorkSpace.add(project);
                        project.setCurrentProject(new Project(itemValue));
                    }else if(itemValue.startsWith("calc")){
                        pnlWorkSpace.add(calculation);
                    }else if(itemValue.startsWith("task")){
                        pnlWorkSpace.add(task);
                    }
                }
            }
        });
        pnlTreeView.add(treeView);

        //add project utils
        pnlWorkSpace.add(project);
    }
    
    
    
}
