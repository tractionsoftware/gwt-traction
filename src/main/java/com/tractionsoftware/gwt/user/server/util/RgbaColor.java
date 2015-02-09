/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2015 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.
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
package com.tractionsoftware.gwt.user.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tractionsoftware.gwt.user.client.util.AbstractRgbaColor;

/**
 * Represents an rgba color with HSL access. Immutable with factory
 * methods for adjustments.
 *
 * This version uses the java.util.regex package for parsing and is
 * intended for use on the server-side.
 */
public class RgbaColor extends AbstractRgbaColor {

    // ----------------------------------------------------------------------
    // private members and constructors

    /**
     * Creates an RgbaColor as rgba(0,0,0,1)
     */
    public RgbaColor() {
        super();
    }

    /**
     * Creates an RgbaColor with alpha=1
     *
     * @param r
     *            the red component [0-255]
     * @param g
     *            the green component [0-255]
     * @param b
     *            the blue component [0-255]
     */
    public RgbaColor(int r, int g, int b) {
        super(r, g, b);
    }

    /**
     * Creates an RgbaColor
     *
     * @param r
     *            the red component [0-255]
     * @param g
     *            the green component [0-255]
     * @param b
     *            the blue component [0-255]
     * @param a
     *            the alpha component [0-1]
     */
    public RgbaColor(int r, int g, int b, float a) {
        super(r, g, b, a);
    }

    // ----------------------------------------------------------------------
    // parsing factory methods

    /**
     * Parses an RgbaColor from a hexadecimal, rgb, rgba, hsl, or hsla
     * value.
     *
     * @return returns the parsed color
     */
    public static RgbaColor from(String color) {
        RgbaColor c = new RgbaColor();
        c.initWithString(color);
        return c;
    }

    /**
     * Parses an RgbaColor from a hexadecimal value.
     *
     * @return returns the parsed color
     */
    public static RgbaColor fromHex(String hex) {
        RgbaColor color = new RgbaColor();
        color.initWithHex(hex);
        return color;
    }

    /**
     * Parses an RgbaColor from an rgb value.
     *
     * @return the parsed color
     */
    public static RgbaColor fromRgb(String rgb) {
        RgbaColor color = new RgbaColor();
        color.initWithRgb(rgb);
        return color;
    }

    /**
     * Parses an RgbaColor from an rgba value.
     *
     * @return the parsed color
     */
    public static RgbaColor fromRgba(String rgba) {
        RgbaColor color = new RgbaColor();
        color.initWithRgba(rgba);
        return color;
    }

    /**
     * Parses an RgbaColor from a CSS3 HSL value, e.g. hsl(25, 100%,
     * 80%)
     *
     * @return the parsed color
     */
    public static RgbaColor fromHsl(String hsl) {
        RgbaColor color = new RgbaColor();
        color.initWithHsl(hsl);
        return color;
    }

    /**
     * Parses an RgbaColor from a CSS3 HSLA value, e.g. hsla(25, 100%,
     * 80%, 0.5)
     *
     * @return returns the parsed color
     */
    public static RgbaColor fromHsla(String hsl) {
        RgbaColor color = new RgbaColor();
        color.initWithHsla(hsl);
        return color;
    }

    /**
     * Convenience to get back to RGB from HSL array returned by
     * toHSL.
     */
    public static RgbaColor fromHsl(float[] hsl) {
        return fromHsl(hsl[0], hsl[1], hsl[2]);
    }

    /**
     * Convenience to get back to RGB from HSL array returned by
     * toHSL.
     */
    public static RgbaColor fromHsl(float H, float S, float L) {
        RgbaColor color = new RgbaColor();
        color.initWithHsl(H, S, L);
        return color;
    }

    /**
     * White is used by default.
     */
    @Override
    protected void initWithDefaultColor() {
        initWithInvalid();
    }

    // ----------------------------------------------------------------------
    // utility methods used for parsing

    private static final Pattern rgb = Pattern.compile("^rgb\\s*\\(\\s*([0-9]+),\\s*([0-9]+),\\s*([0-9]+)\\)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern rgba = Pattern.compile("^rgba\\s*\\(\\s*([0-9]+),\\s*([0-9]+),\\s*([0-9]+),\\s*([0-9]*\\.?[0-9]+)\\)$",
                                                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern hsl = Pattern.compile("^hsl\\s*\\(\\s*([0-9]+),\\s*([0-9]+)%?,\\s*([0-9]+)%?\\)$", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern hsla = Pattern.compile("^hsla\\s*\\(\\s*([0-9]+),\\s*([0-9]+)%?,\\s*([0-9]+)%?,\\s*([0-9]*\\.?[0-9]+)\\)$",
                                                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    @Override
    protected int parseHex(String hex, int s, int e) {
        int v = parseInt(hex.substring(s, e), 16);

        // handle single character parsing (e.g. #abc = #aabbcc)
        return (s + 1 == e) ? 16 * v + v : v;
    }

    @Override
    protected String getRgbParts(String str) {
        Matcher m = rgb.matcher(str);
        return m.matches() ? m.replaceFirst("$1,$2,$3") : "";
    }

    @Override
    protected String getRgbaParts(String str) {
        Matcher m = rgba.matcher(str);
        return m.matches() ? m.replaceFirst("$1,$2,$3,$4") : "";
    }

    @Override
    protected String getHslParts(String str) {
        Matcher m = hsl.matcher(str);
        return m.matches() ? m.replaceFirst("$1,$2,$3") : "";
    }

    @Override
    protected String getHslaParts(String str) {
        Matcher m = hsla.matcher(str);
        return m.matches() ? m.replaceFirst("$1,$2,$3,$4") : "";
    }

    @Override
    protected int parseInt(String val, int radix) {
        try {
            return Integer.parseInt(val, radix);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    protected float parseFloat(String val, int radix) {
        try {
            return Float.parseFloat(val);
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

}
