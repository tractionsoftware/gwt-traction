/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * A combination of UTCDateBox and UTCTimeBox that can be used to
 * choose a time. The value is represented as the number of
 * milliseconds since January 1, 1970, 00:00:00 GMT.
 * 
 * <p>
 * Note: In keeping with the behavior of the GWT DateBox, null is used to
 * represent no value. This means that you should check for null when calling
 * getValue(), just as you would for DateBox. With auto-boxing Long/long, this
 * may seem strange but is consistent.
 */
public class UTCDateTimeControl extends Composite implements HasValue<Long>, HasValueChangeHandlers<Long> {

    private UTCDateBox date;
    private UTCTimeBox time;

    public UTCDateTimeControl() {
        this(new DateBox.DefaultFormat());
    }
    
    /**
     * The format is only used for date formatting. Currently the time
     * format is always based on {@link DateTimeFormat#getMediumTimeFormat()}.
     */
    public UTCDateTimeControl(DateTimeFormat format) {
        this(new DateBox.DefaultFormat(format));
    }
    
    public UTCDateTimeControl(DateBox.Format format) {
        date = new UTCDateBox(new DatePicker(), -1, format);
        time = new UTCTimeBox();

        // tie them together and listen for changes because changing
        // the date will affect the resulting time
        date.addValueChangeHandler(new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange(ValueChangeEvent<Long> event) {
                time.setReferenceDate(event.getValue());
                time.fireValueChangeEvent();
            }
        });
        
        FlowPanel panel = new FlowPanel();
        panel.add(date);
        panel.add(time);
        initWidget(panel);
    }
    
    public UTCDateBox getDateBox() {
        return date;
    }
    
    public UTCTimeBox getTimeBox() {
        return time;
    }
    
    public String getDateText() {
        return date.getDateBox().getTextBox().getValue();
    }
    public String getTimeText() {
        return time.getText();
    }
    
    // ----------------------------------------------------------------------
    // HasValue
    
    @Override
    public Long getValue() {
        Long ret = time.getValue();
        return (ret != null) ? ret : date.getValue();
    }

    @Override
    public void setValue(Long value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        date.setValue(time.getDateValue(value));
        time.setValue(value, fireEvents);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
        // rather than maintain our own listeners, we just forward them
        return time.addHandler(handler, ValueChangeEvent.getType());
    }

    // ----------------------------------------------------------------------
    // style
    
    public void setVisibleLength(int length) {
        date.getDateBox().getTextBox().setVisibleLength(length);
        time.getTextBox().setVisibleLength(length);
    }
}
