/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui;

import java.util.Date;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Time is represented as the number of milliseconds after midnight in
 * UTC relative to a specified reference date. It is meant to be used
 * with UTCDateBox. If you add the value of this control with that of
 * the UTCDateBox, you get a specific minute in time in UTC selected
 * from the user's time zone.
 * 
 * Here are some sample values for EDT (GMT-4):
 * 
 * 12:00 AM => 14400000 = 4 hours<br>
 * 01:00 AM => 18000000 = 5 hours<br>
 * 02:00 AM => 21600000 = 6 hours<br>
 * 03:00 AM => 25200000 = 7 hours<br>
 * 04:00 AM => 28800000 = 8 hours<br>
 * 05:00 AM => 32400000 = 9 hours<br>
 * 06:00 AM => 36000000 = 10 hours<br>
 * 07:00 AM => 39600000 = 11 hours<br>
 * 08:00 AM => 43200000 = 12 hours<br>
 * 09:00 AM => 46800000 = 13 hours<br>
 * 10:00 AM => 50400000 = 14 hours<br>
 * 11:00 AM => 54000000 = 15 hours<br>
 * 12:00 PM => 57600000 = 16 hours<br>
 * 01:00 PM => 61200000 = 17 hours<br>
 * 02:00 PM => 64800000 = 18 hours<br>
 * 03:00 PM => 68400000 = 19 hours<br>
 * 04:00 PM => 72000000 = 20 hours<br>
 * 05:00 PM => 75600000 = 21 hours<br>
 * 06:00 PM => 79200000 = 22 hours<br>
 * 07:00 PM => 82800000 = 23 hours<br>
 * 08:00 PM => 86400000 = 24 hours<br>
 * 09:00 PM => 90000000 = 25 hours<br>
 * 10:00 PM => 93600000 = 26 hours<br>
 * 11:00 PM => 97200000 = 27 hours<br>
 * 
 * Here are some sample values for JST (GMT+9):
 * 
 * 12:00 AM => -32400000 = -9 hours <br>
 * 01:00 AM => -28800000 = -8 hours <br>
 * 02:00 AM => -25200000 = -7 hours <br>
 * 03:00 AM => -21600000 = -6 hours <br>
 * 04:00 AM => -18000000 = -5 hours <br>
 * 05:00 AM => -14400000 = -4 hours <br>
 * 06:00 AM => -10800000 = -3 hours <br>
 * 07:00 AM => -7200000 = -2 hours <br>
 * 08:00 AM => -3600000 = -1 hours <br>
 * 09:00 AM => 0 = 0 hours <br>
 * 10:00 AM => 3600000 = 1 hours <br>
 * 11:00 AM => 7200000 = 2 hours <br>
 * 12:00 PM => 10800000 = 3 hours <br>
 * 01:00 PM => 14400000 = 4 hours <br>
 * 02:00 PM => 18000000 = 5 hours <br>
 * 03:00 PM => 21600000 = 6 hours <br>
 * 04:00 PM => 25200000 = 7 hours <br>
 * 05:00 PM => 28800000 = 8 hours <br>
 * 06:00 PM => 32400000 = 9 hours <br>
 * 07:00 PM => 36000000 = 10 hours <br>
 * 08:00 PM => 39600000 = 11 hours <br>
 * 09:00 PM => 43200000 = 12 hours <br>
 * 10:00 PM => 46800000 = 13 hours <br>
 * 11:00 PM => 50400000 = 14 hours <br>
 * 
 * The control supports an unspecified value of null with a blank textbox.
 * 
 * @author andy
 */
public class UTCTimeBox extends Composite implements HasValue<Long>, HasValueChangeHandlers<Long> {

    private TextBox textbox;
    private DateTimeFormat dateTimeFmt;
    private DateTimeFormat timeFmt;
    private DateTimeFormat dateFmt;
    
    private String referenceDateInShortFormat;
    private long referenceDateAtMidnight = 0;
    private long timeZoneOffsetMillis;
    
    public UTCTimeBox() {
        this.textbox = new TextBox();
        this.dateFmt = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT); 
        this.timeFmt = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
        this.dateTimeFmt = DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT);

        setReferenceDate(null);
        initWidget(textbox);
    }
    
    /**
     * Returns the TextBox on which this control is based.
     */
    public TextBox getTextBox() {
        return textbox;
    }

    // ----------------------------------------------------------------------
    // coordination with dates
    
    public Date getReferenceDate() {
        return new Date(referenceDateAtMidnight);
    }
    
    /**
     * Because time zones vary throughout the year (EST vs EDT), we
     * need a reference Date when parsing a time. This should be a
     * date at midnight in UTC.
     */
    public void setReferenceDate(Long referenceDate) {
        if (referenceDate == null) {
            referenceDate = new Date().getTime();
        }
        referenceDateAtMidnight = UTCDateBox.trimTimeToMidnight(referenceDate);
        referenceDateInShortFormat = dateFmt.format(new Date(referenceDateAtMidnight));
        timeZoneOffsetMillis = UTCDateBox.timezoneOffsetMillis(new Date(referenceDate));
    }

    // ----------------------------------------------------------------------
    // HasValue
    
    public boolean hasValue() { 
        return getText().trim().length() > 0;
    }
    
    /**
     * Returns a full date based on the time and the reference date.
     * 
     * @return A Long corresponding to the number of milliseconds since January
     *         1, 1970, 00:00:00 GMT or null if the specified time is null.
     */
    @Override
    public Long getValue() {
        return getValueFromText();
    }
    
    @Override
    public void setValue(Long value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        setReferenceDate(value);
        syncTextToValue(value);
        if (fireEvents) fireValueChangeEvent();
    }
    
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    protected void fireValueChangeEvent() {
        ValueChangeEvent.fire(this, getValue());        
    }
    
    // ----------------------------------------------------------------------
    // Interaction with the textbox
    
    public String getText() {
        return textbox.getValue();
    }
    
    public void setText(String text) {
        textbox.setValue(text);
    }

    // ----------------------------------------------------------------------
    // synchronization between text and value
    
    private void syncTextToValue(Long value) {
        if (value == null) {
            textbox.setValue("");
        }
        else {
            textbox.setValue(timeFmt.format(new Date(value)));
        }
    }
    
    private Long getValueFromText() {
        // need to update the parsed value
        if (!hasValue()) {
            return null;
        }
        else {
            String text = getText();            
            Date date = dateTimeFmt.parse(referenceDateInShortFormat + " " + text);
            return new Long(referenceDateAtMidnight + getTimeValue(date.getTime()));        
        }
    }    

    public Long getTimeValue() {
        Long ret = getValue();
        return ret != null ? getTimeValue(ret) : null;
    }
    
    public Long getTimeValue(Long value) {
        return normalizeInLocalRange(value.longValue());
    }
    
    public void setTimeValue(Long value) {
        if (value != null) {
            setValue(referenceDateAtMidnight + value);
        }
        else {
            setValue(null);
        }
    }

    private long normalizeInLocalRange(long time) {
        return ((time - timeZoneOffsetMillis) % UTCDateBox.DAY_IN_MS) + timeZoneOffsetMillis;
    }
        
}
