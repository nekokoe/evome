/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Main entry point.
 *
 * @author nekoko
 */
public class MainEntryPoint implements EntryPoint {

    /**
     * Creates a new instance of MainEntryPoint
     */
    
    private GWTServiceAsync rpc = GWTServiceUsageExample.getService();
    private GWTServiceUsageExample example = new GWTServiceUsageExample();
    
    public MainEntryPoint() {
    }

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    public void onModuleLoad() {
        final Label label = new Label("Hello, GWT!!!");
        final Button button = new Button("Click me!");
        
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                label.setVisible(!label.isVisible());
            }
        });
        
        RootPanel.get().add(button);
        RootPanel.get().add(label);
        
        RootPanel.get().add(example);
    }
}
