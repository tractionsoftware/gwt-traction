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

/**
 * Base class for history implementations that use a timer rather than the
 * onhashchange event.
 */
class RawHistoryImplTimer extends RawHistoryImpl {

  @Override
  public native boolean init() /*-{
    var token = '';

    // Get the initial token from the url's hash component.
    var hash = @com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::getLocationHash()();
    if (hash.length > 0) {
      token = this.@com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::decodeFragment(Ljava/lang/String;)(hash.substring(1));
    }

    @com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::setToken(Ljava/lang/String;)(token);

    // Create the timer that checks the browser's url hash every 1/4 s.
    var historyImpl = this;
    $wnd.__checkHistory = $entry(function() {
      $wnd.setTimeout($wnd.__checkHistory, 250);

      var token = '', hash = @com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::getLocationHash()();
      if (hash.length > 0) {
        token = historyImpl.@com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::decodeFragment(Ljava/lang/String;)(hash.substring(1));
      }

      historyImpl.@com.tractionsoftware.gwt.history.client.impl.RawHistoryImpl::newItemOnEvent(Ljava/lang/String;)(token);
    });

    // Kick off the timer.
    $wnd.__checkHistory();
    return true;
  }-*/;
}
