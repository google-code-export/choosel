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
package org.thechiselgroup.choosel.client.views.graph;

import org.thechiselgroup.choosel.client.domain.ncbo.NCBO;
import org.thechiselgroup.choosel.client.ui.widget.graph.GraphDisplay;

public class DefaultArcStyleProvider implements ArcStyleProvider {

    @Override
    public String getArcColor(String arcType) {
	if (NCBO.CONCEPT_NEIGHBOURHOOD_DESTINATION_CONCEPTS.equals(arcType)) {
	    return "#AFC6E5";
	}

	if (GraphViewContentDisplay.ARC_TYPE_MAPPING.equals(arcType)) {
	    return "#D4D4D4";
	}

	return "#AFC6E5";
    }

    @Override
    public String getArcStyle(String arcType) {
	if (NCBO.CONCEPT_NEIGHBOURHOOD_DESTINATION_CONCEPTS.equals(arcType)) {
	    return GraphDisplay.ARC_STYLE_SOLID;
	}

	if (GraphViewContentDisplay.ARC_TYPE_MAPPING.equals(arcType)) {
	    return GraphDisplay.ARC_STYLE_DASHED;
	}

	return GraphDisplay.ARC_STYLE_SOLID;
    }

}
