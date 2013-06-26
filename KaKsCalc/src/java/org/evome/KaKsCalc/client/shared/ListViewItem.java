/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.shared;

/**
 *
 * @author nekoko
 */
public class ListViewItem {
    private String uuid;
    private String value;
    private Type type;
    
    public enum Type{
        TEXT,RES,SEQ
    }
            
    public ListViewItem(Type type, String uuid, String value){
        this.type = type;
        this.uuid = uuid;
        this.value = value;
    }
    
    public String getUUID(){
        return this.uuid;
    }
    public String getValue(){
        return this.value;
    }
    public Type getType(){
        return this.type;
    }
    
    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    public void setValue(String value){
        this.value = value;
    }
    public void setType(Type type){
        this.type = type;
    }
}
