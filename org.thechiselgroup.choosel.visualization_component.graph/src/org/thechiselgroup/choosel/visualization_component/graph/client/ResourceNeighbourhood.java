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
package org.thechiselgroup.choosel.visualization_component.graph.client;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.thechiselgroup.choosel.core.client.resources.Resource;

public class ResourceNeighbourhood implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Serializable> partialProperties;

    private List<Resource> resources;

    @SuppressWarnings("unused")
    private ResourceNeighbourhood() {
        // for GWT serialization
    }

    public ResourceNeighbourhood(Map<String, Serializable> partialProperties,
            List<Resource> resources) {

        this.partialProperties = partialProperties;
        this.resources = resources;
    }

    /**
     * @return Resource properties (with new values) that need to be updated
     *         when processing this response.
     */
    public Map<String, Serializable> getPartialProperties() {
        return partialProperties;
    }

    public List<Resource> getResources() {
        return resources;
    }

}