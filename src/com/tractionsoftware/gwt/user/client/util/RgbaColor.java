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

import com.google.gwt.core.client.GWT;

/**
 * Represents an rgba color. Immutable.
 */
public class RgbaColor {

    // ----------------------------------------------------------------------
    // parsing methods

    /**
     * Parses an RgbaColor from a hexadecimal, rgb, or rgba value.
     * @return returns the parsed color
     */
    public static final RgbaColor from(String color) {
	if (color.startsWith("#")) {
	    return fromHex(color);
	}
	else if (color.startsWith("rgba")) {
	    return fromRgba(color);
	} 
	else if (color.startsWith("rgb")) {
	    return fromRgb(color);
	} 
	else {
	    return getDefaultColor();
	}
    }
    
    /**
     * Parses an RgbaColor from a hexadecimal value.
     * @return returns the parsed color
     */
    public static final RgbaColor fromHex(String hex) {
	if (hex.length() == 0 || hex.charAt(0) != '#') return getDefaultColor();

	// #rgb
	if (hex.length() == 4) {
	    
	    return new RgbaColor(parseHex(hex,1,2),
				parseHex(hex,2,3),
				parseHex(hex,3,4));
	    
	} 
	// #rrggbb
	else if (hex.length() == 7) {

	    return new RgbaColor(parseHex(hex,1,3),
				parseHex(hex,3,5),
				parseHex(hex,5,7));

	}
	else {
	    return getDefaultColor();
	}
    }

    /**
     * Parses an RgbaColor from an rgb value.
     * @return returns the parsed color
     */
    public static final RgbaColor fromRgb(String rgb) {
	if (rgb.length() == 0) return getDefaultColor();
	
	String[] parts = getRgbParts(rgb).split(",");
	if (parts.length == 3) {
	    return new RgbaColor(parseInt(parts[0]),
				 parseInt(parts[1]),
				 parseInt(parts[2]));
	}
	else {
	    return getDefaultColor();
	}
    }

    /**
     * Parses an RgbaColor from an rgba value.
     * @return returns the parsed color
     */
    public static final RgbaColor fromRgba(String rgba) {
	if (rgba.length() == 0) return getDefaultColor();
	
	GWT.log("Parsing: "+rgba, null);

	String[] parts = getRgbaParts(rgba).split(",");

	GWT.log("Parsing: parts="+parts.length, null);

	if (parts.length == 4) {
	    return new RgbaColor(parseInt(parts[0]),
				 parseInt(parts[1]),
				 parseInt(parts[2]),
				 parseFloat(parts[3]));
	}
	else {
	    return getDefaultColor();
	}
    }

    private static final RgbaColor getDefaultColor() {
	return new RgbaColor(255,255,255);
    }

    // ----------------------------------------------------------------------
    // utility methods used for parsing

    private static final String HEX = "0123456789abcdef";

    private static final int parseHex(String hex, int s, int e) {
	int v = parseInt(hex.substring(s,e), 16);

	// handle single character parsing (e.g. #abc = #aabbcc)
	return (s+1 == e) ? 16*v+v : v;
    }
	
    private static native String getRgbParts(String str)
    /*-{
      var re = new RegExp("rgb\\s*\\(\\s*([0-9]+).*,\\s*([0-9]+).*,\\s*([0-9]+).*\\)", "gi");
      return str.replace(re, "$1,$2,$3");
    }-*/;

    private static native String getRgbaParts(String str)
    /*-{
      var re = new RegExp("rgba\\s*\\(\\s*([0-9]+).*,\\s*([0-9]+).*,\\s*([0-9]+).*,\\s*([0-9]*\\.?[0-9]+).*\\)", "gi");
      return str.replace(re, "$1,$2,$3,$4");
    }-*/;

    private static final int parseInt(String val) {
	return parseInt(val, 10);
    }
    private static final native int parseInt(String val, int radix) 
    /*-{
      return parseInt(val, radix) || 0;
      }-*/;

    private static final float parseFloat(String val) {
	return parseFloat(val, 10);
    }
    private static final native float parseFloat(String val, int radix) 
    /*-{
      return parseFloat(val, radix) || 0;
      }-*/;

    // ----------------------------------------------------------------------

    private int r;
    private int g;
    private int b;
    private float a;

    /**
     * Creates an RgbaColor with rgb=0 and alpha=1
     */
    public RgbaColor() {
	this(0,0,0,1);
    }

    /**
     * Creates an RgbaColor with alpha=1
     *
     * @param r the red component (0-255)
     * @param g the green component (0-255)
     * @param b the blue component (0-255)
     */
    public RgbaColor(int r, int g, int b) {
	this(r,g,b,1);
    }
    
    /**
     * Creates an RgbaColor
     *
     * @param r the red component (0-255)
     * @param g the green component (0-255)
     * @param b the blue component (0-255)
     * @param a the alpha component (0-1)
     */
    public RgbaColor(int r, int g, int b, float a) {
	this.r = r;
	this.g = g;
	this.b = b;
	this.a = a;
    }

    /**
     * Returns the value of the red component (0-255)
     */
    public int r() {
	return r;
    }

    /**
     * Returns the value of the green component (0-255)
     */
    public int g() {
	return g;
    }

    /**
     * Returns the value of the blue component (0-255)
     */
    public int b() {
	return b;
    }

    /**
     * Returns the value of the alpha component (0-1)
     */
    public float a() {
	return a;
    }

    /**
     * Returns the color in hexadecimal format (#rrggbb)
     */
    public String toHex() {
	String rs = hexpad(Integer.toHexString(r));
	String gs = hexpad(Integer.toHexString(g));
	String bs = hexpad(Integer.toHexString(b));
	return "#" + rs + gs + bs;
    }
    private static final String hexpad(String s) {
	return (s.length() == 1) ? "0" + s : s;
    }

    /**
     * Returns the color in rgb format, e.g. rgb(r,g,b)
     */
    public String toRgb() {
	return "rgb("+r+","+g+","+b+")";
    }

    /**
     * Returns the color in rgba format, e.g. rgba(r,g,b,a)
     */
    public String toRgba() {
	return "rgba("+r+","+g+","+b+","+a+")";
    }

    public String toString() {
	return toHex();
    }

}
