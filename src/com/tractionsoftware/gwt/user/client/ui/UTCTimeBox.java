/****************************************************************

  Traction Software, Inc. Confidential and Proprietary Information

  Copyright (c) 1996-2012 Traction Software, Inc.
  All rights reserved.

****************************************************************/

// PLEASE DO NOT DELETE THIS LINE -- make copyright depends on it.

package com.tractionsoftware.gwt.user.client.ui;

import java.util.Date;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Time is represented as the number of milliseconds after midnight in
 * UTC relative to a specified reference date. It is meant to be used
 * with UTCDateBox. If you add the value of this control with that of
 * the UTCDateBox, you get a specific minute in time in UTC selected
 * from the user's time zone.
 * 
 * Here are some sample values for EDT (GMT-4):
 * 
 * 12:00 AM => 14400000 = 4 hours<br>
 * 01:00 AM => 18000000 = 5 hours<br>
 * 02:00 AM => 21600000 = 6 hours<br>
 * 03:00 AM => 25200000 = 7 hours<br>
 * 04:00 AM => 28800000 = 8 hours<br>
 * 05:00 AM => 32400000 = 9 hours<br>
 * 06:00 AM => 36000000 = 10 hours<br>
 * 07:00 AM => 39600000 = 11 hours<br>
 * 08:00 AM => 43200000 = 12 hours<br>
 * 09:00 AM => 46800000 = 13 hours<br>
 * 10:00 AM => 50400000 = 14 hours<br>
 * 11:00 AM => 54000000 = 15 hours<br>
 * 12:00 PM => 57600000 = 16 hours<br>
 * 01:00 PM => 61200000 = 17 hours<br>
 * 02:00 PM => 64800000 = 18 hours<br>
 * 03:00 PM => 68400000 = 19 hours<br>
 * 04:00 PM => 72000000 = 20 hours<br>
 * 05:00 PM => 75600000 = 21 hours<br>
 * 06:00 PM => 79200000 = 22 hours<br>
 * 07:00 PM => 82800000 = 23 hours<br>
 * 08:00 PM => 86400000 = 24 hours<br>
 * 09:00 PM => 90000000 = 25 hours<br>
 * 10:00 PM => 93600000 = 26 hours<br>
 * 11:00 PM => 97200000 = 27 hours<br>
 * 
 * Here are some sample values for JST (GMT+9):
 * 
 * 12:00 AM => -32400000 = -9 hours <br>
 * 01:00 AM => -28800000 = -8 hours <br>
 * 02:00 AM => -25200000 = -7 hours <br>
 * 03:00 AM => -21600000 = -6 hours <br>
 * 04:00 AM => -18000000 = -5 hours <br>
 * 05:00 AM => -14400000 = -4 hours <br>
 * 06:00 AM => -10800000 = -3 hours <br>
 * 07:00 AM => -7200000 = -2 hours <br>
 * 08:00 AM => -3600000 = -1 hours <br>
 * 09:00 AM => 0 = 0 hours <br>
 * 10:00 AM => 3600000 = 1 hours <br>
 * 11:00 AM => 7200000 = 2 hours <br>
 * 12:00 PM => 10800000 = 3 hours <br>
 * 01:00 PM => 14400000 = 4 hours <br>
 * 02:00 PM => 18000000 = 5 hours <br>
 * 03:00 PM => 21600000 = 6 hours <br>
 * 04:00 PM => 25200000 = 7 hours <br>
 * 05:00 PM => 28800000 = 8 hours <br>
 * 06:00 PM => 32400000 = 9 hours <br>
 * 07:00 PM => 36000000 = 10 hours <br>
 * 08:00 PM => 39600000 = 11 hours <br>
 * 09:00 PM => 43200000 = 12 hours <br>
 * 10:00 PM => 46800000 = 13 hours <br>
 * 11:00 PM => 50400000 = 14 hours <br>
 * 
 * The control supports an unspecified value of null with a blank textbox.
 * 
 * @author andy
 */
public class UTCTimeBox extends Composite implements HasValue<Long>, HasValueChangeHandlers<Long> {

    private FlowPanel container;
    
    private TextBox textbox;
    private DateTimeFormat timeFormat;
    private DateTimeFormat zoneFormat;
    
    private TimeBoxMenu menu;
    
    private long referenceDateAtMidnight = 0;
    private long referenceTimeZoneOffsetMillis;
    
    private Long lastKnownValue;
    
    private class TextBoxHandler implements KeyDownHandler, KeyUpHandler, BlurHandler, ClickHandler {

