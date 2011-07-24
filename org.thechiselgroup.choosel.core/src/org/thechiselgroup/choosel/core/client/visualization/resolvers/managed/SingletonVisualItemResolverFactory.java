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
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.AbstractVisualItemValueResolverFactory;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedVisualItemValueResolver;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedVisualItemValueResolverDecorator;

public class SingletonVisualItemResolverFactory extends
        AbstractVisualItemValueResolverFactory {

    private VisualItemValueResolver resolver;

    public SingletonVisualItemResolverFactory(String id, DataType dataType,
            String label, VisualItemValueResolver resolver) {

        super(id, dataType, label);

        this.resolver = resolver;
    }

    /**
     * This method does not need to worry about the visualItems because it is
     * fixed value.
     */
    public ManagedVisualItemValueResolver create() {
        return wrap(resolver);
    }

    @Override
    public ManagedVisualItemValueResolver create(
            LightweightCollection<VisualItem> visualItems) {
        return create();
    }

    protected ManagedVisualItemValueResolverDecorator wrap(
            VisualItemValueResolver delegate) {
        return new ManagedVisualItemValueResolverDecorator(id, delegate);
    }

}