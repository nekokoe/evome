/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

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

    private int sessionID, userID;
    private String uuid;
    private Date create, lastActive;
    private String authKey;     //for server, check if session valid
    private Account account;
    
    
    public int getSessionID() {
        return this.sessionID;
    }

    public int getUserID() {
        return this.userID;
    }

    public Date getCreateTime() {
        return this.create;
    }

    public Date getLastActiveTime() {
        return this.lastActive;
    }
    
    public String getUUID(){
        return this.uuid;
    }

    public Account getAccount(){
        return this.account;
    }
    
    public void setSessionID(int id) {
        this.sessionID = id;
    }

    public void setUserID(int id) {
        this.userID = id;
    }

    public void setCreateTime(Date create) {
        this.create = create;
    }

    public void setLastActiveTime(Date active) {
        this.lastActive = active;
    }
    
    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    
    public void setAccount(Account account){
        this.account = account;
    }
    
    
    //code for test purpose
    public static Session sampleData(){
        Session s = new Session();
        s.setSessionID(1);
        s.setUserID(1);
        s.setUUID("");
        s.setAccount(Account.sampleData());
        s.setCreateTime(new Date());
        s.setLastActiveTime(new Date());
        return s;
    }
}
