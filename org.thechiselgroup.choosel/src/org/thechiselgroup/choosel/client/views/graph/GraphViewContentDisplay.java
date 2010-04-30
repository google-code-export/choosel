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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.thechiselgroup.choosel.client.command.CommandManager;
import org.thechiselgroup.choosel.client.configuration.ChooselInjectionConstants;
import org.thechiselgroup.choosel.client.domain.ncbo.NcboUriHelper;
import org.thechiselgroup.choosel.client.error_handling.ErrorHandler;
import org.thechiselgroup.choosel.client.geometry.Point;
import org.thechiselgroup.choosel.client.persistence.Memento;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceCategorizer;
import org.thechiselgroup.choosel.client.resources.ResourceManager;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ui.DetailsWidgetHelper;
import org.thechiselgroup.choosel.client.ui.WidgetAdaptable;
import org.thechiselgroup.choosel.client.ui.popup.PopupManager;
import org.thechiselgroup.choosel.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.client.ui.widget.graph.Arc;
import org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay;
import org.thechiselgroup.choosel.client.ui.widget.graph.GraphWidget;
import org.thechiselgroup.choosel.client.ui.widget.graph.GraphWidgetReadyEvent;
import org.thechiselgroup.choosel.client.ui.widget.graph.GraphWidgetReadyHandler;
import org.thechiselgroup.choosel.client.ui.widget.graph.Node;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeDragEvent;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeDragHandleMouseDownEvent;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeDragHandleMouseDownHandler;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeDragHandleMouseMoveEvent;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeDragHandleMouseMoveHandler;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeDragHandler;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeEvent;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeMenuItemClickedHandler;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeMouseClickEvent;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeMouseClickHandler;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeMouseOutEvent;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeMouseOutHandler;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeMouseOverEvent;
import org.thechiselgroup.choosel.client.ui.widget.graph.NodeMouseOverHandler;
import org.thechiselgroup.choosel.client.views.AbstractViewContentDisplay;
import org.thechiselgroup.choosel.client.views.DragEnabler;
import org.thechiselgroup.choosel.client.views.DragEnablerFactory;
import org.thechiselgroup.choosel.client.views.Layer;
import org.thechiselgroup.choosel.client.views.ResourceItem;
import org.thechiselgroup.choosel.client.views.SlotResolver;
import org.thechiselgroup.choosel.client.views.ViewContentDisplayCallback;

import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.name.Named;

