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
package org.thechiselgroup.choosel.workbench.client.ui.configuration;

import static org.thechiselgroup.choosel.core.client.configuration.ChooselInjectionConstants.*;

import java.util.Map;

import org.thechiselgroup.choosel.core.client.command.CommandManager;
import org.thechiselgroup.choosel.core.client.error_handling.ErrorHandler;
import org.thechiselgroup.choosel.core.client.label.LabelProvider;
import org.thechiselgroup.choosel.core.client.resources.DefaultResourceSetFactory;
import org.thechiselgroup.choosel.core.client.resources.HasResourceCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceByUriMultiCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceMultiCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetFactory;
import org.thechiselgroup.choosel.core.client.resources.ui.DetailsWidgetHelper;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarFactory;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarResourceSetsPresenter;
import org.thechiselgroup.choosel.core.client.ui.SidePanelSection;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightList;
import org.thechiselgroup.choosel.core.client.visualization.DefaultView;
import org.thechiselgroup.choosel.core.client.visualization.ViewPart;
import org.thechiselgroup.choosel.core.client.visualization.behaviors.CompositeVisualItemBehavior;
import org.thechiselgroup.choosel.core.client.visualization.behaviors.HighlightingVisualItemBehavior;
import org.thechiselgroup.choosel.core.client.visualization.behaviors.PopupWithHighlightingVisualItemBehavior;
import org.thechiselgroup.choosel.core.client.visualization.behaviors.SwitchSelectionOnClickVisualItemBehavior;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;
import org.thechiselgroup.choosel.core.client.visualization.model.ViewContentDisplay;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualizationModel;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.DefaultResourceModel;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.DefaultSelectionModel;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.HighlightingModel;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.RequiresAutomaticResourceSet;
import org.thechiselgroup.choosel.core.client.visualization.model.extensions.ResourceModel;
import org.thechiselgroup.choosel.core.client.visualization.model.implementation.DefaultVisualizationModel;
import org.thechiselgroup.choosel.core.client.visualization.model.implementation.FixedSlotResolversVisualizationModelDecorator;
import org.thechiselgroup.choosel.core.client.visualization.model.initialization.ViewContentDisplaysConfiguration;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.DefaultManagedSlotMappingConfiguration;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.DefaultSlotMappingInitializer;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedSlotMappingConfigurationChangedEvent;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedSlotMappingConfigurationChangedEventHandler;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.SlotMappingInitializer;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.VisualItemValueResolverFactoryProvider;
import org.thechiselgroup.choosel.core.client.visualization.model.persistence.ManagedSlotMappingConfigurationPersistence;
import org.thechiselgroup.choosel.core.client.visualization.resolvers.ui.VisualItemValueResolverUIControllerFactoryProvider;
import org.thechiselgroup.choosel.core.client.visualization.ui.DefaultResourceModelPresenter;
import org.thechiselgroup.choosel.core.client.visualization.ui.DefaultSelectionModelPresenter;
import org.thechiselgroup.choosel.core.client.visualization.ui.DefaultVisualMappingsControl;
import org.thechiselgroup.choosel.core.client.visualization.ui.VisualMappingsControl;
import org.thechiselgroup.choosel.dnd.client.resources.DragEnablerFactory;
import org.thechiselgroup.choosel.dnd.client.resources.DragVisualItemBehavior;
import org.thechiselgroup.choosel.dnd.client.resources.DropEnabledViewContentDisplay;
import org.thechiselgroup.choosel.dnd.client.resources.ResourceSetAvatarDropTargetManager;
import org.thechiselgroup.choosel.dnd.client.windows.ViewWindowContent;
import org.thechiselgroup.choosel.dnd.client.windows.WindowContent;
import org.thechiselgroup.choosel.dnd.client.windows.WindowContentProducer;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ViewWindowContentProducer implements WindowContentProducer {

    @Inject
    @Named(AVATAR_FACTORY_ALL_RESOURCES)
    private ResourceSetAvatarFactory allResourcesDragAvatarFactory;

    @Inject
    @Named(DROP_TARGET_MANAGER_VIEW_CONTENT)
    private ResourceSetAvatarDropTargetManager contentDropTargetManager;

    @Inject
    @Named(AVATAR_FACTORY_SELECTION_DROP)
    private ResourceSetAvatarFactory dropTargetFactory;

    @Inject
    private ResourceSetFactory resourceSetFactory;

    @Inject
    @Named(AVATAR_FACTORY_SELECTION)
    private ResourceSetAvatarFactory selectionDragAvatarFactory;

    @Inject
    @Named(LABEL_PROVIDER_SELECTION_SET)
    private LabelProvider selectionModelLabelFactory;

    @Inject
    @Named(AVATAR_FACTORY_SET)
    private ResourceSetAvatarFactory userSetsDragAvatarFactory;

    @Inject
    private ViewContentDisplaysConfiguration viewContentDisplayConfiguration;

    @Inject
    private HighlightingModel hoverModel;

    @Inject
    protected VisualItemValueResolverUIControllerFactoryProvider uiProvider;

    @Inject
    private PopupManagerFactory popupManagerFactory;

    @Inject
    private DetailsWidgetHelper detailsWidgetHelper;

    @Inject
    private DragEnablerFactory dragEnablerFactory;

    @Inject
    private CommandManager commandManager;

    @Inject
    private ErrorHandler errorHandler;

    @Inject
    protected VisualItemValueResolverFactoryProvider resolverFactoryProvider;

    @Inject
    private ManagedSlotMappingConfigurationPersistence slotMappingConfigurationPersistence;

    protected ResourceMultiCategorizer createDefaultCategorizer(
            String contentType) {
        return new ResourceByUriMultiCategorizer();
    }

    // XXX remove
    protected LightweightList<SidePanelSection> createSidePanelSections(
            String contentType, ViewContentDisplay contentDisplay,
            VisualMappingsControl visualMappingsControl,
            ResourceModel resourceModel, VisualizationModel visualizationModel) {

        LightweightList<SidePanelSection> sidePanelSections = CollectionFactory
                .createLightweightList();

        sidePanelSections.add(new SidePanelSection("Mappings",
                visualMappingsControl.asWidget()));
        sidePanelSections.addAll(contentDisplay.getSidePanelSections());

        return sidePanelSections;
    }

    protected SlotMappingInitializer createSlotMappingInitializer(
            String contentType) {
        return new DefaultSlotMappingInitializer();
    }

    /**
     * Hook method that should be overridden to provide customized view parts.
     */
    protected LightweightList<ViewPart> createViewParts(String contentType) {
        return CollectionFactory.createLightweightList();
    }

    protected VisualMappingsControl createVisualMappingsControl(
            HasResourceCategorizer resourceGrouping,
            DefaultManagedSlotMappingConfiguration uiModel, String contentType) {

        // TODO change configuration to configurationUIModel
        return new DefaultVisualMappingsControl(uiModel, resourceGrouping,
                this.uiProvider, resolverFactoryProvider);
    }

    @Override
    public WindowContent createWindowContent(String contentType) {
        assert contentType != null;

        ViewContentDisplay viewContentDisplay = viewContentDisplayConfiguration
                .createDisplay(contentType);

        ViewContentDisplay contentDisplay = new DropEnabledViewContentDisplay(
                viewContentDisplay, contentDropTargetManager);

        ResourceModel resourceModel = new DefaultResourceModel(
                resourceSetFactory);

        if (viewContentDisplay instanceof RequiresAutomaticResourceSet) {
            ((RequiresAutomaticResourceSet) viewContentDisplay)
                    .setAutomaticResources(resourceModel
                            .getAutomaticResourceSet());
        }

        DefaultResourceModelPresenter resourceModelPresenter = new DefaultResourceModelPresenter(
                new ResourceSetAvatarResourceSetsPresenter(
                        allResourcesDragAvatarFactory),
                new ResourceSetAvatarResourceSetsPresenter(
                        userSetsDragAvatarFactory), resourceModel);

        DefaultSelectionModel selectionModel = new DefaultSelectionModel(
                selectionModelLabelFactory, resourceSetFactory);

        DefaultSelectionModelPresenter selectionModelPresenter = new DefaultSelectionModelPresenter(
                new ResourceSetAvatarResourceSetsPresenter(dropTargetFactory),
                new ResourceSetAvatarResourceSetsPresenter(
                        selectionDragAvatarFactory), selectionModel);

        Map<Slot, VisualItemValueResolver> fixedSlotResolvers = viewContentDisplayConfiguration
                .getFixedSlotResolvers(contentType);

        CompositeVisualItemBehavior visualItemBehaviors = new CompositeVisualItemBehavior();

        // visualItemBehaviors.add(new ViewInteractionLogger(logger));
        visualItemBehaviors.add(new HighlightingVisualItemBehavior(hoverModel));
        visualItemBehaviors.add(new DragVisualItemBehavior(dragEnablerFactory));
        visualItemBehaviors.add(new PopupWithHighlightingVisualItemBehavior(
                detailsWidgetHelper, popupManagerFactory, hoverModel));
        visualItemBehaviors.add(new SwitchSelectionOnClickVisualItemBehavior(
                selectionModel, commandManager));

        SlotMappingInitializer slotMappingInitializer = createSlotMappingInitializer(contentType);

        ResourceMultiCategorizer categorizer = createDefaultCategorizer(contentType);

        VisualizationModel visualizationModel = new FixedSlotResolversVisualizationModelDecorator(
                new DefaultVisualizationModel(contentDisplay,
                        selectionModel.getSelectionProxy(),
                        hoverModel.getResources(), visualItemBehaviors,
                        errorHandler, new DefaultResourceSetFactory(),
                        categorizer), fixedSlotResolvers);

        visualizationModel.setContentResourceSet(resourceModel.getResources());

        DefaultManagedSlotMappingConfiguration managedConfiguration = new DefaultManagedSlotMappingConfiguration(
                resolverFactoryProvider, slotMappingInitializer,
                visualizationModel, visualizationModel);

        /**
         * Visual Mappings Control is what sets up the side panel section that
         * handles mapping the slot to its resolvers.
         * 
         */
        final VisualMappingsControl visualMappingsControl = createVisualMappingsControl(
                visualizationModel, managedConfiguration, contentType);
        assert visualMappingsControl != null : "createVisualMappingsControl must not return null";

        LightweightList<ViewPart> viewParts = createViewParts(contentType);

        LightweightList<SidePanelSection> sidePanelSections = createSidePanelSections(
                contentType, contentDisplay, visualMappingsControl,
                resourceModel, visualizationModel);

        for (ViewPart viewPart : viewParts) {
            viewPart.addSidePanelSections(sidePanelSections);
        }

        String label = contentDisplay.getName();

        managedConfiguration
                .addManagedSlotMappingConfigurationChangedEventHandler(new ManagedSlotMappingConfigurationChangedEventHandler() {
                    @Override
                    public void onSlotMappingStateChanged(
                            ManagedSlotMappingConfigurationChangedEvent event) {
                        visualMappingsControl
                                .updateConfigurationForSlotMappingChangedEvent(event);

                    }
                });

        DefaultView view = new DefaultView(contentDisplay, label, contentType,
                selectionModelPresenter, resourceModelPresenter,
                visualMappingsControl, sidePanelSections, visualizationModel,
                resourceModel, selectionModel, managedConfiguration,
                slotMappingConfigurationPersistence, errorHandler);

        for (ViewPart viewPart : viewParts) {
            viewPart.afterViewCreation(view);
        }

        return new ViewWindowContent(view);
    }
}