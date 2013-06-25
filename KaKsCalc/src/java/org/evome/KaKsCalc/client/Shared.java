/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.text.SimpleDateFormat;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author nekoko
 */
public class Shared {
    private static GWTServiceAsync rpc = GWT.create(GWTService.class);
    
    public static GWTServiceAsync getService(){
        return rpc;
    }
    
}
