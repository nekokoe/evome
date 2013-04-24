/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.user.client.rpc.IsSerializable;
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
    private String accountKey;
    private Date create, lastActive;
    private int serverTime;

    public int getSessionID() {
        return this.sessionID;
    }

    public int getUserID() {
        return this.userID;
    }

    public String getAccountKey() {
        return this.accountKey;
    }

    public Date getCreateTime() {
        return this.create;
    }

    public Date getLastActiveTime() {
        return this.lastActive;
    }

    public void setSessionID(int id) {
        this.sessionID = id;
    }

    public void setUserID(int id) {
        this.userID = id;
    }

    public void setAccountKey(String key) {
        this.accountKey = key;
    }

    public void setCreateTime(Date create) {
        this.create = create;
    }

    public void setLastActiveTime(Date active) {
        this.lastActive = active;
    }
    
    
    public static String toAccountKey(Account account){
        //generate AccountKey from account
        String userName = account.getEmail();
        
    }
}
