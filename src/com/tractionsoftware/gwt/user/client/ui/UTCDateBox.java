/*
 * Copyright 2010 Traction Software, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tractionsoftware.gwt.user.client.ui;

import java.util.Date;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * A wrapper around a DateBox that implements HasValue<Long> where the value is
 * the number of milliseconds since January 1, 1970, 00:00:00 GMT <b>at midnight on
 * the day, month, and year selected</b>. This avoids time zone conversion issues
 * encountered using the DateBox.
 */
public class UTCDateBox extends Composite implements HasValue<Long>, HasValueChangeHandlers<Long> {

    private DateBox datebox;
    
    public UTCDateBox() {
        init(new DateBox());
    }
    
    public UTCDateBox(DatePicker picker, long date, DateBox.Format format) {
        init(new DateBox(picker, utc2date(date), format));
    }
    
    private void init(DateBox datebox) {
        this.datebox = datebox;
        
        datebox.addValueChangeHandler(new ValueChangeHandler<Date>() {

            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                // pass this event onto our handlers after converting the value
                fireValueChangeEvent(date2utc(event.getValue()));
            }});
        
        initWidget(datebox);
    }
        
    /**
     * Provides access to the underlying DateBox. Beware using this directly
     * because anything that returns a Date might need to be adjusted to UTC
     * using date2utc.
     */
    public DateBox getDateBox() {
        return datebox;
    }

    // ----------------------------------------------------------------------
    // HasValue 
    
    public Long getValue() {
        return date2utc(datebox.getValue());
    }
    
    public void setValue(Long value) {
        setValue(value, false);
    }
    
    public void setValue(Long value, boolean fireEvents) {
        datebox.setValue(utc2date(value), fireEvents);
    }

    private void fireValueChangeEvent(long value) {
        ValueChangeEvent.fire(this, new Long(value));             
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    // ----------------------------------------------------------------------
    // conversion methods that convert to dates in UTC time
    
    private static final long DAY_IN_MS = 24L*60L*60L*1000L; 
    
    public static final long trimTimeToMidnight(long time) {
        // first trim to midnight
        return time - time % DAY_IN_MS;
    }
    
    /**
     * Converts a time in UTC to a gwt Date object which is in the timezone of
     * the current browser.
     */
    public static final Date utc2date(Long time) {

        // don't accept negative values
        if (time < 0) return null;
        
        // add the timezone offset
        time += timezoneOffsetMillis(new Date(time));

        return new Date(time);
    }

    /**
     * Converts a gwt Date in the timezone of the current browser to a time in UTC.
     */
    public static final Long date2utc(Date date) {

        // use -1 to mean a bogus date
        if (date == null) return null;
        
        long time = date.getTime();
        
        // remove the timezone offset        
        time -= timezoneOffsetMillis(date);
        
        return time;
    }
    
    /**
     * Returns the timezone offset for the specified Date.
     */
    @SuppressWarnings("deprecation")
    public static final long timezoneOffsetMillis(Date date) {
        return date.getTimezoneOffset()*60*1000;        
    }
    
}
