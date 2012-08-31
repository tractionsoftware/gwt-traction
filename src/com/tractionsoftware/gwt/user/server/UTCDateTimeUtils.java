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
package com.tractionsoftware.gwt.user.server;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.tractionsoftware.gwt.user.client.ui.UTCDateBox;
import com.tractionsoftware.gwt.user.client.ui.UTCTimeBox;

/**
 * These utilities are intended for use on the server-side to process
 * the values of the {@link UTCDateBox} and {@link UTCTimeBox}.
 * 
 * @author andy
 */
public class UTCDateTimeUtils {

    /**
     * Returns an appropriate value for the UTCTimeBox for a specified
     * {@link TimeZone} and {@link Date}.
     * 
     * @param zone
     *            The {@link TimeZone} in which the Date will be
     *            rendered.
     * 
     * @param date
     *            The Date which should be displayed in the UTCTimeBox
     * 
     * @return the value for the UTCTimeBox or null if the supplied
     *         date is null
     */
    public static final Long getTimeBoxValue(TimeZone zone, Date date) {

        if (date == null) return null;

        // use a Calendar in the specified timezone to figure out the
        // time which is edited in a format independent of TimeZone.
        Calendar cal = GregorianCalendar.getInstance(zone);
        cal.setTime(date);

        // hh:mm (seconds and milliseconds are generally zero but we
        // include them as well)
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        int millis = cal.get(Calendar.MILLISECOND);

        return (((((hours * 60L) + minutes) * 60L) + seconds) * 1000L) + millis;
    }

    /**
     * Returns the value for the UTCDateBox for a specified
     * {@link TimeZone} and {@link Date}.
     * 
     * @param zone
     *            The {@link TimeZone} in which the Date will be
     *            rendered.
     * 
     * @param date
     *            The Date which should be displayed in the UTCTimeBox
     * 
     * @return the value for the UTCDateBox or null if the supplied date is null
     */
    public static final Long getDateBoxValue(TimeZone zone, Date date) {

        if (date == null) return null;

        // use a Calendar in the specified timezone to figure out the
        // date and then convert to GMT
        Calendar cal = GregorianCalendar.getInstance(zone);
        cal.setTime(date);

        Calendar gmt = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        
        // copy the year, month, and day
        gmt.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        
        // zero everything else out (for midnight)
        gmt.set(Calendar.HOUR_OF_DAY, 0);
        gmt.set(Calendar.MINUTE, 0);
        gmt.set(Calendar.SECOND, 0);
        gmt.set(Calendar.MILLISECOND, 0);

        // midnight at GMT on the date specified
        return gmt.getTimeInMillis();
    }

    /**
     * Returns the {@link Date} for the values of the UTCDateBox and
     * UTCTimeBox which were edited in the specified {@link TimeZone}.
     * 
     * @param zone
     *            The {@link TimeZone} in which the Date was edited.
     * @param dateBoxValue
     *            The value of the {@link UTCDateBox} control.
     * @param timeBoxValue
     *            The value of the {@link UTCTimeBox} control.
     * @return The {@link Date} that has been selected from the
     *         controls or null if the supplied dateBoxValue is null.
     *         If the timeBoxValue is null, midnight is returned.
     */
    public static final Date getDateValue(TimeZone zone, Long dateBoxValue, Long timeBoxValue) {

        if (dateBoxValue == null) return null;

        Calendar gmt = GregorianCalendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmt.setTimeInMillis(dateBoxValue.longValue());

        Calendar cal = GregorianCalendar.getInstance(zone);
        cal.set(gmt.get(Calendar.YEAR), gmt.get(Calendar.MONTH), gmt.get(Calendar.DAY_OF_MONTH));

        int hours, minutes, extraMillis;

        if (timeBoxValue != null) {
            // figure out how many hours and minutes to add to
            // midnight in the specified time zone.
            long localTimeInDay = timeBoxValue.longValue();

            // figure out if there are extra millis in the value
            // (there shoudn't be since the time box control doesn't
            // typically render millis)
            extraMillis = (int) (localTimeInDay % 60 * 1000);
            
            // trim off the seconds
            localTimeInDay -= extraMillis;

            minutes = (int) ((localTimeInDay / 60 / 1000) % 60);
            
            // trim off the minutes
            localTimeInDay -= minutes;

            hours = (int) (localTimeInDay / 60 / 60 / 1000);
        }
        else {
            // midnight
            hours = 0;
            minutes = 0;
            extraMillis = 0;
        }

        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new Date(cal.getTimeInMillis() + extraMillis);
    }
            
}
