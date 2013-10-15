/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon.shared;

import org.evome.evoKalc.daemon.server.SysConfig;
import org.evome.evoKalc.daemon.server.DBConnector;
import java.text.SimpleDateFormat;

import java.util.UUID;
import java.util.Date;

/**
 *
 * @author nekoko
 */
public class Shared {

    public static SysConfig sysconf = new SysConfig();
    private static DBConnector dbconn = new DBConnector();
    public static UUID daemonUUID;
    public static Date starttime;
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static DBConnector dbconn() {
        if (!dbconn.isAlive()) {
            dbconn = new DBConnector();
        }
        return dbconn;
    }
    
    public static String sdformat(Date date){
        if (date == null){
            return "1970-1-1 00:00:00";
        }
        return sdf.format(date);
    }
    
    //quick format
    public static String toFasta(Sequence s){
        return ">" + s.getId() + System.lineSeparator() + s.getSequence() + System.lineSeparator();
    }
}
