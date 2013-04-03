/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author nekoko
 */
public class CalculationUtils extends Composite {
    
    private static CalculationUtilsUiBinder uiBinder = GWT.create(CalculationUtilsUiBinder.class);
    
    interface CalculationUtilsUiBinder extends UiBinder<Widget, CalculationUtils> {
    }
    
    public CalculationUtils() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
