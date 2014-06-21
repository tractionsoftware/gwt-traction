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

import java.util.ArrayList;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;

/**
 * Extends the standard GWT ListBox to automatically provide OPTGROUP
 * elements to group sections of options.
 * <p>
 * Rather than provide a separate API, it uses the names of the OPTION
 * elements to establish the grouping using a simple syntax. The text
 * before the first "|" character is used as the group name and is
 * used to group elements inside an OPTGROUP of that name. The rest of
 * the text is used as the text of the OPTION.
 * <p>
 * It uses "doubling" for escaping so if "||" appears in the
 * name, it is converted to a single "|" and the first single "|"
 * is used as the delimiter.
 * <p>
 * As a simple example, in a normal listbox I might have:
 *
 * <pre>
 * - Item 1A
 * - Item 2A
 * - Item 1B
 * - Item 2B
 * </pre>
 * <p>
 * You could imaging using a text prefix to represent groups so that
 * you have:
 *
 * <pre>
 * - Group A | Item 1
 * - Group A | Item 2
 * - Group B | Item 1
 * - Group B | Item 2
 * </pre>
 * <p>
 * The ListBox would get wide and hard to read. But if you add those
 * same items to a GroupedListBox, it will create OPTGROUPS
 * automatically so that you will have:
 *
 * <pre>
 * - Group A
 * -- Item 1
 * -- Item 2
 * - Group B
 * -- Item 1
 * -- Item 2
 * </pre>
 * <p>
 * With regard to indexes and selection, it will mostly work the same
 * as a normal ListBox. The one difference is that it will not repeat
 * groups. This means that if you add the items in this order:
 *
 * <pre>
 * - Group A | Item 1
 * - Group B | Item 1
 * - Group A | Item 2
 * - Group B | Item 2
 * </pre>
 *
 * then you will rearrange the items to group them:
 *
 * <pre>
 * - Group A
 * -- Item 1
 * -- Item 2
 * - Group B
 * -- Item 1
 * -- Item 2
 * </pre>
 *
 * Note: Though it can be used with a multiple select control, this
 * extends SingleListBox to provide {@link #setValue(String)} and
 * other useful methods.
 *
 */
public class GroupedListBox extends SingleListBox {

    private ArrayList<OptGroup> groups = new ArrayList<OptGroup>();

    private static abstract class OptGroup {

        protected String name;
        protected int count = 0;

        public OptGroup(String name) {
            this.name = name;
        }

        public boolean isMatchingGroup(String groupName) {
            return name.equals(groupName);
        }

        public int getCount() {
            return count;
        }

        public abstract void remove();
        public abstract OptionElement getChildOption(int index);
        public abstract Node getInsertBeforeElement(int index);
        public abstract Element getInsertParent();

        public void increment() {
            count++;
        }

        public void decrement() {
            count--;
        }

    }

    /**
     * Keeps track of OptGroup elements to avoid hitting the DOM
     * constantly.
     */
    private final class RealOptGroup extends OptGroup {

        private OptGroupElement element;

        public RealOptGroup(String name) {
            super(name);
            this.element = Document.get().createOptGroupElement();
            this.element.setLabel(name);
        }

        @Override
        public void remove() {
            element.removeFromParent();
        }

        @Override
        public Node getInsertBeforeElement(int index) {
            Node before;

            // adjust the index to inside the group
            int adjusted = getIndexInGroup(name, index);

            // we had a real index (wasn't negative which means
            // add to the end), but it was too low for this group.
            // put it at the beginning of the group.
            if (adjusted < 0 && index >= 0) {
                adjusted = 0;
            }

            // check the range and if it's out of range, we'll
            // just add it to the end
            // of the group (before == null)
            if (0 <= adjusted && adjusted < count) {
                before = element.getChild(adjusted);
            }
            else {
                before = null;
            }

            return before;
        }

        @Override
        public Element getInsertParent() {
            return element;
        }

        public Element getElement() {
            return element;
        }

        @Override
        public OptionElement getChildOption(int index) {
            return option(element.getChild(index));
        }

    }

