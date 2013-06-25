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

    public UploadInfo(){
        
    }
    
    public UploadInfo(String path, String name, String uuid, Account account) {
        this.path = path;
        this.name = name;
        this.UUID = uuid;
        this.account = account;
    }
}
