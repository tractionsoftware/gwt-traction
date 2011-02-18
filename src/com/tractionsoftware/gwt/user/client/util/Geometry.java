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
package com.tractionsoftware.gwt.user.client.util;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.UIObject;

/**
 * Figures out layout geometry, measures stuff.
 */
public class Geometry {

    public static final void setInvisibleToMeasure(Element e) {
        Style s = e.getStyle();
        s.setProperty("visibility", "hidden");
        s.setProperty("display", "block");
    }

    public static final void unsetInvisibleToMeasure(Element e) {
        Style s = e.getStyle();
        s.setProperty("display", "none");
        s.setProperty("visibility", "");
    }

    public static final Rect getBoundsOfHidden(Element e) {
        setInvisibleToMeasure(e);
        Rect ret = getBounds(e);
        unsetInvisibleToMeasure(e);
        return ret;
    }

    public static final Rect getBounds(Element e) {
        return new Rect(getX(e), getY(e), getW(e), getH(e));
    }

    public static final int getX(Element e) {
        return e.getAbsoluteLeft();
    }

    public static final int getY(Element e) {
        return e.getAbsoluteTop();
    }

    /**
     * Updated to factor in border and padding
     */
    public static final int getW(Element e) {
        int ret = e.getOffsetWidth();
        ret -= MiscUtils.getComputedStyleInt(e, "paddingLeft");
        ret -= MiscUtils.getComputedStyleInt(e, "paddingRight");
        ret -= MiscUtils.getComputedStyleInt(e, "borderLeftWidth");
        ret -= MiscUtils.getComputedStyleInt(e, "borderRightWidth");
        return Math.max(ret, 0);
    }

    /**
     * Updated to factor in border and padding
     */
    public static final int getH(Element e) {
        int ret = e.getOffsetHeight();
        ret -= MiscUtils.getComputedStyleInt(e, "paddingTop");
        ret -= MiscUtils.getComputedStyleInt(e, "paddingBottom");
        ret -= MiscUtils.getComputedStyleInt(e, "borderTopWidth");
        ret -= MiscUtils.getComputedStyleInt(e, "borderBottomWidth");
        return Math.max(ret, 0);
    }

    /**
     * Sets the bounds of a UIObject, moving and sizing to match the
     * bounds specified. Currently used for the itemhover and useful
     * for other absolutely positioned elements.
     */
    public static final void setBounds(UIObject o, Rect bounds) {
        setPosition(o, bounds);
        setSize(o, bounds);
    }

    /**
     * Sets the position of a UIObject
     */
    public static final void setPosition(UIObject o, Rect pos) {
        Style style = o.getElement().getStyle();
        style.setPropertyPx("left", pos.x);
        style.setPropertyPx("top", pos.y);
    }

    /**
     * Sets the size of a UIObject
     */
    public static final void setSize(UIObject o, Rect size) {
        o.setPixelSize(size.w, size.h);

    }

    /**
     * Determines if a point is inside a box.
     */
    public static final boolean isInside(int x, int y, Rect box) {
        return (box.x < x && x < box.x + box.w && box.y < y && y < box.y + box.h);
    }

    /**
     * Determines if a mouse event is inside a box.
     */
    public static final boolean isMouseInside(NativeEvent event, Element element) {
        return isInside(event.getClientX() + Window.getScrollLeft(), event.getClientY() + Window.getScrollTop(), getBounds(element));
    }

    /**
     * This takes into account scrolling and will be in absolute
     * coordinates where the top left corner of the page is 0,0 but
     * the viewport may be scrolled to something else.
     */
    public static final Rect getViewportBounds() {
        return new Rect(Window.getScrollLeft(), Window.getScrollTop(), Window.getClientWidth(), Window.getClientHeight());
    }

}
