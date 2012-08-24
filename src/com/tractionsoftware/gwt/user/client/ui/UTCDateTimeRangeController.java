/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;

/**
 * The date time range controller just manages existing controls and
 * is not a control itself. It ensures that a range is consistent
 * (start < end) and provides some conveniences like automatically
 * adjusting the end time when the start time changes. These behaviors
 * are consistent with other date/time range controls in other
 * commonly used software.
 * 
 * @author andy
 */
public class UTCDateTimeRangeController {

    private UTCDateTimeControl start;
    private UTCDateTimeControl end;
    private HasValue<Boolean> allDayCheckbox;
    
    // we need to keep track of intervals because once the fields
    // change, it's too late to know what they were.
    private long dateIntervalMillis;
    private long timeIntervalMillis;
    
    private long defaultDateIntervalMillis = 0; // same start/end dates
    private long defaultTimeIntervalMillis = 60*60*1000L; // 1 hr 
    
    /**
     * Creates a controller that will manage the date/time range
     * consisting of 2 date/time controls and an optional
     * allDayCheckbox.
     * 
     * @param start
     *            The control for the start date/time
     * 
     * @param end
     *            The control for the end date/time
     * 
     * @param allDayCheckbox
     *            If specified, when the all day checkbox is checked,
     *            the time controls of the date/time controls will be
     *            hidden. null can be used to indicate that no all day
     *            checkbox is used.
     */
    public UTCDateTimeRangeController(UTCDateTimeControl start, UTCDateTimeControl end) {
        this(start, end, null);
    }

    /**
     * Creates a controller that will manage the date/time range
     * consisting of 2 date/time controls and an optional
     * allDayCheckbox.
     * 
     * @param start
     *            The control for the start date/time
     *            
     * @param end
     *            The control for the end date/time
     * 
     * @param allDayCheckbox
     *            If specified, when the all day checkbox is checked,
     *            the time controls of the date/time controls will be
     *            hidden.
     */
    public UTCDateTimeRangeController(UTCDateTimeControl start, UTCDateTimeControl end, HasValue<Boolean> allDayCheckbox) {
        this.start = start;
        this.end = end;
        this.allDayCheckbox = allDayCheckbox;
        updateIntervals();

        // attach the event handlers
        if (allDayCheckbox != null) {
            allDayCheckbox.addValueChangeHandler(new AllDayCheckboxHandler());
        }
        
        start.getDateBox().addValueChangeHandler(new StartDateHandler());
        start.getTimeBox().addValueChangeHandler(new StartTimeHandler());
        
        end.getDateBox().addValueChangeHandler(new EndDateHandler());
        end.getTimeBox().addValueChangeHandler(new EndTimeHandler());
    }

    /**
     * Returns the current default date interval
     */
    public long getDefaultDateIntervalMillis() {
        return defaultDateIntervalMillis;
    }

    /**
     * Sets the default date interval in milliseconds. By default this is 0.
     */
    public void setDefaultDateIntervalMillis(long defaultDateIntervalMillis) {
        this.defaultDateIntervalMillis = defaultDateIntervalMillis;
    }

    /**
     * Returns the current default time interval
     */
    public long getDefaultTimeIntervalMillis() {
        return defaultTimeIntervalMillis;
    }

    /**
     * Sets the default time interval in milliseconds. By default this
     * is 60 minutes (60*60*1000).
     */
    public void setDefaultTimeIntervalMillis(long defaultTimeIntervalMillis) {
        this.defaultTimeIntervalMillis = defaultTimeIntervalMillis;
    }

    // ----------------------------------------------------------------------
    // values 
    
    /**
     * Returns the value for the start field.
     */
    public Long getStartValue() {
        return getValue(start);
    }

    /**
     * Returns the value for the end field.
     */
    public Long getEndValue() {
        return getValue(end);
    }
    
    /**
     * Returns true if the all day checkbox is checked
     */
    public boolean isAllDay() {
        return allDayCheckbox != null && allDayCheckbox.getValue();
    }
    
    private Long getValue(UTCDateTimeControl control) {
        return isAllDay() ? control.getDateBox().getValue() : control.getTimeBox().getValue();        
    }
    
    // ----------------------------------------------------------------------
    // interval management
    
    private void updateIntervals() {
        dateIntervalMillis = getInterval(start.getDateBox(), end.getDateBox(), defaultDateIntervalMillis);
        timeIntervalMillis = getInterval(start, end, defaultTimeIntervalMillis);
    }
    
    /**
     * Determines the interval between two fields.
     * 
     * @param start
     *            The field with the start value.
     * @param end
     *            The field with the end value.
     * @param defaultInterval
     *            If either of the start or end values is unspecified,
     *            this will be used as a default.
     * @return The interval between the start and end valeus (end -
     *         start).
     */
    private long getInterval(HasValue<Long> start, HasValue<Long> end, long defaultInterval) {
        Long startValue = start.getValue();
        Long endValue = end.getValue();
        return (startValue != null && endValue != null) ? endValue - startValue : defaultInterval;
    }
    
    // ----------------------------------------------------------------------
    // handlers
    
    private class AllDayCheckboxHandler implements ValueChangeHandler<Boolean> {
        
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
            boolean allDay = event.getValue();
            start.getTimeBox().setVisible(!allDay);
            end.getTimeBox().setVisible(!allDay);
        }
        
    }
    
    /**
     * When the start date changes, we want to push end date forward,
     * matching the existing interval.
     */
    private class StartDateHandler implements ValueChangeHandler<Long> {

        @Override
        public void onValueChange(ValueChangeEvent<Long> event) {
            Long startValue = start.getDateBox().getValue();
            end.getDateBox().setValue(startValue + dateIntervalMillis);
        }
        
    }
    
    private class StartTimeHandler implements ValueChangeHandler<Long> {

        @Override
        public void onValueChange(ValueChangeEvent<Long> event) {
            // ignore time changes that flow through from date changes
            // when there is no time set.
            Long startValue = start.getTimeBox().getTimeValue();
            if (startValue != null) {
                end.setValue(start.getValue() + timeIntervalMillis);            
            }
        }
        
    }
    
    /**
     * When the end date changes, if the interval is improper (start >
     * end), we want to adjust the start backward, maintaining the
     * interval.
     */
    private class EndDateHandler implements ValueChangeHandler<Long> {

        @Override
        public void onValueChange(ValueChangeEvent<Long> event) {
            Long startValue = start.getDateBox().getValue();
            if (startValue == null || startValue > end.getDateBox().getValue()) {
                start.getDateBox().setValue(end.getDateBox().getValue() - dateIntervalMillis);
            }
            else {
                updateIntervals();
            }
        }
        
    }
    
    private class EndTimeHandler implements ValueChangeHandler<Long> {

        @Override
        public void onValueChange(ValueChangeEvent<Long> event) {
            Long startValue = start.getValue();
            if (startValue == null || startValue > end.getValue()) {
                start.setValue(end.getValue() - timeIntervalMillis);
            }
            else {
                updateIntervals();
            }
        }

        
    }
    
}
