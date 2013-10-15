/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.event;

/**
 *
 * @author nekoko
 */

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class TaskChangeEvent extends GwtEvent<TaskChangeEvent.UIRefreshHandler> {

    public static GwtEvent.Type<TaskChangeEvent.UIRefreshHandler> TYPE = new GwtEvent.Type<TaskChangeEvent.UIRefreshHandler>();

    @Override
    public GwtEvent.Type<TaskChangeEvent.UIRefreshHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public void dispatch(TaskChangeEvent.UIRefreshHandler handler) {
        handler.onRefresh(this);
    }

    public interface UIRefreshHandler extends EventHandler {

        void onRefresh(TaskChangeEvent event);
    }
}
