/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.event;

/**
 *
 * @author nekoko
 */

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ViewChangeEvent extends GwtEvent<ViewChangeEvent.ContentChangeHandler>{
    public static GwtEvent.Type<ViewChangeEvent.ContentChangeHandler> TYPE = new GwtEvent.Type<ViewChangeEvent.ContentChangeHandler>();
    @Override
    public GwtEvent.Type<ViewChangeEvent.ContentChangeHandler> getAssociatedType(){
        return TYPE;
    }
    @Override
    public void dispatch(ViewChangeEvent.ContentChangeHandler handler){
        handler.onContentChange(this);
    }
    public interface ContentChangeHandler extends EventHandler{
        void onContentChange(ViewChangeEvent event);
    }
    
    private Widget widget;
    
    public ViewChangeEvent(Widget w){
        widget = w;
    }
    
    public Widget getWidget(){
        return widget;
    }
    public void setWidget(Widget w){
        widget = w;        
    }
}