        @Override
        public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_UP:
                menu.adjustHighlight(-1);                
                break;
            case KeyCodes.KEY_DOWN:
                menu.adjustHighlight(1);
                break;
            case KeyCodes.KEY_ENTER:
            case KeyCodes.KEY_TAB:
                // accept current value
                if (menu.isVisible()) {
                    menu.acceptHighlighted();
                }
                // fall through
            case KeyCodes.KEY_ESCAPE:
            default:
                hideMenu();
                break;
            }
        }

        @Override
        public void onKeyUp(KeyUpEvent event) {
            syncValueToText();
        }

        @Override
        public void onBlur(BlurEvent event) {
            Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
                
                @Override
                public boolean execute() {
                    //hideMenu();
                    return false;
                }
            }, 10);
        }

        @Override
        public void onClick(ClickEvent event) {
            showMenu();
        }
    }
    
    private class TimeBoxMenuOption extends ComplexPanel {

        private long offsetFromMidnight;
        private String value;
        
        public TimeBoxMenuOption(long offsetFromMidnight) {
            this.offsetFromMidnight = offsetFromMidnight;

            setElement(DOM.createElement("LI"));

            long time = referenceDateAtMidnight + referenceTimeZoneOffsetMillis + offsetFromMidnight;            
            value = timeFormat.format(new Date(time));

            Anchor a = new Anchor(value);
            a.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    acceptValue();
                    hideMenu();
                }

                
            });
            add(a, getElement());
        }
        
        public void acceptValue() {
            setText(value);
        }
        
        public void setSelected(boolean isSelected) {
            setStyleName("selected", isSelected);            
        }
        
        public void setHighlighted(boolean isHighlighted) {
            setStyleName("highlighted", isHighlighted);
        }
        
        private long getTime() {
            long time = referenceDateAtMidnight + referenceTimeZoneOffsetMillis + offsetFromMidnight;
            return time;
        }
        
        public boolean isTimeEqualTo(long time) {
            long compare = getTime();
            return compare == time;            
        }
        
        public boolean isTimeLessThan(long time) {
            return getTime() < time;
        }
        
    }
    
    private class TimeBoxMenu extends ComplexPanel {
        
        private static final long INTERVAL = 30*60*1000L;
        private static final long DAY = 24*60*60*1000L;
        
        private TimeBoxMenuOption[] options;
        private int highlightedOptionIndex = -1;
        
        public TimeBoxMenu() {
            setElement(DOM.createElement("UL"));
            setStyleName("gwt-TimeBox-menu");
            
            int numOptions = (int) (DAY / INTERVAL);
            
            options = new TimeBoxMenuOption[numOptions];
            
            // we need to use times for formatting, but we don't keep
            // them around. the purpose is only to generate text to
            // insert into the textbox.
            for (int i=0; i<numOptions; i++) {
                options[i] = new TimeBoxMenuOption(i * INTERVAL);
                add(options[i], getElement());
            }
        }
        
        public void adjustHighlight(int value) {
            
            // make the list of times visible if it isn't
            if (!isVisible()) {
                setVisible(true);
            }
            
            // highlight the new value
            int index = normalizeOptionIndex(highlightedOptionIndex + value);
            setHighlightedIndex(index);
            scrollToIndex(index);
        }
        
        public void acceptHighlighted() {
            if (hasHighlightedOption()) {
                options[highlightedOptionIndex].acceptValue();
            }
        }
        
        private boolean hasHighlightedOption() {
            return (highlightedOptionIndex != -1);
        }
        
        private int normalizeOptionIndex(int index) {
            if (index < 0) {
                return 0;
            } 
            else if (index >= options.length) {
                return options.length - 1;
            }
            else {
                return index;
            }
        }
        
        public void scrollToIndex(int index) {
            options[normalizeOptionIndex(index)].getElement().scrollIntoView();
        }
        
        public void setHighlightedIndex(int index) {
            if (index != highlightedOptionIndex) {
                if (hasHighlightedOption()) {
                    options[highlightedOptionIndex].setHighlighted(false);
                }
                highlightedOptionIndex = index;
                options[index].setHighlighted(true);
                scrollToIndex(index);
            }
        }
        
        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);

            if (visible) {
                int lastOptionLessThanCurrentTime = 0;

                // reset while we try to find an option to highlight
                highlightedOptionIndex = -1;
                
                Long currentTime = getValue();
                for (int i=0; i<options.length; i++) {
                    TimeBoxMenuOption option = options[i];

                    boolean isEqual = option.isTimeEqualTo(currentTime); 
                    if (isEqual) {
                        highlightedOptionIndex = i;
                    }
                    option.setSelected(isEqual);
                    option.setHighlighted(isEqual);

                    if (option.isTimeLessThan(currentTime)) {
                        lastOptionLessThanCurrentTime = i;
                    }
                }

                int index;
                if (hasHighlightedOption()) {
                    index = highlightedOptionIndex;                    
                } 
                else {
                    index = normalizeOptionIndex(lastOptionLessThanCurrentTime);
                }
                // include a little extra to center the current time
                setHighlightedIndex(index);
                scrollToIndex(index + 6);        
            }
        }
        
    }
    
    public UTCTimeBox() {
        this(DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT));
    }
    
    public UTCTimeBox(DateTimeFormat timeFormat) {
        this.textbox = new TextBox();
        this.timeFormat = timeFormat;
        this.zoneFormat = DateTimeFormat.getFormat("z");

        TextBoxHandler handler = new TextBoxHandler();
        textbox.addKeyDownHandler(handler);
        textbox.addKeyUpHandler(handler);
        textbox.addBlurHandler(handler);
        textbox.addClickHandler(handler);
        
        setReferenceDate(new Date().getTime());
        
        container = new FlowPanel();
        container.setStyleName("gwt-TimeBox");
        container.add(textbox);

        menu = new TimeBoxMenu();
        menu.setVisible(false);
        container.add(menu);        
        
        initWidget(container);
    }
    
    /**
     * Returns the TextBox on which this control is based.
     */
    public TextBox getTextBox() {
        return textbox;
    }

    /**
     * Returns text to indicate the time zone.
     */
    public String getTimeZoneDisplay() {
        return zoneFormat.format(getReferenceDate());
    }
    
    // ----------------------------------------------------------------------
    // menu
    
    public void showMenu() {
        // make the menu visible and select the appropriate value 
        menu.setVisible(true);
    }
    
    public void hideMenu() {
        menu.setVisible(false);
    }
    
    // ----------------------------------------------------------------------
    // coordination with dates
    
    public Date getReferenceDate() {
        return new Date(referenceDateAtMidnight);
    }
    
    /**
     * Because time zones vary throughout the year (EST vs EDT), we
     * need a reference Date when parsing a time. This should be a
     * date at midnight in UTC.
     */
    public void setReferenceDate(Long referenceDate) {
        if (referenceDate != null) {
            referenceDateAtMidnight = UTCDateBox.trimTimeToMidnight(referenceDate);
            referenceTimeZoneOffsetMillis = UTCDateBox.timezoneOffsetMillis(new Date(referenceDate));
        }
    }

    // ----------------------------------------------------------------------
    // HasValue
    
    public boolean hasValue() { 
        return getText().trim().length() > 0;
    }
    
    /**
     * Returns a full date based on the time and the reference date.
     * 
     * @return A Long corresponding to the number of milliseconds since January
     *         1, 1970, 00:00:00 GMT or null if the specified time is null.
     */
    @Override
    public Long getValue() {
        return getValueFromText();
    }
    
    @Override
    public void setValue(Long value) {
        setValue(value, true, true);
    }

    @Override
    public void setValue(Long value, boolean fireEvents) {
        setValue(value, true, fireEvents);
    }
    
    public void setValue(Long value, boolean updateTextBox, boolean fireEvents) {
        setReferenceDate(getDateValue(value));
        
        if (updateTextBox) {
            syncTextToValue(value);
        }

        // keep track of the last known value so that we only fire
        // when it's different.
        Long oldValue = lastKnownValue;
        lastKnownValue = value;
        
        if (fireEvents && !isSameValue(oldValue, value)) fireValueChangeEvent();
    }
    
    private boolean isSameValue(Long a, Long b) {
        return (a == null) ? (b == null) : a.equals(b);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Long> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    protected void fireValueChangeEvent() {
        ValueChangeEvent.fire(this, getValue());        
    }
    
    // ----------------------------------------------------------------------
    // Interaction with the textbox
    
    public String getText() {
        return textbox.getValue();
    }
    
    public void setText(String text) {
        textbox.setValue(text);
    }

    // ----------------------------------------------------------------------
    // synchronization between text and value
    
    private void syncTextToValue(Long value) {
        if (value == null) {
            textbox.setValue("");
        }
        else {
            textbox.setValue(timeFormat.format(new Date(value)));
        }
    }
    
    private void syncValueToText() {
        setValue(getValueFromText(), false, true);
    }
    
    private Long getValueFromText() {
        // need to update the parsed value
        if (!hasValue()) {
            return null;
        }
        else {
            String text = getText();
            Date date = new Date(referenceDateAtMidnight);
            int num = timeFormat.parse(text, 0, date);
            return (num != 0) ? new Long(referenceDateAtMidnight + getTimeValue(date.getTime())) : null;
        }
    }    

    public Long getTimeValue() {
        Long ret = getValue();
        return ret != null ? getTimeValue(ret) : null;
    }
    
    public Long getTimeValue(Long value) {
        return normalizeInLocalRange(value.longValue());
    }

    public Long getDateValue(Long value) {
        // the trim is probably unnecessary
        //Date date = new Date(value - getTimeValue(value));        
        //return UTCDateBox.date2utc(date);
        return (value != null) ? value - getTimeValue(value) : null;
    }
    
    public void setTimeValue(Long value) {
        if (value != null) {
            setValue(referenceDateAtMidnight + value);
        }
        else {
            setValue(null);
        }
    }

    private long normalizeInLocalRange(long time) {
        return ((time - referenceTimeZoneOffsetMillis) % UTCDateBox.DAY_IN_MS) + referenceTimeZoneOffsetMillis;
    }

}
