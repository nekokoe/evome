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
    public void createSession4Account(Account account, AsyncCallback<Session> callback);
}
