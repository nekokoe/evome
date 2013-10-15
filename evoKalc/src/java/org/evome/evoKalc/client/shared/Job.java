/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

/**
 *  A Job wraps a task to Queue
 * @author nekoko
 */

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;


public class Job implements IsSerializable{
    private int id;
    private Task task;
    private String daemon;
    private Date submit;
    
    public Job(){
        
    }
    
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
    
    public Task getTask(){
        return this.task;
    }
    public void setTask(Task t){
        this.task = t;
    }
    
    public String getDaemon(){
        return this.daemon;
    }
    public void setDaemon(String uuid){
        this.daemon = uuid;
    }
    
    public Date getSubmitTime(){
        return this.submit;
    }
    public void setSubmitTime(Date d){
        this.submit = d;
    }
}
