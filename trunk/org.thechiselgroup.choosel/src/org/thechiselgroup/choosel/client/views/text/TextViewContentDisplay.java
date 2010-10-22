/*******************************************************************************
 * Copyright 2009, 2010 Lars Grammel 
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.  
 *******************************************************************************/
package org.thechiselgroup.choosel.client.views.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.thechiselgroup.choosel.client.persistence.Memento;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.ui.CSS;
import org.thechiselgroup.choosel.client.ui.dnd.ResourceSetAvatarDragController;
import org.thechiselgroup.choosel.client.util.CollectionUtils;
import org.thechiselgroup.choosel.client.views.AbstractViewContentDisplay;
import org.thechiselgroup.choosel.client.views.ResourceItem;
import org.thechiselgroup.choosel.client.views.Slot;
import org.thechiselgroup.choosel.client.views.SlotResolver;
import org.thechiselgroup.choosel.client.views.text.TextItem.TextItemLabel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class TextViewContentDisplay extends AbstractViewContentDisplay {

    public class DefaultDisplay implements Display {

        private static final String CSS_TAG_CLOUD = "choosel-TextViewContentDisplay-TagCloud";

        private static final int MAX_FONT_SIZE = 26;

        private List<TextItemLabel> itemLabels = new ArrayList<TextItemLabel>();

        private List<String> textItems = new ArrayList<String>();

        @Override
        public void addItem(TextItem textItem) {
            textItem.init();

            TextItemLabel label = textItem.getLabel();

            itemLabels.add(label);

            label.addMouseOverHandler(labelEventHandler);
            label.addMouseOutHandler(labelEventHandler);
            label.addClickHandler(labelEventHandler);

            // insert at right position to maintain sort..
            // TODO cleanup - performance issues
            // XXX does not update when properties change
            textItems.add(label.getText());
            Collections.sort(textItems, String.CASE_INSENSITIVE_ORDER);
            int row = textItems.indexOf(label.getText());
            itemPanel.insert(label, row);
        }

        @Override
        public void addStyleName(TextItem textItem, String cssClass) {
            textItem.getLabel().addStyleName(cssClass);
        }

        private List<Double> getFontSizeValues() {
            List<Double> fontSizeValues = new ArrayList<Double>();
            for (TextItemLabel textItemLabel : itemLabels) {
                fontSizeValues.add(textItemLabel.getFontSizeValue());
            }
            return fontSizeValues;
        }

        @Override
        public void removeStyleName(TextItem textItem, String cssClass) {
            textItem.getLabel().removeStyleName(cssClass);
        }

        @Override
        public void removeTextItem(TextItem textItem) {
            /*
             * whole row needs to be removed, otherwise lots of empty rows
             * consume the whitespace
             */
            for (int i = 0; i < itemPanel.getWidgetCount(); i++) {
                TextItemLabel label = textItem.getLabel();
                itemLabels.remove(label);
                if (itemPanel.getWidget(i).equals(label)) {
                    itemPanel.remove(i);
                    textItems.remove(i);
                    return;
                }
            }
        }

        @Override
        public void setTagCloud(boolean tagCloud) {
            if (tagCloud) {
                itemPanel.addStyleName(CSS_TAG_CLOUD);
            } else {
                itemPanel.removeStyleName(CSS_TAG_CLOUD);
            }
        }

        @Override
        public void updateFontSizes() {
            List<Double> fontSizeValues = getFontSizeValues();

            for (TextItemLabel itemLabel : itemLabels) {
                String fontSize = groupValueMapper.getGroupValue(
                        itemLabel.getFontSizeValue(), fontSizeValues);

                CSS.setFontSize(itemLabel.getElement(), fontSize);
            }
        }
    }

    public static interface Display {

        void addItem(TextItem textItem);

        void addStyleName(TextItem textItem, String cssClass);

        void removeStyleName(TextItem textItem, String cssClass);

        void removeTextItem(TextItem textItem);

        void setTagCloud(boolean tagCloud);

        void updateFontSizes();

    }

    private class LabelEventHandler implements ClickHandler, MouseOutHandler,
            MouseOverHandler {

        private ResourceSet getResource(GwtEvent<?> event) {
            return getResourceItem(event).getResourceSet();
        }

        private ResourceItem getResourceItem(GwtEvent<?> event) {
            return ((TextItemLabel) event.getSource()).getTextItem()
                    .getResourceItem();
        }

        @Override
        public void onClick(ClickEvent e) {
            getCallback().switchSelection(getResource(e));
        }

        @Override
        public void onMouseOut(MouseOutEvent e) {
            getResourceItem(e).getHighlightingManager().setHighlighting(false);
        }

        @Override
        public void onMouseOver(MouseOverEvent e) {
            getResourceItem(e).getHighlightingManager().setHighlighting(true);
        }
    }

    private static class ResizableScrollPanel extends ScrollPanel implements
            RequiresResize {

        private ResizableScrollPanel(Widget child) {
            super(child);
        }

    }

    public static final String CSS_LIST_VIEW_SCROLLBAR = "listViewScrollbar";

    private final Display display;

    private ResourceSetAvatarDragController dragController;

    private LabelEventHandler labelEventHandler;

    private ScrollPanel scrollPanel;

    private FlowPanel itemPanel;

    private DoubleToGroupValueMapper<String> groupValueMapper;

    private boolean tagCloud = true;

    public TextViewContentDisplay(ResourceSetAvatarDragController dragController) {
        assert dragController != null;

        this.dragController = dragController;
        display = new DefaultDisplay();

        initGroupValueMapper();
    }

    // for test: can change display
    protected TextViewContentDisplay(
            ResourceSetAvatarDragController dragController, Display display) {

        assert dragController != null;
        assert display != null;

        this.dragController = dragController;
        this.display = display;

        initGroupValueMapper();
    }

    @Override
    public Widget createWidget() {
        itemPanel = new FlowPanel();

        labelEventHandler = new LabelEventHandler();

        scrollPanel = new ResizableScrollPanel(itemPanel);
        scrollPanel.addStyleName(CSS_LIST_VIEW_SCROLLBAR);

        display.setTagCloud(isTagCloud());

        return scrollPanel;
    }

    @Override
    public Widget getConfigurationWidget() {
        FlowPanel panel = new FlowPanel();

        final CheckBox oneItemPerRowBox = new CheckBox("One item per row");
        oneItemPerRowBox
                .addValueChangeHandler(new ValueChangeHandler<Boolean>() {

                    @Override
                    public void onValueChange(ValueChangeEvent<Boolean> event) {
                        setTagCloud(!oneItemPerRowBox.getValue());
                    }

                });
        panel.add(oneItemPerRowBox);

        return panel;
    }

    @Override
    public Slot[] getSlots() {
        return new Slot[] { SlotResolver.DESCRIPTION_SLOT,
                SlotResolver.FONT_SIZE_SLOT };
    }

    private void initGroupValueMapper() {
        groupValueMapper = new DoubleToGroupValueMapper<String>(
                new EquidistantBinBoundaryCalculator(), CollectionUtils.toList(
                        "10px", "14px", "18px", "22px", "26px"));
    }

    public boolean isTagCloud() {
        return tagCloud;
    }

    @Override
    public void restore(Memento state) {
        // TODO implement
    }

    @Override
    public Memento save() {
        return new Memento(); // TODO implement
    }

    private void setTagCloud(boolean tagCloud) {
        if (tagCloud == this.tagCloud) {
            return;
        }

        this.tagCloud = tagCloud;
        display.setTagCloud(tagCloud);
    }

    @Override
    public void update(Set<ResourceItem> addedResourceItems,
            Set<ResourceItem> updatedResourceItems,
            Set<ResourceItem> removedResourceItems, Set<Slot> changedSlots) {

        for (ResourceItem resourceItem : addedResourceItems) {
            TextItem textItem = new TextItem(display, dragController,
                    resourceItem);
            display.addItem(textItem);
            resourceItem.setDisplayObject(textItem);
            textItem.updateContent();
            textItem.updateStatusStyling();
        }

        for (ResourceItem resourceItem : updatedResourceItems) {
            TextItem textItem = (TextItem) resourceItem.getDisplayObject();
            textItem.updateContent();
            textItem.updateStatusStyling();
        }

        for (ResourceItem resourceItem : removedResourceItems) {
            display.removeTextItem((TextItem) resourceItem.getDisplayObject());
        }

        if (!changedSlots.isEmpty()) {
            for (ResourceItem resourceItem : callback.getAllResourceItems()) {
                TextItem textItem = (TextItem) resourceItem.getDisplayObject();
                textItem.updateContent();
            }

        }

        display.updateFontSizes();

    }
}
