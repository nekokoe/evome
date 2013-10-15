/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.shared;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import org.evome.evoKalc.client.GWTService;
import org.evome.evoKalc.client.GWTServiceAsync;
import org.evome.evoKalc.client.ui.utils.BalloonTips;

/**
 * quick reference for shared components
 * @author nekoko
 */
public class Shared {
    //register EventBus, for universal event dispatching
    public static EventBus EVENT_BUS = GWT.create(SimpleEventBus.class);
    //the current session
    public static Session MY_SESSION;
    //common RPC service
    public static GWTServiceAsync RPC = GWT.create(GWTService.class);
    //balloon tips
    public static BalloonTips balloon = new BalloonTips( true, false );
    
    //current time string for anonymous name
    public static String nowString(){
        return DateTimeFormat.getFormat("yyyyMMddHHmmss").format(new Date());        
    }
    
    public static String sqlDateFormat(Date d){
        return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(d);
    }
}
