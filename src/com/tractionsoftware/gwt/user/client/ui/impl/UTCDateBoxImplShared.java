/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui.impl;

import com.google.gwt.user.client.ui.Composite;

/**
 * @author andy
 */
public abstract class UTCDateBoxImplShared extends Composite implements UTCDateBoxImpl {

    /**
     * Sets the visible length of the date input. The HTML5
     * implementation will ignore this.
     */
    @Override
    public void setVisibleLength(int length) {}

    /**
     * Sets the date value (as milliseconds at midnight UTC since 1/1/1970)
     */
    @Override
    public final void setValue(Long value) {
        setValue(value, true);
    }    

}
