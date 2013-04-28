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

public class Calculation implements IsSerializable{

    private int calc_id, calc_project, calc_owner;
    private String calc_name, calc_comment;
    private Date create, modify;
    private String owner_text;

    public void setId(int id) {
        this.calc_id = id;
    }

    public void setProject(int project) {
        this.calc_project = project;
    }

    public void setOwner(int owner) {
        this.calc_owner = owner;
    }
    
    public void setOwnerText(String text){
        this.owner_text = text;
    }
    
    public void setName(String name) {
        this.calc_name = name;
    }

    public void setComment(String comment) {
        this.calc_comment = comment;
    }
    
    public void setCreateTime(Date date){
        this.create = date;
    }
    public void setModifyTime(Date date){
        this.modify = date;
    }
    

    public int getId() {
        return this.calc_id;
    }

    public int getProject() {
        return this.calc_project;
    }

    public int getOwner() {
        return this.calc_owner;
    }
    
    public String getOwnerText(){
        return this.owner_text;
    }

    public String getName() {
        return this.calc_name;
    }

    public String getComment() {
        return this.calc_comment;
    }
    
    public Date getCreateTime(){
        return this.create;
    }
    public Date getModifyTime(){
        return this.modify;
    }
}
