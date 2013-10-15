/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

/**
 *
 * @author nekoko
 * 
 * A Pair could be :
 * 1.two sequences anonymous
 * 2.two sequences with name and source
 */

import com.google.gwt.user.client.rpc.IsSerializable;

public class Pair implements IsSerializable{

    private String name = "";
    private String group = "default";
    private Sequence a, b;

    public Pair(){
        
    }
    
    public Pair(Sequence a, Sequence b){
        this.a = a;
        this.b = b;
    }
    
    public Pair(String name, Sequence a, Sequence b){
        this.name = name;
        this.a = a;
        this.b = b;        
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setA(Sequence a) {
        this.a = a;
    }

    public void setB(Sequence b) {
        this.b = b;
    }

    public String getName(){
        return this.name;
    }
    
    
    public Sequence getA() {
        return this.a;
    }

    public Sequence getB() {
        return this.b;
    }
    
    public String getGroup(){
        return this.group;
    }
    public void setGroup(String group){
        this.group = group;
    }
    
    //for property access
    public String getKey(){
        //NOTE: pair is not allowed for name duplication
        //so , the key is generated from group and name, and must be checked when adding to datalist
        return this.group + "_" + this.name;
        //return String.valueOf(this.hashCode());
    }
    
}
