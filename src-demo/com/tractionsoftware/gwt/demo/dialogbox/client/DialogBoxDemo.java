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
package com.tractionsoftware.gwt.demo.dialogbox.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

import com.tractionsoftware.gwt.user.client.ui.TractionDialogBox;

import java.util.Date;

public class DialogBoxDemo implements EntryPoint {

    private Button open;
    private ListBox eventListBox;
    private TractionDialogBox dialog;

    public void onModuleLoad() {
	
	eventListBox = new ListBox(true);
	eventListBox.setVisibleItemCount(20);
	RootPanel.get("eventlog").add(eventListBox);
	
	// create a dialog that we'll reuse
	dialog = new TractionDialogBox(false, false, true);
	dialog.setText("Example Dialog Box");
	dialog.setGlassEnabled(true);
	dialog.setWidget(new HTML("Dialog contents go here!<br>Click the (X) icon to close."));
	dialog.addOpenHandler(new OpenHandler() {
		public void onOpen(OpenEvent event) {
		    addEvent("OPEN");
		}
	    });

	open = new Button("Open Dialog Box");
	open.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {
		    dialog.center();
		}
	    });
	RootPanel.get("openbutton").add(open);

    }    

    private void addEvent(String event) {
	eventListBox.insertItem(new Date()+" - "+event, 0);
    }

}
