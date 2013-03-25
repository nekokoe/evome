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
public class Workspace extends Composite {
    
    private static WorkspaceUiBinder uiBinder = GWT.create(WorkspaceUiBinder.class);
    
    interface WorkspaceUiBinder extends UiBinder<Widget, Workspace> {
    }
    
    public Workspace() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
