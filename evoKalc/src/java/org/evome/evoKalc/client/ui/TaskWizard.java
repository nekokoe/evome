/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.editor.client.Editor;
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
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.MatchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Grid;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.core.client.util.ToggleGroup;

import org.evome.evoKalc.client.shared.*;
import org.evome.evoKalc.client.event.TaskChangeEvent;
import org.evome.evoKalc.client.ui.utils.*;
import org.evome.evoKalc.client.GWTServiceAsync;

/**
 *
 * @author nekoko
 */
public class TaskWizard extends CommonWizard{

    private static GWTServiceAsync rpc = Shared.RPC;
    private static Session session = Shared.MY_SESSION;
    
    //related data
    private Task mytask;    //to generate task
    private ArrayList<Pair> mypairlist;     //gene pair list
    private Config myconfig;        //task parameters    
    private ArrayList<Sequence> mypasteseqs;       //store pasted sequences, pass to server when submitting
    
    
    //step widgets
    final PropertyUI property = new PropertyUI();
    final PairModeUI pairmode = new PairModeUI();
    final UploadSeqUI uploadseq = new UploadSeqUI();
    final PairSelectUI pairselect = new PairSelectUI();
    final ParamUI param = new ParamUI();
    final ConfirmUI confirm = new ConfirmUI(); 
    
    public TaskWizard(){
        //init UI size
        this.setWidth(400);
        this.setHeight(600);        
        
        //init data objs
        mytask = new Task();
        mypairlist = new ArrayList<Pair>();
        myconfig = new Config();
        mypasteseqs = new ArrayList<Sequence>();
        
        //add step widgets
        this.addStepWidget(property.ui, "Set Properties for new task");
        this.addStepWidget(uploadseq.ui, "Paste Sequences in FASTA format");    
        this.addStepWidget(pairmode.ui, "Choose the pair mode");    
        this.addStepWidget(pairselect.ui, "Select gene pairs for calculation");
        this.addStepWidget(param.ui, "Select parameters for calculation");
        this.addStepWidget(confirm.ui, "Confirm before submitting task");
        
        //add handlers
        this.addCommonHandlers();
        this.addPropertyUIHandlers();
        this.addPairModeUIHandlers();
        this.addUploadSeqUIHandlers();
        this.addPairSelectUIHandlers();
        this.addParamUIHandlers();
        

        
    }
    
