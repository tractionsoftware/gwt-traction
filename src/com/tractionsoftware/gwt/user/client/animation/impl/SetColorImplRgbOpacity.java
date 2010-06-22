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
package com.tractionsoftware.gwt.user.client.animation.impl;

import com.google.gwt.dom.client.Element;
import com.tractionsoftware.gwt.user.client.util.RgbaColor;

/**
 * Provides an interface for setting a color. IE doesn't support
 * rgba(), so we just use rgb().
 */
public class SetColorImplRgbOpacity implements SetColorImpl {

    public void setColor(Element e, String cssProperty, RgbaColor color) {
	e.getStyle().setProperty(cssProperty, color.toRgb());
	e.getStyle().setProperty("opacity", String.valueOf(color.a()));
    }

}
