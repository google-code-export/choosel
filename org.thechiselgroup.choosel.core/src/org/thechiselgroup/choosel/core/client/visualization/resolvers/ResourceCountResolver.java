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
package org.thechiselgroup.choosel.core.client.visualization.resolvers;

import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolverContext;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem.Subset;

// TODO DataType.NUMBER
public class ResourceCountResolver extends SubsetViewItemValueResolver {

    public ResourceCountResolver() {
        this(Subset.ALL);
    }

    public ResourceCountResolver(Subset subset) {
        super(subset);
    }

    @Override
    public boolean canResolve(VisualItem viewItem,
            VisualItemValueResolverContext context) {

        return true;
    }

    @Override
    public Object resolve(VisualItem viewItem,
            VisualItemValueResolverContext context, Subset subset) {
        return new Double(viewItem.getResources(subset).size());
    }

    @Override
    public String toString() {
        return "Count";
    }

}