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
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tractionsoftware.gwt.user.client.util.Geometry;

/**
 * This attaches to an input, listening for KeyDown/KeyUp, and
 * automatically resizing the text area. It does this using an
 * invisible span that matches the text of the input.
 */
public class AutoSizingTextBox<T extends Widget & HasTextSelection & HasValue<String> & HasValueChangeHandlers<String> & HasKeyUpHandlers & HasKeyDownHandlers & Focusable & HasText> extends AutoSizingBase<T, InlineLabel> {
    
    private static final class TextBoxForAutoSize extends TextBox implements HasTextSelection {}

    public AutoSizingTextBox() {
	this((T)new TextBoxForAutoSize());
    }
    public AutoSizingTextBox(T box) {
	this(box, 30);
    }
    public AutoSizingTextBox(T box, int extraSize) {
	super(box, new InlineLabel());
	setExtraSize(extraSize);
    }

    protected void onLoad() {
	setMinFromCss("minWidth");
	setMaxFromCss("maxWidth");
	super.onLoad();
    }

    /**
     * Returns the size of the shadow element.
     */
    public int getShadowSize() {
	return Geometry.getW(shadow.getElement());
    }

    /**
     * @param text the text that should be set on the shadow to
     * determine the appropriate size of the widget
     */
    public void setShadowText(String text) {
	shadow.getElement().setInnerHTML(text);
    }

    /**
     * @param size will take into account minSize, maxSize, and
     * extraSize. the implementation should just call setWidth or
     * setHeight as appropriate.
     */
    public void setSize(int size) {
	setWidth(size);
    }

    public void setText(String text) {
	box.setText(text);
	if (text.length() == 0) {
	    setSize(extraSize);
	} 
	else {
	    DeferredCommand.addCommand(new Command() {
		    public void execute() {
			sync();
		    }
		});
	}
    }

}

