/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

/**
 *
 * @author nekoko
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;



public interface UiResources extends ClientBundle{
    public static final UiResources INSTANCE = GWT.create(UiResources.class) ;
    
    public interface Style extends CssResource{
        String logo();
        String marginleft10();
        String marginright10();
        String headerbar();
        String margintop20();
        String showcaseevolviewslogan();
        String marginbottom();
        String greytext();
        String showcaseevolviewlinks();
        String showcasePanel();
        String signin();
        String txtBoxLoginPanel();
        String error();
        String textnormal();
        String htmllink();
        String signinpanel();
        String imageOnStripController();
        String accountsettingsSubheader();
        String texboxInToobar();
        String logosmall();
        String welcomeAtHeader();
        String verticalstripController();
        String imgbtnFirst();
        String logoutbutton();
        String imgbtnLast();
        String accountsettingsHeader();
        String headerbarlogout();
    }
    
    @Source("resources/css.css")
    Style style();
    
}
