/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

/**
 *
 * @author nekoko
 */

import com.google.gwt.user.client.rpc.IsSerializable;


public class Project implements IsSerializable{
    private int id, owner;
    private String name, ownerText, comment;
    private String create, modify;  //store date as string
    
    public Project(String name){//for test purpose
        this.name = name;
    }
    public Project(){
        
    }
    
    public void setId(int id){
        this.id = id;
    }
    public void setOwner(int owner){
        this.owner = owner;
    }
    public void setOwnerText(String owner){
        this.ownerText = owner;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public void setCreateDate(String create){
        this.create = create;
    }
    public void setModifyDate(String modify){
        this.modify = modify;
    }
    
    public int getId(){
        return this.id;
    }
    public int getOwner(){
        return this.owner;
    }
    public String getOwnerText(){
        return this.ownerText;
    }
    public String getName(){
        return this.name;
    }
    public String getComment(){
        return this.comment;
    }
    public String getCreateDate(){
        return this.create;
    }
    public String getModifyDate(){
        return this.modify;
    }
}
