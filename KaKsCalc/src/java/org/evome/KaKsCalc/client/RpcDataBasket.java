/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

/**
 *
 * @author nekoko
 */
public class RpcDataBasket<E> {
    //data container used for passing back RPC return value
    private E data;

    public void set(E set) {
        data = set;
    }

    public E get() {
        return data;
    }
}
