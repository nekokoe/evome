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
    
    public class Sequence{
        private String id, seq;
        public void setId(String id){
            this.id = id;
        }
        public void setSequence(String seq){
            this.seq = seq;
        }
        public String getId(){
            return this.id;
        }
        public String getSequence(){
            return this.seq;
        }
    }
    
    public class DNAPair{
        private Sequence a, b;
        public void setA(Sequence a){
            this.a = a;
        }
        public void setB(Sequence b){
            this.b = b;
        }
        public Sequence getA(){
            return this.a;
        }
        public Sequence getB(){
            return this.b;
        }
    }
}
