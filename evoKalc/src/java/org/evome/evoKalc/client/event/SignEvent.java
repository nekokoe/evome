/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import org.evome.evoKalc.client.shared.Account;


/**
 *    //this event broadcast the TreeViewItem to be updated or removed
 * @author nekoko
 */
public class SignEvent extends GwtEvent<SignEvent.SignEventHandler>{
    public static Type<SignEventHandler> TYPE = new GwtEvent.Type<SignEventHandler>();
    @Override
    public GwtEvent.Type<SignEventHandler> getAssociatedType(){
        return TYPE;
    }
    @Override
    public void dispatch(SignEventHandler handler){
        handler.onSign(this);
    }
    public interface SignEventHandler extends EventHandler{
        void onSign(SignEvent event);
    }
    
    //custom functions
    private Action action;
    private Account account;    //account to sign in
    private String accountKey;
    
    public enum Action{
        SIGN_IN, SIGN_OUT,      //dispatch before calling account RPC
        SIGNED_IN, SIGNED_OUT;  //dispatch after calling account RPC
    }
    //constructor
    public SignEvent(Action action){
        this.action = action;
    }
    
    public SignEvent(Action action, String accountKey) {
        this.action = action;
        this.accountKey = accountKey;
    }
    
    //data
    public Action getAction(){
        return action;
    }    
    public void setAction(Action action){
        this.action = action;
    }
    
    public Account getAccount(){
        return this.account;
    }
    public void setAccount(Account account){
        this.account = account;
    }
    
    public String getAccountKey(){
        return this.accountKey;
    }
    public void setAccountKey(String key){
        this.accountKey = key;
    }
}
