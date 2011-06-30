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
package org.thechiselgroup.choosel.core.client.views.resolvers;

import java.util.Collection;
import java.util.Map;

import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightList;
import org.thechiselgroup.choosel.core.client.views.model.Slot;
import org.thechiselgroup.choosel.core.client.views.model.SlotMappingChangedEvent;
import org.thechiselgroup.choosel.core.client.views.model.SlotMappingChangedHandler;
import org.thechiselgroup.choosel.core.client.views.model.ViewItem;
import org.thechiselgroup.choosel.core.client.views.model.ViewItemResolutionErrorModel;
import org.thechiselgroup.choosel.core.client.views.model.ViewItemValueResolverContext;

import com.google.gwt.event.shared.HandlerManager;

/**
 * This class contains the necessary information to draw a SlotConfiguration UI
 * element. For example, Bar Length is |Sum| of |property|.
 * 
 * Responsibilities: (1) make sure the right factories for the slot and view are
 * available (2) maintain the current resolver for that slot (3) Maintain the
 * current context of what this slot looks like which right now consists of only
 * which view items are in the view
 * 
 * SlotMappingChangedEvents fires an event when a completely new factory is
 * chosen. SlotResolverChangedEvent also fires an event when the resolverFactory
 * stays the same but the internal state of the resolver changes
 */
public class SlotMappingUIModel {

    // TODO should these be regular exceptions?
    public class InvalidResolverException extends IllegalArgumentException {

        private static final long serialVersionUID = 1L;

        public InvalidResolverException(String message) {
            super(message);
        }

    }

    // TODO should these be regular exceptions?
    public class NoAllowableResolverException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public NoAllowableResolverException(String message) {
            super(message);
        }
    }

    /*
     * XXX hack for test cases, remove once slot mapping UI model is separated
     * from SlotModelConfiguration.
     */
    public static boolean TESTING = false;

    private final Slot slot;

    private Map<String, ViewItemValueResolverFactory> allowableResolverFactories;

    private ViewItemValueResolverFactoryProvider provider;

    private HandlerManager eventBus;

    private final ViewItemResolutionErrorModel errorModel;

    private ViewItemValueResolverContext context;

    // TODO this does not take into account the current context
    public SlotMappingUIModel(Slot slot,
            ViewItemValueResolverFactoryProvider provider,
            ViewItemValueResolverContext context,
            ViewItemResolutionErrorModel errorModel) {

        assert slot != null;
        assert provider != null;
        assert context != null;
        assert errorModel != null;

        this.slot = slot;
        this.provider = provider;
        this.context = context;
        this.errorModel = errorModel;

        this.eventBus = new HandlerManager(this);
        this.allowableResolverFactories = CollectionFactory.createStringMap();

        updateAllowableFactories(CollectionFactory
                .<ViewItem> createLightweightList());
    }

    public void addSlotMappingEventHandler(SlotMappingChangedHandler handler) {
        assert handler != null;
        eventBus.addHandler(SlotMappingChangedEvent.TYPE, handler);
    }

    /**
     * @return whether or not the current resolver is allowable
     */
    // TODO this should be a one liner
    public boolean currentResolverIsValid(
            LightweightCollection<ViewItem> currentViewItems) {
        return !errorsInModel()
                && context.getResolver(slot) != null
                && isAllowableResolver(context.getResolver(slot),
                        currentViewItems);
    }

    /**
     * Checks to see if the current resolver factory is changed, and if it has,
     * fires a {@link SlotMappingChangedEvent} to all registered
     * {@link SlotMappingChangedHandler}s.
     * 
     * @throws InvalidResolverException
     *             If the resolver is null or is not allowable.
     * 
     *             TODO This method does not set the resolver, nor should it, it
     *             is just going to show the context's resolver
     */
    public void currentResolverWasSet(ViewItemValueResolver resolver,
            LightweightCollection<ViewItem> viewItems) {
        if (!(resolver instanceof ManagedViewItemValueResolver)) {
            // I am wrong... do something, e.g. exception
            throw new InvalidResolverException(
                    "resolver that was set was not managed");
        }

        ManagedViewItemValueResolver managedResolver = (ManagedViewItemValueResolver) resolver;

        if (!isAllowableResolver(managedResolver, viewItems)) {
            throw new InvalidResolverException(managedResolver.getResolverId());
        }

        // XXX event handler should get removed from previous resolver

        // TODO should we really fire this here??
        eventBus.fireEvent(new SlotMappingChangedEvent(slot, resolver));
    }

    public boolean errorsInModel() {
        return errorModel.hasErrors(slot);
    }

    /**
     * @return The current @link{ViewItemValueResolver}
     */
    public ManagedViewItemValueResolver getCurrentResolver() {
        return (ManagedViewItemValueResolver) context.getResolver(slot);
    }

    public Collection<ViewItemValueResolverFactory> getResolverFactories() {
        return allowableResolverFactories.values();
    }

    public Slot getSlot() {
        return slot;
    }

    /**
     * Returns whether or not the context's current resolver is both Managed,
     * and whether it is in the Allowable Factories
     */
    public boolean isAllowableResolver(ViewItemValueResolver resolver,
            LightweightCollection<ViewItem> currentViewItems) {
        assert resolver != null;
        if (!(resolver instanceof ManagedViewItemValueResolver)) {
            // Not a managed Resolver
            return false;
        }

        ManagedViewItemValueResolver managedResolver = (ManagedViewItemValueResolver) resolver;
        assert managedResolver.getResolverId() != null;

        /*
         * XXX hack for test cases, remove once slot mapping UI model is
         * separated from SlotModelConfiguration.
         */
        if (TESTING) {
            return true;
        }

        if (!allowableResolverFactories.containsKey(managedResolver
                .getResolverId())) {
            // Not an allowable resolver
            return false;
        }

        // TODO this is already check elsewhere
        if (currentViewItems != null) {
            for (ViewItem viewItem : currentViewItems) {
                if (!resolver.canResolve(viewItem, context)) {
                    // resolver can not resolve viewItems
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Updates the allowable resolver factories to only the ones that are
     * allowable given the {@code viewItems} and the current {@link Slot}.
     * 
     * This must be called at least once to initialize the mapping and the
     * currentResolver.
     * 
     * If the resolver can not resolve the set of view items, it will
     * automatically be changed to null
     */
    public void updateAllowableFactories(
            LightweightCollection<ViewItem> viewItems) {

        assert viewItems != null;
        allowableResolverFactories.clear();

        // if (currentResolver != null
        // && !currentResolver.canResolve(slot, resourceSets, null)) {
        // // Uh Oh the current resolver is bad, can we do something here?
        // // TODO handle this elsewhere, update slots should be called on this
        // // slot
        // }

        LightweightList<ViewItemValueResolverFactory> allFactories = provider
                .getResolverFactories();

        assert allFactories != null;

        for (ViewItemValueResolverFactory factory : allFactories) {
            if (factory.canCreateApplicableResolver(slot, viewItems)) {
                allowableResolverFactories.put(factory.getId(), factory);
            }
        }
    }
}
