/*
 * Copyright 2011, The gwtquery team.
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
package com.tractionsoftware.gwt.user.client.impl;

import com.google.gwt.dom.client.Element;

/**
 * A helper class to get computed CSS styles for elements on IE6.
 */
public class DocumentStyleImplIE extends DocumentStyleImpl {

  @Override
  public native String getComputedStyle(Element elem, String hyphenName, String camelName) /*-{
    // code lifted from jQuery
    if (!elem.style || !'currentStyle' in elem || !'runtimeStyle' in elem) return null;
    var style = elem.style;
    var ret = elem.currentStyle[hyphenName] || elem.currentStyle[camelName];
    if ( !/^\d+(px)?$/i.test( ret ) && /^\d/.test( ret ) ) {
      // Remember the original values
      var left = style.left, rsLeft = elem.runtimeStyle.left;
      // Put in the new values to get a computed value out
      elem.runtimeStyle.left = elem.currentStyle.left;
      style.left = ret || 0;
      ret = style.pixelLeft + "px";
      // Revert the changed values
      style.left = left;
      elem.runtimeStyle.left = rsLeft;
    }
    return ret ? ""+ret : null;
  }-*/;

}