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

/**
 * A simple rectangle class. This can be easily modified as all of its
 * members are public.
 */
public final class Rect {

    public int x;
    public int y;
    public int w;
    public int h;
	
    public Rect(int x, int y, int w, int h) {
	this.x = x;
	this.y = y;
	this.w = w;
	this.h = h;
    }

    public Rect(Rect copy) {
	this.x = copy.x;
	this.y = copy.y;
	this.w = copy.w;
	this.h = copy.h;
    }

    public boolean isInsideX(int n) {
	return x < n && n < x + w;
    }

    public boolean isInsideY(int n) {
	return y < n && n < y + h;	
    }

    public int getCenterX() {
	return x+w/2;
    }

    public int getCenterY() {
	return y+h/2;
    }

    public final String toString() {
	return "x="+x+" y="+y+" w="+w+" h="+h+" cx="+getCenterX()+" cy="+getCenterY();
    }

    /**
     * Note, this actually changes this Rect (if that wasn't obvious)
     */
    public final void moveInside(Rect bounds) {
	// check the right edge
	int distanceOffRightSide = (x + w) - (bounds.x + bounds.w);
	if (distanceOffRightSide > 0) {
	    x -= distanceOffRightSide;
	}
	// check the left edge (we prefer this to fit)
	int distanceOffLeftSide = bounds.x - x;
	if (distanceOffLeftSide > 0) {
	    x += distanceOffLeftSide;
	}

	// check the bottom edge
	int distanceOffBottom = (y + h) - (bounds.y + bounds.h);
	if (distanceOffBottom > 0) {
	    y -= distanceOffBottom;
	}
	// check the top edge (we prefer this to fit)
	int distanceOffTop = bounds.y - y;
	if (distanceOffTop > 0) {
	    y += distanceOffTop;
	}	
    }

}
