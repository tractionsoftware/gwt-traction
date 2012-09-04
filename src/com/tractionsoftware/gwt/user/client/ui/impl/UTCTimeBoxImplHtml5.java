/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui.impl;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * Uses an HTML5 input type=time control to implement the UTCTimeBox
 * 
 * @author andy
 */
public class UTCTimeBoxImplHtml5 extends UTCTimeBoxImplShared {

    private class TimeInputWidget extends FocusWidget implements HasValue<Long> {
        
        private Long lastValue = null;
        
        public TimeInputWidget() {
            super(DOM.createElement("input"));
            getElement().setAttribute("type", "time");

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
        public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
            return addHandler(handler, ValueChangeEvent.getType());
        }

        @Override
        public Long getValue() {
            String value = DOM.getElementProperty(getElement(), "value");
            return string2long(value);
        }

        @Override
        public void setValue(Long value) {
            setValue(value, false);
        }

        @Override
        public void setValue(Long value, boolean fireEvents) {
            DOM.setElementProperty(getElement(), "value", long2string(value));
            if (fireEvents) {
                ValueChangeEvent.fire(this, value);
            }
            else {
                // we still want to keep track of changes to the value
                lastValue = value;
            }
        }

        private void fireValueChangeHandler(Long value) {
            ValueChangeEvent.fireIfNotEqual(this, lastValue, value);
            lastValue = value;
        }
        
        private native void addInputHandler(Element elm) /*-{
            elm.oninput = this.@com.tractionsoftware.gwt.user.client.ui.impl.UTCTimeBoxImplHtml5::fire();
        }-*/;
        
    }   

    public void fire() {
        widget.fireValueChangeHandler(widget.getValue());
    }
    
    // ----------------------------------------------------------------------
    
    private static final DateTimeFormat timeInputFormat = DateTimeFormat.getFormat("HH:mm");
    private TimeInputWidget widget;
    
    public UTCTimeBoxImplHtml5() {
        widget = new TimeInputWidget();
        setTimeFormat(timeInputFormat);
        System.err.println("Created UTCTimeBoxImplHtml5");
    }
    
    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public Long getValue() {
        return widget.getValue();
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        widget.setValue(value, fireEvents);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Long> handler) {
        return widget.addValueChangeHandler(handler);
    }

    public void fireValueChangeEvent(Long value) {
        ValueChangeEvent.fire(this, value);        
    }
    
    @Override
    public void fireEvent(GwtEvent<?> event) {
        widget.fireEvent(event);
    }

    @Override
    public String getText() {
        return value2text(getValue());
    }

    @Override
    public void setText(String text) {
        setValue(text2value(text), true);
    }

    @Override
    public void setTabIndex(int tabIndex) {
        widget.setTabIndex(tabIndex);
    }
    
    // ----------------------------------------------------------------------
    // the core translation methods of this class using the form HH:mm

    // we only obey hh:mm
    private Long string2long(String value) {
        return parseUsingFormat(value, timeInputFormat);
    }
    
    private String long2string(Long value) {
        return formatUsingFormat(value, timeInputFormat);
    }    
    
}
 