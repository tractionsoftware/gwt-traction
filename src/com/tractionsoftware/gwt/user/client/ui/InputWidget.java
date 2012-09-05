/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

 ****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasValue;

/**
 * @author andy
 * 
 */
public class InputWidget extends FocusWidget implements HasValue<String> {

    private String lastValue = null;

    public InputWidget(String type) {
        super(DOM.createElement("input"));
        getElement().setAttribute("type", type);

        // fire a change event on change or blur
        addDomHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                fireValueChangeHandler(getValue());
            }
        }, ChangeEvent.getType());

        addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                fireValueChangeHandler(getValue());
            }
        });
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public String getValue() {
        return DOM.getElementProperty(getElement(), "value");
    }

    @Override
    public void setValue(String value) {
        setValue(value, false);
    }

    @Override
    public void setValue(String value, boolean fireEvents) {
        if (value == null) value = ""; 
        DOM.setElementProperty(getElement(), "value", value);
        if (fireEvents) {
            fireValueChangeHandler(value);
        }
        else {
            // we still want to keep track of changes to the value
            lastValue = value;
        }
    }

    private void fireValueChangeHandler(String value) {
        ValueChangeEvent.fireIfNotEqual(this, lastValue, value);
        lastValue = value;
    }

}
