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
import com.google.gwt.query.client.GQuery;

/**
 * Miscellaneous utility functions that are used in a few different places.
 */
public class MiscUtils {

    /**
     * Note: It seems like NativeEvent.getCharCode() would be equivalent. 
     */
    public static native char getCharCode(NativeEvent e)
    /*-{
      return e.charCode || e.keyCode;
    }-*/;    
    
    public static final String getComputedStyle(Element e, String name) {
	// sometimes IE throws an exception. thanks IE!
        try {
            return GQuery.$(e).css(name, true);
        }
        catch (Exception xcp) {
            return null;
        }
    }

    public static final int getComputedStyleInt(Element e, String name) {
        String num = getComputedStyle(e, name);
	return num != null ? parseInt(num) : 0;
    }        
    
    public static final int parseInt(String val) {
	return parseInt(val, 10);
    }
    
    public static final native int parseInt(String val, int radix) 
    /*-{
      return parseInt(val, radix) || 0;
    }-*/;    
    
}
