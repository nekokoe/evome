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
    
    private static DBConnector dbconn = GWTServiceImpl.getDBConn();
    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static Session createSession(Account account){
        Session session;
        String sql = "INSERT INTO `session` SET "
                + "session.uuid='" + UUID.randomUUID().toString() + "',"
                + "session.account='" + account.getUserID() + "',"
                + "session.account_uuid='" + account.getUUID() + "',"
                + "session.create='" + sdf.format(new Date()) + "',"
                + "session.lastActive='" + sdf.format(new Date()) + "'";
        try{
            ResultSet rs = dbconn.execUpdateReturnGeneratedKeys(sql);
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
    
    public static Session getSession(int id){
        Session s = new Session();
        String sql = "SELECT * FROM `session` WHERE session.id = " + id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                s.setAccount(AccountManager.getAccount(rs.getInt("account")));
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
    
    public static Session getSession(UUID uuid){
        Session s = new Session();
        String sql = "SELECT * FROM `session` WHERE session.uuid = '" + uuid.toString() + "'";
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                s.setAccount(AccountManager.getAccount(rs.getInt("account")));
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
        String sql = "SELECT FROM `session` WHERE session.account = " + account.getUserID();
        try{
            ResultSet rs = dbconn.execQuery(sql);
            while (rs.next()){
                Session s = new Session();
                s.setAccount(AccountManager.getAccount(rs.getInt("account")));
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
        String sql = "UPDATE `session` SET session.lastActive = NOW() WHERE session.id = " + s.getSessionID();
        dbconn.execUpdate(sql);
    }
    
    public static void retireTimeoutSession(){
        //sessions timed out by sysconf.SESSION_TIME_OUT is retired
        String sql = "DELETE FROM `session` WHERE TIME_TO_SEC(TIMEDIFF(session.lastActive, NOW())) > " + sysconf.SESSION_TIME_OUT;
        dbconn.execUpdate(sql);
    }
    
    public static boolean authenticValidation(Session s, Account a, String clientKey){
        //this function validates clientkey against session and account in database
        //true: OK, false: failed
        Session ss = SessionManager.getSession(s.getSessionID());
        Account aa = AccountManager.getAccount(a.getUserID());
        //1.generate valkey from ss.uuid and aa.accountkey stored server-end
        String serverKey = Account.md5sum(ss.getUUID() + aa.getAccountKey());
        //2.compare clientKey and serverKey
        return (serverKey.equalsIgnoreCase(clientKey));
    }
    
}
