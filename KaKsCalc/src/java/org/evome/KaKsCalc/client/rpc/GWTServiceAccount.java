/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.evome.KaKsCalc.client.*;
/**
 *
 * @author nekoko
 */
@RemoteServiceRelativePath("gwtserviceaccount")
public interface GWTServiceAccount extends RemoteService{
    //note: non-safety rpc call, for authentic validation is not enforced on every rpc call
    //could be enforced with requiring an authToken with authenticValidation
    //all operations should provide a authToken to take effect, just like
    //authToken = authenticValidation(...);
    //editAccount(account, authToken);
    public Session createSession(Account account);
    public boolean authenticValidation(Account a, Session s, String valkey);
    public int createAccount(Account account);
    public Account getAccount(int account_id);    
    public boolean editAccount(Account account);
    public boolean delAccount(Account account);    
}
