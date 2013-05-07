/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import org.evome.KaKsCalc.client.Account;
import org.evome.KaKsCalc.client.Session;
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
    
    private static DBConnector dbconn = new DBConnector();
    private static SysConfig sysconf = new SysConfig();
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Session createSession(Account account){
        Session session;
        String sql = "INSERT INTO `session` SET ("
                + "uuid='" + UUID.randomUUID().toString() + "',"
                + "account='" + account.getUserID() + "',"
                + "account_uuid='" + account.getUUID() + "',"
                + "create='" + sdf.format(new Date()) + "',"
                + "lastActive='" + sdf.format(new Date()) + "'"
                + ")";
        try{
            ResultSet rs = dbconn.execQueryReturnGeneratedKeys(sql);
            if (rs.next()){
                int id = rs.getInt(1);
                session = getSessionByID(id);             
            }else{
                session = null;
            }                    
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            session = null;
        }
        return session;
    }
    
    public static Session getSessionByID(int id){
        Session s = new Session();
        String sql = "SELECT * FROM `session` WHERE id = " + id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                s.setAccountUUID(rs.getString("account_uuid"));
                s.setCreateTime(rs.getDate("create"));
                s.setLastActiveTime(rs.getDate("lastActive"));
                s.setSessionID(rs.getInt("id"));
                s.setUUID(rs.getString("uuid"));
                s.setUserID(rs.getInt("account"));
            }else{
                s = null;
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            s = null;
        }
        return s;
    }
    
    public static Session getSessionByUUID(String uuid){
        Session s = new Session();
        String sql = "SELECT * FROM `session` WHERE uuid = " + uuid;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                s.setAccountUUID(rs.getString("account_uuid"));
                s.setCreateTime(rs.getDate("create"));
                s.setLastActiveTime(rs.getDate("lastActive"));
                s.setSessionID(rs.getInt("id"));
                s.setUUID(rs.getString("uuid"));
                s.setUserID(rs.getInt("account"));
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
        String sql = "SELECT FROM `session` WHERE account = " + account.getUserID();
        try{
            ResultSet rs = dbconn.execQuery(sql);
            while (rs.next()){
                Session s = new Session();
                s.setAccountUUID(rs.getString("account_uuid"));
                s.setCreateTime(rs.getDate("create"));
                s.setLastActiveTime(rs.getDate("lastActive"));
                s.setSessionID(rs.getInt("id"));
                s.setUUID(rs.getString("uuid"));
                s.setUserID(rs.getInt("account"));
                list.add(s);
            }
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            list = null;
        }
        return list;
    }
    
    public static void activeSession(Session s){
        //call this to keep session alive
        String sql = "UPDATE `session` SET (lastActive=NOW()) WHERE id = " + s.getSessionID();
        ResultSet rs = dbconn.execQuery(sql);
    }
    
    public static void retireTimeoutSession(){
        //sessions timed out by sysconf.SESSION_TIME_OUT is retired
        String sql = "DELETE FROM `session` WHERE TIME_TO_SEC(TIMEDIFF(lastActive, NOW())) > " + sysconf.SESSION_TIME_OUT;
        try{
            ResultSet rs = dbconn.execQuery(sql);
        }catch(Exception ex){
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }
    
    public static boolean authenticValidation(Session s, Account a, String clientKey){
        //this function validates clientkey against session and account in database
        //true: OK, false: failed
        Session ss = SessionManager.getSessionByID(s.getSessionID());
        Account aa = AccountManager.getAccount(a.getUserID());
        //1.generate valkey from ss.uuid and aa.accountkey stored server-end
        String serverKey = Account.md5sum(ss.getUUID() + aa.getAccountKey());
        //2.compare clientKey and serverKey
        return (serverKey.equalsIgnoreCase(clientKey));
    }
    
}
