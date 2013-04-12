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
    private String name, ownerName, comment;
    
    public void setId(int id){
        this.id = id;
    }
    public void setOwner(int owner){
        this.owner = owner;
    }
    public void setOwnerName(String name){
        this.ownerName = name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    
    public int getId(){
        return this.id;
    }
    public int getOwner(){
        return this.owner;
    }
    public String getOwnerName(){
        return this.ownerName;
    }
    public String getName(){
        return this.name;
    }
    public String getComment(){
        return this.comment;
    }
}
