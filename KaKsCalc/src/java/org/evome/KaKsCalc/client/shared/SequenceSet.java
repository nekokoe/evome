/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.shared;

/**
 *
 * @author nekoko
 * 
 * SequenceSet is a set of Sequences
 * In most cases, SequenceSet is used to generate Pairs
 */

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;


public class SequenceSet extends ArrayList implements IsSerializable{
    private static int count = 0;
    
    private String name = "";
    private String parent = "";
    
    public SequenceSet(){
        count++;
    }

    public String getName(){
        return this.name;
    }
    
    public String getParent(){
        return this.parent;
    }
    
    public String getDefaultName(){
        return "noname" + count;
    }    
    
    //for property access
    public String getKey(){
        return (count + this.name);
    }
    
    public void setName(String name){
        this.name = name;
    }
    public void setParent(String uuid){
        this.parent = uuid;
    }
    

    
}
