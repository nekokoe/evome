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
import java.util.Date;


public class Project implements IsSerializable{
    private int id;
    private Account owner;
    private String name, comment;
    private String uuid, parent;
    private Date create, modify;
    
    public void setId(int id){
        this.id = id;
    }
    public void setOwner(Account owner){
        this.owner = owner;
    }
//    public void setOwnerText(String owner){
//        this.ownerText = owner;
//    }
    public void setName(String name){
        this.name = name;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public void setCreateDate(Date create){
        this.create = create;
    }
    public void setModifyDate(Date modify){
        this.modify = modify;
    }
    public void setUUID(String uuid){
        this.uuid = uuid;
    }
    public void setParentUUID(String uuid){
        this.parent = uuid;
    }
    
    public int getId(){
        return this.id;
    }
    public Account getOwner(){
        return this.owner;
    }
//    public String getOwnerText(){
//        return this.ownerText;
//    }
    public String getName(){
        return this.name;
    }
    public String getComment(){
        return this.comment;
    }
    public Date getCreateDate(){
        return this.create;
    }
    public Date getModifyDate(){
        return this.modify;
    }
    public String getUUID(){
        return this.uuid;
    }
    public String getParentUUID(){
        return this.parent;
    }
    //code below for test purpose
    public static Project sampleData(){
        Project sample = new Project();
        sample.setComment("this is a test project");
        sample.setCreateDate(new Date());
        sample.setId(1);
        sample.setModifyDate(new Date());
        sample.setOwner(Account.sampleData());
        return sample;
    }
}
