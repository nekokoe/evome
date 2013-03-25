/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author nekoko
 */
public class Login extends Composite {
    
    private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);
    interface LoginUiBinder extends UiBinder<Widget, Login> {
    }

    public Login() {
        this.initWidget(uiBinder.createAndBindUi(this));        
    }
    
    private String color;
    @UiFactory
    AwesomeButton makeAwesomeButton() { // method name is insignificant
        return new AwesomeButton(color);
    }    
    
    @UiField
    AwesomeButton btnTryWithoutAnAccount;
    
    @UiField
    AwesomeButton btnCreateAccount;



}
