/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.daemon.shared;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
/**
 *
 * @author kudo_s
 */
public class Account  {
    
    //user account info
    private String FirstName = "";
    private String LastName = "";
    private String Email = "";
    //private String Password; //deprecated, use accountKey instead
    private String Institute = "";
    private String activationCode;
    private boolean bAccountstatus = false, bEmailed = false;
    private int userid = 0;
    
    //for database...
    private String accountKey;
    private Date create, access;
    private String uuid;
    private int group;
    private boolean isAnonymous;
    
    //for error report
    private boolean hasError = false;
    private String serverMessage;
    
    
    public Account() {
    }
    
    public void isAnonymous(boolean b){
        this.isAnonymous = b;
    }
    
    public boolean isAnonymous(){
        return this.isAnonymous;
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
    

    void copyAccount( Account sis ) {
        this.Email = sis.getEmail();
        this.FirstName = sis.getFirstName();
        this.Institute = sis.getInsitute();
        this.LastName = sis.getLastName();
//        this.Password = "";
        this.userid = sis.getUserID();
    }    
    
    public void setAccountKey(String email, String password){
        //generate accountKey from email and password
        this.accountKey = md5sum(email + password);
    }
    
    public static String md5sum(String str) {
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
        sample.setAccountStatus(true);      //should be false, set to true for test purpose 2013/9/3
        sample.setActivationCode("");
        sample.setCreateTime(new Date());
        sample.setEmail("test@evome.org");
        sample.setEmailed(false);
        sample.setFirstName("test");
        sample.setGroup(1);
        sample.setInstitute("big");
        sample.setLastName("evome");
        sample.setUserID(1);
        sample.setUUID("48039105-e1d2-4a94-9b18-c091de4bba4e"); //who's this?!
        return sample;
    }
    
    
}
