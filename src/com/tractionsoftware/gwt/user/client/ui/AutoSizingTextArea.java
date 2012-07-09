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


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextArea;
import com.tractionsoftware.gwt.user.client.util.MiscUtils;

/**
 * This attaches to an input, listening for KeyDown/KeyUp, and
 * automatically resizing the text area. It does this using an
 * invisible textarea that matches the text of the input.
 */
public class AutoSizingTextArea extends AutoSizingBase<TextAreaWithSelection, TextArea> implements ResizeHandler {
    
    protected int divExtra = 0;
    protected HandlerRegistration resizeRegistration = null;

    public AutoSizingTextArea() {
	this(new TextAreaWithSelection());
    }
    private AutoSizingTextArea(TextAreaWithSelection box) {
	super(box, new TextArea());
    }
    public AutoSizingTextArea(TextAreaWithSelection box, int minSize, int maxSize) {
	super(box, new TextArea());
	setMinSize(minSize);
	setMaxSize(maxSize);
    }

    @Override
    protected void onLoad() {
	Element boxElement = box.getElement();

	int lineHeight = MiscUtils.getComputedStyleInt(boxElement, "lineHeight");
	setExtraSize(lineHeight);

	setMinFromCss("minHeight");
	setMaxFromCss("maxHeight");
	
	// we also need to make sure the widths are the same.
 	matchStyles("width");

	// don't let the shadow have any size
	shadow.setHeight("0");

	divExtra = 0;
	divExtra += MiscUtils.getComputedStyleInt(boxElement, "paddingTop");
	divExtra += MiscUtils.getComputedStyleInt(boxElement, "paddingBottom");

	resizeRegistration = Window.addResizeHandler(this);

	super.onLoad();
    }

    @Override
    protected void onUnload() {
        // fix leak reported in issue #4
        if (resizeRegistration != null) {
            resizeRegistration.removeHandler();
            resizeRegistration = null;
        }
        
        super.onUnload();
    }
    
    @Override
    public void onResize(ResizeEvent event) {
 	matchStyles("width");
	adjustSize();
    }

    @Override
    public void setHeight(int height) {
	div.setHeight(divExtra+height+"px");	
	box.setHeight(height+"px");
    }

    /**
     * Returns the size of the shadow element
     */
    @Override
    public int getShadowSize() {
	Element shadowElement = shadow.getElement();
	shadowElement.setScrollTop(10000);	    	    
	return shadowElement.getScrollTop();
    }

    /**
     * @param text the text that should be set on the shadow to
     * determine the appropriate size of the widget
     */
    @Override
    public void setShadowText(String text) {
	shadow.setValue(text);
    }

    /**
     * @param size will take into account minSize, maxSize, and
     * extraSize. the implementation should just call setWidth or
     * setHeight as appropriate.
     */
    @Override
    public void setSize(int size) {
	setHeight(size);
	if (size == maxSize) {
	    box.getElement().getStyle().setProperty("overflow", "auto");
	}
    }

    @Override
    public void setText(String text) {
	box.setText(text);
	Scheduler.get().scheduleDeferred(new ScheduledCommand() {            
            @Override
            public void execute() {
                sync();
            }
        });
    }

}

