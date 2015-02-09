/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2015 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.server.util;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 * @author andy
 *
 */
public class TestRgbaColor extends TestCase {

    private void assertRgbEquals(RgbaColor color, String rgb) {
        assertEquals(color, RgbaColor.fromRgb(rgb));
        assertEquals(color, RgbaColor.from(rgb));
    }

    private void assertRgbaEquals(RgbaColor color, String rgba) {
        assertEquals(color, RgbaColor.fromRgba(rgba));
        assertEquals(color, RgbaColor.from(rgba));
    }

    private void assertHexEquals(RgbaColor color, String hex) {
        assertEquals(color, RgbaColor.fromHex(hex));
        assertEquals(color, RgbaColor.from(hex));
    }

    private void assertHslEquals(RgbaColor color, String hsl) {
        assertEquals(color, RgbaColor.fromHsl(hsl));
        assertEquals(color, RgbaColor.from(hsl));
    }

    private void assertHslaEquals(RgbaColor color, String hsla) {
        assertEquals(color, RgbaColor.fromHsla(hsla));
        assertEquals(color, RgbaColor.from(hsla));
    }

    private void assertLightenEquals(String colorExpect, String colorIn, int percent) {
        assertEquals(RgbaColor.from(colorExpect), RgbaColor.from(colorIn).lighten(percent));
    }

    private void assertDarkenEquals(String colorExpect, String colorIn, int percent) {
        assertEquals(RgbaColor.from(colorExpect), RgbaColor.from(colorIn).darken(percent));
    }

    private void assertSaturateEquals(String colorExpect, String colorIn, int percent) {
        assertEquals(RgbaColor.from(colorExpect), RgbaColor.from(colorIn).saturate(percent));
    }

    private void assertDesaturateEquals(String colorExpect, String colorIn, int percent) {
        assertEquals(RgbaColor.from(colorExpect), RgbaColor.from(colorIn).desaturate(percent));
    }

    private void assertAdjustHueEquals(String colorExpect, String colorIn, int degrees) {
        assertEquals(RgbaColor.from(colorExpect), RgbaColor.from(colorIn).adjustHue(degrees));
    }

    private void assertHSL(float[] hsl, String color) {
        RgbaColor start = RgbaColor.from(color);
        float[] HSL = start.convertToHsl();
        assertEquals(Math.round(hsl[0]), Math.round(HSL[0]));
        assertEquals(Math.round(hsl[1]), Math.round(HSL[1]));
        assertEquals(Math.round(hsl[2]), Math.round(HSL[2]));

        RgbaColor end = RgbaColor.fromHsl(HSL);
        assertEquals(start, end);
    }

    public void testParseRgb() {
        assertRgbEquals(new RgbaColor(0, 0, 0, 1), "rgb(0,0,0)");
        assertRgbEquals(new RgbaColor(0, 1, 2, 1), "rgb(0,1,2)");
        assertRgbEquals(new RgbaColor(255, 0, 128, 1), "rgb(255,0,128)");
        assertRgbEquals(null, "rgb(-1,25,999)");
        assertRgbEquals(null, "rgb(00)");
        assertRgbEquals(null, "");
    }

    public void testParseRgba() {
        assertRgbaEquals(new RgbaColor(0, 0, 0, 1), "rgba(0,0,0,1)");
        assertRgbaEquals(new RgbaColor(0, 1, 2, 0.1f), "rgba(0,1,2,0.1)");
        assertRgbaEquals(new RgbaColor(255, 0, 128, 0.3f), "rgba(255,0,128,0.3)");
        assertRgbaEquals(null, "rgba(-1,25,999,1.0)");
        assertRgbaEquals(null, "rgba(00)");
        assertRgbaEquals(null, "");
    }

    public void testParseHex6() {
        assertHexEquals(new RgbaColor(0, 0, 0, 1), "#000000");
        assertHexEquals(new RgbaColor(0, 1, 2, 1), "#000102");
        assertHexEquals(new RgbaColor(255, 0, 128, 1), "#ff0080");
        assertHexEquals(null, "#-234323");
        assertHexEquals(null, "#00");
        assertHexEquals(null, "");
    }

    public void testParseHex3() {
        assertHexEquals(new RgbaColor(0, 0, 0, 1), "#000");
        assertHexEquals(new RgbaColor(0, 17, 34, 1), "#012");
        assertHexEquals(new RgbaColor(255, 0, 136, 1), "#f08");
        assertHexEquals(null, "#-234");
        assertHexEquals(null, "#00");
        assertHexEquals(null, "");
    }

    public void testParseHSL() {
        assertHslEquals(new RgbaColor(0, 0, 0, 1), "hsl(0,0%,0%)");
        assertHslEquals(new RgbaColor(0, 0, 0, 1), "hsl(0, 0, 0)");
        assertHslEquals(new RgbaColor(0, 255, 0, 1), "hsl(120, 100%, 50%)");
        assertHslEquals(null, "hsl(-1,25,999,1.0)");
        assertHslEquals(null, "hsl(00)");
        assertHslEquals(null, "");
    }

