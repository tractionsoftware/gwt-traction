/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui.impl;




/**
 * @author andy
 */
public abstract class UTCDateBoxImplShared implements UTCDateBoxImpl {
    
    /**
     * Sets the date value (as milliseconds at midnight UTC since 1/1/1970)
     */
    @Override
    public final void setValue(Long value) {
        setValue(value, true);
    }    

}
