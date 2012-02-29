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
package com.tractionsoftware.gwt.demo.groupedlistbox.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tractionsoftware.gwt.user.client.ui.GroupedListBox;

public class GroupedListBoxDemo implements EntryPoint {

    private GroupedListBox groupedListBox1;
    private GroupedListBox groupedListBox2;    

    @Override
    public void onModuleLoad() {
	
	groupedListBox1 = new GroupedListBox(false);
	groupedListBox2 = new GroupedListBox(true);
	RootPanel.get("select1").add(groupedListBox1);
	RootPanel.get("select2").add(groupedListBox2);

	addItem("Fruits|Apples");
	addItem("Fruits|Bananas");
	addItem("Fruits|Oranges");
	addItem("Fruits|Pears");	
	addItem("Vegetables|Tomatoes");	
	addItem("Vegetables|Carrots");		
	
	Panel controls = RootPanel.get("controls");
	controls.add(createAddButton("Fruits|Blueberries"));
	controls.add(createAddButton("Vegetables|Broccoli"));
	controls.add(createAddButton("Meats|Chicken"));
	controls.add(createAddButton("Meats|Turkey"));
	
	Button remove = new Button("Remove Selected");
	remove.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                removeSelected();
            }
        });
	
	controls.add(remove);
    }    

    private void addItem(String item) {
        groupedListBox1.addItem(item);
        groupedListBox2.addItem(item);
    }
    
    private void setValue(String item) {
        groupedListBox1.setValue(item);
        groupedListBox2.setValue(item);
    }
    
    private void removeSelected() {
        int index;
        
        index = groupedListBox1.getSelectedIndex();
        if (index >= 0) groupedListBox1.removeItem(index);

        index = groupedListBox2.getSelectedIndex();
        if (index >= 0) groupedListBox2.removeItem(index);
    }
    
    /**
     * @param string
     * @return
     */
    private Widget createAddButton(String value) {
        FlowPanel ret = new FlowPanel();
        final TextBox box = new TextBox();
        ret.add(box);
        box.setValue(value);
        Button add = new Button("Add");
        add.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                setValue(box.getText());
            }
        });
        ret.add(add);
        return ret;
    }

}
