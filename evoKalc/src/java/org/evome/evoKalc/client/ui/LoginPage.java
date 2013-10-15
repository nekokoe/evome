/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.info.Info;
import org.evome.evoKalc.client.shared.*;
import org.evome.evoKalc.client.event.*;
import org.evome.evoKalc.client.ui.utils.BalloonTips;


/**
 *
 * @author nekoko
 */
public class LoginPage extends Composite {

    public interface UiResource extends ClientBundle{
        interface LoginPageCss extends CssResource{
            String logo_title();
            String container();
        }        
        @Source("resources/LoginPage.css")
        LoginPageCss css();
    } 
    
    public static final UiResource res = GWT.create(UiResource.class);
    
    VerticalLayoutContainer.VerticalLayoutData layout = new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(20));
    
    final TextField email = new TextField();
    final PasswordField password = new PasswordField();
    
    public LoginPage() {
        //inject css stylesheet
        res.css().ensureInjected();
        
        CenterLayoutContainer container = new CenterLayoutContainer();
        container.setWidth(Window.getClientWidth() - 30);
        container.setHeight(Window.getClientHeight() - 30);
        container.setStyleName(res.css().container());
        
        
        HorizontalPanel split = new HorizontalPanel();
        container.add(split);
        split.getElement().getStyle().setBackgroundColor("rgba(255,255,255,0.5)");        
        
        Label logolabel = new Label("evoKalc - an easy KaKs calculator");
        ContentPanel login = new ContentPanel();
        split.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        split.setSpacing(20);
        split.add(logolabel);
        split.add(login);  
        
        logolabel.setStylePrimaryName(res.css().logo_title());

        login.setHeadingText("Login with evolgenius account");
        //add login form to login panel
        
        VerticalLayoutContainer vertical = new VerticalLayoutContainer();
        login.add(vertical);
        
        //final TextField account = new TextField();
        //account.setAllowBlank(false);
        email.setEmptyText("Email Address");
        
        //final PasswordField password = new PasswordField();
        //password.setAllowBlank(false);        
        
        final CheckBox cbKeepLogin = new CheckBox();
        cbKeepLogin.setBoxLabel("Keep Signed In");
        
        Anchor aForgetPass = new Anchor();
        aForgetPass.setHTML("Forget password?");
        aForgetPass.addClickHandler(new ClickHandler(){
           @Override
           public void onClick(ClickEvent e){
               //show reset password window
               
           }           
        });

        TextButton btnLogin = new TextButton("Sign In");
        btnLogin.addSelectHandler(new SelectEvent.SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGN_IN));
                //to validate
                if (validate()) {
                    //to find account
                    Shared.RPC.findAccount(email.getValue(), new AsyncCallback<Account>() {
                        @Override
                        public void onSuccess(Account a) {
                            if (a != null) {
                                //account found
                                if (!a.getAccountStatus()){
                                    //account not activated
                                    Info.display("Error", "Your account is not activated");
                                    return;
                                }
                                //try to sign in
                                final String accountKey = Account.md5sum(email.getValue() + password.getValue());
                                Shared.RPC.signInAndBindSession(a, Shared.MY_SESSION, accountKey, new AsyncCallback<Session>() {
                                    @Override
                                    public void onSuccess(Session s) {
                                        if (s != null) {
                                            //login successful
                                            Shared.MY_SESSION = s;
                                            Info.display("Welcome", "Signed in as " + s.getAccount().getFullName());
                                            //dispatch sign in event
                                            if (cbKeepLogin.getValue()){
                                                Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGNED_IN, accountKey));
                                            }else{
                                                Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGNED_IN));
                                            }

                                        } else {
                                            Info.display("Incorrect", "Please check your password and try again");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable caught) {
                                        Info.display("Error", caught.getMessage());
                                    }
                                });
                            } else {
                                Info.display("Error", "account " + email.getCurrentValue() + " not found");
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
        
        //anonymous login function
        TextButton btnAnonymous = new TextButton("Use Anonymously");
        btnAnonymous.addSelectHandler(new SelectEvent.SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGN_IN));
                //anonymous account
                Shared.RPC.anonymousAccount(new AsyncCallback<Account>(){
                    @Override
                    public void onSuccess(Account a){
                        if (a != null){
                            //try to sign in
                            final String accountKey = a.getAccountKey();  //accountKey is send back directly in Account
                            Shared.RPC.signInAndBindSession(a, Shared.MY_SESSION, accountKey, new AsyncCallback<Session>(){
                                @Override
                                public void onSuccess(Session s){
                                    if (s != null) {
                                        //login successful
                                        Shared.MY_SESSION = s;
                                        //dispatch sign in event
                                        Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGNED_IN, accountKey));  //SignEvent with accoutKey enables cookie, auto sign in when refreshing
                                    } else {
                                        Info.display("Incorrect", "Anonymous may be disabled on server");                                        
                                    }
                                }
                                @Override
                                public void onFailure(Throwable caught){
                                    Info.display("Error", caught.getMessage());
                                }
                            });
                        }else{
                            Info.display("Error", "Anonymous login failed");
                        }
                    }
                    @Override
                    public void onFailure(Throwable caught){
                        Info.display("Error", caught.getMessage());
                    }
                });
                
            }
        });
        
        TextButton btnNewAccount = new TextButton("Create Account");
        final AccountEditor ae = new AccountEditor();
        //ae.setModal(true);
        btnNewAccount.addSelectHandler(new SelectEvent.SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                ae.show();
            }
        });
        
        vertical.add(new FieldLabel(email, "Account"), layout);
        vertical.add(new FieldLabel(password, "Password"), layout);
        
        HorizontalLayoutContainer cbHLC = new HorizontalLayoutContainer();
        cbHLC.add(cbKeepLogin, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, -1, new Margins(20,5,20,20)));
        cbHLC.add(aForgetPass, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, -1, new Margins(20,20,20,5)));
        vertical.add(cbHLC, new VerticalLayoutContainer.VerticalLayoutData(1, 60, new Margins(0)));
        
        //vertical.add(cbKeepLogin, layout);
        vertical.add(btnLogin, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0,20,0,20)));
        
        HorizontalLayoutContainer btnHLC = new HorizontalLayoutContainer();
        btnHLC.add(btnAnonymous, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, -1, new Margins(20,5,20,20)));
        btnHLC.add(btnNewAccount, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, -1, new Margins(20,20,20,5)));
        vertical.add(btnHLC, new VerticalLayoutContainer.VerticalLayoutData(1, 60, new Margins(0)));  
        
