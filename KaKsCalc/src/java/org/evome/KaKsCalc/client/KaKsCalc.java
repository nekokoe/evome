/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;


import org.evome.KaKsCalc.client.ui.*;


import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Main entry point.
 *
 * @author nekoko
 */
public class KaKsCalc implements EntryPoint {

    /**
     * Creates a new instance of MainEntryPoint
     */
    
    //register EventBus, for universal event dispatching
    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
    
    
    //session id is generated when browsing the main page
    //session is associated with user and at the server end
    //if session is still alive at the server end, trying to recover the session data to UI
    private static Session session;
    private static Account account;
    
    public KaKsCalc() {

    }
    
    public static Session getSession(){
        return session;
    }
    public static Account getAccount(){
        return account;
    }
    public static void setSession(Session s){
        session = s;
    }
    public static void setAccount(Account a){
        account = a;
    }

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    @Override
    public void onModuleLoad() {
        //add test session
        session = Session.sampleData();
        account = Account.sampleData();
        
        Workspace myWorkSpace = new Workspace(session);
        RootPanel.get().add(myWorkSpace);
        
                
    }

}
