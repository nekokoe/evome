/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Anchor;

import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import org.evome.evoKalc.client.shared.Shared;
import org.evome.evoKalc.client.shared.Task;

import java.util.ArrayList;
import org.evome.evoKalc.client.event.ViewChangeEvent;
import org.evome.evoKalc.client.event.SignEvent;
import org.evome.evoKalc.client.event.TaskChangeEvent;
       

/**
 *
 * @author nekoko
 */
public class Workspace implements IsWidget{
    
    //to build workspace framework
    @Override
    public Widget asWidget(){
        
        CenterLayoutContainer center = new CenterLayoutContainer();
        center.setWidth(Window.getClientWidth() - 50);
        center.setHeight(Window.getClientHeight() - 50);
        
        BorderLayoutContainer conlayout = new BorderLayoutContainer();    //final becasue: event responce
        center.add(conlayout);
        conlayout.setBorders(true);
        //size fixed now
        conlayout.setHeight(600);
        conlayout.setWidth(800);
        
        //quick tool bar on the top, quick start a new calculation task
//        HorizontalLayoutContainer headbar = new HorizontalLayoutContainer();
        VerticalPanel headbar = new VerticalPanel();
        HorizontalPanel head_top = new HorizontalPanel();
        HorizontalPanel head_bottom = new HorizontalPanel();
        headbar.add(head_top);
        headbar.add(head_bottom);
        
        Label headTitle = new Label("evolgenius - evolKalc");    
        
        //sign out button
        TextButton btnSignout = new TextButton("Sign out");
        btnSignout.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                //confirm before signing out
                ConfirmMessageBox confirm =
                        new ConfirmMessageBox("Confirm", "Are you sure want to sign out? (If you are using anonymously, ALL DATA WILL BE LOST!!!)");
                confirm.addHideHandler(new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        MessageBox source = (MessageBox) event.getSource();
                        if (source.getHideButton() == source.getButtonById(Dialog.PredefinedButton.YES.name())) {
                            //directly dispatch sign out event
                            Shared.EVENT_BUS.fireEvent(new SignEvent(SignEvent.Action.SIGN_OUT));
                        }
                    }
                });
                confirm.show();
            }
        });
        
        head_top.setWidth("100%");
        head_top.add(headTitle);
        head_top.add(btnSignout);
        head_top.setCellHorizontalAlignment(headTitle, HasHorizontalAlignment.ALIGN_LEFT);
        head_top.setCellHorizontalAlignment(btnSignout, HasHorizontalAlignment.ALIGN_RIGHT);
        
        TextButton btnNewCalc = new TextButton("Quick Start A New Calculation");
        btnNewCalc.addSelectHandler(new SelectEvent.SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                TaskWizard taskwiz = new TaskWizard();
                taskwiz.show();
            }
        });
        
        
        Anchor aSignedAs = new Anchor();
        aSignedAs.setHTML("Signed in as " + Shared.MY_SESSION.getAccount().getFullName());
        
        head_bottom.setWidth("100%");
        head_bottom.add(btnNewCalc);      
        head_bottom.add(aSignedAs);
        head_bottom.setCellHorizontalAlignment(btnNewCalc, HasHorizontalAlignment.ALIGN_LEFT);
        head_bottom.setCellHorizontalAlignment(aSignedAs, HasHorizontalAlignment.ALIGN_RIGHT);
        
        conlayout.setNorthWidget(headbar, new BorderLayoutData(50){{
            setMargins(new Margins(10));
        }});
        
        //accord selector on the left, providing tasks/configs/settings
        AccordionLayoutContainer accord = new AccordionLayoutContainer();
        accord.setExpandMode(AccordionLayoutContainer.ExpandMode.SINGLE_FILL);
        AccordionLayoutAppearance appearance = GWT.create(AccordionLayoutAppearance.class);
        //tasks
        ContentPanel cpTask = new ContentPanel(appearance);
        cpTask.setHeadingText("My Tasks");
        cpTask.add(new AccordionTask());
        //data
        ContentPanel cpData = new ContentPanel(appearance);
        cpData.setHeadingText("My Data");
        //param
        ContentPanel cpParam = new ContentPanel(appearance);
        cpParam.setHeadingText("My Param");
        cpParam.setToolTip("KaKs Parameters Manager");
        //settings
        ContentPanel cpSetting = new ContentPanel(appearance);
        cpSetting.setHeadingText("Account Settings");
        //help
        ContentPanel cpHelp = new ContentPanel(appearance);
        cpHelp.setHeadingText("How to Use?");
        //add to accord
        accord.add(cpTask);
        accord.add(cpData);
        accord.add(cpParam);
        accord.add(cpSetting);
        accord.add(cpHelp);
        accord.setActiveWidget(cpTask);
        
        BorderLayoutData westLayout = new BorderLayoutData(150);
        conlayout.setWestWidget(accord, westLayout);
        
        //blank simple container on the right
        final ContentPanel content = new ContentPanel();
        MarginData centerData = new MarginData();
        conlayout.setCenterWidget(content, centerData);

        //resiger event response for : ContentChangeEvent
        Shared.EVENT_BUS.addHandler(ViewChangeEvent.TYPE, new ViewChangeEvent.ContentChangeHandler() {

            @Override
            public void onContentChange(ViewChangeEvent event) {                
                content.clear();
                content.add(event.getWidget());
            }
        });
        
        
        
        return center;
    }
}
