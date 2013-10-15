/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;


/**
 *
 * @author nekoko
 */

import com.google.gwt.user.client.rpc.IsSerializable;



public class Sequence implements IsSerializable{

    private String id, seq;
    private String parent;  //store where the sequence comes from, in general parent = ResourceUUID

    public Sequence(){
        
    }
    
    public Sequence(String id, String seq){
        this.id = id;
        this.seq = seq;
    }
    
    public Sequence(String id, String seq, String parent){
        this.id = id;
        this.seq = seq;
        this.parent = parent;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void setSequence(String seq) {
        this.seq = seq;
    }
    
    public void setParent(String parent){
        this.parent = parent;
    }

    public String getId() {
        return this.id;
    }

    public String getSequence() {
        return this.seq;
    }
    
    public String getParent(){
        return this.parent;
    }
    
    //for ModelKeyProvider
    public String getKey(){
        return (this.parent + this.id);
    }

    //quick format
    public String toFasta(){
        return ">" + this.getId() + "\n" + this.getSequence() + "\n";
    }
    
}