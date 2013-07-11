/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import org.evome.KaKsCalc.client.widget.PortletWizard;
import org.evome.KaKsCalc.client.Calculation;
import org.evome.KaKsCalc.client.Task;
import org.evome.KaKsCalc.client.Resource;
import org.evome.KaKsCalc.client.GWTServiceAsync;
import org.evome.KaKsCalc.client.Shared;
import org.evome.KaKsCalc.client.shared.*;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import org.evome.KaKsCalc.client.KaKsCalc;
import org.evome.KaKsCalc.client.ui.events.*;
import org.evome.KaKsCalc.client.widget.resources.ExampleImages;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author nekoko
 */
public class TaskWizard extends PortletWizard{

    private static GWTServiceAsync rpc = Shared.getService();
    
    private Calculation mycalc;
    private Task mytask;
    
    private static ExampleImages images = ExampleImages.INSTANCE;
    
    
    public TaskWizard(){
        super();    //maybe no need to do this?
    }
    
    public TaskWizard(Calculation calc){
        this();
        this.mycalc = calc;
        
        //add step widgets
        final PropertyUI property = new PropertyUI();
        final PasteSeqUI pasteseq = new PasteSeqUI();
        final PairSelectUI pairselect = new PairSelectUI();        
        final ParamUI param = new ParamUI();
        final ConfirmUI confirm = new ConfirmUI();
        this.addStepWidget(property.ui, "Step 1 : Set Properties for new task");
        this.addStepWidget(pasteseq.ui, "Step 2 : Paste Sequences in FASTA format");
        this.addStepWidget(pairselect.ui, "Step 3 : Select gene pairs for calculation");
        this.addStepWidget(param.ui, "Step 4 : Select parameters for calculation");
        this.addStepWidget(confirm.ui, "Step 5 : Confirm before submitting task");
        
        //add handlers
        //common : finish
        this.setFinishHandler(new FinishHandler() {
            @Override
            public void onFinish() {
                //change status
                mytask.setStatus(Task.Status.TASK_READY);
                //update task info
                rpc.editTask(mytask, new AsyncCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        if (!success) {
                            Info.display("Error", "failed to edit task" + mytask.getName());
                        }
                    }
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.display("Error", "Communication with server failed.");
                    }
                });
                //fire UI update event - reborn the tree
                KaKsCalc.EVENT_BUS.fireEvent(new TreeUpdateEvent());
            }
        });
        //common : cancel
        this.setCancelHandler(new CancelHandler(){
            @Override
            public void onCancel(){
                //delete task already added
                if (mytask != null){
                    rpc.delTask(mytask, new AsyncCallback<Boolean>(){
                        @Override
                        public void onSuccess(Boolean success) {
                            if (!success){
                                Info.display("Error", "failed to delete task" + mytask.getName());
                            }
                        }
                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", "Communication with server failed.");
                        }                          
                    });
                }
                mytask = null;
            }
        });
        
        //property : next
        this.addNextStepHandler(property.ui, new NextStepHandler(){
            @Override
            public void onNextStep(){
                //force check input valid
                if (!property.namefield.validate()){
                    return;
                }
                
                //add or update task
                if (mytask == null || mytask.getId() == 0){
                    //add
                    final Task t = new Task();
                    t.setCalculation(mycalc);
                    t.setProjcet(mycalc.getProject());
                    t.setOwner(mycalc.getOwner());                    
                    t.setComment(property.commentfield.getText());
                    t.setName(property.namefield.getText());
                    rpc.addNewTask(t, new AsyncCallback<Task>() {
                        @Override
                        public void onSuccess(Task task) {
                            mytask = task;
                        }
                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", "Communication with server failed.");
                        }
                    });
                }else{
                    //edit
                    mytask.setCalculation(mycalc);
                    mytask.setComment(property.commentfield.getText());
                    mytask.setName(property.namefield.getText());
                    rpc.editTask(mytask, new AsyncCallback<Boolean>(){
                        @Override
                        public void onSuccess(Boolean success) {
                            if (!success){
                                Info.display("Error", "failed to edit task" + mytask.getName());
                            }
                        }
                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", "Communication with server failed.");
                        }                        
                    });
                }                
            };
        });
        
        //PasteSeq : next
        this.addNextStepHandler(pasteseq.ui, new NextStepHandler(){
            @Override
            public void onNextStep(){
                if (pasteseq.sequences.getValue().isEmpty()){
                    return; //do nothing if no input
                }
                UploadInfo info = new UploadInfo();
                info.UUID = mycalc.getUUID();
                info.account = mycalc.getOwner();
                info.type = UploadInfo.Type.TEXT;
                info.setText(pasteseq.sequences.getValue());
                rpc.uploadAsResource(info, new AsyncCallback<Resource>() {
                    @Override
                    public void onSuccess(Resource res) {
                        Info.display("Info", "Successfully save sequences as" + res.getName());
                    }
                    @Override
                    public void onFailure(Throwable caught) {
                        Info.display("Error", "Communication with server failed.");
                    }
                });
            }
        });
        
        //PasteSeq : back
        this.addPrevStepHandler(pasteseq.ui, new PrevStepHandler(){
            @Override
            public void onPrevStep(){
                
            }
        });
        
        //PairSelect : next
        
        //PairSelect : back
        
        //
    }
    
    
    
    
    
    //==============================================================================
    private class PropertyUI {

        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        FieldLabel namelabel = new FieldLabel();
        TextField namefield = new TextField();
        FieldLabel commentlabel = new FieldLabel();
        TextArea commentfield = new TextArea();

        public PropertyUI() {
            //set vertical layout            
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            //add task info form fields
            namelabel.setText("Name");
            namelabel.setWidget(namefield);
            namefield.setAllowBlank(false);
            commentlabel.setText("Description");
            commentlabel.setWidget(commentfield);
            //add widgets 2 container
            ui.add(new Label("Fill out the fields to create a new task"), layout);
            ui.add(namelabel, layout);
            ui.add(commentlabel, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
        }
    }
    
    //==============================================================================
    private class PasteSeqUI {

        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = 
                new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        FieldLabel seqlabel = new FieldLabel();
        TextArea sequences = new TextArea();

        public PasteSeqUI() {
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            sequences.setAllowBlank(false);
            sequences.setEmptyText("paste sequences in FASTA format here...");
            //add widgets to container
            ui.add(new Label("You can paste sequence in FASTA format in this box"), layout);
            ui.add(sequences, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
            ui.add(new Label("or, you could upload FASTA file(s) with the Sequence File Uploader nearby... "), layout);
        }
    }
    
    //==============================================================================
    private class PairSelectUI {
        //data store
        ArrayList<Pair> pairlist = new ArrayList<Pair>();
        //ui layouts
        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        //Property access
        SequenceSetProperties setprops = GWT.create(SequenceSetProperties.class);
        //list store
        ListStore<SequenceSet> store = new ListStore<SequenceSet>(setprops.key());
        //UI widgets
        ToolBar toolbar = new ToolBar();
        TextButton add = new TextButton("Add");
        TextButton delete = new TextButton("Delete");
        TextButton edit = new TextButton("Edit");
        //pair list
        ListView<SequenceSet, String> seqsetlist = new ListView<SequenceSet, String> (store, setprops.name());
        
        
        public PairSelectUI() {
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            ui.add(new Label("Select gene pairs for calculation"), layout);
            ui.add(toolbar, layout);
            ui.add(seqsetlist, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
            
            add.setIcon(images.add());
            delete.setIcon(images.delete());
            edit.setIcon(images.list());
            toolbar.add(add);
            toolbar.add(delete);
            toolbar.add(edit);
            //set seqsetlist selection mode
            seqsetlist.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
            
            //add button handlers
            add.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    final PairSelector selector = new PairSelector();
                    selector.addHideHandler(new HideEvent.HideHandler() {

                        @Override
                        public void onHide(HideEvent event) {
                            //add generated seqset to list
                            seqsetlist.getStore().add(selector.myseqset);
                        }
                    });
                    selector.show();                            
                }
            });
            edit.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    final PairSelector selector = new PairSelector(seqsetlist.getSelectionModel().getSelectedItem());
                    selector.addHideHandler(new HideEvent.HideHandler() {

                        @Override
                        public void onHide(HideEvent event) {
                            seqsetlist.getStore().update(selector.myseqset);
                        }
                    });
                    selector.show();
                }   
            });
            delete.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    seqsetlist.getStore().remove(seqsetlist.getSelectionModel().getSelectedItem());
                }
            });
        }
        
        
    }
    
    private class PairSelector extends Window{
        public SequenceSet myseqset;
        
        VerticalLayoutContainer container = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        //property access
        ResourceProperties resources = GWT.create(ResourceProperties.class);
        SequenceProperties props = GWT.create(SequenceProperties.class);
        //list storage
        ListStore<Resource> filestore = new ListStore<Resource>(resources.key());
        ListStore<Sequence> leftstore = new ListStore<Sequence>(props.key());
        ListStore<Sequence> rightstore = new ListStore<Sequence>(props.key());
        //ui widgets
        TextField setName = new TextField();
        ComboBox<Resource> filelist = new ComboBox<Resource>(filestore, resources.label());
        DualListField<Sequence, String> seqpairs = new DualListField<Sequence, String>(leftstore, rightstore, props.id(), new TextCell());        
        
        public PairSelector(){
            //set window appearance
            this.setHeadingText("Select sequences from files and add to pair group");
            //add widgets to window
            container.setWidth(400);
            container.setHeight(500);
            container.add(new FieldLabel(setName, "Group Name"), layout);
            container.add(new FieldLabel(filelist,"Select File"), layout);
            container.add(new Label("Add sequences to pair group"), layout);
            container.add(seqpairs, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
            container.add(new Label("In a group, each sequence is paired with all others for calculation."), layout);
            container.add(new Label("e.g. 3 sequences A,B,C are paired as A-B, B-C, A-C"), layout);
            this.setWidget(container);
            //set filelist
            fillFileList(mycalc.getUUID()); 
            filelist.addSelectionHandler(new SelectionHandler<Resource>(){
               @Override
               public void onSelection(SelectionEvent<Resource> event){
                   fillSeqList(event.getSelectedItem());
               }
            });
            
            //set pair selector
            seqpairs.setEnableDnd(true);
            
            //process when closing the window
            this.addHideHandler(new HideEvent.HideHandler() {

                @Override
                public void onHide(HideEvent event) {
                    if (myseqset == null){
                        myseqset = new SequenceSet();       //move new instance call here to solve auto number mistake
                    }
                    
                    if (setName.getText().isEmpty()){
                        myseqset.setName(myseqset.getDefaultName());
                    }else{
                        myseqset.setName(setName.getText());
                    }                    
                    myseqset.clear();
                    myseqset.addAll(rightstore.getAll());
                }
            });
        }
        
        public PairSelector(SequenceSet seqset){
            this();
            //to edit pair, directly edit on pair
            myseqset = seqset;
            //add seqset to right list
            rightstore.addAll(seqset);
            //set group name
            setName.setText(seqset.getName());
        }
        
        private void fillFileList(String parent){
            rpc.childResources(parent, new AsyncCallback<ArrayList<Resource>>() {
                @Override
                public void onSuccess(ArrayList<Resource> reslist) {
                    filestore.addAll(reslist);
                    filelist.setValue(filestore.get(0), true);      //try to select the first
                    fillSeqList(filestore.get(0));                  //try to select the first
                }

                @Override
                public void onFailure(Throwable caught) {
                    Info.display("Error", "Communication with server failed.");
                }
            });
        }
        
        private void fillSeqList(Resource res){
            leftstore.clear();
            rpc.parseSeqIDs(res, new AsyncCallback<ArrayList<Sequence>>() {
                @Override
                public void onSuccess(ArrayList<Sequence> idlist) {
                    leftstore.addAll(idlist);
                    //remove seqset from left list
                    //REMEMBER : RPC CALL IS NOT SEQUENTIAL!!!
                    for (Iterator<Sequence> it = rightstore.getAll().iterator(); it.hasNext();) {
                        try{
                            leftstore.remove(it.next());
                        }catch(Exception ex){
                            
                        }
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    Info.display("Error", "Communication with server failed.");
                }
            });
          
        }
    }
    
    //==============================================================================
    private class ParamUI{
        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        
        public ParamUI(){
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            ui.add(new Label("Select parameters for calculation"), layout);
        }
        
    }

    //==============================================================================    
    private class ConfirmUI{
        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        public ConfirmUI(){
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            ui.add(new Label("Confirm your input and submit the task "), layout);            
        }
    }    
}
