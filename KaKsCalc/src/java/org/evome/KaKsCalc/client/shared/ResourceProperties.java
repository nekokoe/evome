/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import org.evome.KaKsCalc.client.Resource;

/**
 *
 * @author nekoko
 */
public interface ResourceProperties extends PropertyAccess<Resource> {

    @Path("UUID")
    ModelKeyProvider<Resource> key();

    ValueProvider<Resource, String> name();

    @Path("name")
    LabelProvider<Resource> label();
}