/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import org.evome.KaKsCalc.client.rpc.GWTServiceDatabase;
import org.evome.KaKsCalc.client.rpc.GWTServiceAccount;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author nekoko
 */
@RemoteServiceRelativePath("gwtservice")
public interface GWTService extends RemoteService {

    public String myMethod(String s);
}
