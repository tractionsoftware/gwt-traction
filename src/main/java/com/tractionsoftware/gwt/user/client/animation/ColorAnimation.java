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
package com.tractionsoftware.gwt.user.client.animation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.tractionsoftware.gwt.user.client.util.RgbaColor;
import com.tractionsoftware.gwt.user.client.animation.impl.SetColorImpl;

/**
 * Animates a color transition.
 */
public class ColorAnimation extends TractionAnimation {

    static final SetColorImpl impl = GWT.create(SetColorImpl.class);

    private Element[] elm;
    private String cssProperty;
    private RgbaColor fromColor;

    // note that we don't store toColor. instead we store the offset
    // that we'll use to compute the animation.
    private RgbaColor addColor;

    /**
     * Creates a ColorAnimation
     *
     * @param elm The Elements to modify
     * @param cssProperty The property to modify, e.g. color or backgroundColor
     * @param fromColor The color from which to start the transition
     * @param toColor The color at the end of the transition
     */
    public ColorAnimation(Element[] elm, String cssProperty, RgbaColor fromColor, RgbaColor toColor) {
	this.elm = elm;
	this.cssProperty = cssProperty;
	this.fromColor = fromColor;
	this.addColor = new RgbaColor(toColor.r() - fromColor.r(),
				      toColor.g() - fromColor.g(),
				      toColor.b() - fromColor.b(),
				      toColor.a() - fromColor.a());
    }

    @Override
    protected void onUpdate(double progress) {
	RgbaColor newColor = new RgbaColor(fromColor.r() + (int) (addColor.r() * progress),
					   fromColor.g() + (int) (addColor.g() * progress),
					   fromColor.b() + (int) (addColor.b() * progress),
					   fromColor.a() + (float) (addColor.a() * progress));
	for (Element e : elm) {
	    impl.setColor(e, cssProperty, newColor);
	}
    }

}