    /**
     * Used for ungrouped elements at the beginning of the list.
     */
    private final class FakeOptGroup extends OptGroup {

        public FakeOptGroup() {
            super("");
        }

        @Override
        public void remove() {
            Element select = getElement();
            while (count-- > 0) {
                Element option = select.getFirstChildElement();
                if (option != null) {
                    select.removeChild(option);
                }
            }
            count = 0;
        }

        @Override
        public Node getInsertBeforeElement(int index) {
            Node before = null;
            Element parent = getElement();

            // make sure we're not past the initial "group" of
            // ungrouped options
            int max = getIndexOfFirstGroup();
            if (index < 0 || index > max) {
                if (max < getChildCount()) {
                    before = parent.getChild(max);
                }
            }
            else if (0 <= index && index < getChildCount()) {
                before = parent.getChild(index);
            }

            return before;
        }

        @Override
        public Element getInsertParent() {
            return getElement();
        }

        @Override
        public OptionElement getChildOption(int index) {
            return option(getElement().getChild(index));
        }

    }

    public GroupedListBox() {
        this(false);
    }

    public GroupedListBox(boolean isMultipleSelect) {
        super(true,isMultipleSelect);
        addFakeOptGroup();
    }

    @Override
    public void clear() {

        // we need special handling to remove any OPTGROUP elements
        for (OptGroup group : groups) {
            group.remove();
        }

        groups.clear();
        addFakeOptGroup();
    }

    @Override
    public int getItemCount() {
        return getElement().getElementsByTagName("OPTION").getLength();
    }

    /**
     * This is provided for testing purposes only. getItemCount() uses
     * the DOM and this uses the data-structures that we maintain to
     * improve DOM access.
     */
    public int getItemCountFromGroups() {
        int ret = 0;
        for (OptGroup group : groups) {
            ret += group.getCount();
        }
        return ret;
    }

    @Override
    public String getItemText(int index) {
        OptionElement opt = getOption(index);
        return opt.getInnerText();
    }

    @Override
    public int getSelectedIndex() {
        int sz = getItemCount();
        for (int i=0; i<sz; i++) {
            if (getOption(i).isSelected()) return i;
        }
        return -1;
    }

    @Override
    public String getValue(int index) {
        OptionElement option = getOption(index);
        return option.getValue();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        addFakeOptGroup();
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        groups.clear();
    }

    @Override
    public void insertItem(String item, String value, int index) {
        // find the delimiter if there is one
        int pipe = (item != null) ? item.indexOf('|') : -1;
        while (pipe != -1 && pipe + 1 != item.length() && item.charAt(pipe + 1) == '|') {
            pipe = item.indexOf('|', pipe + 2);
        }

        // extract the group if we found a delimiter
        String group = null;
        if (pipe != -1) {
            group = item.substring(0, pipe).trim();
            item = item.substring(pipe + 1).trim();

            // make sure we convert || -> | in the group name
            group = group.replace("||", "|");
        }
        // convert || -> | in the item name
        if (item != null) {
            item = item.replace("||", "|");
        }
        // make sure we always have a group
        if (group == null) {
            group = "";
        }

        Element parent;
        Node before;

        OptGroup optgroup = findOptGroup(group);
        if (optgroup != null) {
            parent = optgroup.getInsertParent();
            before = optgroup.getInsertBeforeElement(index);
        }
        else {
            optgroup = createOptGroup(group);
            parent = optgroup.getInsertParent();
            before = null;
        }
        optgroup.increment();

        OptionElement option = createOption(item, value);
        parent.insertBefore(option, before);
    }

    @Override
    public boolean isItemSelected(int index) {
        OptionElement option = getOption(index);
        return (option != null) ? option.isSelected() : false;
    }

    @Override
    public void removeItem(int index) {

        int childIndex = index;
        for (int i=0; i<groups.size(); i++) {
            OptGroup group = groups.get(i);
            int count = group.getCount();
            if (childIndex < count) {

                // do the remove
                OptionElement element = group.getChildOption(childIndex);
                element.removeFromParent();

                group.decrement();

                // remove empty groups
                if (group.getCount() <= 0) {
                    group.remove();
                    groups.remove(i);
                }

                return;
            }
            else {
                childIndex -= count;
            }
        }

        throw new IndexOutOfBoundsException("problem in removeItem: index="+index+" range=[0-"+(getItemCount()-1)+"]");
    }

