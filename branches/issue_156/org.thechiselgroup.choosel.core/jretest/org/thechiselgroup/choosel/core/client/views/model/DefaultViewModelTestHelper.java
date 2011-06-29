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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.thechiselgroup.choosel.core.client.test.ResourcesTestHelper.emptyLightweightCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.mockito.ArgumentCaptor;
import org.thechiselgroup.choosel.core.client.resources.DataType;
import org.thechiselgroup.choosel.core.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.core.client.resources.DefaultResourceSetFactory;
import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceByUriTypeCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceCategorizerToMultiCategorizerAdapter;
import org.thechiselgroup.choosel.core.client.resources.ResourceGrouping;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetChangedEventHandler;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightList;
import org.thechiselgroup.choosel.core.client.util.collections.NullIterator;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolverFactory;
import org.thechiselgroup.choosel.core.client.views.resolvers.ViewItemValueResolverFactoryProvider;

import com.google.gwt.event.shared.HandlerRegistration;

// TODO refactor, change into object-based class
public final class DefaultViewModelTestHelper {

    public static LightweightCollection<ViewItem> captureAddedViewItems(
            ViewContentDisplay contentDisplay) {

        return captureAddedViewItems(contentDisplay, 1).get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<LightweightCollection<ViewItem>> captureAddedViewItems(
            ViewContentDisplay contentDisplay, int wantedNumberOfInvocation) {

        ArgumentCaptor<LightweightCollection> captor = ArgumentCaptor
                .forClass(LightweightCollection.class);
        verify(contentDisplay, times(wantedNumberOfInvocation)).update(
                captor.capture(), emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(Slot.class));

        return cast(captor.getAllValues());
    }

    public static List<ViewItem> captureAddedViewItemsAsList(
            ViewContentDisplay contentDisplay) {

        return captureAddedViewItems(contentDisplay).toList();
    }

    public static LightweightCollection<ViewItem> captureRemovedViewItems(
            ViewContentDisplay contentDisplay) {

        return captureRemovedViewItems(contentDisplay, 1).get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<LightweightCollection<ViewItem>> captureRemovedViewItems(
            ViewContentDisplay contentDisplay, int wantedNumberOfInvocation) {

        ArgumentCaptor<LightweightCollection> captor = ArgumentCaptor
                .forClass(LightweightCollection.class);
        verify(contentDisplay, times(wantedNumberOfInvocation)).update(
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(ViewItem.class), captor.capture(),
                emptyLightweightCollection(Slot.class));

        return cast(captor.getAllValues());
    }

    public static LightweightCollection<ViewItem> captureUpdatedViewItems(
            ViewContentDisplay contentDisplay) {

        return captureUpdatedViewItems(contentDisplay, 1).get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<LightweightCollection<ViewItem>> captureUpdatedViewItems(
            ViewContentDisplay contentDisplay, int wantedNumberOfInvocation) {

        ArgumentCaptor<LightweightCollection> captor = ArgumentCaptor
                .forClass(LightweightCollection.class);
        verify(contentDisplay, times(wantedNumberOfInvocation)).update(
                emptyLightweightCollection(ViewItem.class), captor.capture(),
                emptyLightweightCollection(ViewItem.class),
                emptyLightweightCollection(Slot.class));

        return cast(captor.getAllValues());
    }

    /**
     * convert to LightWeightCollection<ViewItem>
     */
    @SuppressWarnings("rawtypes")
    private static List<LightweightCollection<ViewItem>> cast(
            List<LightweightCollection> allValues) {

        List<LightweightCollection<ViewItem>> result = new ArrayList<LightweightCollection<ViewItem>>();
        for (LightweightCollection<ViewItem> lightweightCollection : allValues) {
            result.add(lightweightCollection);
        }
        return result;
    }

    public static void stubHandlerRegistration(ResourceSet mockedResources,
            HandlerRegistration handlerRegistrationToReturn) {

        when(mockedResources.iterator()).thenReturn(
                NullIterator.<Resource> nullIterator());

        when(
                mockedResources
                        .addEventHandler(any(ResourceSetChangedEventHandler.class)))
                .thenReturn(handlerRegistrationToReturn);
    }

    private Slot[] slots = new Slot[0];

    private ResourceSet containedResources = new DefaultResourceSet();

    private ResourceSet highlightedResources = new DefaultResourceSet();

    private ResourceSet selectedResources = new DefaultResourceSet();

    private ViewContentDisplay viewContentDisplay = mock(ViewContentDisplay.class);

    private ViewItemValueResolverFactoryProvider resolverProvider = mock(ViewItemValueResolverFactoryProvider.class);

    private LightweightList<ViewItemValueResolverFactory> resolverFactories = CollectionFactory
            .createLightweightList();

    public boolean addAllToContainerResources(ResourceSet resources) {
        return getContainedResources().addAll(resources);
    }

    public boolean addToContainedResources(Resource resource1) {
        return getContainedResources().add(resource1);
    }

    public Slot[] createSlots(DataType... dataTypes) {
        assert dataTypes != null;

        Slot[] slots = new Slot[dataTypes.length];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot("slot" + i, "Slot " + i, dataTypes[i]);
        }

        setSlots(slots);

        return slots;
    }

    public DefaultViewModel createTestViewModel() {
        when(resolverProvider.getResolverFactories()).thenReturn(
                resolverFactories);

        when(viewContentDisplay.getSlots()).thenReturn(slots);
        when(viewContentDisplay.isReady()).thenReturn(true);

        SlotMappingConfiguration slotMappingConfiguration = spy(new SlotMappingConfiguration(
                slots));

        // TODO we want to make the categorizer more flexible
        ResourceGrouping resourceGrouping = new ResourceGrouping(
                new ResourceCategorizerToMultiCategorizerAdapter(
                        new ResourceByUriTypeCategorizer()),
                new DefaultResourceSetFactory());

        resourceGrouping.setResourceSet(containedResources);

        return spy(new DefaultViewModel(viewContentDisplay,
                slotMappingConfiguration, selectedResources,
                highlightedResources, mock(ViewItemBehavior.class),
                resourceGrouping, mock(Logger.class)));
    }

    public ResourceSet getContainedResources() {
        return containedResources;
    }

    public ResourceSet getHighlightedResources() {
        return highlightedResources;
    }

    public LightweightList<ViewItemValueResolverFactory> getResolverFactories() {
        return resolverFactories;
    }

    public ViewItemValueResolverFactoryProvider getResolverProvider() {
        return resolverProvider;
    }

    public ResourceSet getSelectedResources() {
        return selectedResources;
    }

    public ViewContentDisplay getViewContentDisplay() {
        return viewContentDisplay;
    }

    public void mockContainedResources() {
        this.containedResources = mock(ResourceSet.class);
    }

    public void mockHighlightedResources() {
        this.highlightedResources = mock(ResourceSet.class);
    }

    public void mockSelectedResources() {
        this.selectedResources = mock(ResourceSet.class);
    }

    public void setContainedResources(ResourceSet containedResources) {
        this.containedResources = containedResources;
    }

    public void setHighlightedResources(ResourceSet highlightedResources) {
        this.highlightedResources = highlightedResources;
    }

    public void setSelectedResources(ResourceSet selectedResources) {
        this.selectedResources = selectedResources;
    }

    public void setSlots(Slot... slots) {
        this.slots = slots;
    }

}