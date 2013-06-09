/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

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
    private int id, group, permission;
    private Task task;
    private Account owner;
    private String name, uuid, comment;
    private Date create, modify;
    private ResType type;
    //type enum
    public enum ResType{
        REGULAR,BINARY,URL,TEMP,
        CONFIG,XML,JSON,
        DNA,RNA,PROTEIN,MUSCLE,AXT,KAKS,GRAPH
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
    public Task getTask(){
        return this.task;
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
    public Date getCreateDate(){
        return this.create;
    }
    public Date getModifyDate(){
        return this.modify;
    }
    public ResType getType(){
        return this.type;
    }
    public String getComment(){
        return this.comment;
    }
    
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setType(ResType type){
        this.type = type;
    }
    public void setOwner(Account owner){
        this.owner = owner;
    }
    public void setGroup(int group){
        this.group = group;
    }
    public void setTask(Task task){
        this.task = task;
    }
    public void setUUID(String uuid){
        this.uuid = uuid;
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
}