    private void addCommonHandlers(){
        //common : finish
        this.setFinishHandler(new FinishHandler() {
            @Override
            public void onFinish() {

                if (mytask.getStatus() == Task.Status.NEW) {
                    //add new task
                    rpc.createTaskByWizard(session, mytask, mypairlist, myconfig, new AsyncCallback<Task>() {
                        @Override
                        public void onSuccess(Task t) {
                            if (t != null)                            {
                                Info.display("Success", "new task added:" + t.getName());
                                mytask = t;
                                Shared.EVENT_BUS.fireEvent(new TaskChangeEvent());   //update UI content
                            }else{
                                Info.display("Error", "failed to add new task " + t.getName());
                            }
                        }
                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", "Communication with server failed.");
                        }
                    });
                } else {
                    //update task info
                    rpc.editTaskByWizard(session, mytask, mypairlist, myconfig, new AsyncCallback<Task>() {
                        @Override
                        public void onSuccess(Task t) {
                            if (t != null)                            {
                                Info.display("Success", "task info changed : " + t.getName());
                                mytask = t;
                                Shared.EVENT_BUS.fireEvent(new TaskChangeEvent());   //update UI content
                            }else{
                                Info.display("Error", "failed to update task info : " + t.getName());
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", "Communication with server failed." + caught.getMessage());
                        }
                    });
                }
            }
        });
        
        //common : cancel
        this.setCancelHandler(new CancelHandler(){
            @Override
            public void onCancel(){

            }
        });        
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
    
    private void addPropertyUIHandlers() {
        //property : next
        this.addNextStepHandler(property.ui, new NextStepHandler() {
            @Override
            public void onNextStep() {

                //to make it easy, communication is carried out at the last step

                //add or update task
                mytask.setOwner(Shared.MY_SESSION.getAccount());
                mytask.setComment(property.commentfield.getText());
                mytask.setName(property.namefield.getText());
            }
        });
        //validator
        this.addInputValidator(property.ui, new InputValidator(){
            @Override
            public boolean onValidate(){
                if  (property.namefield.getValue().isEmpty()){
                    return false;
                }
                return true;
            }
        });
    }
    
    //==============================================================================
    private class UploadSeqUI {

        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = 
                new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        
        FileUploader uploader = new FileUploader();
        TextButton openPaste = new TextButton("Paste Sequences...");
        PasteSequence paste = new PasteSequence();
        
        public UploadSeqUI() {
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            //add widgets to container
            ui.add(new Label("You can upload sequence in FASTA format..."), layout);
            ui.add(uploader, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
            ui.add(new Label("or, you can paste FASTA sequences directly by clicking this button :"), layout);
            ui.add(openPaste, layout);
            
            openPaste.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    paste.show();
                }
            });
        }
    }
    
    private void addUploadSeqUIHandlers(){
        //PasteSeq : next
        this.addNextStepHandler(uploadseq.ui, new NextStepHandler(){
            @Override
            public void onNextStep(){
                
            }
        });
        
        //PasteSeq : back
        this.addPrevStepHandler(uploadseq.ui, new PrevStepHandler(){
            @Override
            public void onPrevStep(){
                
            }
        });
    }
    
    private class PasteSequence extends Window{
        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = 
                new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        FieldLabel seqlabel = new FieldLabel();
        TextArea sequences = new TextArea();
        
        public PasteSequence(){
            this.setHeadingText("Paste Your Sequences");
            this.setWidth(500);
            this.setHeight(400);
            
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            sequences.setAllowBlank(false);
            sequences.setEmptyText("paste sequences in FASTA format here...");
            //add widgets to container
            ui.add(new Label("You can paste sequence in FASTA format in this box"), layout);
            ui.add(sequences, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
            
            this.add(ui);
            TextButton btnOK = new TextButton("OK");
            btnOK.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    mypasteseqs = text2Sequence(sequences.getText());
                    //upload pasted seqs
                    if (mypasteseqs.size() > 0) {
                        UploadInfo upload = new UploadInfo(session, "pasted_" + Shared.nowString(), UploadInfo.Type.SEQLIST);
                        upload.setSeqList(mypasteseqs);
                        rpc.uploadAsResource(upload, new AsyncCallback<Resource>() {
                            @Override
                            public void onSuccess(Resource res) {
                                Info.display("Info", "Pasted sequence saved as " + res.getUUID());
                            }

                            @Override
                            public void onFailure(Throwable caught) {
                                Info.display("Error", "Communication with server failed.");
                            }
                        });
                    }

                    hide();
                }
            });
            this.addButton(btnOK);
            
        }
        
        private ArrayList<Sequence> text2Sequence(String text){
            ArrayList<Sequence> seqlist = new ArrayList<Sequence>();
            HashMap<String, String> seqhash = new HashMap<String, String>();
            //1.kill \r
            text = text.replaceAll("\r", "");
            //2.split lines
            String[] lines = text.split("\\n");
            String id = "";
            //3.match and put seqs
            RegExp regex = RegExp.compile("^>(.+)");
            for (String s : lines){
                MatchResult match = regex.exec(s);
                if (match != null){
                    id = match.getGroup(1);
                }else{
                    if (!id.isEmpty()){
                        seqhash.put(id, seqhash.get(id) + s);
                    }
                }
            }
            //4.convert hash to arraylist
            for (Iterator<String> it = seqhash.keySet().iterator(); it.hasNext(); ){
                String sid = it.next();
                seqlist.add(new Sequence(sid, seqhash.get(sid)));
            }
            return seqlist;
        }
    }
    
    //==============================================================================
    private class PairModeUI{
        //ui layouts
        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        //UI widgets
        Radio bySelector = new Radio();
        Radio byPairList = new Radio();
        Radio byConsensus = new Radio();
        ToggleGroup toggle = new ToggleGroup();
        
        public PairModeUI(){
            toggle.add(bySelector);
            toggle.add(byPairList);
            toggle.add(byConsensus);
            
            bySelector.setBoxLabel("Pair Selector");
            ui.add(bySelector, layout);
            ui.add(new HTML("Manually select the sequence pairs using the Pair Selector."), layout);
            byPairList.setBoxLabel("Pair List");
            ui.add(byPairList, layout);
            ui.add(new HTML("Upload or paste a list of pair. Each line of the list is like this : <br>"
                    + "PairName  SeqID_A  SeqID_B ."), layout);
            byConsensus.setBoxLabel("Sequentially Paired");
            ui.add(byConsensus, layout);
            ui.add(new HTML("Provide 2 files, whose sequneces are sequentially paired. <br>"
                    + "e.g. Suppose you have A1,B1,...,Z1 in file 1, and A2,B2,...,Z2 in file 2, <br>"
                    + "in this mode, the pairs are considered to be (A1,A2),(B1,B2),...,(Z1,Z2)"), layout);
            
            //set the pair selector as default
            bySelector.setValue(true, true);
            //temporary disable the pairlist and consensus method
            byPairList.setEnabled(false);
            byConsensus.setEnabled(false);

        }
                
    }
    
    private void addPairModeUIHandlers(){
        this.addNextStepHandler(pairmode.ui, new NextStepHandler(){
            @Override
            public void onNextStep(){
                if (pairmode.bySelector.getValue()){
                    myconfig.setPairMode(Config.PairMode.Select);
                    jumpTo(pairselect.ui);
                }else if (pairmode.byPairList.getValue()){
                    myconfig.setPairMode(Config.PairMode.List);                    
                }else if (pairmode.byConsensus.getValue()){
                    myconfig.setPairMode(Config.PairMode.Sequential);
                }
                
            }
        });
        this.addPrevStepHandler(pairmode.ui, new PrevStepHandler(){
            @Override
            public void onPrevStep(){
                
            }
        });
    }
    
    //==============================================================================    
    private class PairSelectUI {
        //ui layouts
        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        //Property access
        PairProps props = GWT.create(PairProps.class);
        //list store
        ListStore<Pair> pairstore = new ListStore<Pair>(props.key());
        //UI widgets
        ToolBar toolbar = new ToolBar();
        TextButton add = new TextButton("Add");
        TextButton delete = new TextButton("Delete");
        TextButton edit = new TextButton("Edit");
        //pair list
        ListView<Pair, String> pairlist = new ListView<Pair, String> (pairstore, props.name());
        
        
        public PairSelectUI() {
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            ui.add(new Label("Select gene pairs for calculation"), layout);
            ui.add(toolbar, layout);
            ui.add(pairlist, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
            
            toolbar.add(add);
            toolbar.add(delete);
            toolbar.add(edit);
            //set seqsetlist selection mode
            pairlist.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
            
            //add button handlers
            add.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    final PairSelector selector = new PairSelector();
                    selector.addHideHandler(new HideEvent.HideHandler() {

                        @Override
                        public void onHide(HideEvent event) {   //there's another onHide within the PairSelector, be ware that these two calls is async.
                            //if hide by close button?
                            if (selector.isHideByCloseButton){
                                return;     //do nothing
                            }
                            //add generated seqset to list
                            if (selector.selectedPair.getName().isEmpty()){
                                selector.selectedPair.setName("untitled");
                            }
                            if (pairlist.getStore().findModelWithKey(selector.selectedPair.getKey()) != null){
                                //already exsits in list
                                //auto rename
                                selector.selectedPair.setName(selector.selectedPair.getName() + pairlist.getStore().size());
                            }
                            pairlist.getStore().add(selector.selectedPair);
                        }
                    });
                    selector.show();                           
                }
            });
            edit.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    final PairSelector selector = new PairSelector(pairlist.getSelectionModel().getSelectedItem());
                    final String pairname = pairlist.getSelectionModel().getSelectedItem().getName();
                    selector.addHideHandler(new HideEvent.HideHandler() {

                        @Override
                        public void onHide(HideEvent event) {
                            //if hide by close button?
                            if (selector.isHideByCloseButton){
                                return;     //do nothing
                            }
                            //if name is blank
                            if (selector.selectedPair.getName().isEmpty()){
                                selector.selectedPair.setName("untitled");
                            }
                            //if name changed and duplicate with existing
                            if (!selector.selectedPair.getName().equals(pairname) 
                                    && pairlist.getStore().findModelWithKey(selector.selectedPair.getKey()) != null){
                                //auto rename
                                selector.selectedPair.setName(selector.selectedPair.getName() + pairlist.getStore().size());
                            }                            
                            pairlist.getStore().update(selector.selectedPair);
                        }
                    });
                    selector.show();
                }   
            });
            delete.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    pairlist.getStore().remove(pairlist.getSelectionModel().getSelectedItem());
                }
            });
        }

        
    }
    
    private void addPairSelectUIHandlers(){
        //PairSelect : next
        this.addNextStepHandler(pairselect.ui, new NextStepHandler(){
            @Override
            public void onNextStep(){
                //add pairs in PairSelector to pairlist
                mypairlist.clear();
                mypairlist.addAll(pairselect.pairlist.getStore().getAll());


            }
        });
        
        //PairSelect : back
        this.addPrevStepHandler(pairselect.ui, new PrevStepHandler(){
            @Override
            public void onPrevStep(){
                
            }            
        });
    }
    //NOTE：the popup pairselector was moved to individual class
    
    
    private class PairListUI{
        
    }
    
    private class PairConsensusUI{
        
    }

    //==============================================================================
    private class ParamUI{
        ArrayList<CheckBox> metlist = new ArrayList<CheckBox>();
        
        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        //property access
        ListStore<Config.Gencode> codestore = new ListStore<Config.Gencode>(new ModelKeyProvider<Config.Gencode>(){
           @Override
           public String getKey(Config.Gencode code){
               return Integer.toString(code.ordinal());
           }
        });
        LabelProvider<Config.Gencode> codelp = new LabelProvider<Config.Gencode>(){
            @Override
            public String getLabel(Config.Gencode code){
                //return code.name();
                return Config.codeMap.get(code);
            }
        };
        ValueProvider<Config.Gencode, String> codevp = new ValueProvider<Config.Gencode, String>(){
            @Override
            public String getValue(Config.Gencode code){
                return Config.codeMap.get(code);
            }
            @Override
            public void setValue(Config.Gencode code, String value){
                
            }
            @Override
            public String getPath(){
                return "code";
            }
        };
        
        //data field widgets
        ComboBox<Config.Gencode> codeselect = new ComboBox<Config.Gencode>(codestore, codelp);
        
        
        public ParamUI(){
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            ui.add(new Label("Select parameters for calculation"), layout);
            ui.add(new Label("Genetic Codon Table"), layout);
            ui.add(codeselect, new VerticalLayoutContainer.VerticalLayoutData(0.75, -1, new Margins(10)));
            ui.add(new Label("Select KaKs Method(s)"), layout);
            ui.add(initMethodSelectGrid(), layout);
            
            codeselect.setForceSelection(true);
            codeselect.setTriggerAction(TriggerAction.ALL);
            codeselect.setValue(Config.Gencode.Standard, true, true);
            for (Config.Gencode gencode : Config.Gencode.values()) {
                if ("".equals(Config.codeMap.get(gencode))) {
                    //skip
                } else {
                    codestore.add(gencode);
                }

            }
            
        }
        
        private Grid initMethodSelectGrid(){
            metlist.clear();
            int row = (int)Math.round(Math.sqrt(Config.Method.values().length) + 0.5);
            Grid grid = new Grid(row + 1, row);
            //add the "select all" checkbox"
            final CheckBox checkAll = new CheckBox();
            checkAll.setBoxLabel("Select All");
            checkAll.addValueChangeHandler(new ValueChangeHandler<Boolean>(){
               @Override
               public void onValueChange(ValueChangeEvent<Boolean> event){
                   for (Iterator<CheckBox> it = metlist.iterator(); it.hasNext();){
                       CheckBox c = it.next();
                       c.setValue(checkAll.getValue(), true, true);
                   }                   
               }
            });
            grid.setWidget(0, 0, checkAll);            
            //begin to add other checkboxes to grid            
            for (Config.Method method : Config.Method.values()){
                if (method.compareTo(Config.Method.ALL) == 0){
                    continue;
                }
                CheckBox c = new CheckBox();
                c.setBoxLabel(method.name());
                c.setData("method", method);
                grid.setWidget(method.ordinal()/row + 1, method.ordinal()%row, c);
                //default method
                if (method.equals(Config.Method.MA)){
                    c.setValue(true, true, true);
                }                
                metlist.add(c);
            }
            return grid;
        }
        
    }
    
    private void addParamUIHandlers(){
        //Param : next
        this.addNextStepHandler(param.ui, new NextStepHandler(){
           @Override
           public void onNextStep(){
               //read and save task settings
               //set config owner(db required)
               myconfig.setOwner(session.getAccount());
               //gencode
               myconfig.setKaKsGeneticCode(param.codeselect.getCurrentValue());
               //methods
               myconfig.clearKaKsMethods();
               for (Iterator<CheckBox> it = param.metlist.iterator(); it.hasNext(); ){
                   CheckBox c = it.next();
                   if (c.getValue()){
                       myconfig.addKaKsMethod(Config.Method.valueOf(c.getBoxLabel()));
                   }
               }
                
               //set data for confirm step
               confirm.setData();               
           }
        });
        
        //Param : back        
    }
    
    
    //==============================================================================    
    private class ConfirmUI{
        VerticalLayoutContainer ui = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        VerticalLayoutContainer.VerticalLayoutData layout_indent = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10,30,10,10));
        //property access
        PairProps pairprops = GWT.create(PairProps.class);
        
        //data widgets
        Label lbltaskname = new Label();
        Label lblgencode = new Label();
        Label lblmethod = new Label();
        ListView<Pair, String> pairlist = new ListView<Pair, String>(new ListStore(pairprops.key()), pairprops.name());
        
        public ConfirmUI(){
            ui.setScrollMode(ScrollSupport.ScrollMode.AUTO);
            
            ui.add(new Label("Please confirm your task information. If needed, go back to modify. When you finish, click submit."), layout);
            ui.add(new Label("Task Name"), layout);
            ui.add(lbltaskname, layout_indent);
            ui.add(new Label("Gene Pairs"), layout);
            ui.add(pairlist, layout_indent);
            ui.add(new Label("Genetic Code"), layout);
            ui.add(lblgencode, layout_indent);
            ui.add(new Label("Calculation Method"), layout);
            ui.add(lblmethod, layout_indent);               
            
            lblmethod.getElement().getStyle().setProperty("word-wrap", "normal");
            
        }
        
        public void setData(){
            lbltaskname.setText(mytask.getName());
            pairlist.getStore().clear();
            pairlist.getStore().addAll(mypairlist);
            lblgencode.setText(Config.codeMap.get(myconfig.getKaKsGeneticCode()));
            lblmethod.setText(myconfig.methods2String().replaceAll(",", ", "));
            
        }
    }    
}
