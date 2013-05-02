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

    private int calc_id;
    private Account owner;
    private Project project;
    private String calc_name, calc_comment;
    private Date create, modify;

    public void setId(int id) {
        this.calc_id = id;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
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

    public Project getProject() {
        return this.project;
    }

    public Account getOwner() {
        return this.owner;
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
    
    
    //for test purpose
    public static Calculation sampleData(){
        Calculation sample = new Calculation();
        sample.setComment("this is a test calculation");
        sample.setCreateTime(new Date());
        sample.setId(1);
        sample.setModifyTime(new Date());
        sample.setName("test calculation");
        sample.setOwner(Account.sampleData());
        sample.setProject(Project.sampleData());
        return sample;
    }
    
}
