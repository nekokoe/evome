/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.evome.KaKsCalc.client.*;

/**
 *
 * @author nekoko
 */
public interface GWTServiceAccountAsync {
    public void createSession(Account account, AsyncCallback<Session> callback);
    public void authenticValidation(Account a, Session s, String valkey, AsyncCallback<Boolean> callback);
    public void createAccount(Account account, AsyncCallback<Integer> callback);
    public void getAccount(int account_id, AsyncCallback<Account> callback);    
    public void editAccount(Account account, AsyncCallback<Boolean> callback);
    public void delAccount(Account account, AsyncCallback<Boolean> callback);  
}
