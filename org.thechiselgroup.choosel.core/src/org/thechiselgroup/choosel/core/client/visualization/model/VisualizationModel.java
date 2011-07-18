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
package org.thechiselgroup.choosel.core.client.visualization.model;

import org.thechiselgroup.choosel.core.client.resources.HasResourceCategorizer;
import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;

// TODO javadoc
// NOTE: facade to visualization subsystem 
public interface VisualizationModel extends HasResourceCategorizer,
        SlotMappingConfiguration, VisualItemResolutionErrorModel {

    /**
     * @return {@link VisualItemContainer} that contains all {@link VisualItem}s
     *         (including the ones that have errors).
     */
    VisualItemContainer getFullVisualItemContainer();

    /**
     * @return {@link ResourceSet} of the {@link Resource}s that are visualized
     *         in this {@link VisualizationModel}.
     */
    // TODO rename
    ResourceSet getContentResourceSet();

    // TODO set
    // TODO ReadableResourcesSet
    ResourceSet getHighlightedResources();

    // TODO set
    // TODO ReadableResourceSet
    ResourceSet getSelectedResources();

    // TODO remove, expose visualization properties instead
    ViewContentDisplay getViewContentDisplay();

    // TODO rename
    void setContentResourceSet(ResourceSet resources);

}