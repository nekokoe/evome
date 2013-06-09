/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import java.util.ArrayList;
import java.util.List;
import org.evome.KaKsCalc.client.ui.TreeViewItem;

/**
 *
 * @author nekoko
 */
public class TreeSelectChangeEvent extends GwtEvent<TreeSelectChangeEvent.TreeSelectChangeHandler> {

    public static GwtEvent.Type<TreeSelectChangeHandler> TYPE = new GwtEvent.Type<TreeSelectChangeHandler>();

    @Override
    public GwtEvent.Type<TreeSelectChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TreeSelectChangeHandler handler) {
        handler.onTreeSelectChange(this);
    }

    public interface TreeSelectChangeHandler extends EventHandler {
        void onTreeSelectChange(TreeSelectChangeEvent event);
    }
    
    //event custom data and methods
    private List<TreeViewItem> select;
    
    public TreeSelectChangeEvent(){
        
    }
    public TreeSelectChangeEvent(TreeViewItem s){
        setSelection(s);
    }
    
    public TreeSelectChangeEvent(List<TreeViewItem> s){
        setSelection(s);
    }
    
    
    public final void setSelection(List<TreeViewItem> select){
        this.select = select;
    }
    
    public final void setSelection(TreeViewItem s){
        select = new ArrayList<TreeViewItem>();
        select.add(s);
    }
    
    public final List<TreeViewItem> getSelection(){
        return this.select;
    }
}
