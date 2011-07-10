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

import java.util.Map;

import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceCategorizer;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetUtils;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolverContext;

// TODO DataType.Color
public class ViewItemColorResolver extends AbstractBasicVisualItemValueResolver {

    private static final String[] COLORS = new String[] { "#6495ed", "#b22222",
            "#A9C0B1" };

    private ResourceCategorizer categorizer;

    private Map<String, String> resourceTypeToColor = CollectionFactory
            .createStringMap();

    public ViewItemColorResolver(ResourceCategorizer categorizer) {
        this.categorizer = categorizer;
    }

    @Override
    public boolean canResolve(VisualItem viewItem,
            VisualItemValueResolverContext context) {

        return true;
    }

    @Override
    public Object resolve(VisualItem viewItem,
            VisualItemValueResolverContext context) {
        // TODO what if resource.isEmpty?
        if (viewItem.getResources().isEmpty()) {
            return COLORS[0]; // XXX we need something better
        }

        Resource resource = viewItem
        .getResources().getFirstElement();
        String resourceType = categorizer.getCategory(resource);

        if (!resourceTypeToColor.containsKey(resourceType)) {
            resourceTypeToColor.put(resourceType,
                    COLORS[resourceTypeToColor.size()]);
        }

        return resourceTypeToColor.get(resourceType);
    }
}