/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.evome.KaKsCalc.client.Account;

/**
 *
 * @author nekoko
 */
public class UploadInfo implements IsSerializable {

    public String path, name, UUID;
    public Account account;
    public Type type;
    private String text;    

    public enum Type{
        TEXT,FILE;
    }
    
    public UploadInfo(){
        path = "";
        name = "";
        UUID = "";
        account = Account.sampleData();
        text = "";
    }
    
    public UploadInfo(String path, String name, String uuid, Account account) {
        this.path = path;
        this.name = name;
        this.UUID = uuid;
        this.account = account;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public String getText(){
        return this.text;
    }
}
