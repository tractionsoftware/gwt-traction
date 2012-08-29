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
package com.tractionsoftware.gwt.demo.utcdatetime.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.tractionsoftware.gwt.user.client.ui.UTCDateBox;
import com.tractionsoftware.gwt.user.client.ui.UTCDateTimeRangeController;
import com.tractionsoftware.gwt.user.client.ui.UTCTimeBox;

public class UTCDateTimeDemo implements EntryPoint {

    private ListBox eventListBox;
    
    private UTCTimeBox startTime;
    private UTCDateBox startDate;

    private UTCTimeBox endTime;
    private UTCDateBox endDate;
    
    private CheckBox allday;

    @Override
    public void onModuleLoad() {

        eventListBox = new ListBox(true);
        eventListBox.setVisibleItemCount(20);
        eventListBox.setWidth("800px");
        RootPanel.get("eventlog").add(eventListBox);

        startDate = createDateBox("start-date");
        startTime = createTimeBox("start-time");

        endDate = createDateBox("end-date");
        endTime = createTimeBox("end-time");
        
        allday = new CheckBox("All Day");
        
        // constructing this will bind all of the events
        new UTCDateTimeRangeController(startDate, startTime, endDate, endTime, allday);
        
        RootPanel startPanel = RootPanel.get("start");
        startPanel.add(startDate);
        startPanel.add(startTime);
        startPanel.add(allday);

        RootPanel endPanel = RootPanel.get("end");
        endPanel.add(endDate);
        endPanel.add(endTime);
        
        startDate.setValue(UTCDateBox.getValueForToday());
        startTime.setValue(UTCTimeBox.getValueForNextHour());
    }    

    private UTCDateBox createDateBox(final String name) {
        UTCDateBox datebox = new UTCDateBox();
        datebox.addValueChangeHandler(new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange(ValueChangeEvent<Long> event) {
                Long value = event.getValue();
                if (value != null) {
                    Date date = new Date(value);
                    long time = date.getTime();
                    eventListBox.insertItem("(" + name + ") ValueChangeEvent: [date=" + date + "] [milliseconds=" + time + "]", 0);
                }
                else {
                    eventListBox.insertItem("(" + name + ") ValueChangeEvent: [date=null]", 0);
                }
            }
        });        
        return datebox;
    }
    
    private UTCTimeBox createTimeBox(final String name) {
        UTCTimeBox timebox = new UTCTimeBox();
        timebox.addValueChangeHandler(new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange(ValueChangeEvent<Long> event) {
                Long value = event.getValue();
                if (value != null) {
                    eventListBox.insertItem("(" + name + ") ValueChangeEvent: [time=" + value + "]", 0);
                }
                else {
                    eventListBox.insertItem("(" + name + ") ValueChangeEvent: [time=null]", 0);
                }
            }
        });        
        return timebox;
    }
    
}
