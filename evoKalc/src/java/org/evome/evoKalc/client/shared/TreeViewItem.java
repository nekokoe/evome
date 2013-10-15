/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

/**
 *
 * @author nekoko
 */
public class TreeViewItem<E> {
    private E item;
    
    public TreeViewItem(){
        
    }
    
    public TreeViewItem(E item){
        this.item = item;
    }
    
    public String getKey(){
        return String.valueOf(this.item.hashCode());
    }
    
    public String getName(){
        return this.item.toString();
    }
    
    public E getItem(){
        return item;
    }
    
    public void setItem(E item){
        this.item = item;
    }

}
