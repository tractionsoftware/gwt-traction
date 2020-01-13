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
 * The date/time range controller just manages existing controls and
 * is not a control itself. It ensures that a range is consistent
 * (start < end) and provides some conveniences like automatically
 * adjusting the end time when the start time changes. These behaviors
 * are consistent with other date/time range controls in other
 * commonly used software.
 *
 * @author andy
 */
public class UTCDateTimeRangeController {

    /**
     * This allows us to treat a datetime as a single value, making it
     * easy for comparison and adjustment. We don't actually expose
     * this because the timezone issues make it too confusing to
     * clients.
     */
    private static long getCombinedValue(UTCDateBox date, UTCTimeBox time) {

        Long dateValue = date.getValue();
        Long timeValue = time.getValue();

        if (dateValue != null) {
            if (timeValue != null) {
                return dateValue + timeValue;
            }
            return dateValue;
        }

        if (timeValue != null) {
            return timeValue;
        }
        return 0;

    }

    /**
     * Sets the "combined" date and time value by modifying the values
     * of the given UTCDateBox and UTCTimeBox controls.
     *
     * <p>
     * This makes it easy to treat a date/time composite as a single
     * value for comparison and adjustment purposes. We don't actually
     * expose this because the time zone and other issues would be
     * confusing for clients.
     *
     * @implNote Even if both controls' values are modified, only one
     *           {@link ValueChangeEvent} is fired: if the date value
     *           is modified, the ValueChangeEvent will be fired for
     *           that control; and if the time value is modified, the
     *           ValueChangeEvent will be fired for that control
     *           instead.
     *
     * @param dateControl
     *            the UTCDateBox field control that handles the date
     *            part of the date/time value being set.
     * @param timeControl
     *            the UTCTimeBox field control that handles the time
     *            part of the date/time value being set, if any.
     */
    private static void setCombinedValue(UTCDateBox dateControl, UTCTimeBox timeControl, long newValue) {
        boolean setDate = setDateValue(dateControl, newValue, false);
        boolean setTime = setTimeValue(timeControl, newValue, false);
        // Fire only one of these events to publish the change to
        // listeners. Otherwise, event listener loops may result.
        if (setDate) {
            ValueChangeEvent.fire(dateControl, dateControl.getValue());
        }
        else if (setTime) {
            ValueChangeEvent.fire(timeControl, timeControl.getValue());
        }
    }

    private static boolean setDateValue(UTCDateBox dateControl, long newValue, boolean fireEvent) {
        long newDateValue = datePartMillis(newValue);
        Long currentDateValue = dateControl.getValue();
        if (currentDateValue == null || currentDateValue.longValue() != newDateValue) {
            dateControl.setValue(newDateValue, fireEvent);
            return true;
        }
        return false;
    }

    private static boolean setTimeValue(UTCTimeBox timeControl, long newValue, boolean fireEvent) {
        long newTimeValue = timePartMillis(newValue);
        Long currentTimeValue = timeControl.getValue();
        if (currentTimeValue == null || currentTimeValue.longValue() != newTimeValue) {
            timeControl.setValue(newTimeValue, fireEvent);
            return true;
        }
        return false;
    }

    private static long datePartMillis(long datetime) {
        return datetime - timePartMillis(datetime);
    }

    private static long timePartMillis(long datetime) {
        return datetime % UTCDateBox.DAY_IN_MS;
    }

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
     */
    public UTCDateTimeRangeController(UTCDateBox startDate, UTCTimeBox startTime, UTCDateBox endDate, UTCTimeBox endTime) {
        this(startDate, startTime, endDate, endTime, null);
    }

    /**
     * Creates a controller that will manage the date/time range
     * consisting of 2 date/time controls and an optional
     * allDayCheckbox.
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
     * is 60 minutes (60*60*1000). The is the default between the
     * start and end date/time.
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
    // ValueChangeHandlers

    private final class AllDayCheckboxHandler implements ValueChangeHandler<Boolean> {

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
    private final class StartDateTimeHandler implements ValueChangeHandler<Long> {

        @Override
        public void onValueChange(ValueChangeEvent<Long> event) {
            if (startTime.getValue() != null) {
                long newCombinedValue = getCombinedValue(startDate, startTime) + intervalMillis;
                setCombinedValue(endDate, endTime, newCombinedValue);
            }
            else {
                long newCombinedValue = getCombinedValue(startDate, startTime) + datePartMillis(intervalMillis);
                setDateValue(endDate, newCombinedValue, true);
            }
        }

    }

    /**
     * When the end date changes, if the interval is improper (start >
     * end), we want to adjust the start backward, maintaining the
     * interval.
     */
    private final class EndDateTimeHandler implements ValueChangeHandler<Long> {

        @Override
        public void onValueChange(ValueChangeEvent<Long> event) {

            long startCombined = getCombinedValue(startDate, startTime);
            long endCombined = getCombinedValue(endDate, endTime);

            if (isMissingStartDate() || isMissingOnlyStartTime() || (endCombined != 0 && startCombined > endCombined)) {
                if (endTime.getValue() != null) {
                    long newCombinedValue = endCombined - intervalMillis;
                    setCombinedValue(startDate, startTime, newCombinedValue);
                }
                else {
                    long newCombinedValue = endCombined - datePartMillis(intervalMillis);
                    setDateValue(startDate, newCombinedValue, true);
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

}
