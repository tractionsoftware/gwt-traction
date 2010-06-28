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

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.tractionsoftware.gwt.user.client.animation.impl.SetOpacityImpl;

/**
 * Animates an opacity transition.
 */
public class OpacityAnimation extends Animation {

    static final SetOpacityImpl impl = GWT.create(SetOpacityImpl.class);

    private Element[] elm;
    private float fromOpacity;

    // note that we don't store toOpacity. instead we store the offset
    // that we'll use to compute the animation.
    private float addOpacity;

    /**
     * Creates an OpacityAnimation
     *
     * @param elm The Elements to modify
     * @param fromOpacity The opacity value at the start of the animation (0-1)
     * @param toOpacity The opacity value at the end of the animation (0-1)
     */
    public OpacityAnimation(Element[] elm, float fromOpacity, float toOpacity) {
	this.elm = elm;
	this.fromOpacity = fromOpacity;
	this.addOpacity = toOpacity - fromOpacity;
    }

    @Override
    protected void onUpdate(double progress) {
	float newOpacity = fromOpacity + (float) (addOpacity * progress);
	for (Element e : elm) {
	    impl.setOpacity(e, newOpacity);
	}
    }    

}