    @Override
    public void setItemSelected(int index, boolean selected) {
        OptionElement option = getOption(index);
        option.setSelected(selected);
    }

    @Override
    public void setItemText(int index, String text) {
        if (text == null) {
            throw new NullPointerException("Cannot set an option to have null text");
        }
        OptionElement option = getOption(index);
        option.setText(text);
    }

    @Override
    public void setSelectedIndex(int index) {
        int sz = getItemCount();
        for (int i = 0; i < sz; i++) {
            getOption(i).setSelected(i == index);
        }
        if (index < 0) getSelectElement().setSelectedIndex(index);
    }

    @Override
    public void setValue(int index, String value) {
        OptionElement option = getOption(index);
        option.setValue(value);
    }

    protected SelectElement getSelectElement() {
        return getElement().cast();
    }

    protected void checkIndex(int index) {
        if (index < 0 || index >= getItemCount()) {
            throw new IndexOutOfBoundsException(index+" out of range [0-"+(getItemCount()-1)+"]");
        }
    }

    // ----------------------------------------------------------------------
    // Convenience for dealing with the DOM directly instead of using
    // SelectElement.getOptions, etc

    protected int getChildCount() {
        // number in the ungrouped group, plus the number of groups,
        // minus one for the fake, ungrouped group
        return groups.get(0).getCount() + groups.size() - 1;
    }

    /**
     * We always keep a FakeOptGroup at the top.
     */
    private void addFakeOptGroup() {
        if (groups.isEmpty()) {
            groups.add(new FakeOptGroup());
        }
    }

    /**
     * Returns the FakeOptGroup at the top.
     */
    private OptGroup getFakeOptGroup() {
        return groups.get(0);
    }

    protected OptGroup findOptGroup(String groupName) {
        for (OptGroup group : groups) {
            if (group.isMatchingGroup(groupName)) {
                return group;
            }
        }
        return null;
    }

    protected OptGroup getOptGroup(int index) {
        int childIndex = index;
        for (OptGroup group : groups) {
            int count = group.getCount();
            if (childIndex < count) {
                return group;
            }
            else {
                childIndex -= count;
            }
        }

        throw new IndexOutOfBoundsException("problem in getOption: index="+index+" range=[0-"+(getItemCount()-1)+"]");
    }

    protected RealOptGroup createOptGroup(String groupName) {
        RealOptGroup newgroup = new RealOptGroup(groupName);
        groups.add(newgroup);

        // make sure we put the new OPTGROUP in the SELECT
        getElement().appendChild(newgroup.getElement());

        return newgroup;
    }

    protected OptionElement getOption(int index) {
        checkIndex(index);

        int childIndex = index;
        for (OptGroup group : groups) {
            int count = group.getCount();
            if (childIndex < count) {
                return group.getChildOption(childIndex);
            }
            else {
                childIndex -= count;
            }
        }

        throw new IndexOutOfBoundsException("problem in getOption: index="+index+" range=[0-"+(getItemCount()-1)+"]");
    }

    private OptionElement option(Node node) {
        if (node == null) return null;
        return OptionElement.as(Element.as(node));
    }

    protected int getIndexOfFirstGroup() {
        return getFakeOptGroup().getCount();
    }

    protected int getIndexInGroup(String groupName, int index) {
        if (groupName == null) return index;

        int adjusted = index;

        for (OptGroup group : groups) {
            if (group.isMatchingGroup(groupName)) {
                break;
            }
            else {
                adjusted -= group.getCount();
            }
        }
        return adjusted;
    }

    protected OptionElement createOption(String item, String value) {
        OptionElement option = Document.get().createOptionElement();
        option.setText(item);
        option.setInnerText(item);
        option.setValue(value);
        return option;
    }

}
