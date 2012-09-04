/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.tractionsoftware.gwt.user.client.ui.impl.UTCTimeBoxImpl;

/**
 * @author andy
 */
public class UTCTimeBox extends Composite implements HasValue<Long>, HasValueChangeHandlers<Long>, HasText {

    public UTCTimeBoxImpl impl;
    
    /**
     * By default the predefined SHORT time format will be used.
     */
    public UTCTimeBox() {
        this(DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT));
    }
    
    /**
     * Allows a UTCTimeBox to be created with a specified format.
     */
    public UTCTimeBox(DateTimeFormat timeFormat) {
        // used deferred binding for the implementation
        impl = GWT.create(UTCTimeBoxImpl.class);
        impl.setTimeFormat(timeFormat);
        initWidget(impl.asWidget());
    }
    
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
        return impl.addValueChangeHandler(handler);
    }

    @Override
    public Long getValue() {
        return impl.getValue();
    }

    @Override
    public void setValue(Long value) {
        impl.setValue(value);
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        impl.setValue(value, fireEvents);
    }

    @Override
    public String getText() {
        return impl.getText();
    }

    @Override
    public void setText(String text) {
        impl.setText(text);
    }

    /**
     * The HTML5 implementation will ignore this.
     */
    public void setVisibleLength(int length) {
        impl.setVisibleLength(length);
    }

    public void setTabIndex(int tabIndex) {
        impl.setTabIndex(tabIndex);
    }
    
    /**
     * If this is a text based control, it will validate the value
     * that has been typed.
     */
    public void validate() {
        impl.validate();
    }
    
    // ----------------------------------------------------------------------
    // utils

    public static final Long getValueForNextHour() {
        Date date = new Date();
        long value = UTCDateBox.date2utc(date);

        // remove anything after an hour and add an hour
        long hour = 60 * 60 * 1000;
        value = value % UTCDateBox.DAY_IN_MS;
        return value - (value % hour) + hour;
    }

    
}
