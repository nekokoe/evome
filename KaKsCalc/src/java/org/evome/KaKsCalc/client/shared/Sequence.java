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

    public void setId(String id) {
        this.id = id;
    }

    public void setSequence(String seq) {
        this.seq = seq;
    }

    public String getId() {
        return this.id;
    }

    public String getSequence() {
        return this.seq;
    }
}