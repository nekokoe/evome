/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.SingleUploader;
import com.sencha.gxt.widget.core.client.button.TextButton;
import java.util.ArrayList;
import com.sencha.gxt.widget.core.client.info.Info;
import org.evome.KaKsCalc.client.Shared.DNAPair;
import org.evome.KaKsCalc.client.Shared.Sequence;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import org.evome.KaKsCalc.client.*;

/**
 *
 * @author nekoko
 * 
 * task wizard consists of 4 steps
 * 1.upload fasta file
 * 2.select gene pairs
 * 3.select parameters
 * 4.confirm and submit
 */
public class TaskWizard extends Composite {
    
    
    private final ArrayList<String> filelist = new ArrayList<String>();
    private final ArrayList<DNAPair> pairlist = new ArrayList<DNAPair>(); 
    private final SimpleContainer container = new SimpleContainer();
    private final FramedPanel attrib = initAttribPanel();
    private final FramedPanel upload = initUploadPanel();
    private final FramedPanel select = initSelectPanel();
    private final FramedPanel param = initParamPanel();
    private final FramedPanel confirm = initConfirmPanel();
    private final MarginData margin = new MarginData(0);
    private final Task mytask = new Task();
    
    private int step = 1;   //counter of steps
    
    public TaskWizard(){
        //initWidget(uiBinder.createAndBindUi(this));
        initWidget(container);
        //init container
        container.add(attrib, margin);        
    }
    
    public TaskWizard(Calculation calc) {
        this();        
        mytask.setCalculation(calc);
    }
    
    
    private FramedPanel initAttribPanel(){
        //set task basic info
        FramedPanel panel = new FramedPanel();
        panel.setHeaderVisible(false);
//        panel.setWidth(400);
        panel.setHeight(400);
        //set vertical layout
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        panel.add(vertical);
        //add task info form fields
        FieldLabel namelabel = new FieldLabel();
        final TextField namefield = new TextField();
        namelabel.setText("Name");        
        namelabel.setWidget(namefield);
        FieldLabel commentlabel = new FieldLabel();
        final TextArea commentfield = new TextArea();
        commentlabel.setText("Description");        
        commentlabel.setWidget(commentfield);
        //add widgets 2 container
        vertical.add(new Label("Step " + step + " : add new task"), layout);
        vertical.add(namelabel, layout);
        vertical.add(commentlabel, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
        //add buttons
        TextButton next = new TextButton("next");
        next.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               step++;
               mytask.setName(namefield.getText());
               mytask.setComment(commentfield.getText());
               container.clear();
               container.add(upload, margin);
           }
        });
        panel.addButton(next);
        return panel;
    }
    
    private FramedPanel initUploadPanel(){
        FramedPanel panel = new FramedPanel();
//        panel.setWidth(400);
        panel.setHeight(400);
//        panel.setHeadingText("Step 2 : upload sequence");
        panel.getElement().setMargins(20);
        //add vertical layout
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        panel.add(vertical);

        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));

        //set file list
        final ListView<String, String> fileListView = new ListView<String, String>(new ListStore<String>(new ModelKeyProvider<String>(){
            @Override
            public String getKey(String key){
                return key;
            }
        }), new ValueProvider<String, String>(){
            @Override
            public String getValue(String s){
                return s;
            }
            @Override
            public void setValue(String s, String value){
            }
            @Override
            public String getPath(){
                return "key";
            }
        });
        
        fileListView.getSelectionModel().setSelectionMode(Style.SelectionMode.SIMPLE);

        //set uploader
        SingleUploader uploader = new SingleUploader();
        uploader.avoidRepeatFiles(true);
        uploader.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
            @Override
            public void onFinish(IUploader uploader) {
                if (uploader.getStatus() == Status.SUCCESS){
                    UploadedInfo info = uploader.getServerInfo();
                    Info.display("Upload Success", "Finished uploading file " + info.name);
                    filelist.add(info.message);
                    fileListView.getStore().add(info.name);
                }
            }
        });
        
        //add widgets to container
        vertical.add(new Label("Step " + step + " : upload sequence file"), layout);
        vertical.add(uploader, layout);
        vertical.add(fileListView, layout);
        
        //add bottom buttons
        TextButton btnBack = new TextButton("back");
        TextButton btnNext = new TextButton("next");
        //add click event for back and next
        btnBack.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               step++;
               container.clear();
               container.add(attrib, margin);
           }
        });        
        btnNext.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               //register file resources, remove temp files
               
               
               container.clear();
               container.add(select, margin);
           }
        });
        panel.addButton(btnBack);
        panel.addButton(btnNext);
        return panel;
    }
    
    private FramedPanel initSelectPanel(){
        FramedPanel panel = new FramedPanel();
        //panel.setWidth(400);
        panel.setHeight(400);
        //panel.setHeadingText("Step 3 : select gene pairs for calculation");
        panel.getElement().setMargins(20);
        //add vertical layout
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        panel.add(vertical);
        //
        vertical.add(new Label("Select gene pairs for kaks calculation"), layout);
        //add button
        TextButton btnBack = new TextButton("back");
        TextButton btnNext = new TextButton("next");
        //add click event for buttons
        btnBack.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               container.clear();
               container.add(upload, margin);
           }
        });
        btnNext.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               container.clear();
               container.add(param, margin);
           }
        });      
        panel.addButton(btnBack);
        panel.addButton(btnNext);
        return panel;
    }
    
    private FramedPanel initParamPanel(){
        FramedPanel panel = new FramedPanel();
        panel.setWidth(400);
        panel.setHeight(400);
        panel.setHeadingText("Step 4 : select params for calculation");
        panel.add(new Label(""));
        //add buttons
        TextButton btnBack = new TextButton("back");
        TextButton btnNext = new TextButton("next");
        //add click event for buttons
        btnBack.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               container.clear();
               container.add(select, margin);
           }
        });
        btnNext.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               container.clear();
               container.add(confirm, margin);
           }
        });           
        panel.addButton(btnBack);
        panel.addButton(btnNext);
        return panel;
    }
    
    private FramedPanel initConfirmPanel(){
        FramedPanel panel = new FramedPanel();
        panel.setWidth(400);
        panel.setHeight(400);
        panel.setHeadingText("Step 5 : confirm before submitting task");
        panel.add(new Label("Select gene pairs for kaks calculation"));
        //add buttons
        TextButton btnBack = new TextButton("back");
        TextButton btnSubmit = new TextButton("SUBMIT");
        //add click events for buttons
        btnBack.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               container.clear();
               container.add(param, margin);
           }
        });
        btnSubmit.addSelectHandler(new SelectEvent.SelectHandler(){
           @Override
           public void onSelect(SelectEvent event){
               //submit task
           }
        });           
        panel.addButton(btnBack);
        panel.addButton(btnSubmit);
        return panel;
    }
    
    private class WizFlow {
        private ArrayList<Widget> wizFlow;
        //wizFlow controls the workflow of wizard, could be changed on demand, but please note:
        //1. the create panel will create a task which is needed for other steps. It is forced placing on the first.
        //2. the confirm panel will check params and end the wizard, steps after it will be ignored        
    }
}
