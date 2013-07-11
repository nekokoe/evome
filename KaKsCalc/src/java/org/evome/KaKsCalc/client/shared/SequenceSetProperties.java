/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.evome.KaKsCalc.client.shared;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 *
 * @author nekoko
 */
public interface SequenceSetProperties extends PropertyAccess<SequenceSet> {

    ModelKeyProvider<SequenceSet> key();

    ValueProvider<SequenceSet, String> name();

    @Editor.Path("name")
    LabelProvider<SequenceSet> label();
}
