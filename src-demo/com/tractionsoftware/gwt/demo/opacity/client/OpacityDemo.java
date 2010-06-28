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
package com.tractionsoftware.gwt.demo.opacity.client;

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

import com.tractionsoftware.gwt.user.client.animation.OpacityAnimation;

public class OpacityDemo implements EntryPoint {

    private TextBox startOpacity;
    private TextBox endOpacity;
    private TextBox duration;

    public void onModuleLoad() {
	
	Panel controls = RootPanel.get("controls");
	
	startOpacity = createTextBox("1.0");
	endOpacity = createTextBox("0.1");
	duration = createTextBox("5000");

	addTextBox(controls, "Start Opacity", startOpacity);
	addTextBox(controls, "End Opacity", endOpacity);
	addTextBox(controls, "Duration", duration);

	Button start = new Button("Start");
	start.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {
		    OpacityAnimation animation = new OpacityAnimation(new Element[] {
								      Document.get().getElementById("box1"),
								      Document.get().getElementById("box2"),
								      Document.get().getElementById("box3")
								  },
								  Float.parseFloat(startOpacity.getText()),
								  Float.parseFloat(endOpacity.getText()));
		    animation.run(Integer.parseInt(duration.getText()));
		}
	    });

	controls.add(start);
    }    

    private static final TextBox createTextBox(String text) {
	TextBox ret = new TextBox();
	ret.setVisibleLength(10);
	ret.setText(text);
	return ret;
    }
    
    private static final void addTextBox(Panel panel, String label, TextBox box) {
	panel.add(new Label(label));
	panel.add(box);
    }

}
