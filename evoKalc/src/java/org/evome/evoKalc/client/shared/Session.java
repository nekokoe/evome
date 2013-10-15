/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

/**
 *
 * @author nekoko
 */
public class Session implements IsSerializable {
    /* user certification, user/password pair was actually not pass directly to the server
     * accountKey was calculated using combination of use/password/serverTime on both client and server side
     * if they coordinate, login is successful, and session is bind to the account on server side 
     * session is always active unless sessionID disappeared, e.g. closing browser
     */

    private int sessionID;
    private String session_uuid;
    private Date create, lastActive;
    private String authKey;     //for server, check if session valid
    private Account account;
    
    private boolean isSignedIn = false;
    
    public int getSessionID() {
        return this.sessionID;
    }


    public Date getCreateTime() {
        return this.create;
    }

    public Date getLastActiveTime() {
        return this.lastActive;
    }
    
    public String getUUID(){
        return this.session_uuid;
    }

    public Account getAccount(){
        return this.account;
    }
    
    public void setSessionID(int id) {
        this.sessionID = id;
    }

    public void setCreateTime(Date create) {
        this.create = create;
    }

    public void setLastActiveTime(Date active) {
        this.lastActive = active;
    }
    
    public void setUUID(String uuid){
        this.session_uuid = uuid;
    }
    
    public void setAccount(Account account){
        this.account = account;
    }

    public String getAuthKey(){
        return this.authKey;
    }
    public void setAuthKey(String authkey){
        this.authKey = authkey;
    }
    
    public boolean isSignedIn(){
        return this.isSignedIn;
    }
    public void signedIn(){
        this.isSignedIn = true;
    }
    public void signedOut(){
        this.isSignedIn = false;
    }
    
    
    //code for test purpose
    public static Session sampleData(){
        Session s = new Session();
        s.setSessionID(1);
        s.setUUID("");
        s.setAccount(Account.sampleData());
        s.setCreateTime(new Date());
        s.setLastActiveTime(new Date());
        return s;
    }
}
