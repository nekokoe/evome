/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client;

import com.google.gwt.user.client.rpc.IsSerializable;
/**
 *
 * @author kudo_s
 */
public class Account implements IsSerializable {
    //use this once the account is created
    private String SessionID;
    //use this to send back any errors
    private String DisplayError;
    //user account info
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private String Institute, activation_code = "";
    private boolean bAccountstatus = false, bEmailed = false;
    private int userid = 0;
    
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

    public void setPassword( String Password ) {
        this.Password = Password;
    }

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
        this.activation_code = code;
    }
    
    public void setEmailed( boolean b ){
        this.bEmailed = b;
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

    public String getEmail() {
        return this.Email;
    }

    public String getPassword() {
        return this.Password;
    }

    public String getDisplayError() {
        return this.DisplayError;
    }
    
    public String getInsitute(){
        return this.Institute;
    }
    
    public boolean getIsAccountStatusOK(){
        return this.bAccountstatus;
    }

    public String getActivationCode() {
        return this.activation_code;
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

    void copyAccount( Account sis ) {
        this.DisplayError = sis.getDisplayError();
        this.Email = sis.getEmail();
        this.FirstName = sis.getFirstName();
        this.Institute = sis.getInsitute();
        this.LastName = sis.getLastName();
        this.Password = "";
        this.SessionID = sis.getSessionID();
        this.userid = sis.getUserID();
    }
    
    public boolean getIsSessionIDValid(){
        return SessionID != null && !SessionID.isEmpty();
    }
}
