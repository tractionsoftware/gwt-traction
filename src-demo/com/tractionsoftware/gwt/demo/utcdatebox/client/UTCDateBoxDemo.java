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
package com.tractionsoftware.gwt.demo.utcdatebox.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.tractionsoftware.gwt.user.client.ui.UTCDateBox;

public class UTCDateBoxDemo implements EntryPoint {

    private ListBox eventListBox;
    private DateBox datebox;
    private UTCDateBox utcdatebox;

    @Override
    public void onModuleLoad() {

        eventListBox = new ListBox(true);
        eventListBox.setVisibleItemCount(20);
        eventListBox.setWidth("800px");
        RootPanel.get("eventlog").add(eventListBox);

        datebox = new DateBox(new DatePicker(), null, new DateBox.DefaultFormat(DateTimeFormat.getFormat("MMM dd, yyyy")));
        datebox.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                addEvent("DateBox", event.getValue(), event.getValue().getTime());
            }
        });
        RootPanel.get("datebox").add(datebox);

        utcdatebox = new UTCDateBox();
        utcdatebox.addValueChangeHandler(new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange(ValueChangeEvent<Long> event) {
                addEvent("UTCDateBox", new Date(event.getValue()), event.getValue());
            }
        });
        RootPanel.get("utcdatebox").add(utcdatebox);
    }    

    private void addEvent(String name, Date date, long time) {
	eventListBox.insertItem("("+name+") ValueChangeEvent: [date="+date+"] [milliseconds="+time+"]", 0);
    }
}
