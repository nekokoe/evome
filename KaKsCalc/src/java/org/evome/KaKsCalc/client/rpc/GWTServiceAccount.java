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
    public Session createSession4Account(Account account);
}
