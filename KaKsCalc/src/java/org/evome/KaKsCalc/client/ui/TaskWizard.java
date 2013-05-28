/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.google.gwt.uibinder.client.UiField;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import com.sencha.gxt.widget.core.client.button.TextButton;

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
    
    private static TaskWizardUiBinder uiBinder = GWT.create(TaskWizardUiBinder.class);
    
    interface TaskWizardUiBinder extends UiBinder<Widget, TaskWizard> {
    }
    
    public TaskWizard() {
        initWidget(uiBinder.createAndBindUi(this));
        //init step 1
        
    }
    
    @UiField
    HorizontalLayoutContainer container;
    @UiField
    ContentPanel pnlUpload, pnlSelectPair, pnlSetting, pnlConfirm;
    @UiField
    MultiUploader uploader;
    @UiField
    TextButton btnNextStep1, btnNextStep2, btnNextStep3, btnBackStep2, btnBackStep3, btnBackStep4, btnSubmitStep4;
    
}
