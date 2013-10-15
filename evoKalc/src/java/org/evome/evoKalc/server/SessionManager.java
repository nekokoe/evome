/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.server;

import org.evome.evoKalc.client.shared.Account;
import org.evome.evoKalc.client.shared.Session;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.util.ArrayList;
/**
 *
 * @author nekoko
 */
public class SessionManager {
    
    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DBConnector conn(){
        return GWTServiceImpl.getDBConn();
    }

    public static Session createSession(){
        Session session;
        String sql = "INSERT INTO `session` SET "
                + "session.uuid='" + UUID.randomUUID().toString() + "',"
                + "session.create='" + sdf.format(new Date()) + "',"
                + "session.lastActive='" + sdf.format(new Date()) + "'";
        try{
            ResultSet rs = conn().execUpdateReturnGeneratedKeys(sql);
            if (rs.next()){
                int id = rs.getInt(1);
                session = getSession(id);             
            }else{
                session = null;
            }                    
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            session = null;
        }
        return session;
    }
    
    public static Session bindSession(Session s, Account a){
        //check if account exists?
        if (AccountManager.isAccountOK(a)){
            a = AccountManager.getAccount(a.getUserID());
            String sql = "UPDATE `session` SET "
                    + "session.account='" + a.getUserID() + "',"
                    + "session.account_uuid='" + a.getUUID() + "',"
                    + "session.lastActive='" + sdf.format(new Date()) + "'"                    
                    + " WHERE session.uuid = '" + s.getUUID() + "' AND session.authKey='" + s.getAuthKey() + "'";
            if (conn().execUpdate(sql) > 0){
                //bind success
                s.setAccount(a);
                return s;
            }
        }else{
            System.err.println("account:" + a.getUUID() + " is not activated or not here");
        }
        return null;
    }
    
    public static boolean detachSession(Session s) {
        String sql = "UPDATE `session` SET "
                + "session.account=0,"
                + "session.account_uuid='',"
                + "session.authKey=''"
                + " WHERE session.uuid = '" + s.getUUID() + "' AND session.authKey='" + s.getAuthKey() + "'";
        
        if (conn().execUpdate(sql) > 0) {
            return true;            
        }else{
            return false;
        }
    }
    
    public static Session getSession(int id){
        Session s = new Session();
        String sql = "SELECT * FROM `session` WHERE session.id = " + id;
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                s.setAccount(AccountManager.getAccount(rs.getInt("account")));
                s.setCreateTime(rs.getDate("create"));
                s.setLastActiveTime(rs.getDate("lastActive"));
                s.setSessionID(rs.getInt("id"));
                s.setUUID(rs.getString("uuid"));
                //NOTE: getSession don't return the authKey, which could only get accesss when binding session
            }else{
                s = null;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            s = null;
        }
        return s;
    }
    
    public static Session getSession(UUID uuid){
        Session s = new Session();
        String sql = "SELECT * FROM `session` WHERE session.uuid = '" + uuid.toString() + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                s.setAccount(AccountManager.getAccount(rs.getInt("account")));
                s.setCreateTime(rs.getDate("create"));
                s.setLastActiveTime(rs.getDate("lastActive"));
                s.setSessionID(rs.getInt("id"));
                s.setUUID(rs.getString("uuid"));
            }else{
                s = null;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            s = null;
        }
        return s;        
    }
    
    public static ArrayList<Session> getAccountSessionList(Account account){
        //this function enables user to recover broken session or kick off other sessions
        ArrayList<Session> list = new ArrayList<Session>();
        String sql = "SELECT FROM `session` WHERE session.account = " + account.getUserID();
        try{
            ResultSet rs = conn().execQuery(sql);
            while (rs.next()){
                Session s = new Session();
                s.setAccount(AccountManager.getAccount(rs.getInt("account")));
                s.setCreateTime(rs.getDate("create"));
                s.setLastActiveTime(rs.getDate("lastActive"));
                s.setSessionID(rs.getInt("id"));
                s.setUUID(rs.getString("uuid"));
                list.add(s);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            list = null;
        }
        return list;
    }
    
    public static boolean activeSession(Session s){
        //call this to keep session alive
        String sql = "UPDATE `session` SET session.lastActive = NOW() WHERE session.id = " + s.getSessionID();
        return (conn().execUpdate(sql) > 0)? true : false;
    }
    
    public static void retireTimeoutSession(){
        //sessions timed out by sysconf.SESSION_TIME_OUT is retired
        String sql = "DELETE FROM `session` WHERE TIME_TO_SEC(TIMEDIFF(session.lastActive, NOW())) > " + sysconf.SESSION_TIME_OUT;
        conn().execUpdate(sql);
    }
    
    public static boolean authenticValidation(Session s, Account a, String accountKey){
        //this function validates clientkey against session and account in database
        //true: OK, false: failed
        //get ServerSession and ServerAccount
        //Session ss = SessionManager.getSession(s.getSessionID());
        //Account sa = AccountManager.getAccount(a.getUserID());
        //generate serverkey
        String serverKey = AccountManager.getAccountKey(a);    //don't use sa.getAccountKey(), may be NULL
        //compare clientKey with serverKey
        if (serverKey.equalsIgnoreCase(accountKey)){
            System.out.println("authentic validation passed for account:" + a.getUUID() + " session: " + s.getUUID());
            return true;
        }else{
            System.err.println("authentic failed for some reasons...");
            return false;
        }
    }
    
    public static String updateAuthKey(Session s, Account a, String clientKey){
        if (authenticValidation(s, a, clientKey)){
            //success
            System.out.println("authentic validation successful!");
            //new random authKey
            String authKey = UUID.randomUUID().toString();
            //write to db
            String sql = "UPDATE `session` SET "
                    + "session.authKey='" + authKey + "'"
                    + " WHERE session.id = " + s.getSessionID()  + "";
            //return if updated
            if (conn().execUpdate(sql) > 0){
                return authKey;
            }            
        }
        System.err.println("update authentic key failed...");        
        return null;
    }
    
    public static boolean isSessionValid(Session s){
        //check the session with authKey, userid, 
        String sql = "SELECT * FROM `session` WHERE "
                + "session.id=" + s.getSessionID() + " AND "
                + "session.account=" + s.getAccount().getUserID() + " AND "
                + "session.account_uuid='" + s.getAccount().getUUID() + "' AND "
                + "session.authKey='" + s.getAuthKey() + "'"; 
        ResultSet rs = conn().execQuery(sql);
        try{
            if (rs.next()){
                //valid
                activeSession(s);
                return true;                
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
        }
        //not valid
        return false;
    }
    
}