    public void testParseHSLA() {
        assertHslaEquals(new RgbaColor(0, 0, 0, 0.5f), "hsla(0,0%,0%,0.5)");
        assertHslaEquals(new RgbaColor(0, 0, 0, 0.5f), "hsla(0,0,0,0.5)");
        assertHslaEquals(new RgbaColor(0, 255, 0, 0.1f), "hsla(120, 100%, 50%, 0.1)");
        assertHslaEquals(null, "hsl(-1,25,999,1.0)");
        assertHslaEquals(null, "hsl(00)");
        assertHslaEquals(null, "");
    }

    public void testHSL() {
        RgbaColor start = RgbaColor.from("#203f5e");
        assertEquals(32, start.r());
        assertEquals(63, start.g());
        assertEquals(94, start.b());

        float[] HSL = start.convertToHsl();
        assertEquals(210, Math.round(HSL[0])); // H
        assertEquals(49, Math.round(HSL[1])); // S
        assertEquals(25, Math.round(HSL[2])); // L

        RgbaColor end = RgbaColor.fromHsl(HSL);

        assertEquals(start, end);
    }

    public void testHSLBlack() {
        assertHSL(new float[] { 0, 0, 0 }, "#000");
    }

    public void testHSLWhite() {
        assertHSL(new float[] { 0, 0, 100 }, "#fff");
    }

    public void testHSLRed() {
        assertHSL(new float[] { 0, 100, 50 }, "#f00");
    }

    public void testHSLGreen() {
        assertHSL(new float[] { 120, 100, 50 }, "#0f0");
    }

    public void testHSLBlue() {
        assertHSL(new float[] { 240, 100, 50 }, "#00f");
    }

    public void testRedish() {
        assertHSL(new float[] { 0, 50, 50 }, "#BF4040");
    }

    public void testLighten() {
        assertLightenEquals("#2d5984", "#203f5e", 10);
        assertLightenEquals("#ee0000", "#800", 20);
        assertLightenEquals("#880000", "#200", 20);
        assertLightenEquals("#ff2222", "#800", 30);
    }

    public void testDarken() {
        assertDarkenEquals("#132638", "#203F5E", 10);
        assertDarkenEquals("#220000", "#800", 20);
        assertDarkenEquals("#880000", "#e00", 20);
    }

    public void testSaturate() {
        assertSaturateEquals("#9e3f3f", "#855", 20);
    }

    public void testDesaturate() {
        assertDesaturateEquals("#726b6b", "#855", 20);
    }

    public void testAdjustHue() {
        assertAdjustHueEquals("#886a11", "#811", 45);
        assertAdjustHueEquals("#881111", "#811", 360);
        assertAdjustHueEquals("#881111", "#811", 0);
        assertAdjustHueEquals("#881111", "#811", -360);
    }

    public void testHueCheck() {
         assertEquals(200f, RgbaColor.hueCheck(200));
         assertEquals(210.5f, RgbaColor.hueCheck(210.5f));
         assertEquals(5.5f, RgbaColor.hueCheck(5.5f));
         assertEquals(5.5f, RgbaColor.hueCheck(365.5f));
         assertEquals(5.5f, RgbaColor.hueCheck(725.5f));
         assertEquals(5.5f, RgbaColor.hueCheck(-354.5f));
         assertEquals(5.5f, RgbaColor.hueCheck(-714.5f));
    }

    public void testComplement() {
        assertHSL(new float[] { 300, 50, 50 }, RgbaColor.fromHsl(120, 50, 50).complement().toHex());
    }

    public void testSpreadInRange() {
        Assert.assertArrayEquals(new float[] { 5,15,25,35,45,55,65,75,85,95 }, RgbaColor.getSpreadInRange(55, 10, 100, 0), 0.5f);
        Assert.assertArrayEquals(new float[] { 5,25,45,65,85 }, RgbaColor.getSpreadInRange(45, 5, 100, 0), 0.5f);
        Assert.assertArrayEquals(new float[] { 0,64,128,192 }, RgbaColor.getSpreadInRange(64, 4, 256, 0), 0.5f);
        Assert.assertArrayEquals(new float[] { 25,50,75,100 }, RgbaColor.getSpreadInRange(100, 4, 100, 0), 0.5f);
        Assert.assertArrayEquals(new float[] { 0,25,50,75 }, RgbaColor.getSpreadInRange(25, 4, 100, 0), 0.5f);
        Assert.assertArrayEquals(new float[] { 0,25,50,75 }, RgbaColor.getSpreadInRange(0, 4, 100, 0), 0.5f);
        Assert.assertArrayEquals(new float[] { 24,49,74,99 }, RgbaColor.getSpreadInRange(99, 4, 100, 0), 0.5f);
        Assert.assertArrayEquals(new float[] { 10,30,50,70 }, RgbaColor.getSpreadInRange(50, 4, 80, 10), 0.5f);
    }

    public void testPaletteVaryLightness() {
        RgbaColor base = RgbaColor.fromHsl(0, 50, 50);
        RgbaColor[] mono = base.getPaletteVaryLightness(4);
        Assert.assertArrayEquals(new RgbaColor[] {
                                                  RgbaColor.fromHsl(0, 50, 10),
                                                  RgbaColor.fromHsl(0, 50, 30),
                                                  RgbaColor.fromHsl(0, 50, 50),
                                                  RgbaColor.fromHsl(0, 50, 70)
        }, mono);

    }

}
