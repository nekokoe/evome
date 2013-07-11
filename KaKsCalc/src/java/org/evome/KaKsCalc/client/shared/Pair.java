/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.shared;

/**
 *
 * @author nekoko
 * 
 * A Pair could be :
 * 1.two sequences anonymous
 * 2.two sequences with name and source
 */

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import org.evome.KaKsCalc.client.Resource;

public class Pair implements IsSerializable{

    private String name, parent;
    private Sequence a, b;

    public Pair(Sequence a, Sequence b){
        this.a = a;
        this.b = b;
    }
    
    public Pair(String name, String parent, Sequence a, Sequence b){
        this.name = name;
        this.parent = parent;
        this.a = a;
        this.b = b;        
    }
    
    public void setParent(String uuid){
        this.parent = uuid;        
    } 
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setA(Sequence a) {
        this.a = a;
    }

    public void setB(Sequence b) {
        this.b = b;
    }

    public String getName(){
        return this.name;
    }
    public String getParent(){
        return this.parent;
    }
    
    public Sequence getA() {
        return this.a;
    }

    public Sequence getB() {
        return this.b;
    }
    
    //for property access
    public String getKey(){
        return this.parent + this.name;
    }
    
    public interface PairProperties extends PropertyAccess<Resource> {

        ModelKeyProvider<Pair> key();

        ValueProvider<Pair, String> name();

        @Editor.Path("name")
        LabelProvider<Pair> label();
    }
 
}
