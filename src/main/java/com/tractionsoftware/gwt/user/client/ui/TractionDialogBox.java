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

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Extends the standard GWT DialogBox by adding an OpenHandler and a
 * close icon with support for other controls.
 */
public class TractionDialogBox extends DialogBox implements HasOpenHandlers<TractionDialogBox> {

    private SimplePanel container;
    private FlowPanel controls;
    private Anchor close;

    public static class TractionCaption extends FlowPanel implements Caption {

        private FlowPanel caption = new FlowPanel();

        public TractionCaption() {
            setStyleName("Caption");
            add(caption);
        }

        @Override
        public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
            return addDomHandler(handler, MouseDownEvent.getType());
        }

        @Override
        public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
            return addDomHandler(handler, MouseUpEvent.getType());
        }

        @Override
        public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
            return addDomHandler(handler, MouseOutEvent.getType());
        }

        @Override
        public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
            return addDomHandler(handler, MouseOverEvent.getType());
        }

        @Override
        public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
            return addDomHandler(handler, MouseMoveEvent.getType());
        }

        @Override
        public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
            return addDomHandler(handler, MouseWheelEvent.getType());
        }

        @Override
        public String getHTML() {
            return caption.getElement().getInnerHTML();
        }

        @Override
        public void setHTML(String html) {
            caption.getElement().setInnerHTML(html);
        }

        @Override
        public String getText() {
            return caption.getElement().getInnerText();
        }

        @Override
        public void setText(String text) {
            caption.getElement().setInnerText(text);
        }

        @Override
        public void setHTML(SafeHtml html) {
            caption.getElement().setInnerSafeHtml(html);
        }

    }

    public TractionDialogBox(boolean autoHide, boolean modal) {
	this(autoHide, modal, true);
    }
    public TractionDialogBox(boolean autoHide, boolean modal, boolean showCloseIcon) {
	super(autoHide, modal, new TractionCaption());

	container = new SimplePanel();
	container.addStyleName("dialogContainer");

	close = new Anchor();
	close.setStyleName("x");
	close.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onCloseClick(event);
            }
	});
	setCloseIconVisible(showCloseIcon);

	controls = new FlowPanel();
	controls.setStyleName("dialogControls");
	controls.add(close);

	// add the controls to the end of the caption
	TractionCaption caption = (TractionCaption) getCaption();
	caption.add(controls);
    }

    @Override
    public void setWidget(Widget widget) {
        if (container.getWidget() == null) {
            // setup
            super.setWidget(container);
        }
        // add the new widget
        container.setWidget(widget);
    }

    public void setCloseIconVisible(boolean visible) {
	close.setVisible(visible);
    }

    /**
     * Returns the FlowPanel that contains the controls. More controls
     * can be added directly to this.
     */
    public FlowPanel getControlPanel() {
	return controls;
    }

    /**
     * Called when the close icon is clicked. The default
     * implementation hides dialog box.
     */
    protected void onCloseClick(ClickEvent event) {
	hide();
    }

    // ----------------------------------------------------------------------
    // HasOpenHandlers

    @Override
    public HandlerRegistration addOpenHandler(OpenHandler<TractionDialogBox> handler) {
	return addHandler(handler, OpenEvent.getType());
    }

    /**
     * Overrides show to call {@link #adjustGlassSize()} if the dialog
     * is already showing and fires an {@link OpenEvent} after the
     * normal show.
     */
    @Override
    public void show() {
	boolean fireOpen = !isShowing();
	super.show();

	// adjust the size of the glass
	if (isShowing()) {
	    adjustGlassSize();
	}

	// fire the open event
	if (fireOpen) {
	    OpenEvent.fire(this, this);
	}
    }

    // ----------------------------------------------------------------------

    /**
     * This can be called to adjust the size of the dialog glass. It
     * is implemented using JSNI to bypass the "private" keyword on
     * the glassResizer.
     */
    public void adjustGlassSize() {
        if (isGlassEnabled()) {
            ResizeHandler handler = getGlassResizer();
            if (handler != null) handler.onResize(null);
        }
    }

    /**
     * Bypass "private" on glassResizer
     */
    private native ResizeHandler getGlassResizer() /*-{
        return this.@com.google.gwt.user.client.ui.PopupPanel::glassResizer;
    }-*/;

}
