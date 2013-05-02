/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
/**
 *
 * @author kudo_s
 */
public class Account implements IsSerializable {
    private static Shared shared = new Shared();
    
    
    //use this once the account is created
    private String SessionID;
    //use this to send back any errors
    private String DisplayError;
    //user account info
    private String FirstName;
    private String LastName;
    private String Email;
    //private String Password; //deprecated, use accountKey instead
    private String Institute, activationCode = "";
    private boolean bAccountstatus = false, bEmailed = false;
    private int userid = 0;
    //user accouint info append...
    private String accountKey, newAccountKey; //newAccountKey is used when updating accountKey
    private Date create, access;
    private String uuid;
    private int group;
    
    
    public Account() {
    }
    
    public void setSessionID( String SessionID ) {
        this.SessionID = SessionID;
    }
    
    public void setFirstName( String FirstName ) {
        this.FirstName = FirstName;
    }
    
    public void setLastName( String LastName ) {
        this.LastName = LastName;
    }
    
    public void setEmail( String email ) {
        this.Email = email;
    }

//    public void setPassword( String Password ) {
//        this.Password = Password;
//    }

    public void setDisplayError( String DisplayError ) {
        this.DisplayError = DisplayError;
    }
    
    public void setInstitute( String ins ){
        this.Institute = ins;
    }
    
    public void setAccountStatus( boolean b ){
        this.bAccountstatus = b;
    }
    
    public void setActivationCode( String code ){
        this.activationCode = code;
    }
    
    public void setEmailed( boolean b ){
        this.bEmailed = b;
    }
    
    public void setAccountKey(String key){ //by nekokoe
        this.accountKey = key;
    }
    
    public void setNewAccountKey(String key){ //by nekokoe
        this.newAccountKey = key;
    }
    
    public void setCreateTime(Date date){   //by nekokoe
        this.create = date;
    }
    
    public void setAccessTime(Date date){   //by nekokoe
        this.access = date;
    }
    
    public void setUUID(String uuid){   //by nekokoe
        this.uuid = uuid;
    }
    
    public void setGroup(int group){    //by nekokoe
        this.group = group;
    }
    
    public String getSessionID() {
        return this.SessionID;
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public String getFullName(){
        return this.FirstName + " " + this.LastName;
    }
    
    public String getEmail() {
        return this.Email;
    }

//    public String getPassword() {
//        return this.Password;
//    }

    public String getDisplayError() {
        return this.DisplayError;
    }
    
    public String getInsitute(){
        return this.Institute;
    }
    
    public boolean getAccountStatus(){
        return this.bAccountstatus;
    }

    public String getActivationCode() {
        return this.activationCode;
    }
    
    public boolean getIsEmailSent(){
        return this.bEmailed;
    }

    public void setUserID(int aInt) {
        this.userid = aInt;
    }
    
    public int getUserID(){
        return this.userid;
    }
    
    public String getAccountKey(){  //by nekokoe
        return this.accountKey;
    }
    
    public String getNewAccountKey(){  //by nekokoe
        return this.newAccountKey;
    }
    
    public Date getCreateTime(){    //by nekokoe
        return this.create;
    }
    
    public Date getAccessTime(){    //by nekokoe
        return this.access;
    }
    
    public String getUUID(){    //by nekokoe
        return this.uuid;
    }
    
    public int getGroup(){  //by nekokoe
        return this.group;
    }
    
    public boolean getIsSessionIDValid(){
        return SessionID != null && !SessionID.isEmpty();
    }

    void copyAccount( Account sis ) {
        this.DisplayError = sis.getDisplayError();
        this.Email = sis.getEmail();
        this.FirstName = sis.getFirstName();
        this.Institute = sis.getInsitute();
        this.LastName = sis.getLastName();
//        this.Password = "";
        this.SessionID = sis.getSessionID();
        this.userid = sis.getUserID();
    }    
    
    public void setAccountKey(String email, String password){
        //generate accountKey from email and password
        this.accountKey = md5sum(email + password);
    }
    
    public void setNewAccountKey(String email, String password){
        //generate accountKey from email and password
        this.newAccountKey = md5sum(email + password);        
    }

    
    private static String md5sum(String str) {
        String md5str = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            BigInteger bigint = new BigInteger(1, md5.digest());
            md5str = bigint.toString(16);
        } catch (Exception ex) {
        }
        return md5str;
    }
    
    
    //code below is for test purpose
    public static Account sampleData(){
        Account sample = new Account();
        sample.setAccessTime(new Date());
        sample.setAccountKey("test@evome.org", "123456");
        sample.setAccountStatus(false);
        sample.setActivationCode(shared.randomUUID());
        sample.setCreateTime(new Date());
        sample.setEmail("test@evome.org");
        sample.setEmailed(false);
        sample.setFirstName("test");
        sample.setGroup(1);
        sample.setInstitute("big");
        sample.setLastName("evome");
        sample.setUserID(1);
        sample.setUUID(shared.randomUUID());
        return sample;
    }
}
