/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.server;

import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.evoKalc.client.shared.*;

/**
 *
 * @author nekoko
 */
public class AccountManager {
    private static SysConfig sysconf = GWTServiceImpl.getSysConfig();
    private static DBConnector conn(){
        return GWTServiceImpl.getDBConn();
    }
    
    public static Account getAccount(int id){
        Account account = new Account();
        String sql = "SELECT * FROM `account` WHERE account.id = "+ id;
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                account.setAccessTime(rs.getDate("access"));
                //account.setAccountKey(rs.getString("accountKey"));
                account.setAccountStatus(rs.getBoolean("active"));
                //account.setActivationCode(rs.getString("activationCode"));
                account.setCreateTime(rs.getDate("create"));
                account.setFirstName(rs.getString("firstname"));
                account.setGroup(rs.getInt("group"));
                account.setInstitute(rs.getString("institute"));
                account.setLastName(rs.getString("lastname"));
                account.setUUID(rs.getString("uuid"));
                account.setUserID(rs.getInt("id"));
                account.setEmail(rs.getString("email"));
            }else{
                account = null;
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);    
            account = null;
        }
        return account;
    }
    
    public static Account getAccount(UUID uuid){
        Account account = new Account();
        String sql = "SELECT * FROM `account` WHERE account.uuid = '"+ uuid.toString() + "'";
        try{
            ResultSet rs = conn().execQuery(sql);
            if (rs.next()){
                account.setAccessTime(rs.getDate("access"));
                //account.setAccountKey(rs.getString("accountKey"));
                account.setAccountStatus(rs.getBoolean("active"));
                //account.setActivationCode(rs.getString("activationCode"));
                account.setCreateTime(rs.getDate("create"));
                account.setFirstName(rs.getString("firstname"));
                account.setGroup(rs.getInt("group"));
                account.setInstitute(rs.getString("institute"));
                account.setLastName(rs.getString("lastname"));
                account.setUUID(rs.getString("uuid"));
                account.setUserID(rs.getInt("id"));
                account.setEmail(rs.getString("email"));
            }else{
                account = null;
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);    
            account = null;
        }
        return account;        
    }
    
    public static String getAccountKey(Account account){
        String sql = "SELECT * FROM `account` WHERE account.id = '"+ account.getUserID() + "'";
        ResultSet rs = conn().execQuery(sql);
        try{
            if (rs.next()){
                return rs.getString("accountKey");
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);              
        }
        return null;
    }
    
    public static String getActivationCode(Account account){
        String sql = "SELECT * FROM `account` WHERE account.id = '"+ account.getUserID() + "'";
        ResultSet rs = conn().execQuery(sql);
        try{
            if (rs.next()){
                return rs.getString("activationCode");
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);              
        }
        return null;        
    }
    
    public static Account createAccount(Account account){
        //add new account using infomation provided in account
        //return account_id, return 0 if failed
        Account account_new = null;
        String sql = "INSERT INTO `account` SET "
                + "account.email = '" + account.getEmail() + "',"
                + "account.firstname = '" + account.getFirstName() + "',"
                + "account.lastname = '" + account.getLastName() + "',"
                + "account.institute = '" + account.getInsitute() + "',"
                + "account.accountKey = '" + account.getAccountKey() + "',"
                + "account.create = '" + conn().getSQLTime() + "',"
                + "account.access = '" + conn().getSQLTime() + "',"
                + "account.active = 1,"     //WARN: set to 0 for beta test, pls cancel when release
                + "account.activationCode = '" + UUID.randomUUID().toString() + "',"
                + "account.group = '" + account.getGroup() + "',"
                + "account.uuid = '" + UUID.randomUUID().toString() + "'"
                + "";
        try{
            ResultSet rs  = conn().execUpdateReturnGeneratedKeys(sql);
            if (rs.next()){
                int id = rs.getInt(1);
                account_new = getAccount(id);
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return account_new;
    }

    public static void migrateAccount(){
        //this function migrates other account
        //to be implemented
    }
    
    public static boolean editAccount(Account account){
        //update account data
        //NOTE: uuid can't be changed, and also create and group
        if (!account.getAccountStatus()){
            //if user changed the email addr, account should be re-activated
            account.setActivationCode(UUID.randomUUID().toString());           
        }
        String sql = "UPDATE `account` SET "
                + "account.email = '" + account.getEmail() + "',"
                + "account.firstname = '" + account.getFirstName() + "',"
                + "account.lastname = '" + account.getLastName() + "',"
                + "account.institute = '" + account.getInsitute() + "',"
                + "account.accountKey = '" + account.getAccountKey() + "',"
                + "account.access = '" + conn().getSQLTime() + "',"
                + "account.active = '" + account.getAccountStatus() + "',"
                + "account.activationCode = '" + account.getActivationCode() + "'" 
                + " WHERE (uuid = '" + account.getUUID() + "' AND id = '" + account.getUserID() + "')";
        if (conn().execUpdate(sql) > 0){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean removeAccount(Account account){
        //WARNING: this is unsafe and uneccesary in most situation, use carefully
        String sql = "DELETE FROM `account` WHERE (id = '" + account.getUserID() + "' AND uuid = '" + account.getUUID() + "' AND accountKey = '" + account.getAccountKey() + "')"; 
        System.out.print(sql);
        try {
            //remove data folder first
            if (ResourceManager.removeAccountFolder(sysconf.DATA_ROOT_PATH, account)) {
                //and then remove db record
                int rs = conn().execUpdate(sql);
                if (rs > 0) {
                    return true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean isAccountOK(Account account){
        Account checkit = getAccount(account.getUserID());
        return (checkit != null);
    }
    
    public static Account findAccount(String email){
        Account a = null;
        String sql = "SELECT * FROM `account` WHERE account.email='" + email + "'";
        ResultSet rs = conn().execQuery(sql);
        try{
            if (rs.next()){
                a = getAccount(rs.getInt("id"));
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return a;
    }
    
    public static Account anonymousAccount(){
        //create an anonymous account to 'account' with group=65535
        Account a = new Account();
        String uuid = UUID.randomUUID().toString();
        String accountKey = Account.md5sum(uuid);
        String sql = "INSERT INTO `account` SET "
                + "account.email='nobody@home',"
                + "account.firstname='anonymous',"
                + "account.lastname='nobody',"
                + "account.institute='homeless',"
                + "account.active=1,"
                + "account.group=65535,"
                + "account.accountKey='" + accountKey +  "',"
                + "account.uuid='" + uuid + "',"
                + "account.create='" + conn().getSQLTime() + "',"
                + "account.access='" + conn().getSQLTime() + "'"
                + "";
        try{
            ResultSet rs = conn().execUpdateReturnGeneratedKeys(sql);
            if (rs.next()){
                a.setUserID(rs.getInt(1));
                a.setAccessTime(new Date());
                a.setAccountKey(accountKey);
                a.setAccountStatus(true);
                a.setActivationCode("");
                a.setCreateTime(new Date());
                a.setFirstName("anonymous");
                a.setGroup(65535);
                a.setInstitute("homeless");
                a.setLastName("nobody");
                a.setUUID(uuid);
                a.setEmail("nobody@home");
            }else{
                a = null;
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            a = null;
        }
        return a;
    }
}
