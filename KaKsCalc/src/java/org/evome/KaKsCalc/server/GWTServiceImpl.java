/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.evome.KaKsCalc.client.GWTService;
import org.evome.KaKsCalc.client.Session;
import java.util.Date;

/**
 *
 * @author nekoko
 */
public class GWTServiceImpl extends RemoteServiceServlet implements GWTService {
    
    @Override
    public long getServerTime(){
        Date date = new Date();
        return date.getTime();
    }
    
}
