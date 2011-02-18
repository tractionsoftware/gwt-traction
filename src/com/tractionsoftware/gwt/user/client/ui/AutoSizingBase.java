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


import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyCodeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.tractionsoftware.gwt.user.client.util.MiscUtils;

/**
 * This attaches to an input, listening for KeyDown/KeyUp, and
 * automatically resizing the text area. It does this using a shadow
 * control that matches the text of the input.
 */
public abstract class AutoSizingBase<T extends Widget & HasTextSelection & HasValue<String> & HasValueChangeHandlers<String> & HasKeyDownHandlers & HasKeyUpHandlers & Focusable & HasText, S extends Widget> extends Composite 
    implements KeyDownHandler, KeyUpHandler, ValueChangeHandler<String>,
	       // for PillList
	       Focusable, HasText {

    public static final int DEFAULT_MAX = 10000;
    public static final int DEFAULT_MIN = 0;

    // ----------------------------------------------------------------------
    // abstract methods for subclass

    /**
     * Returns the size of the shadow element
     */
    public abstract int getShadowSize();

    /**
     * @param text the text that should be set on the shadow to
     * determine the appropriate size of the widget
     */
    public abstract void setShadowText(String text);

    /**
     * @param size will take into account minSize, maxSize, and
     * extraSize. the implementation should just call setWidth or
     * setHeight as appropriate.
     */
    public abstract void setSize(int size);

    // ----------------------------------------------------------------------
    // shared configuration

    // size is either width or height depending on the control
    // (TextBox vs. TextArea)

    public final int getMinSize() {
	return minSize;
    }
    public final void setMinSize(int minSize) {
	this.minSize = minSize;
    }

    public final int getMaxSize() {
	return maxSize;
    }
    public final void setMaxSize(int maxSize) {
	this.maxSize = maxSize;
    }

    /**
     * This is the amount of extra horizontal or vertical space that
     * will be added.
     */
    public final int getExtraSize() {
	return extraSize;
    }
    public final void setExtraSize(int extraSize) {
	this.extraSize = extraSize;
    }

    // ----------------------------------------------------------------------
    // check for max-height and min-height properties and use them
    // instead of anything configured. if you need to control the
    // min/max in code, don't set those css properties.
    //
    // also note that we remove the properties from the textarea AND
    // the shadow. this is important because otherwise they interfere
    // with the auto-sizing
    //

    public final void setMinFromCss(String property) {
	int min = getAndResetValueFromCss(property, "0");
	if (min > 0) {
	    setMinSize(min);
	}
    }

    public final void setMaxFromCss(String property) {
	int max = getAndResetValueFromCss(property, "none");
	if (max > 0) {
	    setMaxSize(max);
	}
    }

    public final int getAndResetValueFromCss(String property, String reset) {
	int value = MiscUtils.getComputedStyleInt(box.getElement(), property);
	if (value > 0) {
	    box.getElement().getStyle().setProperty(property, reset);
	    shadow.getElement().getStyle().setProperty(property, reset);
	}	
	return value;
    }
    
    // ----------------------------------------------------------------------
    
    protected int minSize = DEFAULT_MIN;
    protected int maxSize = DEFAULT_MAX;
    protected int extraSize;
    
    protected final T box;
    protected final S shadow;
    protected final FlowPanel div = new FlowPanel();

    public AutoSizingBase(T box, S shadow) {
	this.box = box;
	this.shadow = shadow;

 	box.addKeyDownHandler(this);
 	box.addKeyUpHandler(this);
	box.addValueChangeHandler(this);

	div.setStyleName("gwt-traction-input-autosize");
	shadow.setStyleName("gwt-traction-input-shadow");
	
	// make sure the shadow isn't in the tab order
	if (shadow instanceof Focusable) {
	    // we can't use -1 because FocusWidget.onAttach looks for
	    // that and sets it to 0. any negative value will remove
	    // it from the tab order.
	    ((Focusable) shadow).setTabIndex(-2);
	}

	// note this has to be in a FlowPanel to work
	div.add(box);
	div.add(shadow);

	initWidget(div);	
    }

    /**
     * Matches the styles and adjusts the size. This needs to be
     * called after the input is added to the DOM, so we do it in
     * onLoad.
     */
    protected void onLoad() {
	super.onLoad();

	// these styles need to be the same for the box and shadow so
	// that we can measure properly
	matchStyles("fontSize");
	matchStyles("fontFamily");
	matchStyles("fontWeight");
	matchStyles("lineHeight");
	matchStyles("paddingTop");
	matchStyles("paddingRight");
	matchStyles("paddingBottom");
	matchStyles("paddingLeft");

	adjustSize();
    }

    public T getWidget() {
	return box;
    }

    // ----------------------------------------------------------------------
    // style manipulation

    public void matchStyles(String name) {
	// sometimes IE throws an exception. thanks IE!
        String value = MiscUtils.getComputedStyle(box.getElement(), name);
        if (value != null) {
	    shadow.getElement().getStyle().setProperty(name, value);	
	}
    }

    public void setStyles(String name, String value) {
	box.getElement().getStyle().setProperty(name, value);
	shadow.getElement().getStyle().setProperty(name, value);
    }

    // ----------------------------------------------------------------------
    // event handling code

    /**
     * On key down we assume the key will go at the end. It's the most
     * common case and not that distracting if that's not true.
     */
    public void onKeyDown(KeyDownEvent event) {
	char c = MiscUtils.getCharCode(event.getNativeEvent());
	onKeyCodeEvent(event, box.getValue()+c);
    }    

    public void onKeyUp(KeyUpEvent event) {
	onKeyCodeEvent(event, box.getValue());
    }    

    protected void onKeyCodeEvent(KeyCodeEvent event, String newShadowText) {
	// ignore arrow keys
	switch (event.getNativeKeyCode()) {
	case KeyCodes.KEY_UP:
	case KeyCodes.KEY_DOWN:
	case KeyCodes.KEY_LEFT:
	case KeyCodes.KEY_RIGHT:
	    break;
	default:
	    // don't do this if there's a selection because it will get smaller
	    if (box.getSelectionLength() == 0) {
		setShadowText(newShadowText);
		adjustSize();	    
		break;
	    }
	}	
    }
        
    public void onValueChange(ValueChangeEvent<String> event) {
	// here, we just match them and adjust the size again. this
	// will handle backspace and typing over a selection.
	sync();
    }

    public void sync() {
	setShadowText(box.getValue());
	adjustSize();
    }

    // ----------------------------------------------------------------------
    // the meat (not very meaty)

    public void resetSize() {
	setSize(Math.max(minSize, extraSize));
    }
    
    public void adjustSize() {
	int size = getShadowSize() + extraSize;
	if (size < minSize) {
	    size = minSize;
	}
	else if (size > maxSize) {
	    size = maxSize;
	}
	setSize(size);
    }

    public void setWidth(int width) {
	box.setWidth(width+"px");
	div.setWidth(width+"px");
    }
    
    public void setHeight(int height) {
	box.setHeight(height+"px");
	div.setHeight(height+"px");	
    }
    
    // ----------------------------------------------------------------------
    // Focusable (proxy to SuggestBox)
    
    public final int getTabIndex() {
	return box.getTabIndex();
    }
    
    public final void setTabIndex(int index) {
	box.setTabIndex(index);
    }
    
    public final void setFocus(boolean focus) {
	if (focus) {
	    DeferredCommand.addCommand(new Command() {
		    public void execute() {
			box.setFocus(true);
		    }
		});
	}
	else {
	    box.setFocus(false);
	}
    }

    public final void setAccessKey(char key) {
	box.setAccessKey(key);
    }

    // ----------------------------------------------------------------------
    // HasText

    public final String getText() {
	return box.getText();
    }

    public abstract void setText(String text);

}

