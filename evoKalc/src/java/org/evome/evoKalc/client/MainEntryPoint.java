/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.DOM;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.info.Info;

import org.evome.evoKalc.client.ui.LoginPage;
import org.evome.evoKalc.client.ui.Workspace;
import org.evome.evoKalc.client.ui.resources.CommonResources;
import org.evome.evoKalc.client.shared.Shared;
import org.evome.evoKalc.client.shared.Session;
import org.evome.evoKalc.client.event.*;



/**
 * Main entry point.
 *
 * @author nekoko
 */
public class MainEntryPoint implements EntryPoint {
    private static final int AUTO_REFRESH_INTERVAL = 5000;   //5s = 5000ms
    
    
    public static final CommonResources res = GWT.create(CommonResources.class);
    CookieBox cookie = new CookieBox();
    //Timer autoRefresh;
    
    /**
     * Creates a new instance of MainEntryPoint
     */
    public MainEntryPoint() {

    }

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry-point
     */
    @Override
    public void onModuleLoad() {
        //register auto refreshing timer
//        autoRefresh = new Timer() {
//            @Override
//            public void run() {
//                Shared.EVENT_BUS.fireEvent(new UIRefreshEvent());
//            }
//        };
//        autoRefresh.scheduleRepeating(AUTO_REFRESH_INTERVAL);
        
        
        //read cookies to recover previous session
        if (cookie.getSeesionID().isEmpty()) {
            //create new session
            newSession();
        }else{
            //try to rebind the old session
            Shared.RPC.getSession(cookie.getSeesionID(), new AsyncCallback<Session>() {
                @Override
                public void onSuccess(Session s) {
                    if (s == null){
                        //the session is absent on server
                        Info.display("Expired", "Your session is expired, please sign in again");
                        newSession();
                    }else{
                        Shared.MY_SESSION = s;                        
                        //try to auto sign in using saved accountKey
                        if (cookie.getAccountKey(s).isEmpty() || s.getAccount() == null){
                            //not to keep signed in / or not yet signed in
                            //do nothing, wait for signing in
                        }else{
                            //try to sign in
                            Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGN_IN));    //didn't set the account key because setting it result in overwrite the cookies
                            Shared.RPC.signInAndBindSession(s.getAccount(), s, cookie.getAccountKey(s), new AsyncCallback<Session>() {
                                @Override
                                public void onSuccess(Session s) {
                                    if (s != null) {
                                        //login successful
                                        Shared.MY_SESSION = s;
                                        //dispatch sign in event
                                        Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGNED_IN));
                                    } else {
                                        Info.display("Incorrect", "Please check your password and try again");
                                    }
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    Info.display("Error", caught.getMessage());
                                }
                            });                           
                        }
                    }
                }
                @Override
                public void onFailure(Throwable caught) {
                    Info.display("Error", caught.getMessage());
                }
            });
        }
        


        
        
        res.common_css().ensureInjected();        
        Widget loginpage = new LoginPage();
        RootPanel.get().add(loginpage);

        //add event handler for SignEvent event
        Shared.EVENT_BUS.addHandler(SignEvent.TYPE, new SignEvent.SignEventHandler(){
            @Override
            public void onSign(SignEvent event){
                //process sign_in
                //process signed_in
                if (event.getAction().equals(SignEvent.Action.SIGNED_IN)) {
                    //write cookies if keep signed in
                    if (event.getAccountKey() != null && !event.getAccountKey().isEmpty()) {
                        cookie.setAccountKey(Shared.MY_SESSION, event.getAccountKey());
                    }
                    //already signed in, load workspace
                    RootPanel.get().clear();
                    Widget workspace = new Workspace().asWidget();
                    RootPanel.get().add(workspace);                    
                    Info.display("Welcome", "Signed in as " + Shared.MY_SESSION.getAccount().getFullName());
                }
                //process sign_out
                if (event.getAction().equals(SignEvent.Action.SIGN_OUT)){
                    String accountKey = cookie.getAccountKey(Shared.MY_SESSION);
                    Shared.RPC.signOutAndDetachSession(Shared.MY_SESSION, accountKey, new AsyncCallback<Session>() {
                        @Override
                        public void onSuccess(Session s) {
                            if (s != null) {
                                Shared.MY_SESSION = s;
                                //sign out seems OK on server side, dispatch SIGNED_OUT event
                                Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGNED_OUT));     //WARN: don't dispatch SIGN_OUT avoiding deadlock call
                            } else {
                                Info.display("Error", "Account is already signed out or database error");
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", caught.getMessage());
                        }
                    });
                }
                //process signed_out
                if (event.getAction().equals(SignEvent.Action.SIGNED_OUT)){
                    //delete cookies
                    cookie.discardCookie(Shared.MY_SESSION);                    
                    //return UI to welcome status
                    RootPanel.get().clear();
                    RootPanel.get().add(new LoginPage());                    
                    Info.display("See you", "You are now signed out!");
                }
            }
        });
        
        
    }
    
    private void newSession() {
        Shared.RPC.createSession(new AsyncCallback<Session>() {
            @Override
            public void onSuccess(Session s) {
                Shared.MY_SESSION = s;
                cookie.setSessionCookie(s);
            }

            @Override
            public void onFailure(Throwable caught) {
                Info.display("Error", caught.getMessage());
            }
        });
    }
}
