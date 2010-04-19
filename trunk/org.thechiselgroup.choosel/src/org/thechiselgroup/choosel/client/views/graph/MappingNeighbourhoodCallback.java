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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.thechiselgroup.choosel.client.domain.ncbo.NCBO;
import org.thechiselgroup.choosel.client.domain.ncbo.NcboUriHelper;
import org.thechiselgroup.choosel.client.error_handling.ErrorHandler;
import org.thechiselgroup.choosel.client.geometry.Point;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.ui.widget.graph.Arc;
import org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay;
import org.thechiselgroup.choosel.client.ui.widget.graph.Node;
import org.thechiselgroup.choosel.client.views.ViewContentDisplayCallback;

// TODO check for existing references, existing mappings
// TODO Also automatically load neighbourhood to display links
// TODO 2 modes of callbacks: show // not show
public class MappingNeighbourhoodCallback extends AbstractNeighbourhoodCallback {

    public MappingNeighbourhoodCallback(GraphDisplay graph,
	    ViewContentDisplayCallback contentDisplayCallback,
	    ErrorHandler errorHandler) {

	super(graph, contentDisplayCallback, errorHandler);
    }

    private void addRelationshipArcs(List<Relationship> displayableRelationships) {
	for (Relationship relationship : displayableRelationships) {
	    // FIXME concept short ids vs concept ids
	    // FIXME check for duplicates
	    // FIXME have real arc id
	    String sourceId = relationship.getSource().getUri();
	    String targetId = relationship.getTarget().getUri();

	    Arc arc = new Arc(getArcId(sourceId, targetId), sourceId, targetId,
		    "mapping");

	    graph.addArc(arc);
	    graph.setArcStyle(arc, GraphDisplay.ARC_COLOR, "#D4D4D4");
	    graph.setArcStyle(arc, GraphDisplay.ARC_STYLE,
		    GraphDisplay.ARC_STYLE_DASHED);
	}
    }

    // for test
    protected String getArcId(String sourceId, String targetId) {
	return sourceId + "_" + targetId;
    }

    // filter mappings: check if source & target node are contained
    private List<Resource> calculateDisplayableMappings(
	    NeighbourhoodServiceResult result) {

	Set<Resource> neighbours = result.getNeighbours();
	List<Resource> displayableMappings = new ArrayList<Resource>();
	for (Resource resource : neighbours) {
	    if (resource.getUri().startsWith(NcboUriHelper.NCBO_MAPPING)) {
		// we have a mapping --> check if source and target are
		// contained
		String sourceUri = (String) resource
			.getValue(NCBO.MAPPING_SOURCE);
		String destinationUri = (String) resource
			.getValue(NCBO.MAPPING_DESTINATION);

		if (containsUri(sourceUri) && containsUri(destinationUri)
			&& !contains(resource)) {
		    displayableMappings.add(resource);
		}
	    }
	}
	return displayableMappings;
    }

    private List<Relationship> calculateDisplayableRelationships(
	    List<Relationship> relationships) {

	List<Relationship> result = new ArrayList<Relationship>();

	for (Relationship mapping : relationships) {
	    String destinationUri = mapping.getTarget().getUri();
	    String sourceUri = mapping.getSource().getUri();

	    if (containsUri(sourceUri) && containsUri(destinationUri)
		    && !graph.containsArc(getArcId(sourceUri, destinationUri))) {
		result.add(mapping);
	    }
	}

	return result;
    }

    @Override
    public void onSuccess(NeighbourhoodServiceResult result) {
	List<Resource> displayableMappings = calculateDisplayableMappings(result);
	addResources(displayableMappings);
	List<Relationship> displayableRelationships = calculateDisplayableRelationships(result
		.getRelationships());
	addRelationshipArcs(displayableRelationships);
	layoutNodes(displayableMappings);
    }

    protected void layoutNodes(List<Resource> displayableMappings) {
	// for displayable mappings: find connected node position, calculate,
	// intermediate distance, position, layout

	List<Node> nodesToLayout = new ArrayList<Node>();
	for (Resource resource : displayableMappings) {
	    Node sourceNode = getRelatedNode(resource, NCBO.MAPPING_SOURCE);
	    Point sourceLocation = graph.getLocation(sourceNode);

	    Node targetNode = getRelatedNode(resource, NCBO.MAPPING_DESTINATION);
	    Point targetLocation = graph.getLocation(targetNode);

	    Point intermediateLocation = new Point(
		    (sourceLocation.x + targetLocation.x) / 2,
		    (sourceLocation.y + targetLocation.y) / 2);

	    Node node = getNode(resource);
	    graph.setLocation(node, intermediateLocation);
	    nodesToLayout.add(node);
	}

	graph.layOutNodes(nodesToLayout);
    }

    private Node getRelatedNode(Resource resource, String key) {
	Node sourceNode = getNode(contentDisplayCallback
		.getResourceByUri((String) resource.getValue(key)));
	return sourceNode;
    }
}