/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
        final RpcDataBasket<String> rdb = new RpcDataBasket<String>();
        rpc.randomUUID(new AsyncCallback<String>(){
            @Override
            public void onFailure(Throwable caught) {//failed 
            }
            @Override
            public void onSuccess(String uuid) {
                rdb.set(uuid);
            }
        });
        return rdb.get();
    }
    

    //data container used for passing RPC return value
    private class RpcDataBasket<E>{
        private E data;    
        public void set(E set){
            data = set;
        }
        public E get(){
            return data;
        }
    }
}
