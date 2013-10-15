/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 *
 * @author nekoko
 */
public class UploadInfo implements IsSerializable {

    private String path, name;
    private Session session;
    private Type type;
    private String text;
    
    private ArrayList<Sequence> seqlist;

    public enum Type{
        TEXT,FILE,SEQLIST;
    }
    
    public UploadInfo(){

    }
    
    public UploadInfo(Session s, String name, Type type){
        this.session = s;
        this.name = name;
        this.type = type;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public String getText(){
        return this.text;
    }
    
    public String getPath(){
        return this.path;
    }
    public void setPath(String path){
        this.path = path;
    }
    
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    public Session getSession(){
        return this.session;
    }
    public void setSession(Session s){
        this.session = s;
    }
    
    public Type getType(){
        return this.type;
    }
    public void setType(Type t){
        this.type = t;
    }
    
    public ArrayList<Sequence> getSeqList(){
        return this.seqlist;
    }
    public void setSeqList(ArrayList<Sequence> list){
        this.seqlist = list;
    }
}
