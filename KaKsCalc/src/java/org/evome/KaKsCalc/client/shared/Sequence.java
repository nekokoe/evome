/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.shared;

/**
 *
 * @author nekoko
 */
public class Sequence {

    private String id, seq;
    private String parent;  //store where the sequence comes from, in general parent = ResourceUUID

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
}