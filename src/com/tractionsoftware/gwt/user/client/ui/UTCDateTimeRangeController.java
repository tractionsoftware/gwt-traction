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

    private UTCDateBox startDate;
    private UTCTimeBox startTime;
    
    private UTCDateBox endDate;
    private UTCTimeBox endTime;
    
    private HasValue<Boolean> allDayCheckbox;
    
    // we need to keep track of interval because once the fields
    // change, it's too late to know what they were. note that the
    // interval is always measured in millis and may be greater than
    // DAY_IN_MS
    private long intervalMillis;
    
    private long defaultIntervalMillis = 60*60*1000L; // 1 hr
    
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
     */
    public UTCDateTimeRangeController(UTCDateBox startDate, UTCTimeBox startTime, UTCDateBox endDate, UTCTimeBox endTime) {
        this(startDate, startTime, endDate, endTime, null);
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
    public UTCDateTimeRangeController(UTCDateBox startDate, UTCTimeBox startTime, UTCDateBox endDate, UTCTimeBox endTime, HasValue<Boolean> allDayCheckbox) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.allDayCheckbox = allDayCheckbox;
        updateInterval();

        // attach the event handlers
        if (allDayCheckbox != null) {
            allDayCheckbox.addValueChangeHandler(new AllDayCheckboxHandler());
        }
        
        StartDateTimeHandler startHandler = new StartDateTimeHandler();
        EndDateTimeHandler endHandler = new EndDateTimeHandler();
        
        startDate.addValueChangeHandler(startHandler);
        startTime.addValueChangeHandler(startHandler);
        
        endDate.addValueChangeHandler(endHandler);
        endTime.addValueChangeHandler(endHandler);
    }

    /**
     * Returns the current default time interval
     */
    public long getDefaultIntervalMillis() {
        return defaultIntervalMillis;
    }

    /**
     * Sets the default time interval in milliseconds. By default this
     * is 60 minutes (60*60*1000).
     */
    public void setDefaultIntervalMillis(long defaultIntervalMillis) {
        this.defaultIntervalMillis = defaultIntervalMillis;
        updateInterval();
    }

    // ----------------------------------------------------------------------
    // values 
    
    /**
     * Returns true if the all day checkbox is checked
     */
    public boolean isAllDay() {
        return allDayCheckbox != null && allDayCheckbox.getValue();
    }
    
    // ----------------------------------------------------------------------
    // interval management
    
    private void updateInterval() {
        intervalMillis = getCombinedValue(endDate, endTime) - getCombinedValue(startDate, startTime);
        
        // if this is zero, most likely the times aren't set and the
        // dates are the same. in this case we don't really want a
        // zero interval. if we *really* want a zero interval, the
        // default would be zero so this is safe.
        if (intervalMillis == 0) {
            intervalMillis = defaultIntervalMillis;
        }
    }
    
    // ----------------------------------------------------------------------
    // handlers
    
    private class AllDayCheckboxHandler implements ValueChangeHandler<Boolean> {
        
        @Override
        public void onValueChange(ValueChangeEvent<Boolean> event) {
            boolean allDay = event.getValue();
            startTime.setVisible(!allDay);
            endTime.setVisible(!allDay);
        }
        
    }
    
    /**
     * When the start date changes, we want to push end date forward,
     * matching the existing interval.
     */
    private class StartDateTimeHandler implements ValueChangeHandler<Long> {

        @Override
        public void onValueChange(ValueChangeEvent<Long> event) {
            if (startTime.getValue() != null) {
                setCombinedValue(endDate, endTime, getCombinedValue(startDate, startTime) + intervalMillis, true);
            }
            else {
                setCombinedValue(endDate, endTime, getCombinedValue(startDate, startTime) + datePartMillis(intervalMillis), false);                
            }
        }
        
    }

    /**
     * When the end date changes, if the interval is improper (start >
     * end), we want to adjust the start backward, maintaining the
     * interval.
     */
    private class EndDateTimeHandler implements ValueChangeHandler<Long> {

        @Override
        public void onValueChange(ValueChangeEvent<Long> event) {
            long startCombined = getCombinedValue(startDate, startTime);
            long endCombined = getCombinedValue(endDate, endTime);
            if (isMissingStartDate() || isMissingOnlyStartTime() || (endCombined != 0 && startCombined > endCombined)) {
                if (endTime.getValue() != null) {
                    setCombinedValue(startDate, startTime, endCombined - intervalMillis, true);
                }
                else {
                    setCombinedValue(startDate, startTime, endCombined - datePartMillis(intervalMillis), false);                    
                }
            }
            else {
                updateInterval();
            }
        }

        private boolean isMissingStartDate() {
            return startDate.getValue() == null;
        }
        
        private boolean isMissingOnlyStartTime() {
            return startTime.getValue() == null && startDate.getValue() != null && endDate.getValue() != null && endTime.getValue() != null;
        }
        
    }
    
    /**
     * This allows us to treat a datetime as a single value. We don't
     * actually expose this because the timezone issues make it too
     * confusing to clients.
     */
    private long getCombinedValue(UTCDateBox date, UTCTimeBox time) {
        Long dateValue = date.getValue();
        Long timeValue = time.getValue();
    
        if (dateValue != null) {
            if (timeValue != null) {
                return dateValue + timeValue;
            }
            else {
                return dateValue;
            }
        }
        else {
            if (timeValue != null) {
                return timeValue;
            }
            else {
                return 0;
            }
        }
    }
    
    /**
     * This allows us to treat a datetime as a single value. We don't
     * actually expose this because the timezone issues make it too
     * confusing to clients.
     * 
     * @param setTimeValue
     *            Sometimes we don't want to set the time value
     *            explicitly. Generally this is the case when we
     *            haven't specified a time.
     */
    private void setCombinedValue(UTCDateBox date, UTCTimeBox time, long value, boolean setTimeValue) {
        date.setValue(datePartMillis(value), false);        
        if (setTimeValue) {
            time.setValue(timePartMillis(value), false);
        }
    }
    
    private long datePartMillis(long datetime) {
        return datetime - timePartMillis(datetime);
    }
    
    private long timePartMillis(long datetime) {
        return datetime % UTCDateBox.DAY_IN_MS;        
    }
    
}