//        HorizontalPanel btnSplit = new HorizontalPanel();
//        btnSplit.setSpacing(20);
//        btnSplit.add(btnAnonymous);
//        btnSplit.add(btnNewAccount);
//        btnSplit.setCellHorizontalAlignment(btnAnonymous, HasHorizontalAlignment.ALIGN_LEFT);
//        btnSplit.setCellHorizontalAlignment(btnNewAccount, HasHorizontalAlignment.ALIGN_RIGHT);
//        vertical.add(btnSplit, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0)));        
        
        this.initWidget(container);
    }
    
    private boolean validate(){
        boolean validate = true;
        //force the balloon tips on top of me
        //email
        try{
        if (email.getValue() == null 
                || email.getValue().isEmpty() 
                || !email.getValue().matches("^\\w[\\w.]*@\\w+(\\.\\w+)+")){
            validate = false;
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);
            balloon.setWidgetToShow(new HTML("email address is empty or invalid"));
            balloon.showRelativeToWidget(email.getParent(), BalloonTips.TipPos.rightTo);
        }
        }catch(Exception ex){
            email.setText(ex.getMessage());
        }
        //Info.display("test"," to validate : " + validate);            
        if (password.getValue() == null
                || password.getValue().isEmpty()
                || !password.getValue().matches("\\w+")){
            validate = false;
            BalloonTips balloon = new BalloonTips(true, false);
            balloon.showOnTopOf(this);
            balloon.setWidgetToShow(new HTML("password is empty or invalid"));
            balloon.showRelativeToWidget(password.getParent(), BalloonTips.TipPos.rightTo);            
        }
    
        return validate;
    }
}
