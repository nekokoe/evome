/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server.rpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.evome.KaKsCalc.client.*;
import org.evome.KaKsCalc.client.rpc.GWTServiceAccount;
import org.evome.KaKsCalc.server.SessionManager;

/**
 *
 * @author nekoko
 */
public class GWTServiceAccountImpl extends RemoteServiceServlet implements GWTServiceAccount{
    @Override
    public Session createSession(Account account){
        return SessionManager.createSession(account);
    }
    @Override
    public boolean authenticValidation(Account a, Session s, String valkey){
        
    }
    public int createAccount(Account account);
    public Account getAccount(int account_id);    
    public boolean editAccount(Account account);
    public boolean delAccount(Account account); 
}
