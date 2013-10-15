/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.evoKalc.client.ui;


import com.sencha.gxt.widget.core.client.Window;

import org.evome.evoKalc.client.shared.*;

/**
 * a floating window to show the kaks result
 *
 * @author nekoko
 * 
 * 
 * 
 */
public class ShowResult extends Window{
    
    private Task mytask;
    
    public ShowResult(Task t){
        mytask = t;
    }
}
