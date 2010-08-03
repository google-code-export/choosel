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
package org.thechiselgroup.choosel.client.views;

import java.util.ArrayList;
import java.util.List;

import org.thechiselgroup.choosel.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourcesAddedEvent;
import org.thechiselgroup.choosel.client.resources.ResourcesAddedEventHandler;
import org.thechiselgroup.choosel.client.resources.ResourcesRemovedEvent;
import org.thechiselgroup.choosel.client.resources.ResourcesRemovedEventHandler;
import org.thechiselgroup.choosel.client.ui.popup.PopupClosingEvent;
import org.thechiselgroup.choosel.client.ui.popup.PopupClosingHandler;
import org.thechiselgroup.choosel.client.ui.popup.PopupManager;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

// TODO separate out resource item controller
public class ResourceItem {

    public static enum Status {

        DEFAULT, GRAYED_OUT, HIGHLIGHTED, HIGHLIGHTED_SELECTED, SELECTED

    }

    private String category;

    private boolean highlighted = false;

    protected final HoverModel hoverModel;

    protected final PopupManager popupManager;

    // TODO update & paint on changes in resources!!!
    private final ResourceSet resources;

    private boolean selected;

    private boolean selectionStatusVisible;

    private final ResourceItemValueResolver valueResolver;

    private HighlightingManager highlightingManager;

    /**
     * The representation of this resource item in the specific display. This is
     * set by the display to enable fast reference to this display element, and
     * should be casted into the specific type.
     * 
     * TODO dispose
     */
    private Object displayObject;

    private ResourceSet highlightedResources;

    public ResourceItem(String category, ResourceSet resources,
            HoverModel hoverModel, PopupManager popupManager,
            ResourceItemValueResolver valueResolver) {

        assert category != null;
        assert resources != null;
        assert hoverModel != null;
        assert popupManager != null;
        assert valueResolver != null;

        this.category = category;
        this.resources = resources;
        this.popupManager = popupManager;
        this.hoverModel = hoverModel; // TODO separate controller
        this.valueResolver = valueResolver;

        this.highlightedResources = new DefaultResourceSet();

        initHighlighting();
        initPopupHighlighting();
        initResourceEventHandling(resources);

        updateContent();
    }

    public void addHighlightedResources(ResourceSet highlightedResources) {
        assert highlightedResources != null;

        List<Resource> resourcesToAdd = new ArrayList<Resource>();
        resourcesToAdd.addAll(highlightedResources.toList());
        resourcesToAdd.retainAll(resources.toList());

        this.highlightedResources.addAll(resourcesToAdd);
    }

    public Status calculateStatus() {
        if (isHighlighted() && isSelected()) {
            return Status.HIGHLIGHTED_SELECTED;
        }

        if (isHighlighted()) {
            return Status.HIGHLIGHTED;
        }

        if (isSelected()) {
            return Status.SELECTED;
        }

        return Status.DEFAULT;
    }

    protected Status calculateStatusNormalVsGraySelection() {
        if (isHighlighted()) {
            return Status.HIGHLIGHTED;
        }

        if (selectionStatusVisible && isSelected()) {
            return Status.DEFAULT;
        }

        if (selectionStatusVisible) {
            return Status.GRAYED_OUT;
        }

        return Status.DEFAULT;
    }

    public Object getDisplayObject() {
        return displayObject;
    }

    public ResourceSet getHighlightedResources() {
        return highlightedResources;
    }

    /**
     * @return highlighting manager that manages the hightlighting for this
     *         visual representation of the resource item. For the popup, there
     *         is a separate highlighting manager.
     */
    public HighlightingManager getHighlightingManager() {
        return highlightingManager;
    }

    public final PopupManager getPopupManager() {
        return popupManager;
    }

    public ResourceSet getResourceSet() {
        return resources;
    }

    public Object getResourceValue(String slotID) {
        return valueResolver.resolve(slotID, category, resources);
    }

    private void initHighlighting() {
        this.highlightingManager = new HighlightingManager(hoverModel,
                resources);
    }

    private void initPopupHighlighting() {
        final HighlightingManager highlightingManager = new HighlightingManager(
                hoverModel, resources);

        popupManager.addPopupMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent e) {
                highlightingManager.setHighlighting(true);
            }
        });
        popupManager.addPopupMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                highlightingManager.setHighlighting(false);
            }
        });
        popupManager.addPopupClosingHandler(new PopupClosingHandler() {
            @Override
            public void onPopupClosing(PopupClosingEvent event) {
                highlightingManager.setHighlighting(false);
            }
        });
    }

    private void initResourceEventHandling(ResourceSet resources) {
        resources.addEventHandler(new ResourcesAddedEventHandler() {
            @Override
            public void onResourcesAdded(ResourcesAddedEvent e) {
                updateContent();
            }
        });
        resources.addEventHandler(new ResourcesRemovedEventHandler() {
            @Override
            public void onResourcesRemoved(ResourcesRemovedEvent e) {
                updateContent();
            }
        });
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public boolean isSelected() {
        return selected;
    }

    public void removeHighlightedResources(ResourceSet highlightedResources) {
        assert highlightedResources != null;

        List<Resource> resourcesToRemove = new ArrayList<Resource>();
        resourcesToRemove.addAll(highlightedResources.toList());
        resourcesToRemove.retainAll(resources.toList());

        this.highlightedResources.removeAll(resourcesToRemove);
    }

    public void setDisplayObject(Object displayObject) {
        this.displayObject = displayObject;
    }

    // TODO introduce partial highlighting
    public void setHighlighted(boolean highlighted) {
        if (this.highlighted == highlighted) {
            return;
        }

        this.highlighted = highlighted;
    }

    public void setSelected(boolean selected) {
        if (this.selected == selected) {
            return;
        }

        this.selected = selected;
        updateStyling();
    }

    public void setSelectionStatusVisible(boolean visible) {
        if (this.selectionStatusVisible == visible) {
            return;
        }

        this.selectionStatusVisible = visible;
        updateStyling();
    }

    /**
     * Should be overridden by subclasses to apply correct styling to the visual
     * representation. Gets called whenever status (highlighting, selecting,
     * etc.) changes.
     * 
     * @param status
     *            Current status of resource item.
     */
    protected void setStatusStyling(Status status) {

    }

    /**
     * Should be overridden by subclasses to update the content of the visual
     * representation. Gets called whenever the underlying resource set changes.
     */
    protected void updateContent() {
    }

    protected void updateStyling() {
        setStatusStyling(calculateStatus());
    }
}