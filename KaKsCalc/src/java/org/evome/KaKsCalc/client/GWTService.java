/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author nekoko
 */
@RemoteServiceRelativePath("gwtservice")
public interface GWTService extends RemoteService {

    public long getServerTime(); //server time in second from 1970-1-1
    
}
