/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.SingleUploader;
import org.evome.KaKsCalc.client.widget.PortletWizard;
import org.evome.KaKsCalc.client.Calculation;
import org.evome.KaKsCalc.client.GWTServiceAsync;
import org.evome.KaKsCalc.client.Shared;

/**
 *
 * @author nekoko
 */
public class TaskWizard extends PortletWizard{

    private static GWTServiceAsync rpc = Shared.getService();
    
    private Calculation mycalc;
    
    
    public TaskWizard(){
        super();    //no need to do this?
    }
    
    public TaskWizard(Calculation calc){
        this();
        this.mycalc = calc;
        
        this.addStepWidget(initPropertyUI(), "Step 1 : Set Properties for New Task");
        this.addStepWidget(initPasteSeqUI(), "Step 2 : Paste Sequences in FASTA format");
        this.addStepWidget(initPairSelectUI(), "Step 3 : Select gene pairs for Calculation");
    }
    
    
    
    
    
    
    private VerticalLayoutContainer initPropertyUI(){
        //set vertical layout
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);
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
        vertical.add(new Label("Fill out the fields to create a new task"), layout);
        vertical.add(namelabel, layout);
        vertical.add(commentlabel, new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(10)));
        return vertical;
    }    
    
    private VerticalLayoutContainer initPasteSeqUI(){
        //add vertical layout
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10));
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);

        
        
        //add widgets to container
        vertical.add(new Label("You can paste sequence in FASTA format in this box"), layout);
        vertical.add(new Label("or, you could upload FASTA file(s) with the Sequence File Uploader nearby... "), layout);
        //vertical.add(uploader, layout);
        //vertical.add(fileListView, layout);
        return vertical;
    }
    
    private VerticalLayoutContainer initPairSelectUI(){
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);

        //
        vertical.add(new Label("Select gene pairs for calculation"), layout);

        return vertical;
    }    
    
    private VerticalLayoutContainer initParamUI(){
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);

        //
        vertical.add(new Label("Select parameters for calculation"), layout);

        return vertical;
    }
    
    private VerticalLayoutContainer initConfirmPanel(){
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10));
        vertical.setScrollMode(ScrollSupport.ScrollMode.AUTO);

        //
        vertical.add(new Label("Confirm your input and submit the task "), layout);

        return vertical;
    }
    
}
