/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 * this wraps a resource on server side
 * client can only access resource(file) by passing Resource instance
 * which contains resID
 * 
 * @author nekoko
 */
public class Resource implements IsSerializable{
    private static int count = 0;
    
    private int id, group, permission;
    private Account owner;
    private String name, comment;
    private String uuid, parent;    //parent is usually a task
    private Date create, modify;
    private Type type = Type.Default;
    
    private String message; //used to return server message when getting with RPC calls    
    //type enum
    public enum Type{
        All,Default,
        Sequence,Pairlist,Config,Result         
    }
    
    
    
    public Resource(){
        count++;
    }
    
    public Resource(String uuid){
        this.uuid = uuid;
    }
    
    public int getId(){
        return this.id;
    }
    public Account getOwner(){
        return this.owner;
    }
    public int getGroup(){
        return this.group;
    }

    public int getPermission(){
        return this.permission;
    }
    public String getName(){
        return this.name;
    }
    public String getUUID(){
        return this.uuid;
    }
    public String getParentUUID(){
        return this.parent;
    }
    public Date getCreateDate(){
        return this.create;
    }
    public Date getModifyDate(){
        return this.modify;
    }
    public Type getType(){
        return this.type;
    }

    public String getComment(){
        return this.comment;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    public String getDefaultName(){
        return "noname" + count;
    }
    
    //sets
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setType(Type type){
        this.type = type;
    }

    public void setOwner(Account owner){
        this.owner = owner;
    }
    public void setGroup(int group){
        this.group = group;
    }

    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    public void setParentUUID(String uuid){
        this.parent = uuid;
    }
    public void setCreateDate(Date create){
        this.create= create;
    }
    public void setModifyDate(Date modify){
        this.modify = modify;
    }
    public void setPermission(int permission){
        this.permission = permission;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getKey(){
        return String.valueOf(this.hashCode());
    }
}
