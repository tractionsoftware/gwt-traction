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
package com.tractionsoftware.gwt.demo.singlelistbox.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

import com.tractionsoftware.gwt.user.client.ui.SingleListBox;

import java.util.Date;

public class SingleListBoxDemo implements EntryPoint {

    private ListBox eventListBox;
    private SingleListBox singleListBox;

    public void onModuleLoad() {
	
	eventListBox = new ListBox(true);
	eventListBox.setVisibleItemCount(20);
	RootPanel.get("eventlog").add(eventListBox);
	
	singleListBox = new SingleListBox();
	singleListBox.addItem("Apples");
	singleListBox.addItem("Bananas");
	singleListBox.addItem("Oranges");
	singleListBox.addItem("Pears");	
	singleListBox.addValueChangeHandler(new ValueChangeHandler<String>() {
		public void onValueChange(ValueChangeEvent<String> event) {
		    addEvent("ValueChangeEvent: "+event.getValue());
		}
	    });
	Panel select = RootPanel.get("select");
	select.add(singleListBox);

	Panel toggle = RootPanel.get("toggle");
	toggle.add(createSetAddMissingValue(true));
	toggle.add(createSetAddMissingValue(false));
	
	// create a dialog that we'll reuse
	Panel controls1 = RootPanel.get("controls1");
	controls1.add(createSelectButton("Bananas"));
	controls1.add(createSelectButton("Pears"));

	Panel controls2 = RootPanel.get("controls2");
	controls2.add(createSelectButton("Kiwis"));
	controls2.add(createSelectButton("Watermelons"));
    }    

    private void addEvent(String event) {
	eventListBox.insertItem(new Date()+" - "+event, 0);
    }

    private Button createSelectButton(final String value) {
	Button ret = new Button("setValue(\""+value+"\",true)");
	ret.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {
		    singleListBox.setValue(value, true);
		}
	    });
	return ret;
    }

    private Button createSetAddMissingValue(final boolean addMissingValue) {
	Button ret = new Button("setAddMissingValue("+addMissingValue+")");
	ret.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {
		    singleListBox.setAddMissingValue(addMissingValue);
		}
	    });
	return ret;
    }
    
}
