/*******************************************************************************
 * Copyright (C) 2011 Lars Grammel 
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
package org.thechiselgroup.choosel.core.client.views.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightList;
import org.thechiselgroup.choosel.core.client.views.resolvers.ManagedViewItemValueResolver;
import org.thechiselgroup.choosel.core.client.views.resolvers.SlotMappingUIModel;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolver;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolverFactoryProvider;

public class SlotMappingConfigurationUIModel {

    private ViewItemValueResolverFactoryProvider resolverProvider;

    private Map<Slot, SlotMappingUIModel> slotsToSlotMappings = new HashMap<Slot, SlotMappingUIModel>();

    private SlotMappingInitializer slotMappingInitializer;

    private ViewModel viewModel;

    private final ViewItemResolutionErrorModel errorModel;

    public SlotMappingConfigurationUIModel(
            ViewItemValueResolverFactoryProvider resolverProvider,
            SlotMappingInitializer slotMappingInitializer, ViewModel viewModel,
            ViewItemResolutionErrorModel errorModel) {

        assert resolverProvider != null;
        assert slotMappingInitializer != null;
        assert viewModel != null;
        assert errorModel != null;

        this.resolverProvider = resolverProvider;
        this.slotMappingInitializer = slotMappingInitializer;
        this.viewModel = viewModel;
        this.errorModel = errorModel;

        // this does not set up a mapping
        initSlotModels(viewModel.getSlots());

        viewModel.addHandler(new SlotMappingChangedHandler() {
            @Override
            public void onSlotMappingChanged(SlotMappingChangedEvent e) {
                handleResolverChange(e.getSlot(), e.getCurrentResolver());
            }
        });
        viewModel.addHandler(new ViewItemContainerChangeEventHandler() {
            @Override
            public void onViewItemContainerChanged(
                    ViewItemContainerChangeEvent event) {
                updateVisualMappings();
            }
        });
    }

    public ManagedViewItemValueResolver getCurrentResolver(Slot slot) {
        assert slotsToSlotMappings.containsKey(slot);

        return slotsToSlotMappings.get(slot).getCurrentResolver();
    }

    public SlotMappingUIModel getSlotMappingUIModel(Slot slot) {
        return slotsToSlotMappings.get(slot);
    }

    public LightweightList<SlotMappingUIModel> getSlotMappingUIModels() {
        LightweightList<SlotMappingUIModel> uiModels = CollectionFactory
                .createLightweightList();
        uiModels.addAll(slotsToSlotMappings.values());
        return uiModels;
    }

    public Slot[] getSlots() {
        return viewModel.getSlots();
    }

    public LightweightList<Slot> getSlotsWithInvalidResolvers() {
        LightweightList<Slot> invalidSlots = CollectionFactory
                .createLightweightList();
        for (Entry<Slot, SlotMappingUIModel> entry : slotsToSlotMappings
                .entrySet()) {

            if (!entry.getValue().inValidState(viewModel.getViewItems())) {
                invalidSlots.add(entry.getKey());
            }
        }
        return invalidSlots;
    }

    public void handleResolverChange(Slot slot, ViewItemValueResolver resolver) {
        slotsToSlotMappings.get(slot).currentResolverWasSet(resolver,
                viewModel.getViewItems());
    }

    private void initSlotModels(Slot[] slots) {
        for (Slot slot : slots) {
            slotsToSlotMappings.put(slot, new SlotMappingUIModel(slot,
                    resolverProvider, viewModel, errorModel));
        }
    }

    private void resetMappingsFromInitializer(
            LightweightCollection<Slot> unconfiguredSlots,
            LightweightCollection<ViewItem> viewItems) {

        assert !unconfiguredSlots.isEmpty();

        // TODO this is not a good way to convert to an array, find a better way
        // to do this
        Slot[] slotsAsArray = unconfiguredSlots.toList().toArray(
                new Slot[unconfiguredSlots.size()]);

        for (Entry<Slot, ViewItemValueResolver> entry : slotMappingInitializer
                .getResolvers(viewModel.getResourceGrouping().getResourceSet(),
                        slotsAsArray).entrySet()) {

            Slot slot = entry.getKey();
            ViewItemValueResolver resolver = entry.getValue();

            if (getSlotMappingUIModel(slot).isAllowableResolver(resolver,
                    viewItems)) {
                viewModel.setResolver(slot, resolver);
            } else {
                // Oh god, even the initializer was wrong
                // TODO throw an exception or something
            }
        }
    }

    // TODO shouldn't this be pushed to the SlotMappingUIModel
    public void setCurrentResolver(Slot slot,
            ManagedViewItemValueResolver resolver) {
        slotsToSlotMappings.get(slot).setResolver(resolver);
    }

    /**
     * Call this method whenever the model changes (whenever the
     * {@link ViewItem}s change).
     */
    private void updateUIModels(LightweightCollection<ViewItem> viewItems) {
        for (SlotMappingUIModel uiModel : slotsToSlotMappings.values()) {
            uiModel.updateAllowableFactories(viewItems);
        }
    }

    private void updateVisualMappings() {
        updateVisualMappings(viewModel.getViewItems());
    }

    // TODO handle view items with errors in here
    private void updateVisualMappings(LightweightCollection<ViewItem> viewItems) {

        // check to see if the configuration is still valid
        updateUIModels(viewItems);

        // reset the unconfigured slots
        LightweightCollection<Slot> slots = viewModel.getUnconfiguredSlots();

        if (!slots.isEmpty()) {
            resetMappingsFromInitializer(slots, viewItems);
        }
        // TODO assert that all of the slots now have valid resolvers
    }
}