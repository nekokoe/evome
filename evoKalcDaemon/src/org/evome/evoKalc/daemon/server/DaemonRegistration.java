/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Scanner;


import org.evome.evoKalc.daemon.shared.*;
/**
 *  database interface for Daemon Registration
 * @author nekoko
 */
public class DaemonRegistration {
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DBConnector conn(){
        return Shared.dbconn();
    }
    
    public static boolean regDaemon(UUID uuid){
        //remove old daemon registeration if exists        
        removeDaemon(uuid);
        //register new daemon
        String sql = "INSERT INTO `daemon` SET "
                + "daemon.uuid='" + uuid.toString() + "',"
                + "daemon.enable=1,"
                + "daemon.lastActive='" + sdf.format(new Date()) + "'";
                
        try{
            ResultSet rs = conn().execUpdateReturnGeneratedKeys(sql);
            if (rs.next()){
                return true;
            }
        }catch(Exception ex){
            Logger.getLogger(org.evome.evoKalc.daemon.server.DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return false;
    }
    
    public static boolean activeDaemon(UUID uuid) {
        //to keep daemon record alive
        String sql = "UPDATE `daemon` SET "
                + "daemon.lastActive='" + sdf.format(new Date()) + "'"
                + " WHERE daemon.uuid='" + uuid.toString() + "'";
        if (conn().execUpdate(sql) == 0) {
            //faile to active, daemon may not registered
            return regDaemon(uuid);
        } else {
            return true;
        }

    }
    
    public static boolean removeDaemon(UUID uuid){
        //to remove daemon
        String sql = "DELETE FROM `daemon` WHERE daemon.uuid='" + uuid.toString() + "'";
        return (conn().execUpdate(sql) == 0)? false : true;              
    }
    
    
    public static void retireDaemon(){
        retireDaemon(3600); //default is 1 hour (3600 sec)
    }
    
    public static void retireDaemon(int seconds){
        //to retire daemons not active for 'seconds'
        String sql = "DELETE FROM `daemon` WHERE TIME_TO_SEC(TIMEDIFF(daemon.lastActive, NOW())) > " + seconds ;
        conn().execUpdate(sql);
    }    
}
