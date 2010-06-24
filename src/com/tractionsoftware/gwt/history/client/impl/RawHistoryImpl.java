/*
 * Based on code from GWT HistoryImpl, Copyright 2008 Google Inc.
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
package com.tractionsoftware.gwt.history.client.impl;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Native implementation associated with {@link com.google.gwt.user.client.History} 
 * and provided instead of the normal HistoryImpl.
 */
public class RawHistoryImpl extends com.google.gwt.user.client.impl.HistoryImpl {

    /**
     * The only change is to call out to getLocationHash() so that we
     * can provide an implementation that doesn't use
     * window.location.hash, which is automatically decoded by some
     * browers (at least Firefox).
     */
    @Override
    public native boolean init() 
    /*-{
      var token = '';

      // Get the initial token from the url's hash component.
      var hash = @com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::getLocationHash()();
      if (hash.length > 0) {
        token = this.@com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::decodeFragment(Ljava/lang/String;)(hash.substring(1));
      }

      @com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::setToken(Ljava/lang/String;)(token);

      var historyImpl = this;
      $wnd.onhashchange = $entry(function() {
        var token = '', hash = @com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::getLocationHash()();
        if (hash.length > 0) {
          token = historyImpl.@com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::decodeFragment(Ljava/lang/String;)(hash.substring(1));
        }

        historyImpl.@com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::newItemOnEvent(Ljava/lang/String;)(token);
      });

      return true;
    }-*/;

    /**
     * Some browsers either don't provide or prematurely decode
     * window.location.hash, so we don't use it.
     */
    private static native String getLocationHash() 
    /*-{
      var href = $wnd.location.href;
      var hashLoc = href.lastIndexOf("#");
      return (hashLoc > 0) ? href.substring(hashLoc) : "";
    }-*/;

    /**
     * No decoding for RawHistory.
     */
    @Override
    protected String decodeFragment(String encodedFragment) {
	return encodedFragment;
    }

    /**
     * No encoding for RawHistory.
     */
    @Override
    protected String encodeFragment(String fragment) {
	return fragment;
    }
    
}
