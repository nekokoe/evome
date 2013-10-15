/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui;
import com.sencha.gxt.widget.core.client.Window;

import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.button.TextButton;
import org.evome.evoKalc.client.shared.Account;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import org.evome.evoKalc.client.shared.Shared;
import org.evome.evoKalc.client.ui.utils.BalloonTips;


/**
 *
 * @author nekoko
 */
public class AccountEditor extends Window{
    Account myaccount;
    
    private TextField uid = new TextField();
    private TextField email = new TextField();
    private PasswordField oldpass = new PasswordField();
    private PasswordField pass = new PasswordField();
    private PasswordField cfmpass = new PasswordField();
    private TextField firstname = new TextField();
    private TextField lastname = new TextField();
    private TextField institute = new TextField();
    
    private TextButton btnOK = new TextButton();
    
    public AccountEditor(){
        //new account
        initUI();
        myaccount = new Account();
        //alter widgets
        this.setHeadingText("Create New Account");
        uid.getParent().removeFromParent();
        oldpass.getParent().removeFromParent();
        email.setEnabled(true);
        btnOK.setText("Create Account");        
        btnOK.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                //Info.display("test", "to create account");
                if (validate()){
                    copyData();
                    //find account if exsits?
                    Shared.RPC.findAccount(myaccount.getEmail(), new AsyncCallback<Account>() {
                        @Override
                        public void onSuccess(Account a) {
                            if (a == null) {
                                //OK to create account
                                Shared.RPC.createAccount(myaccount, new AsyncCallback<Account>() {
                                    @Override
                                    public void onSuccess(Account a) {
                                        if (a != null) {
                                            Info.display("Success", "Welcome " + a.getFullName());
                                            hide();
                                        } else {
                                            Info.display("Error", "Fail to create account");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable caught) {
                                        Info.display("Error", caught.getMessage());
                                    }
                                });
                            } else {
                                //account already exist
                                Info.display("Error", "Email address already exists, please try agian");
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            Info.display("Error", caught.getMessage());
                        }
                    });

                }
            }
        });
    }
    
    public AccountEditor(Account a){
        //edit account
        initUI(); 
        myaccount = a;
        pasteData();        
        //alter widgets
        this.setHeadingText("Edit Account Info");
        uid.setVisible(true);
        uid.setEnabled(false);
        email.setEnabled(false);
        btnOK.setText("Edit Account");  
        btnOK.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                //Info.display("test", "to edit account");
                if (validate()){
                    copyData();
                    Shared.RPC.editAccount(Shared.MY_SESSION, myaccount, new AsyncCallback<Boolean>(){
                        @Override
                        public void onSuccess(Boolean b){
                            if (b){
                                Info.display("Success", "Account Info Updated");
                                hide();
                            }else{
                                Info.display("Error", "Fail to update account info,");
                            }
                        }
                        @Override
                        public void onFailure(Throwable caught){
                            Info.display("Error", caught.getMessage());
                        }
                        
                    });
                }
            }
        });        
    }
    
    private void initUI(){
        this.setWidth(400);
        this.setHeight(400);
        
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData vlayout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(20));
        vertical.add(new FieldLabel(uid, "User ID"), vlayout);
        vertical.add(new FieldLabel(email, "Email"), vlayout);
        vertical.add(new FieldLabel(oldpass, "Old Password"), vlayout);
        vertical.add(new FieldLabel(pass, "New Password"), vlayout);
        vertical.add(new FieldLabel(cfmpass, "Confirm Password"), vlayout);
        vertical.add(new FieldLabel(firstname, "First Name"), vlayout);
        vertical.add(new FieldLabel(lastname, "Last Name"), vlayout);
        vertical.add(new FieldLabel(institute, "Institute"), vlayout);
        this.add(vertical);
        

        
        //cancel button
        TextButton btnCancel = new TextButton("Cancel");
        btnCancel.addSelectHandler(new SelectEvent.SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });
        
        this.addButton(btnOK);
        this.addButton(btnCancel);
    }
    
    private void pasteData(){
        uid.setText(Integer.toString(myaccount.getUserID()));
        email.setText(myaccount.getEmail());
        firstname.setText(myaccount.getFirstName());
        lastname.setText(myaccount.getLastName());
        institute.setText(myaccount.getInsitute());
    }
    
    private boolean validate(){
        boolean valid = true;
        //force the balloon tips on top of me
        //email
        if (email.getText() == null 
                || email.getText().isEmpty() 
                || !email.getText().matches("^\\w[\\w.]*@\\w+(\\.\\w+)+")){
            valid = false;
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);
            balloon.setWidgetToShow(new HTML("email address is not valid"));
            balloon.showRelativeToWidget(email.getParent(), BalloonTips.TipPos.rightTo);
        }
        //old pass
