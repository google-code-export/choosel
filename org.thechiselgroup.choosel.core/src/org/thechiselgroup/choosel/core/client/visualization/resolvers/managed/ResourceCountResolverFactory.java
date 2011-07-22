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
package org.thechiselgroup.choosel.core.client.visualization.resolvers.managed;

import org.thechiselgroup.choosel.core.client.util.DataType;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem.Subset;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedVisualItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedVisualItemValueResolverDecorator;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.VisualItemValueResolverFactory;
import org.thechiselgroup.choosel.core.client.visualization.resolvers.ResourceCountResolver;

public class ResourceCountResolverFactory implements
        VisualItemValueResolverFactory {

    // TODO move
    public static final String ID = "ResourceCountResolverFactory";

    private final Subset subset;

    public ResourceCountResolverFactory() {
        this(Subset.ALL);
    }

    public ResourceCountResolverFactory(Subset subset) {
        this.subset = subset;
    }

    @Override
    public boolean canCreateApplicableResolver(Slot slot,
            LightweightCollection<VisualItem> visualItems) {
        return slot.getDataType().equals(DataType.NUMBER);
    }

    public ManagedVisualItemValueResolver create() {
        return new ManagedVisualItemValueResolverDecorator(getId(),
                new ResourceCountResolver(subset));
    }

    @Override
    public ManagedVisualItemValueResolver create(
            LightweightCollection<VisualItem> visualItems) {
        return new ManagedVisualItemValueResolverDecorator(getId(),
                new ResourceCountResolver(subset));
    }

    // TODO use class name?
    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getLabel() {
        return "Count";
    }

}
