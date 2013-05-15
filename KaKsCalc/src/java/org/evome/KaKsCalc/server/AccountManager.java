/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Account;

/**
 *
 * @author nekoko
 */
public class AccountManager {
    
    private static DBConnector dbconn = GWTServiceImpl.getDBConn();
    
    public static Account getAccount(int id){
        Account account = new Account();
        String sql = "SELECT * FROM `account` WHERE id = "+ id;
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                account.setAccessTime(rs.getDate("access"));
                account.setAccountKey(rs.getString("accountKey"));
                account.setAccountStatus(rs.getBoolean("active"));
                account.setActivationCode(rs.getString("activationCode"));
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
    
    public static int createAccount(Account account){
        //add new account using infomation provided in account
        //return account_id, return 0 if failed
        int id = 0;
        String sql = "INSERT INTO `account` SET ("
                + "email = '" + account.getEmail() + "',"
                + "firstname = '" + account.getFirstName() + "',"
                + "lastname = '" + account.getLastName() + "',"
                + "institute = '" + account.getInsitute() + "',"
                + "accountKey = '" + account.getAccountKey() + "',"
                + "created = '" + dbconn.getSQLTime() + "',"
                + "access = '" + dbconn.getSQLTime() + "',"
                + "active = 0" 
                + "activationCode = '" + UUID.randomUUID().toString() + "',"
                + "group = '" + account.getGroup() + "',"
                + "uuid = '" + UUID.randomUUID().toString() + "'"
                + ")";
        try{
            ResultSet rs  = dbconn.execUpdateReturnGeneratedKeys(sql);
            if (rs.next()){
                id = rs.getInt(1);
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public static void migrateAccount(){
        //this function migrates other account
        //to be implemented
    }
    
    public static boolean editAccount(Account account){
        //update account data
        //NOTE: uuid can't be changed, and also create and group
        if (!account.getAccountStatus()){  //if user changed the email addr, account should be re-activated
            account.setActivationCode(UUID.randomUUID().toString());
        }
        String sql = "UPDATE `account` SET "
                + "account.email = '" + account.getEmail() + "',"
                + "account.firstname = '" + account.getFirstName() + "',"
                + "account.lastname = '" + account.getLastName() + "',"
                + "account.institute = '" + account.getInsitute() + "',"
                + "account.accountKey = '" + account.getNewAccountKey() + "',"
                + "account.access = '" + dbconn.getSQLTime() + "',"
                + "account.active = '" + account.getAccountStatus() + "',"
                + "account.activationCode = '" + account.getActivationCode() + "'" 
                + " WHERE (uuid = '" + account.getUUID() + "' AND id = '" + account.getUserID() + "')";
        if (dbconn.execUpdate(sql) > 0){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean removeAccount(Account account){
        //WARNING: this is unsafe and uneccesary in most situation, use carefully
        String sql = "DELETE FROM `account` WHERE (id = '" + account.getUserID() + "' AND uuid = '" + account.getUUID() + "' AND accountKey = '" + account.getAccountKey() + "')"; 
        try{
            ResultSet rs = dbconn.execQuery(sql);
            if (rs.next()){
                return true;
            }
        }catch(Exception ex){
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return false;
    }
    
    public static boolean isAccountOK(Account account){
        Account checkit = getAccount(account.getUserID());
        return (checkit != null && checkit.getAccountStatus());
    }
    
    
    public static Account anonymousAccount(){
        //create an anonymous account to 'account' with group=65535
        Account a = new Account();
        String uuid = UUID.randomUUID().toString();
        String accountKey = Account.md5sum(uuid);
        String sql = "INSERT INTO `account` SET ("
                + "account.email='nobody@home'"
                + "account.firstname='anonymous',"
                + "account.lastname='nobody',"
                + "account.institute='homeless',"
                + "account.active=1,"
                + "account.group=65535,"
                + "account.accountKey='" + accountKey +  "',"
                + "account.uuid='" + uuid + "',"
                + "account.create='" + dbconn.getSQLTime() + "',"
                + "account.access='" + dbconn.getSQLTime() + "'"
                + ")";
        try{
            ResultSet rs = dbconn.execUpdateReturnGeneratedKeys(sql);
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
