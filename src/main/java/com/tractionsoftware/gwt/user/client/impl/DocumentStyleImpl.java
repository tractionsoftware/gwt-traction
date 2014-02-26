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
 * A helper class to get computed CSS styles for elements.
 */
public class DocumentStyleImpl {

  public native String getComputedStyle(Element elem, String hyphenName, String camelName) /*-{
    try {
     var cStyle = $doc.defaultView.getComputedStyle(elem);
     return cStyle && cStyle.getPropertyValue ? cStyle.getPropertyValue(hyphenName) : null;
    } catch(e) {return null;}
  }-*/;

}
