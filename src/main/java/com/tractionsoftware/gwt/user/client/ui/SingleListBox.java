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
package com.tractionsoftware.gwt.user.client.ui;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

/**
 * A subclass of ListBox representing a single select pulldown and
 * implementing HasValue<String>. It listens to its own ChangeEvent
 * and fires a ValueChangeEvent.
 */
public class SingleListBox extends ListBox implements HasValue<String> {

    private boolean addMissingValue;

    /**
     * Create a new SingleListBox with setAddMissingValue(true)
     */
    public SingleListBox() {
	this(true);
    }
    
    /**
     * Create a new SingleListBox
     */
    public SingleListBox(boolean addMissingValue) {
        this(addMissingValue,false);
    }
    
    protected SingleListBox(boolean addMissingValue, boolean isMultipleSelect) {
	super(isMultipleSelect);
	setAddMissingValue(addMissingValue);

	// Listen to our own change events and broadcast as
	// ValueChangeEvent<String>
	addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                ValueChangeEvent.fire(SingleListBox.this, getValue());
            }
	});        
    }

    // ----------------------------------------------------------------------
    // 

    /**
     * Gets the current setting for add missing value.
     *
     * @return returns true if missing values will be added to the
     * ListBox when setValue is called.
     */
    public boolean getAddMissingValue() {
	return addMissingValue;
    }

    /**
     * Sets the current setting for add missing value.
     *
     * @param addMissingValue If true, when setValue is called, if the
     * item cannot be found in the list, it will be added
     * automatically to the end of the list.
     */
    public void setAddMissingValue(boolean addMissingValue) {
	this.addMissingValue = addMissingValue;
    }

    // ----------------------------------------------------------------------
    // HasValue implementation

    /**
     * Returns the current value selected in the ListBox.
     */
    @Override
    public String getValue() {
	return getSelectedValue(this);
    }

    /**
     * Selects the specified value in the list.
     * 
     * @param value the new value
     * @see #setAddMissingValue
     */
    @Override
    public void setValue(String value) {
	setValue(value, false);
    }

    /**
     * Selects the specified value in the list.
     * 
     * @param value the new value
     * @param fireEvents if true, a ValueChangeEvent event will be fired
     * @see #setAddMissingValue
     */
    @Override
    public void setValue(String value, boolean fireEvents) {
	boolean added = setSelectedValue(this, value, addMissingValue);
	if (added && fireEvents) {
	    ValueChangeEvent.fire(this, getValue());
	}
    }

    /**
     * Finds the index of the specified value.
     *
     * @param value the value to find
     * @return returns the index of the value or -1 if the value was not found
     * in the ListBox
     */
    public int findValueIndex(String value) {
	return findValueInListBox(this, value);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
	return addHandler(handler, ValueChangeEvent.getType());
    }

    // ----------------------------------------------------------------------
    // utility functions for working with ListBox options

    /**
     * Utility function to get the current value.
     */
    public static final String getSelectedValue(ListBox list) {
	int index = list.getSelectedIndex();
	return (index >= 0) ? list.getValue(index) : null;
    }

    /**
     * Utility function to get the current text.
     */
    public static final String getSelectedText(ListBox list) {
	int index = list.getSelectedIndex();
	return (index >= 0) ? list.getItemText(index) : null;
    }

    /**
     * Utility function to find the first index of a value in a
     * ListBox.
     */
    public static final int findValueInListBox(ListBox list, String value) {
	for (int i=0; i<list.getItemCount(); i++) {
	    if (value.equals(list.getValue(i))) {
		return i;
	    }
	}
	return -1;
    }

    /**
     * Utility function to set the current value in a ListBox.
     *
     * @return returns true if the option corresponding to the value
     * was successfully selected in the ListBox
     */
    public static final boolean setSelectedValue(ListBox list, String value, boolean addMissingValues) {
	if (value == null) {
	    list.setSelectedIndex(0);
	    return false;
	}
	else {
	    int index = findValueInListBox(list, value);
	    if (index >= 0) {
		list.setSelectedIndex(index);
		return true;
	    }

	    if (addMissingValues) {
		list.addItem(value, value);

		// now that it's there, search again
		index = findValueInListBox(list, value);
		list.setSelectedIndex(index);
		return true;
	    }

	    return false;
	}
    }
    
}
