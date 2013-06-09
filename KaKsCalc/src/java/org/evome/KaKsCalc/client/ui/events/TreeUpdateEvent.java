/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import org.evome.KaKsCalc.client.ui.TreeViewItem;

/**
 *    //this event broadcat the TreeViewItem to be updated or removed
 * @author nekoko
 */
public class TreeUpdateEvent extends GwtEvent<TreeUpdateEvent.TreeUpdateEventHandler>{
    public static Type<TreeUpdateEventHandler> TYPE = new GwtEvent.Type<TreeUpdateEventHandler>();
    @Override
    public GwtEvent.Type<TreeUpdateEventHandler> getAssociatedType(){
        return TYPE;
    }
    @Override
    public void dispatch(TreeUpdateEventHandler handler){
        handler.onUpdate(this);
    }
    public interface TreeUpdateEventHandler extends EventHandler{
        void onUpdate(TreeUpdateEvent event);
    }
    
    //custom functions
    private TreeViewItem tvi, parent;   //parent is used only for ADD action
    private Action action;
    public enum Action{
        SELECT,RELOAD,ADD,UPDATE,DELETE
    }
    //constructor
    public TreeUpdateEvent(){
        
    }    
    public TreeUpdateEvent(TreeViewItem node, TreeUpdateEvent.Action action){
        this.tvi = node;
        this.action = action;
    }
    public TreeUpdateEvent(TreeViewItem parent, TreeViewItem node){
        this.parent = parent;
        this.tvi = node;
        this.action = Action.ADD;
    }
    
    //gets
    public TreeViewItem getTreeViewItem(){
        return this.tvi;
    }
    public Action getAction(){
        return this.action;
    }
    public TreeViewItem getParentTVI(){
        return this.parent;
    }
    //sets
    public void setTreeViewItem(TreeViewItem tvi){
        this.tvi = tvi;
    }
    public void setAction(Action action){
        this.action = action;
    }
    public void setParentTVI(TreeViewItem tvi){
        this.parent = tvi;
    }
}
