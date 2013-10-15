/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import java.util.ArrayList;
import java.util.Iterator;
import org.evome.evoKalc.client.shared.*;
import org.evome.evoKalc.client.ui.utils.BalloonTips;

/**
 *
 * @author nekoko
 */
public class PairSelector extends Window {
    //RPCs
    
    //ui
    VerticalLayoutContainer container = new VerticalLayoutContainer();
    VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
    //private data
    public Pair selectedPair;
    //property access
    ResourceProps resprops = GWT.create(ResourceProps.class);
    SequenceProps seqprops = GWT.create(SequenceProps.class);
    //list storage
    ListStore<Resource> filestore = new ListStore<Resource>(resprops.key());
    ListStore<Sequence> leftstore = new ListStore<Sequence>(seqprops.key());
    ListStore<Sequence> rightstore = new ListStore<Sequence>(seqprops.key());
    //ui widgets
    TextField setName = new TextField();
    ComboBox<Resource> filelist = new ComboBox<Resource>(filestore, resprops.label());
    DualListField<Sequence, String> seqpairs = new DualListField<Sequence, String>(leftstore, rightstore, seqprops.name(), new TextCell());
    TextButton btnOK = new TextButton("OK");
    //switch:
    boolean isHideByCloseButton = true;

    public PairSelector() {
        init();
        selectedPair = new Pair();
    }

    public PairSelector(Pair pair) {
        init();
        //to edit pair, directly edit on pair
        selectedPair = pair;
        //add seqset to right list
        rightstore.add(pair.getA());
        rightstore.add(pair.getB());
        //set group name
        setName.setText(pair.getName());
    }

    private void init() {
        //set window appearance
        this.setHeadingText("Select sequences from files and add to pair group");
        this.setModal(true);    //ummmm.....
        this.setWidth(400);
        this.setHeight(500);
        //add widgets to window
//            container.setWidth(400);
//            container.setHeight(500);
        container.add(new FieldLabel(setName, "Pair Name"), layout);
        container.add(new FieldLabel(filelist, "Select File"), layout);
        container.add(new Label("Add sequences to pair group"), layout);
        container.add(seqpairs, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));

        this.setWidget(container);
        this.addButton(btnOK);

        //set filelist
        setFileList();

        //set pair selector
        seqpairs.setEnableDnd(true);
        rightstore.addStoreAddHandler(new StoreAddEvent.StoreAddHandler<Sequence>(){
            @Override
            public void onAdd(StoreAddEvent<Sequence> event){
                //check and limit to 2 seqs
                if (rightstore.size() > 2){
                    for (Iterator<Sequence> it = event.getItems().iterator(); it.hasNext();){
                        Sequence s = it.next();
                        //add back to leftstore
                        leftstore.add(rightstore.findModelWithKey(s.getKey()));
                        rightstore.remove(rightstore.findModelWithKey(s.getKey()));
                    }
                    BalloonTips balloon = new BalloonTips(true, false);
                    balloon.showOnTopOf(seqpairs);
                    balloon.setWidgetToShow(new HTML("Only 2 sequences allowed for one pair"));
                    balloon.showRelativeToWidget(seqpairs.getToView().getParent(), BalloonTips.TipPos.rightTo);
                }
            }
        });
        

        //process when clicking OK
        btnOK.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                //check selected pair number
                if (rightstore.getAll().size() < 2) {
                    Info.display("Error", "You must select 2 or more sequences for a pair group!");
                    return;
                }

                isHideByCloseButton = false;

                selectedPair.setName(setName.getText());
                selectedPair.setA(rightstore.get(0));
                selectedPair.setB(rightstore.get(1));
                
                hide();
            }
        });
          
    }

    private void setFileList() {
        Shared.RPC.userResources(Shared.MY_SESSION, Shared.MY_SESSION.getAccount(), Resource.Type.Sequence, new AsyncCallback<ArrayList<Resource>>() {
            @Override
            public void onSuccess(ArrayList<Resource> reslist) {
                filestore.addAll(reslist);
                if (filestore.size() > 0){                          //only select when list not empty
                    filelist.setValue(filestore.get(0), true);      //try to select the first
                    setSeqList(filestore.get(0));                  //try to select the first
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                Info.display("Error", "Communication with server failed.");
            }
        });
        filelist.addSelectionHandler(new SelectionHandler<Resource>() {
            @Override
            public void onSelection(SelectionEvent<Resource> event) {
                setSeqList(event.getSelectedItem());
            }
        });        
    }

    private void setSeqList(Resource res) {
        leftstore.clear();
        Shared.RPC.parseSeqIDs(Shared.MY_SESSION, res, new AsyncCallback<ArrayList<Sequence>>() {
            @Override
            public void onSuccess(ArrayList<Sequence> idlist) {
                leftstore.addAll(idlist);
                //remove seqset from left list
                //REMEMBER : RPC CALL IS NOT SEQUENTIAL!!!
                for (Iterator<Sequence> it = rightstore.getAll().iterator(); it.hasNext();) {
                    try {
                        leftstore.remove(leftstore.findModelWithKey(it.next().getKey()));        
                    } catch (Exception ex) {
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
