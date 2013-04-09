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
public class CalculationEdit extends Composite {
    
    private static CalculationEditUiBinder uiBinder = GWT.create(CalculationEditUiBinder.class);
    
    interface CalculationEditUiBinder extends UiBinder<Widget, CalculationEdit> {
    }
    
    public CalculationEdit() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
