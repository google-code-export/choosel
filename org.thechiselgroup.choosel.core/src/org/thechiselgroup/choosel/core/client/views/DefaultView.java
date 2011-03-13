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
package org.thechiselgroup.choosel.core.client.views;

import org.thechiselgroup.choosel.core.client.persistence.Memento;
import org.thechiselgroup.choosel.core.client.persistence.Persistable;
import org.thechiselgroup.choosel.core.client.persistence.PersistableRestorationService;
import org.thechiselgroup.choosel.core.client.resources.ResourceByPropertyMultiCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceByUriMultiCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceMultiCategorizer;
import org.thechiselgroup.choosel.core.client.resources.persistence.ResourceSetAccessor;
import org.thechiselgroup.choosel.core.client.resources.persistence.ResourceSetCollector;
import org.thechiselgroup.choosel.core.client.ui.ImageButton;
import org.thechiselgroup.choosel.core.client.ui.Presenter;
import org.thechiselgroup.choosel.core.client.util.DisposeUtil;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.windows.AbstractWindowContent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

public class DefaultView extends AbstractWindowContent implements View {

    private class ViewPanel extends DockPanel implements ViewProvider {

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
            DefaultView.this.setSize(width, height);
            super.setPixelSize(width, height);
        }

    }

    private static final String CSS_EXPANDER = "DefaultView-Expander";

    private static final String CSS_CONFIGURATION_PANEL = "DefaultView-ConfigurationPanel";

    private static final String CSS_VIEW_CONFIGURATION_PANEL = "view-configurationPanel";

    // TODO rename
    private DockPanel configurationBar;

    // TODO rename
    private StackPanel sideBar;

    /**
     * The main panel of this view. It contains all other widgets of this view.
     */
    private ViewPanel viewPanel;

    private Presenter resourceModelPresenter;

    private Presenter selectionModelPresenter;

    private int width;

    private int height;

    /**
     * Sections that will be displayed in the side panel. This is a lightweight
     * collections so we can check whether it is empty or not.
     */
    private LightweightCollection<SidePanelSection> sidePanelSections;

    private ViewContentDisplay contentDisplay;

    private ViewModel model;

    private ResourceModel resourceModel;

    private SelectionModel selectionModel;

    private boolean isInitialized;

    private static final String MEMENTO_CONTENT_DISPLAY = "content-display";

    private static final String MEMENTO_RESOURCE_MODEL = "resource-model";

    private static final String MEMENTO_SELECTION_MODEL = "selection-model";

    private static final String MEMENTO_SLOT_MAPPINGS = "slot-mappings";

    private static final String MEMENTO_GROUPING = "grouping";

    // TODO change parameter order in constructor
    // TODO change contentDisplay to more restricted interface
    public DefaultView(ViewContentDisplay contentDisplay, String label,
            String contentType, Presenter selectionModelPresenter,
            Presenter resourceModelPresenter,
            VisualMappingsControl visualMappingsControl,
            LightweightCollection<SidePanelSection> sidePanelSections,
            ViewModel viewModel, ResourceModel resourceModel,
            SelectionModel selectionModel) {

        super(label, contentType);

        assert contentDisplay != null;
        assert selectionModelPresenter != null;
        assert resourceModelPresenter != null;
        assert sidePanelSections != null;
        assert viewModel != null;
        assert resourceModel != null;
        assert selectionModel != null;

        this.contentDisplay = contentDisplay;
        this.selectionModelPresenter = selectionModelPresenter;
        this.resourceModelPresenter = resourceModelPresenter;
        this.sidePanelSections = sidePanelSections;
        this.model = viewModel;
        this.selectionModel = selectionModel;
        this.resourceModel = resourceModel;
    }

    @Override
    public Widget asWidget() {
        return viewPanel;
    }

    @Override
    public void dispose() {
        resourceModelPresenter.dispose();
        resourceModelPresenter = null;

        selectionModelPresenter.dispose();
        selectionModelPresenter = null;

        DisposeUtil.dispose(model);
        model = null;
    }

    // protected for test access
    protected void doRestore(Memento state,
            PersistableRestorationService restorationService,
            ResourceSetAccessor accessor) {

        assert isInitialized : "view has to be initialized before restoring it";

        contentDisplay.startRestore();

        // restoreGrouping(state.getChild(MEMENTO_GROUPING));
        restoreGrouping(state.getChild(MEMENTO_GROUPING));
        restore(resourceModel, state, MEMENTO_RESOURCE_MODEL,
                restorationService, accessor);
        restore(selectionModel, state, MEMENTO_SELECTION_MODEL,
                restorationService, accessor);
        contentDisplay.restore(state.getChild(MEMENTO_CONTENT_DISPLAY),
                restorationService, accessor);
        restore(model.getSlotMappingConfiguration(), state,
                MEMENTO_SLOT_MAPPINGS, restorationService, accessor);

        contentDisplay.endRestore();
    }

    @Override
    public ViewModel getModel() {
        return model;
    }

    protected String getModuleBase() {
        return GWT.getModuleBaseURL();
    }

    @Override
    public ResourceModel getResourceModel() {
        return resourceModel;
    }

    @Override
    public SelectionModel getSelectionModel() {
        return selectionModel;
    }

    @Override
    public void init() {
        assert !isInitialized : "view has already been initialized";

        resourceModelPresenter.init();
        initUI();

        isInitialized = true;
    }

    private void initConfigurationPanelUI() {
        configurationBar = new DockPanel();
        configurationBar.setSize("100%", "");
        configurationBar.setStyleName(CSS_VIEW_CONFIGURATION_PANEL);

        initResourceModelPresenter();
        initSideBarExpander();
        initSelectionModelPresenter();
    }

    private void initResourceModelPresenter() {
        Widget widget = resourceModelPresenter.asWidget();

        configurationBar.add(widget, DockPanel.WEST);
        configurationBar.setCellHorizontalAlignment(widget,
                HasAlignment.ALIGN_LEFT);
    }

    private void initSelectionModelPresenter() {
        selectionModelPresenter.init();

        Widget widget = selectionModelPresenter.asWidget();
        configurationBar.add(widget, DockPanel.EAST);
        configurationBar.setCellHorizontalAlignment(widget,
                HasAlignment.ALIGN_RIGHT);
        configurationBar.setCellWidth(widget, "100%"); // eats up all space
    }

    private void initSideBar() {
        assert sideBar == null;
        assert sidePanelSections != null;

        sideBar = new StackPanel();
        sideBar.setStyleName(CSS_CONFIGURATION_PANEL);
        sideBar.setVisible(false);

        for (SidePanelSection sidePanelSection : sidePanelSections) {
            sideBar.add(sidePanelSection.getWidget(),
                    sidePanelSection.getSectionTitle());
        }
    }

    private void initSideBarExpander() {
        ImageButton expander = ImageButton.createExpanderButton();

        expander.setStyleName(CSS_EXPANDER);

        expander.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                sideBar.setVisible(!sideBar.isVisible());
                updateContentDisplaySize();
            }
        });

        configurationBar.add(expander, DockPanel.EAST);
        configurationBar.setCellHorizontalAlignment(expander,
                HasAlignment.ALIGN_RIGHT);
    }

    protected void initUI() {
        initConfigurationPanelUI();
        initSideBar();

        viewPanel = new ViewPanel();

        viewPanel.setBorderWidth(0);
        viewPanel.setSpacing(0);

        viewPanel.setSize("500px", "300px");

        viewPanel.add(configurationBar, DockPanel.NORTH);
        viewPanel.add(contentDisplay.asWidget(), DockPanel.CENTER);
        viewPanel.add(sideBar, DockPanel.EAST);

        viewPanel.setCellHeight(contentDisplay.asWidget(), "100%");
    }

    @Override
    public void restore(final Memento state,
            final PersistableRestorationService restorationService,
            final ResourceSetAccessor accessor) {

        /*
         * wait for content to be ready (needed for graph view swf loading on
         * restore)
         */
        // XXX this might be the cause for issue 25
        if (contentDisplay.isReady()) {
            doRestore(state, restorationService, accessor);
        } else {
            new Timer() {
                @Override
                public void run() {
                    restore(state, restorationService, accessor);
                }
            }.schedule(200);
        }
    }

    private void restore(Object target, Memento parentMemento,
            String targetMementoKey,
            PersistableRestorationService restorationService,
            ResourceSetAccessor accessor) {

        if (target instanceof Persistable) {
            ((Persistable) target).restore(
                    parentMemento.getChild(targetMementoKey),
                    restorationService, accessor);
        }
    }

    // TODO extract constants
    private void restoreGrouping(Memento groupingMemento) {
        assert groupingMemento != null;

        String categorizerType = (String) groupingMemento.getValue("type");

        if ("byProperty".equals(categorizerType)) {
            String property = (String) groupingMemento.getValue("property");
            model.setCategorizer(new ResourceByPropertyMultiCategorizer(
                    property));
        } else if ("byUri".equals(categorizerType)) {
            model.setCategorizer(new ResourceByUriMultiCategorizer());
        }
    }

    private void save(Object target, Memento parentMemento,
            String targetMementoKey, ResourceSetCollector resourceSetCollector) {

        if (target instanceof Persistable) {
            parentMemento.addChild(targetMementoKey,
                    ((Persistable) target).save(resourceSetCollector));
        }
    }

    @Override
    public Memento save(ResourceSetCollector resourceSetCollector) {
        Memento memento = new Memento();

        saveGrouping(memento);
        save(selectionModel, memento, MEMENTO_SELECTION_MODEL,
                resourceSetCollector);
        save(resourceModel, memento, MEMENTO_RESOURCE_MODEL,
                resourceSetCollector);
        memento.addChild(MEMENTO_CONTENT_DISPLAY,
                contentDisplay.save(resourceSetCollector));
        save(model.getSlotMappingConfiguration(), memento,
                MEMENTO_SLOT_MAPPINGS, resourceSetCollector);

        return memento;
    }

    // TODO extract constants
    private void saveGrouping(Memento memento) {
        Memento groupingMemento = new Memento();

        ResourceMultiCategorizer categorizer = model.getCategorizer();
        if (categorizer instanceof ResourceByPropertyMultiCategorizer) {
            groupingMemento.setValue("type", "byProperty");
            groupingMemento.setValue("property",
                    ((ResourceByPropertyMultiCategorizer) categorizer)
                            .getProperty());
        } else if (categorizer instanceof ResourceByUriMultiCategorizer) {
            groupingMemento.setValue("type", "byUri");
        }

        memento.addChild(MEMENTO_GROUPING, groupingMemento);
    }

    /**
     * Sets the size of the default view.
     * 
     * @param width
     * @param height
     */
    private void setSize(int width, int height) {
        assert height >= 0;
        assert width >= 0;

        this.width = width;
        this.height = height;

        updateContentDisplaySize();
    }

    private void updateContentDisplaySize() {
        /*
         * special resize method required, because otherwise window height
         * cannot be reduced by dragging - see
         * http://code.google.com/p/google-web-toolkit/issues/detail?id=316
         */

        int targetHeight = height - configurationBar.getOffsetHeight();
        int targetWidth = sideBar.isVisible() ? width
                - sideBar.getOffsetWidth() : width;

        Widget contentWidget = contentDisplay.asWidget();
        contentWidget.setPixelSize(targetWidth, targetHeight);

        /*
         * TODO fixes problem with list?? --> this should be handled by the
         * content display... --> move into abstract impl.
         */
        if (contentWidget instanceof RequiresResize) {
            ((RequiresResize) contentWidget).onResize();
        }

        contentDisplay.checkResize();
    }

}