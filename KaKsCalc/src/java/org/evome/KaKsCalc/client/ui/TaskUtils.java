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
public class TaskUtils extends Composite {
    
    private static TaskUtilsUiBinder uiBinder = GWT.create(TaskUtilsUiBinder.class);
    
    interface TaskUtilsUiBinder extends UiBinder<Widget, TaskUtils> {
    }
    
    public TaskUtils() {
        initWidget(uiBinder.createAndBindUi(this));

    }
}
