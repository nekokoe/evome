/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.server;

import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.evome.KaKsCalc.client.Account;

/**
 *
 * @author nekoko
 */
public class AccountManager {
    
    private static DBConnector dbconn = new DBConnector();
    
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
            ResultSet rs  = dbconn.execQueryReturnGeneratedKeys(sql);
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
        String sql = "UPDATE `account` SET ("
                + "email = '" + account.getEmail() + "',"
                + "firstname = '" + account.getFirstName() + "',"
                + "lastname = '" + account.getLastName() + "',"
                + "institute = '" + account.getInsitute() + "',"
                + "accountKey = '" + account.getNewAccountKey() + "',"
                + "access = '" + dbconn.getSQLTime() + "',"
                + "active = '" + account.getAccountStatus() + "',"
                + "activationCode = '" + account.getActivationCode() + "'" 
                + ") WHERE (uuid = '" + account.getUUID() + "' AND id = '" + account.getUserID() + "')";
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
    
}