//        if (myaccount.getUserID() != 0) {
//            //new user don't validate oldpass
//            if (oldpass.getText().isEmpty()
//                    || !oldpass.getText().matches("\\w+")) {
//                valid = false;
//                BalloonTips balloon = new BalloonTips(true, false);
//                balloon.showOnTopOf(this);
//                balloon.setWidgetToShow(new HTML("old password is empty or invalid"));
//                balloon.showRelativeToWidget(oldpass.getParent(), BalloonTips.TipPos.rightTo);
//            }
//        }
        //old pass
        if (!oldpass.getText().isEmpty()
                && !oldpass.getText().matches("\\w+")){
            valid = false;            
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);
            balloon.setWidgetToShow(new HTML("password is empty or invalid"));
            balloon.showRelativeToWidget(pass.getParent(), BalloonTips.TipPos.rightTo);            
        }
            
        
        //new pass
        if (pass.getText().isEmpty() && myaccount.getUserID() == 0 
                || !pass.getText().isEmpty() && !pass.getText().matches("\\w+")){
            valid = false;            
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);
            balloon.setWidgetToShow(new HTML("new password is empty or invalid"));
            balloon.showRelativeToWidget(pass.getParent(), BalloonTips.TipPos.rightTo);
        }
        //confirm pass
        if (cfmpass.getText().isEmpty() && myaccount.getUserID() == 0 
                || !cfmpass.getText().isEmpty() && !cfmpass.getText().matches("\\w+")){
            valid = false;
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);
            balloon.setWidgetToShow(new HTML("confirm password is empty or invalid"));
            balloon.showRelativeToWidget(cfmpass.getParent(), BalloonTips.TipPos.rightTo);            
        }
        
        //check if pass = cfmpass
        if (!pass.getText().equals(cfmpass.getText())){
            valid = false;
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);
            balloon.setWidgetToShow(new HTML("input doesn't match your password above"));
            balloon.showRelativeToWidget(cfmpass.getParent(), BalloonTips.TipPos.rightTo);              
        }
        
        if (firstname.getText() == null
                || firstname.getText().isEmpty()){
            valid = false;
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);            
            balloon.setWidgetToShow(new HTML("firstname is required"));
            balloon.showRelativeToWidget(firstname.getParent(), BalloonTips.TipPos.rightTo);
        }
        if (lastname.getText() == null
                || lastname.getText().isEmpty()){
            valid = false;
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);            
            balloon.setWidgetToShow(new HTML("lastname is required"));
            balloon.showRelativeToWidget(lastname.getParent(), BalloonTips.TipPos.rightTo);            
        }
        if (institute.getText() == null
                || institute.getText().isEmpty()){
            valid = false;
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);            
            balloon.setWidgetToShow(new HTML("institute is required"));
            balloon.showRelativeToWidget(institute.getParent(), BalloonTips.TipPos.rightTo);                        
        }
        return valid;
    }
    
    private void copyData(){
        //check if email address changed?
        //tricky: this also works with new account~
        if (!myaccount.getEmail().equals(email.getText())){
            myaccount.setAccountStatus(false);
        }
        //copy data from form to myaccount
        myaccount.setEmail(email.getText());
        myaccount.setFirstName(firstname.getText());
        myaccount.setLastName(lastname.getText());
        myaccount.setInstitute(institute.getText());
        if (!pass.getText().isEmpty()){
            //leave empty if don't want to update password
            myaccount.setAccountKey(email.getText(), pass.getText());
            if (!oldpass.getText().isEmpty()){
                //should check oldpass with server, but not implemented
            }
        }

    }
            
    
}
