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

import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.ui.popup.PopupManager;

public abstract class IconResourceItem extends ResourceItem {

    protected static final String CSS_RESOURCE_ITEM_ICON = "resourceItemIcon";

    protected static final int Z_INDEX_DEFAULT = 5;

    protected static final int Z_INDEX_HIGHLIGHTED = 20;

    protected static final int Z_INDEX_SELECTED = 10;

    private String defaultColor;

    private String highlightColor;

    private String selectedColor;

    public IconResourceItem(String category, ResourceSet resources,
            HoverModel hoverModel, PopupManager popupManager,
            ResourceItemValueResolver layerModel) {

        super(category, resources, hoverModel, popupManager, layerModel);
        initColors();
    }

    // XXX likely to break, needs refactoring
    protected String calculateBorderColor(String color) {
        if ("#FDF49A".equals(color)) {
            return "rgb(212, 208, 200)"; // highlight
        } else if ("#E7B076".equals(color)) {
            return "#7D5F40"; // selection
        } else if ("#6495ed".equals(color)) {
            return "rgb(68, 117, 149)"; // first color from default slot
                                        // resolver
        } else if ("#b22222".equals(color)) {
            return "#740B0B"; // 2nd color from default slot resolver
        } else {
            return "darkgray"; // default
        }
    }

    protected String getDefaultColor() {
        return defaultColor;
    }

    protected String getHighlightColor() {
        return highlightColor;
    }

    protected String getSelectedColor() {
        return selectedColor;
    }

    private void initColors() {
        // TODO move colors to color provider
        // TODO add border (should be automatically calculated based on color)
        defaultColor = (String) getResourceValue(SlotResolver.COLOR_SLOT);
        highlightColor = "#FDF49A";
        selectedColor = "#E7B076";
    }

}