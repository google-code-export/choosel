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
package org.thechiselgroup.choosel.client.views.graph;

import static org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay.NODE_BACKGROUND_COLOR;
import static org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay.NODE_BORDER_COLOR;
import static org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay.NODE_FONT_COLOR;
import static org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay.NODE_FONT_WEIGHT;
import static org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay.NODE_FONT_WEIGHT_BOLD;
import static org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay.NODE_FONT_WEIGHT_NORMAL;
import static org.thechiselgroup.choosel.client.views.graph.GraphViewContentDisplay.NODE_BACKGROUND_COLOR_SLOT;
import static org.thechiselgroup.choosel.client.views.graph.GraphViewContentDisplay.NODE_BORDER_COLOR_SLOT;
import static org.thechiselgroup.choosel.client.views.graph.GraphViewContentDisplay.NODE_LABEL_SLOT;

import org.thechiselgroup.choosel.client.ui.Colors;
import org.thechiselgroup.choosel.client.ui.widget.graph.Node;
import org.thechiselgroup.choosel.client.views.DefaultResourceItem;
import org.thechiselgroup.choosel.client.views.ResourceItem;
import org.thechiselgroup.choosel.client.views.graph.GraphViewContentDisplay.Display;

public class GraphItem {

    private Display display;

    private Node node;

    private final ResourceItem resourceItem;

    public GraphItem(ResourceItem resourceItem, String category,
            GraphViewContentDisplay.Display display) {

        assert resourceItem != null;
        assert category != null;
        assert display != null;

        this.resourceItem = resourceItem;
        this.display = display;

        node = new Node(resourceItem.getGroupID(), getLabelValue(), category);
    }

    public String getLabelValue() {
        return (String) resourceItem.getResourceValue(NODE_LABEL_SLOT);
    }

    public Node getNode() {
        return node;
    }

    public String getNodeBackgroundColorValue() {
        return (String) resourceItem
                .getResourceValue(NODE_BACKGROUND_COLOR_SLOT);
    }

    public String getNodeBorderColorValue() {
        return (String) resourceItem.getResourceValue(NODE_BORDER_COLOR_SLOT);
    }

    public void updateNode(DefaultResourceItem.Status status) {
        switch (status) {
        case PARTIALLY_HIGHLIGHTED_SELECTED:
        case HIGHLIGHTED_SELECTED: {
            display.setNodeStyle(node, NODE_BACKGROUND_COLOR, Colors.YELLOW_1);
            display.setNodeStyle(node, NODE_BORDER_COLOR, Colors.ORANGE);
            display.setNodeStyle(node, NODE_FONT_COLOR, Colors.ORANGE);
            display.setNodeStyle(node, NODE_FONT_WEIGHT, NODE_FONT_WEIGHT_BOLD);
        }
            break;
        case PARTIALLY_HIGHLIGHTED:
        case HIGHLIGHTED: {
            display.setNodeStyle(node, NODE_BACKGROUND_COLOR, Colors.YELLOW_1);
            display.setNodeStyle(node, NODE_BORDER_COLOR, Colors.YELLOW_2);
            display.setNodeStyle(node, NODE_FONT_COLOR, Colors.BLACK);
            display.setNodeStyle(node, NODE_FONT_WEIGHT,
                    NODE_FONT_WEIGHT_NORMAL);
        }
            break;
        case DEFAULT: {
            display.setNodeStyle(node, NODE_BACKGROUND_COLOR,
                    getNodeBackgroundColorValue());
            display.setNodeStyle(node, NODE_BORDER_COLOR,
                    getNodeBorderColorValue());
            display.setNodeStyle(node, NODE_FONT_COLOR, Colors.BLACK);
            display.setNodeStyle(node, NODE_FONT_WEIGHT,
                    NODE_FONT_WEIGHT_NORMAL);
        }
            break;
        case SELECTED: {
            display.setNodeStyle(node, NODE_BACKGROUND_COLOR,
                    getNodeBackgroundColorValue());
            display.setNodeStyle(node, NODE_BORDER_COLOR, Colors.ORANGE);
            display.setNodeStyle(node, NODE_FONT_COLOR, Colors.ORANGE);
            display.setNodeStyle(node, NODE_FONT_WEIGHT, NODE_FONT_WEIGHT_BOLD);
        }
            break;
        }
    }

}