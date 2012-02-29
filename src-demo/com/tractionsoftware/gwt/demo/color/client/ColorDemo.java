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
package com.tractionsoftware.gwt.demo.color.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.tractionsoftware.gwt.user.client.animation.ColorAnimation;
import com.tractionsoftware.gwt.user.client.util.RgbaColor;

public class ColorDemo implements EntryPoint {

    private TextBox startColor;
    private TextBox endColor;
    private TextBox duration;

    @Override
    public void onModuleLoad() {
	
	Panel controls = RootPanel.get("controls");
	
	startColor = createTextBox("rgba(255,255,0,1)");
	endColor = createTextBox("rgba(255,0,255,0)");
	duration = createTextBox("5000");

	addTextBox(controls, "Start Color", startColor);
	addTextBox(controls, "End Color", endColor);
	addTextBox(controls, "Duration", duration);

	Button start = new Button("Start");
	start.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
	        ColorAnimation animation = new ColorAnimation(new Element[] {
								      Document.get().getElementById("box1"),
								      Document.get().getElementById("box2"),
								      Document.get().getElementById("box3")
								  },
								  "backgroundColor",
								  RgbaColor.from(startColor.getText()),
								  RgbaColor.from(endColor.getText()));
	        animation.run(Integer.parseInt(duration.getText()));
	    }
	});

	controls.add(start);
    }    

    private static final TextBox createTextBox(String text) {
	TextBox ret = new TextBox();
	ret.setVisibleLength(20);
	ret.setText(text);
	return ret;
    }
    
    private static final void addTextBox(Panel panel, String label, TextBox box) {
	panel.add(new Label(label));
	panel.add(box);
    }

}
