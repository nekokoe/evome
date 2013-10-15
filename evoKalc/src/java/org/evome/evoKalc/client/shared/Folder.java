/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author nekoko
 */
public class Folder implements IsSerializable{
    private String name;
    private Object classify;    //don't know what classify is need, force type conversion when needed
    
    public Folder(){
        
    }
    
    public Folder(String name){
        this.name = name;
    }
    
    public Folder(String name, Object classify){
        //with classify appended
        this.name = name;
        this.classify = classify;
    }
    
    
    @Override
    public String toString(){
        return this.name;
    }    
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setClassify(Object c){
        classify = c;
    }
    public Object getClassify(){
        return classify;
    }
}
