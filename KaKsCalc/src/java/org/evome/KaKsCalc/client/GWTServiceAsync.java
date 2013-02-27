/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author nekoko
 */
public interface GWTServiceAsync {

    public void myMethod(String s, AsyncCallback<String> callback);
}
