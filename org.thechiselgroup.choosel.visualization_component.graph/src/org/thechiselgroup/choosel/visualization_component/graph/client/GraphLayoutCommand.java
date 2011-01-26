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
package org.thechiselgroup.choosel.visualization_component.graph.client;

import java.util.List;
import java.util.Map;

import org.thechiselgroup.choosel.core.client.command.UndoableCommand;
import org.thechiselgroup.choosel.core.client.geometry.Point;
import org.thechiselgroup.choosel.core.client.util.HasDescription;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.GraphDisplay;
import org.thechiselgroup.choosel.visualization_component.graph.client.widget.Node;

// TODO store results after execute for redo because some graph layouts are non-deterministic
// TODO use animations (requires extending the flex graph interface) 
public class GraphLayoutCommand implements HasDescription, UndoableCommand {

    private GraphDisplay display;

    private String layout;

    private List<Node> nodes;

    private Map<String, Point> nodeLocations = CollectionFactory
            .createStringMap();

    public GraphLayoutCommand(GraphDisplay display, String layout,
            List<Node> nodes) {

        this.display = display;
        this.layout = layout;
        this.nodes = nodes;
    }

    @Override
    public void execute() {
        for (Node node : nodes) {
            nodeLocations.put(node.getId(), display.getLocation(node));
        }

        display.runLayout(layout);
    }

    @Override
    public String getDescription() {
        return "Graph layout " + layout;
    }

    @Override
    public void undo() {
        for (Node node : nodes) {
            display.setLocation(node, nodeLocations.get(node.getId()));
        }
    }

}
