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
package com.tractionsoftware.gwt.user.client.impl;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Implementation used for browsers that are not Internet Explorer.
 */
public class ViewportImplStandard implements ViewportImpl {

    private static JavaScriptObject dispatchFocusEvent;
    private static JavaScriptObject dispatchBlurEvent;

    public native void addEventListeners() 
    /*-{
	@com.tractionsoftware.gwt.user.client.impl.ViewportImplStandard::dispatchFocusEvent = 
	    $entry(function(evt) {
		       @com.tractionsoftware.gwt.user.client.Viewport::dispatchFocusEvent(Lcom/google/gwt/user/client/Event;)(evt);
		   });

	@com.tractionsoftware.gwt.user.client.impl.ViewportImplStandard::dispatchBlurEvent = 
	    $entry(function(evt) {
		       @com.tractionsoftware.gwt.user.client.Viewport::dispatchBlurEvent(Lcom/google/gwt/user/client/Event;)(evt);
		   });

 	$wnd.addEventListener("focus", @com.tractionsoftware.gwt.user.client.impl.ViewportImplStandard::dispatchFocusEvent, false);
 	$wnd.addEventListener("blur", @com.tractionsoftware.gwt.user.client.impl.ViewportImplStandard::dispatchBlurEvent, false);
    }-*/;
    
}
