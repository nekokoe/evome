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

import org.evome.KaKsCalc.client.ui.*;




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
    
    //session id is generated when browsing the main page
    //session is associated with user and at the server end
    //if session is still alive at the server end, trying to recover thde session data to UI
    private String sessionID;
    
    private GWTServiceAsync rpc = GWTServiceUsageExample.getService();
    
    public MainEntryPoint() {
    }

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    @Override
    public void onModuleLoad() {
        //RootPanel.get().add(example);
        //Login loginpage = new Login();
        //RootPanel.get().add(new Login());
        RootPanel.get().add(new Workspace());
        
    }
}
