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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Time is represented as the number of milliseconds after midnight
 * independent of time zone.
 * 
 * It will use the GWT DateTimeFormat to parse in the browser
 * timezone, but it will then convert the time to be independent of
 * timezone.
 * 
 * The control supports an unspecified value of null with a blank
 * textbox.
 * 
 * @author andy
 */
public class UTCTimeBox extends Composite implements HasValue<Long>, HasValueChangeHandlers<Long> {

    private FlowPanel container;
    
    private TextBox textbox;
    private DateTimeFormat timeFormat;
    
    private TimeBoxMenu menu;
    
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
                if (menu.isShowing()) {
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
    
    private class TimeBoxMenuOption extends FlowPanel {

        private long offsetFromMidnight;
        private String value;
        
        public TimeBoxMenuOption(long offsetFromMidnight) {
            this.offsetFromMidnight = offsetFromMidnight;

            long time = UTCDateBox.timezoneOffsetMillis(new Date(0)) + offsetFromMidnight;            
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
            return offsetFromMidnight;
        }
        
        public boolean isTimeEqualTo(long time) {
            long compare = getTime();
            return compare == time;            
        }
        
        public boolean isTimeLessThan(long time) {
            return getTime() < time;
        }
        
    }
    
    private class TimeBoxMenu extends PopupPanel {
        
        private static final long INTERVAL = 30*60*1000L;
        private static final long DAY = 24*60*60*1000L;
        
        private TimeBoxMenuOption[] options;
        private int highlightedOptionIndex = -1;
        
        public TimeBoxMenu() {
            super(true);
            setStyleName("gwt-TimeBox-menu");
            addAutoHidePartner(textbox.getElement());            
            
            FlowPanel container = new FlowPanel();
            
            int numOptions = (int) (DAY / INTERVAL);            
            options = new TimeBoxMenuOption[numOptions];
            
            // we need to use times for formatting, but we don't keep
            // them around. the purpose is only to generate text to
            // insert into the textbox.
            for (int i=0; i<numOptions; i++) {
                options[i] = new TimeBoxMenuOption(i * INTERVAL);
                container.add(options[i]);
            }
            
            add(container);
        }
        
        public void adjustHighlight(int value) {
            
            // make the list of times visible if it isn't
            if (!isShowing()) {
                showTimePicker();
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
        
        public void showTimePicker() {

            showRelativeTo(textbox);
            
            int lastOptionLessThanCurrentTime = 0;

            // reset while we try to find an option to highlight
            highlightedOptionIndex = -1;

            Long currentTime = getValue();
            for (int i = 0; i < options.length; i++) {
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
    
    public UTCTimeBox() {
        this(DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT));
    }
    
    public UTCTimeBox(DateTimeFormat timeFormat) {
        this.textbox = new TextBox();
        this.timeFormat = timeFormat;

        TextBoxHandler handler = new TextBoxHandler();
        textbox.addKeyDownHandler(handler);
        textbox.addKeyUpHandler(handler);
        textbox.addBlurHandler(handler);
        textbox.addClickHandler(handler);
        
        container = new FlowPanel();
        container.setStyleName("gwt-TimeBox");
        container.add(textbox);

        menu = new TimeBoxMenu();
        
        initWidget(container);
    }
    
    /**
     * Returns the TextBox on which this control is based.
     */
    public TextBox getTextBox() {
        return textbox;
    }

    // ----------------------------------------------------------------------
    // menu
    
    public void showMenu() {
        // make the menu visible and select the appropriate value 
        menu.showTimePicker();
    }
    
    public void hideMenu() {
        menu.hide();
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
        syncValueToText();
    }

    // ----------------------------------------------------------------------
    // synchronization between text and value
    
    private void syncTextToValue(Long value) {
        textbox.setValue(value2text(value));
    }
    
    private void syncValueToText() {
        setValue(getValueFromText(), false, true);
    }
    
    private Long getValueFromText() {
        return text2value(getText());
    }    

    private long normalizeInLocalRange(long time) {
        return (time + UTCDateBox.DAY_IN_MS) % UTCDateBox.DAY_IN_MS;
    }

    // ----------------------------------------------------------------------
    // parsing and formatting

    private Long text2value(String text) {
        if (text.trim().length() == 0) {
            return null;
        } else {
            Date date = new Date(0);
            int num = timeFormat.parse(text, 0, date);
            return (num != 0) ? new Long(normalizeInLocalRange(date.getTime() - UTCDateBox.timezoneOffsetMillis(date))) : null;
        }
    }
    
    private String value2text(Long value) {
        if (value == null) {
            return "";
        }
        else {
            // midnight 1/1/1970 GMT
            Date date = new Date(0);
            // offset by timezone and value
            date.setTime(UTCDateBox.timezoneOffsetMillis(date) + value.longValue());
            // format it
            return timeFormat.format(date);
        }
    }
    
}
