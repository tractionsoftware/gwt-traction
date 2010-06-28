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

public abstract class TractionAnimation extends Animation {

    /**
     * Sets the animation to the starting point, canceling any
     * animation in progress.
     */
    public void setPositionAtStart() {
	setPosition(0.0);
    }

    /**
     * Sets the animation to the end point, canceling any animation in
     * progress.
     */
    public void setPositionAtEnd() {
	setPosition(1.0);
    }

    /**
     * Sets the animation to a particular point, cancelling an
     * animation in progress. Note that the animation will always
     * reset on run.
     */
    public void setPosition(double progress) {
	cancel();
	onUpdate(interpolate(progress));	
    }

}
