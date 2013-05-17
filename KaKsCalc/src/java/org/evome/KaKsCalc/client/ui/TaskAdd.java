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
public class TaskAdd extends Composite {
    
    private static TaskAddUiBinder uiBinder = GWT.create(TaskAddUiBinder.class);
    
    interface TaskAddUiBinder extends UiBinder<Widget, TaskAdd> {
    }
    
    public TaskAdd() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
