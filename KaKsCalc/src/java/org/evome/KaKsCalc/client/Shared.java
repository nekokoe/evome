/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.text.SimpleDateFormat;

/**
 *
 * @author nekoko
 */
public class Shared {
    private static GWTServiceAsync rpc = GWT.create(GWTService.class);
    
    public static GWTServiceAsync getService(){
        return rpc;
    }
        
    public String randomUUID(){
        final String[] strs = new String[0];
        rpc.randomUUID(new AsyncCallback<String>(){
            @Override
            public void onFailure(Throwable caught) {//failed 
            }
            @Override
            public void onSuccess(String uuid) {
                strs[0] = uuid;
            }
        });
        return strs[0];
    }


}
