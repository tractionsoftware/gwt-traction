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
package com.tractionsoftware.gwt.demo.utctimebox.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.tractionsoftware.gwt.user.client.ui.UTCTimeBox;

public class UTCTimeBoxDemo implements EntryPoint {

    private ListBox eventListBox;
    private UTCTimeBox timebox;

    @Override
    public void onModuleLoad() {

        eventListBox = new ListBox(true);
        eventListBox.setVisibleItemCount(20);
        eventListBox.setWidth("800px");
        RootPanel.get("eventlog").add(eventListBox);

        timebox = new UTCTimeBox(DateTimeFormat.getFormat("hh:mm a"));
        timebox.addValueChangeHandler(new ValueChangeHandler<Long>() {
            @Override
            public void onValueChange(ValueChangeEvent<Long> event) {
                addEvent("UTCTimeBox", event.getValue());
            }
        });
        timebox.setValue(UTCTimeBox.getValueForNextHour());
        
        RootPanel.get("utctimebox").add(timebox);
    }    

    private void addEvent(String name, Long time) {
	eventListBox.insertItem("("+name+") ValueChangeEvent: [time="+time+"]", 0);
    }
}
