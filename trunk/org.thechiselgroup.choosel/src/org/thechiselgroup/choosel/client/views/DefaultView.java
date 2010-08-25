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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.thechiselgroup.choosel.client.configuration.ChooselInjectionConstants;
import org.thechiselgroup.choosel.client.label.LabelProvider;
import org.thechiselgroup.choosel.client.persistence.Memento;
import org.thechiselgroup.choosel.client.persistence.Persistable;
import org.thechiselgroup.choosel.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceCategoriesChangedEvent;
import org.thechiselgroup.choosel.client.resources.ResourceCategoriesChangedHandler;
import org.thechiselgroup.choosel.client.resources.ResourceCategoryChange;
import org.thechiselgroup.choosel.client.resources.ResourceMultiCategorizer;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSetEventForwarder;
import org.thechiselgroup.choosel.client.resources.ResourceSetFactory;
import org.thechiselgroup.choosel.client.resources.ResourceSplitter;
import org.thechiselgroup.choosel.client.resources.ResourcesAddedEvent;
import org.thechiselgroup.choosel.client.resources.ResourcesAddedEventHandler;
import org.thechiselgroup.choosel.client.resources.ResourcesRemovedEvent;
import org.thechiselgroup.choosel.client.resources.ResourcesRemovedEventHandler;
import org.thechiselgroup.choosel.client.resources.SwitchingResourceSet;
import org.thechiselgroup.choosel.client.resources.persistence.ResourceSetAccessor;
import org.thechiselgroup.choosel.client.resources.persistence.ResourceSetCollector;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetsPresenter;
import org.thechiselgroup.choosel.client.ui.CSS;
import org.thechiselgroup.choosel.client.ui.WidgetFactory;
import org.thechiselgroup.choosel.client.ui.popup.DefaultPopupManager;
import org.thechiselgroup.choosel.client.util.Disposable;
import org.thechiselgroup.choosel.client.util.HandlerRegistrationSet;
import org.thechiselgroup.choosel.client.util.Initializable;
import org.thechiselgroup.choosel.client.windows.AbstractWindowContent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DefaultView extends AbstractWindowContent implements View {

    private class MainPanel extends DockPanel implements ViewProvider {

        /**
         * Enables finding this view by searching the widget hierarchy.
         */
        @Override
        public View get() {
            return DefaultView.this;
        }

        @Override
        protected void onDetach() {
            dispose();
            super.onDetach();
        }

        /*
         * Fixes bug that map is not resized if window is resized
         */
        @Override
        public void setPixelSize(int width, int height) {
            resize(width, height);
            super.setPixelSize(width, height);
        }

    }

    private static final String CSS_VIEW_CONFIGURATION_PANEL = "view-configurationPanel";

    private static final String MEMENTO_CONTENT_DISPLAY = "contentDisplay";

    static final String MEMENTO_SELECTION = "selection";

    static final String MEMENTO_SELECTION_SET_COUNT = "selectionSetCount";

    static final String MEMENTO_SELECTION_SET_PREFIX = "selectionSet-";

    private ResourceSetEventForwarder allResourcesToSplitterForwarder;

    /**
     * Maps category names (representing the resource sets that are calculated
     * by the resource splitter) to the resource items that display the resource
     * sets in the view.
     */
    private Map<String, ResourceItem> categoriesToResourceItems = new HashMap<String, ResourceItem>();

    private ResourceItemValueResolver configuration;

    private DockPanel viewPanel;

    private DockPanel configurationPanel;

    private ViewContentDisplay contentDisplay;

    private ViewContentDisplayCallback contentDisplayCallback;

    private HoverModel hoverModel;

    private DockPanel mainPanel;

    private ResourceSetFactory resourceSetFactory;

    private ResourceSplitter resourceSplitter;

    private SwitchingResourceSet selection;

    private ResourceSetsPresenter selectionDropPresenter;

    private LabelProvider selectionModelLabelFactory;

    private ResourceSetsPresenter selectionPresenter;

    private HandlerRegistration selectionResourceAddedHandlerRegistration;

    private HandlerRegistration selectionResourceRemovedHandlerRegistration;

    // XXX might not be necessary (use presenter instead?)
    private List<ResourceSet> selectionSets = new ArrayList<ResourceSet>();

    private ResourceModel resourceModel;

    private ResourceModelPresenter resourceModelPresenter;

    private static final String MEMENTO_RESOURCE_MODEL = "resource-model";

    private HandlerRegistrationSet handlerRegistrations = new HandlerRegistrationSet();

    private static final String IMAGE_VIEW_MENU = "images/menu.png";

    private static final String IMAGE_CONFIGURATION_MENU = "images/configuration.png";

    @Inject
    public DefaultView(
            @Named(ChooselInjectionConstants.LABEL_PROVIDER_SELECTION_SET) LabelProvider selectionModelLabelFactory,
            ResourceSetFactory resourceSetFactory,
            @Named(ChooselInjectionConstants.AVATAR_FACTORY_SELECTION) ResourceSetsPresenter selectionPresenter,
            @Named(ChooselInjectionConstants.AVATAR_FACTORY_SELECTION_DROP) ResourceSetsPresenter selectionDropPresenter,
            ResourceSplitter resourceSplitter,
            ViewContentDisplay contentDisplay, String label,
            String contentType, ResourceItemValueResolver configuration,
            ResourceModel resourceModel,
            ResourceModelPresenter resourceModelPresenter, HoverModel hoverModel) {

        super(label, contentType);

        assert configuration != null;
        assert selectionModelLabelFactory != null;
        assert resourceSetFactory != null;
        assert selectionPresenter != null;
        assert selectionDropPresenter != null;
        assert resourceSplitter != null;
        assert contentDisplay != null;
        assert resourceModel != null;
        assert resourceModelPresenter != null;
        assert hoverModel != null;

        this.configuration = configuration;
        this.selectionModelLabelFactory = selectionModelLabelFactory;
        this.resourceSetFactory = resourceSetFactory;
        this.selectionPresenter = selectionPresenter;
        this.selectionDropPresenter = selectionDropPresenter;
        this.resourceSplitter = resourceSplitter;
        this.contentDisplay = contentDisplay;
        this.resourceModel = resourceModel;
        this.resourceModelPresenter = resourceModelPresenter;
        this.hoverModel = hoverModel;
    }

    @Override
    public void addSelectionSet(ResourceSet selectionSet) {
        assert selectionSet != null;

        selectionSets.add(selectionSet);
        selectionPresenter.addResourceSet(selectionSet);
    }

    @Override
    public Widget asWidget() {
        return mainPanel;
    }

    private ResourceSet calculateAffectedResources(
            List<Resource> affectedResources) {

        Set<Resource> affectedResourcesInThisView = resourceModel
                .retain(affectedResources);

        // TODO ResourceSet should inherit Set<Resource>
        ResourceSet affectedResourcesInThisView2 = new DefaultResourceSet();
        affectedResourcesInThisView2.addAll(affectedResourcesInThisView);
        return affectedResourcesInThisView2;
    }

    private void checkResize() {
        contentDisplay.checkResize();
    }

    @Override
    public boolean containsSelectionSet(ResourceSet resourceSet) {
        return selectionSets.contains(resourceSet);
    }

    private ResourceItem createResourceItem(String category,
            ResourceSet resources) {

        // Added when changing resource item to contain resource sets
        // TODO use factory & dispose + clean up

        // TODO provide configuration to content display in callback
        ResourceItem resourceItem = contentDisplay.createResourceItem(
                configuration, category, resources, hoverModel);

        categoriesToResourceItems.put(category, resourceItem);

        // TODO introduce partial selection

        ResourceSet affectedResources = calculateAffectedResources(hoverModel
                .toList());
        if (!affectedResources.isEmpty()) {
            resourceItem.addHighlightedResources(affectedResources);
        }

        // / TODO is this necessary?
        // checkResize();

        return resourceItem;
    }

    @Override
    public void dispose() {
        Log.debug("dispose view " + toString());

        removeSelectionModelResourceHandlers();

        for (ResourceItem resourceItem : categoriesToResourceItems.values()) {
            resourceItem.dispose();
        }

        if (resourceModel instanceof Disposable) {
            ((Disposable) resourceModel).dispose();
        }
        resourceModel = null;
        allResourcesToSplitterForwarder.dispose();
        allResourcesToSplitterForwarder = null;
        contentDisplay.dispose();
        contentDisplay = null;
        selectionPresenter.dispose();
        selectionPresenter = null;
        selectionDropPresenter.dispose();
        selectionDropPresenter = null;
        resourceModelPresenter.dispose();
        resourceModelPresenter = null;
        selection.dispose();
        selection = null;
        hoverModel = null;

        handlerRegistrations.dispose();
        handlerRegistrations = null;
    }

    // protected for test access
    protected void doRestore(Memento state, ResourceSetAccessor accessor) {
        contentDisplay.startRestore();

        restoreResourceModel(state, accessor);
        restoreSelection(state, accessor);
        restoreContentDisplay(state);

        contentDisplay.endRestore();
    }

    public Map<String, ResourceSet> getCategorizedResourceSets() {
        return resourceSplitter.getCategorizedResourceSets();
    }

    protected String getModuleBase() {
        return GWT.getModuleBaseURL();
    }

    // TODO improve algorithm: switch depending on size of resource vs size of
    // resource items --> change to collection
    private Set<ResourceItem> getResourceItems(Iterable<Resource> resources) {
        assert resources != null;

        Set<ResourceItem> result = new HashSet<ResourceItem>();
        for (Resource resource : resources) {
            result.addAll(getResourceItems(resource));
        }
        return result;
    }

    private List<ResourceItem> getResourceItems(Resource resource) {
        assert resource != null;

        // TODO PERFORMANCE introduce field map: Resource --> List<ResourceItem>
        // such a map would need to be updated
        List<ResourceItem> result = new ArrayList<ResourceItem>();
        for (ResourceItem resourceItem : categoriesToResourceItems.values()) {
            if (resourceItem.getResourceSet().contains(resource)) {
                result.add(resourceItem);
            }
        }

        return result;
    }

    @Override
    public ResourceModel getResourceModel() {
        return resourceModel;
    }

    @Override
    public ResourceSet getSelection() {
        return selection.getDelegate();
    }

    // for test
    public List<ResourceSet> getSelectionSets() {
        return selectionSets;
    }

    @Override
    public void init() {
        if (this.resourceModel instanceof Initializable) {
            ((Initializable) this.resourceModel).init();
        }

        resourceModelPresenter.init();

        initResourceSplitter();
        initAllResourcesToResourceSplitterLink();
        initHoverModelHooks();

        initUI();

        initContentDisplay();
        initSelectionModel();
    }

    private void initAllResourcesToResourceSplitterLink() {
        allResourcesToSplitterForwarder = new ResourceSetEventForwarder(
                resourceModel.getResources(), resourceSplitter);
        allResourcesToSplitterForwarder.init();
    }

    private void initConfigurationMenu() {
        if (contentDisplay.getConfigurations().isEmpty()) {
            return;
        }

        Image image = new Image(getModuleBase() + IMAGE_CONFIGURATION_MENU);

        CSS.setMarginTopPx(image, 3);
        CSS.setMarginRightPx(image, 4);

        WidgetFactory widgetFactory = new WidgetFactory() {
            @Override
            public Widget createWidget() {
                VerticalPanel panel = new VerticalPanel();

                // TODO change styling of buttons
                panel.add(new HTML("<b>Configuration Menu</b>"));
                for (final ViewContentDisplayConfiguration action : contentDisplay
                        .getConfigurations()) {

                    Button w = new Button(action.getLabel());
                    w.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            action.execute();
                        }
                    });
                    panel.add(w);
                }

                return panel;
            }
        };

        DefaultPopupManager manager = new DefaultPopupManager(widgetFactory);
        DefaultPopupManager.linkManagerToSource(manager, image);
        // TODO activate menu on click with left mouse button
        // TODO change popup menu location

        configurationPanel.add(image, DockPanel.EAST);
        configurationPanel.setCellHorizontalAlignment(image,
                HasAlignment.ALIGN_RIGHT);
    }

    private void initConfigurationPanelUI() {
        configurationPanel = new DockPanel();
        configurationPanel.setSize("100%", "");
        configurationPanel.setStyleName(CSS_VIEW_CONFIGURATION_PANEL);

        initConfigurationMenu();
    }

    // TODO eliminate inner class, implement methods in DefaultView & test them
    private void initContentDisplay() {
        contentDisplayCallback = new ViewContentDisplayCallback() {

            @Override
            public boolean containsResource(Resource resource) {
                return resourceModel.getResources().containsResourceWithUri(
                        resource.getUri());
            }

            @Override
            public boolean containsResourceWithUri(String uri) {
                return resourceModel.getResources()
                        .containsResourceWithUri(uri);
            }

            @Override
            public Collection<ResourceItem> getAllResourceItems() {
                return categoriesToResourceItems.values();
            }

            @Override
            public Iterable<Resource> getAllResources() {
                return resourceModel.getResources();
            }

            @Override
            public ResourceSet getAutomaticResourceSet() {
                return resourceModel.getAutomaticResourceSet();
            }

            @Override
            public Resource getResourceByUri(String uri) {
                return resourceModel.getResources().getByUri(uri);
            }

            @Override
            public List<ResourceItem> getResourceItems(Resource resource) {
                return DefaultView.this.getResourceItems(resource);
            }

            @Override
            public void setCategorizer(ResourceMultiCategorizer categorizer) {
                resourceSplitter.setCategorizer(categorizer);
            }

            // TODO this means that we need a wrapper around resource set
            // to make this happen
            @Override
            public void switchSelection(ResourceSet resources) {
                // XXX HACK TODO cleanup --> we create selections when stuff
                // gets selected...
                if (!selection.hasDelegate()) {
                    ResourceSet set = resourceSetFactory.createResourceSet();
                    set.setLabel(selectionModelLabelFactory.nextLabel());
                    addSelectionSet(set);
                    setSelection(set);
                }

                assert selection != null;

                getSelection().switchContainment(resources);
            }
        };
        contentDisplay.init(contentDisplayCallback);
    }

    private void initHoverModelHooks() {
        handlerRegistrations.addHandlerRegistration(hoverModel
                .addEventHandler(new ResourcesAddedEventHandler() {
                    @Override
                    public void onResourcesAdded(ResourcesAddedEvent e) {
                        updateHighlighting(e.getAddedResources(), true);
                    }

                }));
        handlerRegistrations.addHandlerRegistration(hoverModel
                .addEventHandler(new ResourcesRemovedEventHandler() {
                    @Override
                    public void onResourcesRemoved(ResourcesRemovedEvent e) {
                        updateHighlighting(e.getRemovedResources(), false);
                    }
                }));
    }

    private void initResourceModelPresenterUI() {
        Widget widget = resourceModelPresenter.asWidget();

        viewPanel.add(widget, DockPanel.WEST);
        viewPanel.setCellHorizontalAlignment(widget, HasAlignment.ALIGN_LEFT);
    }

    private void initResourceSplitter() {
        resourceSplitter.addHandler(new ResourceCategoriesChangedHandler() {

            @Override
            public void onResourceCategoriesChanged(
                    ResourceCategoriesChangedEvent e) {

                assert e != null;
                updateResourceItemsOnModelChange(e.getChanges());
            }
        });
    }

    private void initSelectionDragSourceUI() {
        selectionPresenter.init();

        Widget widget = selectionPresenter.asWidget();

        viewPanel.add(widget, DockPanel.EAST);
        viewPanel.setCellHorizontalAlignment(widget, HasAlignment.ALIGN_RIGHT);
        viewPanel.setCellWidth(widget, "100%"); // eats up all
                                                // space
    }

    private void initSelectionDropPresenterUI() {
        selectionDropPresenter.init();

        DefaultResourceSet resources = new DefaultResourceSet();
        resources.setLabel("add selection");
        selectionDropPresenter.addResourceSet(resources);

        Widget widget = selectionDropPresenter.asWidget();

        viewPanel.add(widget, DockPanel.EAST);
        viewPanel.setCellHorizontalAlignment(widget, HasAlignment.ALIGN_RIGHT);
    }

    private void initSelectionModel() {
        selection = new SwitchingResourceSet();

        selectionResourceAddedHandlerRegistration = selection
                .addEventHandler(new ResourcesAddedEventHandler() {
                    @Override
                    public void onResourcesAdded(ResourcesAddedEvent e) {
                        updateSelectionStatusDisplay(e.getAddedResources(),
                                true);
                    }
                });
        selectionResourceRemovedHandlerRegistration = selection
                .addEventHandler(new ResourcesRemovedEventHandler() {
                    @Override
                    public void onResourcesRemoved(ResourcesRemovedEvent e) {
                        updateSelectionStatusDisplay(e.getRemovedResources(),
                                false);
                    }
                });
    }

    // TODO move non-ui stuff to constructor
    protected void initUI() {
        initViewPanelUI();
        initConfigurationPanelUI();

        mainPanel = new MainPanel();

        mainPanel.setBorderWidth(0);
        mainPanel.setSpacing(0);

        mainPanel.setSize("500px", "300px");

        mainPanel.add(viewPanel, DockPanel.NORTH);
        mainPanel.add(configurationPanel, DockPanel.NORTH);
        mainPanel.add(contentDisplay.asWidget(), DockPanel.CENTER);

        mainPanel.setCellHeight(contentDisplay.asWidget(), "100%");
    }

    private void initViewMenu() {
        if (contentDisplay.getActions().isEmpty()) {
            return;
        }

        Image image = new Image(getModuleBase() + IMAGE_VIEW_MENU);

        CSS.setMarginTopPx(image, 3);
        CSS.setMarginRightPx(image, 29);

        WidgetFactory widgetFactory = new WidgetFactory() {
            @Override
            public Widget createWidget() {
                VerticalPanel panel = new VerticalPanel();

                // TODO change styling of buttons
                panel.add(new HTML("<b>View Menu</b>"));
                for (final ViewContentDisplayAction action : contentDisplay
                        .getActions()) {

                    Button w = new Button(action.getLabel());
                    w.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            action.execute();
                        }
                    });
                    panel.add(w);
                }

                return panel;
            }
        };

        DefaultPopupManager manager = new DefaultPopupManager(widgetFactory);
        DefaultPopupManager.linkManagerToSource(manager, image);
        // TODO activate menu on click with left mouse button
        // TODO change popup menu location

        viewPanel.add(image, DockPanel.EAST);
        viewPanel.setCellHorizontalAlignment(image, HasAlignment.ALIGN_RIGHT);
    }

    private void initViewPanelUI() {
        viewPanel = new DockPanel();
        viewPanel.setSize("100%", "");
        viewPanel.setStyleName(CSS_VIEW_CONFIGURATION_PANEL);

        initResourceModelPresenterUI();
        initViewMenu();
        initSelectionDropPresenterUI();
        initSelectionDragSourceUI();
    }

    private ResourceItem removeResourceItem(String category) {
        assert category != null : "category must not be null";
        assert categoriesToResourceItems.containsKey(category);

        ResourceItem resourceItem = categoriesToResourceItems.remove(category);
        contentDisplay.removeResourceItem(resourceItem);
        resourceItem.dispose();

        assert !categoriesToResourceItems.containsKey(category);

        return resourceItem;
    }

    private void removeSelectionModelResourceHandlers() {
        if (selection != null) {
            selectionResourceAddedHandlerRegistration.removeHandler();
            selectionResourceRemovedHandlerRegistration.removeHandler();
        }

        selectionResourceAddedHandlerRegistration = null;
        selectionResourceRemovedHandlerRegistration = null;
    }

    @Override
    public void removeSelectionSet(ResourceSet selectionSet) {
        assert selectionSet != null;
        this.selectionSets.remove(selectionSet);
        selectionPresenter.removeResourceSet(selectionSet);
    }

    protected void resize(int width, int height) {
        /*
         * special resize method required, because otherwise window height
         * cannot be reduced by dragging - see
         * http://code.google.com/p/google-web-toolkit/issues/detail?id=316
         */
        int targetHeight = height - viewPanel.getOffsetHeight();
        contentDisplay.asWidget().setPixelSize(width, targetHeight);

        /*
         * fixes problem with list??
         */
        if (contentDisplay.asWidget() instanceof RequiresResize) {
            ((RequiresResize) contentDisplay.asWidget()).onResize();
        }

        checkResize();
    }

    @Override
    public void restore(final Memento state, final ResourceSetAccessor accessor) {
        // wait for content to be ready (needed for graph view swf loading on
        // restore)
        // XXX this might be the cause for issue 25
        if (contentDisplay.isReady()) {
            doRestore(state, accessor);
        } else {
            new Timer() {
                @Override
                public void run() {
                    restore(state, accessor);
                }
            }.schedule(200);
        }
    }

    private void restoreContentDisplay(Memento state) {
        Memento contentDisplayState = state.getChild(MEMENTO_CONTENT_DISPLAY);
        contentDisplay.restore(contentDisplayState);
    }

    private void restoreResourceModel(Memento state,
            ResourceSetAccessor accessor) {

        if (!(this.resourceModel instanceof Persistable)) {
            return;
        }

        ((Persistable) this.resourceModel).restore(
                state.getChild(MEMENTO_RESOURCE_MODEL), accessor);
    }

    private ResourceSet restoreResourceSet(Memento state,
            ResourceSetAccessor accessor, String key) {
        int id = (Integer) state.getValue(key);
        ResourceSet resourceSet = accessor.getResourceSet(id);
        return resourceSet;
    }

    private void restoreSelection(Memento state, ResourceSetAccessor accessor) {
        int selectionSetCount = (Integer) state
                .getValue(MEMENTO_SELECTION_SET_COUNT);
        for (int i = 0; i < selectionSetCount; i++) {
            addSelectionSet(restoreResourceSet(state, accessor,
                    MEMENTO_SELECTION_SET_PREFIX + i));
        }

        if (state.getValue(MEMENTO_SELECTION) != null) {
            setSelection(restoreResourceSet(state, accessor, MEMENTO_SELECTION));
        }
    }

    @Override
    public Memento save(ResourceSetCollector persistanceManager) {
        Memento memento = new Memento();

        storeSelection(persistanceManager, memento);
        storeResourceModel(persistanceManager, memento);
        storeContentDisplaySettings(memento);

        // TODO later: store layer settings

        return memento;
    }

    @Override
    public void setSelection(ResourceSet newSelectionModel) {
        assert newSelectionModel == null
                || selectionSets.contains(newSelectionModel);

        selection.setDelegate(newSelectionModel);
        selectionPresenter.setSelectedResourceSet(newSelectionModel);
    }

    private void storeContentDisplaySettings(Memento memento) {
        memento.addChild(MEMENTO_CONTENT_DISPLAY, contentDisplay.save());
    }

    private void storeResourceModel(ResourceSetCollector persistanceManager,
            Memento memento) {

        if (!(this.resourceModel instanceof Persistable)) {
            return;
        }

        memento.addChild(MEMENTO_RESOURCE_MODEL,
                ((Persistable) this.resourceModel).save(persistanceManager));
    }

    private void storeResourceSet(ResourceSetCollector persistanceManager,
            Memento memento, String key, ResourceSet resources) {
        memento.setValue(key, persistanceManager.storeResourceSet(resources));
    }

    private void storeSelection(ResourceSetCollector persistanceManager,
            Memento memento) {

        memento.setValue(MEMENTO_SELECTION_SET_COUNT, selectionSets.size());
        for (int i = 0; i < selectionSets.size(); i++) {
            storeResourceSet(persistanceManager, memento,
                    MEMENTO_SELECTION_SET_PREFIX + i, selectionSets.get(i));
        }

        if (selection.hasDelegate()) {
            storeResourceSet(persistanceManager, memento, MEMENTO_SELECTION,
                    getSelection());
        }
    }

    private void updateHighlighting(List<Resource> affectedResources,
            boolean highlighted) {

        assert affectedResources != null;

        ResourceSet affectedResourcesInThisView = calculateAffectedResources(affectedResources);

        if (affectedResourcesInThisView.isEmpty()) {
            return;
        }

        Set<ResourceItem> affectedResourceItems = getResourceItems(affectedResourcesInThisView);
        for (ResourceItem resourceItem : affectedResourceItems) {
            if (highlighted) {
                resourceItem
                        .addHighlightedResources(affectedResourcesInThisView);
            } else {
                resourceItem
                        .removeHighlightedResources(affectedResourcesInThisView);
            }
            // TODO replace with add / remove of resources from item
            // --> can we have filtered view on hover set instead??
            // --> problem with the order of update calls
            // ----> use view-internal hover model instead?
            // TODO dispose resource items once filtered set is used
            // TODO check that highlighting is right from the beginning
        }

        contentDisplay.update(Collections.<ResourceItem> emptySet(),
                affectedResourceItems, Collections.<ResourceItem> emptySet());
    }

    // TODO use viewContentDisplay.update to perform single update
    // TODO test update gets called with the right sets
    // (a) add
    // (b) remove
    // (c) update
    // (d) add + update
    // (e) remove + update
    private void updateResourceItemsOnModelChange(
            Set<ResourceCategoryChange> changes) {

        assert changes != null;
        assert !changes.isEmpty();

        Set<ResourceItem> addedResourceItems = new HashSet<ResourceItem>();
        Set<ResourceItem> removedResourceItems = new HashSet<ResourceItem>();

        for (ResourceCategoryChange change : changes) {
            switch (change.getDelta()) {
            case ADD: {
                addedResourceItems.add(createResourceItem(change.getCategory(),
                        change.getResourceSet()));
            }
                break;
            case REMOVE: {
                removedResourceItems.add(removeResourceItem(change
                        .getCategory()));
            }
                break;
            case UPDATE: {
                // TODO implement
            }
                break;
            }
        }

        contentDisplay.update(addedResourceItems,
                Collections.<ResourceItem> emptySet(), removedResourceItems);
    }

    private void updateSelectionStatusDisplay(List<Resource> resources,
            boolean selected) {

        Set<ResourceItem> resourceItems = getResourceItems(resources);
        for (ResourceItem resourceItem : resourceItems) {
            resourceItem.setSelected(selected);
        }

        contentDisplay.update(Collections.<ResourceItem> emptySet(),
                resourceItems, Collections.<ResourceItem> emptySet());
    }

}