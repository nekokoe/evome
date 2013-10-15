/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.core.client.ValueProvider;
        
import com.google.gwt.user.client.ui.Composite;

import org.evome.evoKalc.client.shared.*;
import com.google.gwt.editor.client.Editor;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.info.Info;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * carry out the task info, and enable the task info edit
 * 
 * @author nekoko
 */
public class TaskGridList extends Composite{
    public interface TaskProperties extends PropertyAccess<Task>{
        @Editor.Path("id")
        ModelKeyProvider<Task> key();
        
        ValueProvider<Task, Integer> id();
        ValueProvider<Task, String> name();
        @Editor.Path("createDate")
        ValueProvider<Task, Date> create();
        @Editor.Path("finishDate")
        ValueProvider<Task, Date> finish();
        ValueProvider<Task, Task.Status> status();
        @Editor.Path("priorityRank")
        ValueProvider<Task, Task.Priority> priority();
        @Editor.Path("kaKsConfig")
        ValueProvider<Task, Config> config();
        @Editor.Path("UUID")
        ValueProvider<Task, String> result();
    }
    
    private static final TaskProperties props = GWT.create(TaskProperties.class);
    
    private ListStore<Task> taskStore;
    private Grid<Task> taskGrid;
    
    public TaskGridList(ArrayList<Task> tasklist){
        taskStore = new ListStore<Task>(props.key());
        taskStore.addAll(tasklist);
        initGrid();
        initWidget(taskGrid);
    }
    
    private void initGrid(){
        //id
        ColumnConfig<Task, Integer> idcol = new ColumnConfig<Task, Integer>(props.id(), 50, "ID");
        //name
        ColumnConfig<Task, String> namecol = new ColumnConfig<Task, String>(props.name(), 100, "Name");
        //created on
        ColumnConfig<Task, Date> createcol = new ColumnConfig<Task, Date>(props.create(), 100, "Created on");
        createcol.setCell(new SimpleSafeHtmlCell<Date>(new AbstractSafeHtmlRenderer<Date>(){
            @Override
            public SafeHtml render(Date date){
                return SafeHtmlUtils.fromString(Shared.sqlDateFormat(date));
            }
        }));
        //finished on
        ColumnConfig<Task, Date> finishcol = new ColumnConfig<Task, Date>(props.finish(), 100, "Finished on");
        finishcol.setCell(new SimpleSafeHtmlCell<Date>(new AbstractSafeHtmlRenderer<Date>(){
            @Override
            public SafeHtml render(Date date){
                return SafeHtmlUtils.fromString(Shared.sqlDateFormat(date));
            }
        }));
        //status
        ColumnConfig<Task, Task.Status> statuscol = new ColumnConfig<Task, Task.Status>(props.status(), 100, "Status");
        statuscol.setCell(new SimpleSafeHtmlCell<Task.Status>(new AbstractSafeHtmlRenderer<Task.Status>(){
            @Override
            public SafeHtml render(Task.Status status){
                return SafeHtmlUtils.fromString(status.name());
            }
        }));
        //priority
        ColumnConfig<Task, Task.Priority> prcol = new ColumnConfig<Task, Task.Priority>(props.priority(), 100, "Priority");
        prcol.setCell(new SimpleSafeHtmlCell<Task.Priority>(new AbstractSafeHtmlRenderer<Task.Priority>(){
            @Override
            public SafeHtml render(Task.Priority pr){
                return SafeHtmlUtils.fromString(pr.name());
            }
        }));


        List<ColumnConfig<Task, ?>> columnlist = new ArrayList<ColumnConfig<Task, ?>>();
        columnlist.add(idcol);
        columnlist.add(namecol);
        columnlist.add(statuscol);
        columnlist.add(prcol);
        columnlist.add(createcol);
        columnlist.add(finishcol);


        
        ColumnModel<Task> cmodel = new ColumnModel<Task>(columnlist);
        
        //create grid instance
        taskGrid = new Grid<Task>(taskStore, cmodel);
        
        //add double click handler on ROW
        //to show task details
        taskGrid.addRowDoubleClickHandler(new RowDoubleClickEvent.RowDoubleClickHandler() {
            @Override
            public void onRowDoubleClick(RowDoubleClickEvent event) {
                Task t = taskStore.get(event.getRowIndex());
                Info.display("test", "double click on task " + t.getName());
            }
        });
       
    }
}
