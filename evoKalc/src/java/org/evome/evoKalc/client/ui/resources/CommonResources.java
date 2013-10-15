/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;



/**
 *
 * @author nekoko
 */
public interface CommonResources extends ClientBundle{
    @Source("org/evome/evoKalc/client/ui/resources/CommonCss.css")
    CssResource common_css();
    
    @Source("org/evome/evoKalc/client/ui/resources/Tulips.jpg")
    ImageResource bg_tulips();
}
