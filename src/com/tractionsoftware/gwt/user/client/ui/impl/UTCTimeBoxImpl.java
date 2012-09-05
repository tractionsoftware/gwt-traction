/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui.impl;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Interface for UTCTimeBox implementations that are quite different
 * in appearance (HTML4 vs HTML5).
 * 
 * @author andy
 */
public interface UTCTimeBoxImpl extends IsWidget, HasValue<Long>, HasValueChangeHandlers<Long>, HasText {
    
    /**
     * Sets the DateTimeFormat for this UTCTimeBox. The HTML5
     * implementation will ignore this.
     */
    public void setTimeFormat(DateTimeFormat timeFormat); 
    
    /**
     * Sets the visible length of the time input. The HTML5
     * implementation will ignore this.
     */
    public void setVisibleLength(int length);    

    /**
     * Validates the value that has been typed into the text input.
     * The HTML5 implementation will do nothing.
     */
    public void validate();

    /**
     * Sets the tab index for the control. 
     */
    public void setTabIndex(int tabIndex);
        
}
