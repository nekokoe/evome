/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author nekoko
 */
public class TaskWizard extends Composite {
    
    private static TaskWizardUiBinder uiBinder = GWT.create(TaskWizardUiBinder.class);
    
    interface TaskWizardUiBinder extends UiBinder<Widget, TaskWizard> {
    }
    
    public TaskWizard() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