// TODO separate out ncbo specific stuff and service calls
// TODO register listener for double click on node --> change expansion state
public class GraphViewContentDisplay extends AbstractViewContentDisplay
	implements GraphNodeExpansionCallback {

    public static class DefaultDisplay extends GraphWidget implements Display {

	// TODO why is size needed in the first place??
	public DefaultDisplay() {
	    this(400, 300);
	}

	public DefaultDisplay(int width, int height) {
	    super(width, height);
	}

	@Override
	public Widget asWidget() {
	    return this;
	}

    }

    public static interface Display extends WidgetAdaptable, GraphDisplay {
    }

    private class GraphEventHandler extends ViewToIndividualItemEventForwarder
	    implements NodeMouseOverHandler, NodeMouseOutHandler,
	    NodeMouseClickHandler, MouseMoveHandler, NodeDragHandler,
	    NodeDragHandleMouseDownHandler, NodeDragHandleMouseMoveHandler {

	// XXX find cleaner solution that maps to nodes
	private DragEnabler dragEnabler;

	@Override
	public void onDrag(NodeDragEvent event) {
	    commandManager.addExecutedCommand(new MoveNodeCommand(display,
		    event.getNode(), new Point(event.getStartX(), event
			    .getStartY()), new Point(event.getEndX(), event
			    .getEndY())));
	}

	@Override
	public void onMouseClick(NodeMouseClickEvent event) {
	    getCallback().switchSelection(getGraphItem(event).getResource());
	}

	@Override
	public void onMouseDown(NodeDragHandleMouseDownEvent event) {
	    dragEnabler = dragEnablerFactory
		    .createDragEnabler(getGraphItem(event.getNode()));

	    dragEnabler.createTransparentDragProxy(event.getMouseX()
		    + asWidget().getAbsoluteLeft(), event.getMouseY()
		    + asWidget().getAbsoluteTop());
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
	    // TODO restrict to mouse move for current graph item
	    Collection<GraphItem> values = nodeIdToGraphItemMap.values();
	    for (GraphItem graphItem : values) {
		// TODO relative to root pane instead of client area
		graphItem.getPopupManager().onMouseMove(event.getClientX(),
			event.getClientY());
	    }
	}

	@Override
	public void onMouseMove(NodeDragHandleMouseMoveEvent event) {
	    dragEnabler.forwardMouseMove(event.getMouseX()
		    + asWidget().getAbsoluteLeft(), event.getMouseY()
		    + asWidget().getAbsoluteTop());
	}

	@Override
	public void onMouseOut(NodeMouseOutEvent event) {
	    onMouseOut(getGraphItem(event), event.getMouseX(), event
		    .getMouseY());
	}

	@Override
	public void onMouseOver(NodeMouseOverEvent event) {
	    onMouseOver(getGraphItem(event), event.getMouseX(), event
		    .getMouseY());
	}

    }

    // TODO move to ncbo stuff
    public static final String ARC_TYPE_MAPPING = "mapping";

    private static final String MEMENTO_X = "x";

    // advanced node class: (incoming, outgoing, expanded: state machine)

    private static final String MEMENTO_Y = "y";

    private ArcStyleProvider arcStyleProvider;

    private final CommandManager commandManager;

    private final NeighbourhoodServiceAsync conceptNeighbourhoodService;

    private final Display display;

    public DragEnablerFactory dragEnablerFactory;

    private ErrorHandler errorHandler;

    private final NeighbourhoodServiceAsync mappingNeighbourhoodService;

    private final Map<String, GraphItem> nodeIdToGraphItemMap = new HashMap<String, GraphItem>();

    private boolean ready = false;

    private ResourceCategorizer resourceCategorizer;

    private ResourceManager resourceManager;

    @Inject
    public GraphViewContentDisplay(
	    Display display,
	    @Named(ChooselInjectionConstants.HOVER_MODEL) ResourceSet hoverModel,
	    @Named("mapping") NeighbourhoodServiceAsync mappingService,
	    @Named("concept") NeighbourhoodServiceAsync conceptNeighbourhoodService,
	    PopupManagerFactory popupManagerFactory,
	    DetailsWidgetHelper detailsWidgetHelper,
	    CommandManager commandManager, ResourceManager resourceManager,
	    ErrorHandler errorHandler, DragEnablerFactory dragEnablerFactory,
	    ResourceCategorizer resourceCategorizer,
	    ArcStyleProvider arcStyleProvider) {

	super(popupManagerFactory, detailsWidgetHelper, hoverModel);

	assert display != null;
	assert mappingService != null;
	assert conceptNeighbourhoodService != null;
	assert commandManager != null;
	assert resourceManager != null;
	assert errorHandler != null;
	assert dragEnablerFactory != null;
	assert resourceCategorizer != null;
	assert arcStyleProvider != null;

	this.arcStyleProvider = arcStyleProvider;
	this.resourceCategorizer = resourceCategorizer;
	this.display = display;
	this.mappingNeighbourhoodService = mappingService;
	this.conceptNeighbourhoodService = conceptNeighbourhoodService;
	this.commandManager = commandManager;
	this.resourceManager = resourceManager;
	this.errorHandler = errorHandler;
	this.dragEnablerFactory = dragEnablerFactory;
    }

    @Override
    public void checkResize() {
    }

    public void createArc(String arcType, String sourceId, String targetId) {
	Arc arc = new Arc(getArcId(arcType, sourceId, targetId), sourceId,
		targetId, arcType);

	display.addArc(arc);

	display.setArcStyle(arc, GraphDisplay.ARC_COLOR, arcStyleProvider
		.getArcColor(arcType));
	display.setArcStyle(arc, GraphDisplay.ARC_STYLE, arcStyleProvider
		.getArcStyle(arcType));
    }

    @Override
    public GraphItem createResourceItem(Layer layer, Resource resource) {
	assert layer != null;
	assert resource != null;

	String label = layer.getValue(SlotResolver.GRAPH_LABEL_SLOT, resource);
	String category = getCategory(resource);
	PopupManager popupManager = createPopupManager(resource, layer
		.getResolver(SlotResolver.DESCRIPTION_SLOT));

	GraphItem item = new GraphItem(resource, hoverModel, popupManager,
		label, category, display, layer);

	nodeIdToGraphItemMap.put(item.getNode().getId(), item);

	display.addNode(item.getNode());
	positionNode(item.getNode());

	// TODO remove once new drag and drop mechanism works...
	display.setNodeStyle(item.getNode(), "showDragImage", "true");

	setGraphItemColors(resource, item);

	if (category.equals(NcboUriHelper.NCBO_CONCEPT)) {
	    // TODO this should be false if set of available neighbourhoods
	    // equals 0
	    display.setNodeStyle(item.getNode(), "showArrow", "true");
	}
	if (category.equals(NcboUriHelper.NCBO_MAPPING)) {
	    // TODO this should be false if set of available neighbourhoods
	    // equals 0
	    display.setNodeStyle(item.getNode(), "showArrow", "true");
	}

	if (NcboUriHelper.NCBO_CONCEPT.equals(category)) {
	    new AutomaticConceptExpander(mappingNeighbourhoodService,
		    errorHandler).expand(resource, this);
	}

	if (NcboUriHelper.NCBO_MAPPING.equals(category)) {
	    new AutomaticMappingExpander().expand(resource, this);
	}

	return item;
    }

    // TODO encapsulate in display, use dependency injection
    @Override
    protected Widget createWidget() {
	return display.asWidget();
    }

    public String getArcId(String arcType, String sourceId, String targetId) {
	// FIXME this needs escaping of special characters to work properly
	return arcType + ":" + sourceId + "_" + targetId;
    }

    public String getCategory(Resource resource) {
	return resourceCategorizer.getCategory(resource);
    }

    public Display getDisplay() {
	return display;
    }

    private GraphItem getGraphItem(Node node) {
	return nodeIdToGraphItemMap.get(node.getId());
    }

    private GraphItem getGraphItem(NodeEvent<?> event) {
	return getGraphItem(event.getNode());
    }

    private Resource getResource(Node node) {
	return getGraphItem(node).getResource();
    }

    public ResourceManager getResourceManager() {
	return resourceManager;
    }

    @Override
    public String[] getSlotIDs() {
	return new String[] { SlotResolver.DESCRIPTION_SLOT,
		SlotResolver.GRAPH_LABEL_SLOT };
    }

    @Override
    public void init(ViewContentDisplayCallback callback) {
	super.init(callback);

	display.addGraphWidgetReadyHandler(new GraphWidgetReadyHandler() {
	    @Override
	    public void onWidgetReady(GraphWidgetReadyEvent event) {
		ready = true;

		GraphEventHandler handler = new GraphEventHandler();

		display.addEventHandler(NodeDragHandleMouseDownEvent.TYPE,
			handler);
		display.addEventHandler(NodeMouseOverEvent.TYPE, handler);
		display.addEventHandler(NodeMouseOutEvent.TYPE, handler);
		display.addEventHandler(NodeMouseClickEvent.TYPE, handler);
		display.addEventHandler(NodeDragEvent.TYPE, handler);
		display.addEventHandler(MouseMoveEvent.getType(), handler);

		// TODO inject from external structure
		registerNodeMenuItem(NcboUriHelper.NCBO_CONCEPT, "Concepts",
			new ConceptConceptNeighbourhoodExpander(
				conceptNeighbourhoodService, errorHandler));

		registerNodeMenuItem(NcboUriHelper.NCBO_CONCEPT, "Mappings",
			new ConceptMappingNeighbourhoodExpander(
				mappingNeighbourhoodService, errorHandler));

		registerNodeMenuItem(NcboUriHelper.NCBO_MAPPING, "Concepts",
			new MappingExpander());
	    }
	});
    }

    @Override
    public boolean isReady() {
	return ready;
    }

    private void positionNode(Node node) {
	// FIXME positioning: FlexVis takes care of positioning nodes into empty
	// space except for first node - if the node is the first node, we put
	// it in the center
	// TODO improve interface to access all resources
	Iterator<Resource> it = getCallback().getAllResources().iterator();

	if (!it.hasNext()) {
	    return; // for tests
	}

	it.next(); // there are already some nodes (this one is already added)
	if (it.hasNext()) {
	    return; // there are already some nodes (this one is already added)
	}

	Widget displayWidget = display.asWidget();

	if (displayWidget == null) {
	    return; // for tests
	}

	int height = displayWidget.getOffsetHeight();
	int width = displayWidget.getOffsetWidth();

	display.setLocation(node, new Point(width / 2, height / 2));
    }

    private void registerNodeMenuItem(String category, String menuLabel,
	    final GraphNodeExpander nodeExpander) {

	display.addNodeMenuItemHandler(menuLabel,
		new NodeMenuItemClickedHandler() {
		    @Override
		    public void onNodeMenuItemClicked(Node node) {
			nodeExpander.expand(getResource(node),
				GraphViewContentDisplay.this);
		    }
		}, category);
    }

    @Override
    public void removeResourceItem(ResourceItem resourceItem) {
	nodeIdToGraphItemMap.remove(((GraphItem) resourceItem).getNode()
		.getId());
	display.removeNode(((GraphItem) resourceItem).getNode());
    }

    @Override
    public void restore(Memento state) {
	Iterable<Resource> allResources = getCallback().getAllResources();
	for (Resource resource : allResources) {
	    GraphItem item = (GraphItem) getCallback()
		    .getResourceItem(resource);
	    Memento nodeMemento = state.getChild(resource.getUri());
	    Point location = new Point((Integer) nodeMemento
		    .getValue(MEMENTO_X), (Integer) nodeMemento
		    .getValue(MEMENTO_Y));

	    display.setLocation(item.getNode(), location);
	}
    }

    @Override
    public Memento save() {
	Memento state = new Memento();
	Iterable<Resource> allResources = getCallback().getAllResources();
	for (Resource resource : allResources) {
	    GraphItem item = (GraphItem) getCallback()
		    .getResourceItem(resource);
	    Point location = display.getLocation(item.getNode());

	    Memento nodeMemento = new Memento();
	    nodeMemento.setValue(MEMENTO_X, location.x);
	    nodeMemento.setValue(MEMENTO_Y, location.y);

	    state.addChild(resource.getUri(), nodeMemento);
	}
	return state;
    }

    private void setGraphItemColors(Resource resource, GraphItem item) {
	// TODO use dependency injection
	String category = getCategory(resource);

	if (category.equals(NcboUriHelper.NCBO_CONCEPT)) {
	    String backgroundColor = "#DAE5F3";
	    String borderColor = "#AFC6E5";
	    item.setDefaultColors(backgroundColor, borderColor);
	}

	if (category.equals(NcboUriHelper.NCBO_MAPPING)) {
	    String backgroundColor = "#E4E4E4";
	    String borderColor = "#D4D4D4";
	    item.setDefaultColors(backgroundColor, borderColor);
	}
    }
}
