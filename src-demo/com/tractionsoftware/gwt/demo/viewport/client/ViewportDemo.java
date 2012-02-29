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
package com.tractionsoftware.gwt.demo.viewport.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;

import com.tractionsoftware.gwt.user.client.Viewport;

import java.util.Date;

public class ViewportDemo implements EntryPoint {

    private Label focusLabel;
    private ListBox eventListBox;

    @Override
    public void onModuleLoad() {
	
	focusLabel = new Label();
	RootPanel.get("hasfocus").add(focusLabel);

	eventListBox = new ListBox(true);
	eventListBox.setVisibleItemCount(20);
	RootPanel.get("eventlog").add(eventListBox);

	update();
	
	Viewport.get().addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                addEvent("FOCUS");
                update();
            }
	});

	Viewport.get().addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                addEvent("BLUR");
                update();
            }
	});
    }    

    private void addEvent(String event) {
	eventListBox.insertItem(new Date()+" - "+event, 0);
    }

    private void update() {
	boolean hasFocus = Viewport.hasFocus();
	String msg = (hasFocus ? "TRUE" : "FALSE") + " from Viewport.hasFocus()";
	focusLabel.setText(msg);
	Window.setTitle(msg);
    }

}
