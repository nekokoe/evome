/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client;

import com.google.gwt.user.client.Cookies;
import java.util.Date;
import org.evome.evoKalc.client.shared.Session;

/**
 *
 * @author nekoko
 */
public class CookieBox {
    private String cookieID = "bb304f84d6fc23b949940183beb4e73c";   //md5sum of "org.evome.evoKalc"
    
    private long cookieExpire = 1000 * 60 * 60 * 24 * 7; //cookie will expire in 7 days
    
    public CookieBox(){
        
    }
    
    public String getSeesionID() {
        String sid = Cookies.getCookie(cookieID);
        return (sid == null) ? "" : sid;
    }
    
    public String getAccountKey(Session s) {
        String accountkey = Cookies.getCookie(s.getUUID());
        return (accountkey == null) ? "" : accountkey;
    }

    public void setSessionCookie(Session s){
        Cookies.setCookie(cookieID, s.getUUID(), new Date(System.currentTimeMillis() + cookieExpire));
    }
    
    public void setAccountKey(Session s, String accountKey){
        Cookies.setCookie(s.getUUID(), accountKey, new Date(System.currentTimeMillis() + cookieExpire));
    }
    
    public void discardCookie(Session s){
        Cookies.removeCookie(Cookies.getCookie(s.getUUID()));
        //Cookies.removeCookie(cookieID);
    }
}
