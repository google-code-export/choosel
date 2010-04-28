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
package org.thechiselgroup.choosel.client.views;

import java.util.List;

import org.thechiselgroup.choosel.client.resolver.FixedValuePropertyValueResolver;
import org.thechiselgroup.choosel.client.resolver.PropertyValueResolverConverterWrapper;
import org.thechiselgroup.choosel.client.resolver.SimplePropertyValueResolver;
import org.thechiselgroup.choosel.client.test.ResourcesTestHelper;
import org.thechiselgroup.choosel.client.util.ConversionException;
import org.thechiselgroup.choosel.client.util.Converter;

public class DefaultSlotResolver implements SlotResolver {

    public void createColorSlotResolver(Layer layerModel, List<Layer> layers) {

	String color = SlotResolver.COLORS[layers.size()];

	layerModel.putResolver(COLOR_SLOT_ID,
		new FixedValuePropertyValueResolver(color));
    }

    public void createDateSlotResolver(Layer layerModel) {
	layerModel.putResolver(SlotResolver.DATE_SLOT_ID,
		new SimplePropertyValueResolver("date"));

    }

    public void createDescriptionSlotResolver(Layer layerModel) {
	// TODO switch based on category -- need category as part of layerModel
	// TODO resources as part of layerModel
	// TODO how to do the automatic color assignment?
	// TODO refactor // extract
	if ("tsunami".equals(layerModel.getCategory())) {
	    layerModel.putResolver(SlotResolver.DESCRIPTION_SLOT_ID,
		    new SimplePropertyValueResolver("date"));
	} else if ("earthquake".equals(layerModel.getCategory())) {
	    layerModel.putResolver(SlotResolver.DESCRIPTION_SLOT_ID,
		    new SimplePropertyValueResolver("description"));
	} else if (ResourcesTestHelper.DEFAULT_TYPE.equals(layerModel
		.getCategory())) {
	    layerModel.putResolver(SlotResolver.DESCRIPTION_SLOT_ID,
		    new SimplePropertyValueResolver(
			    ResourcesTestHelper.LABEL_KEY));
	} else {
	    throw new RuntimeException("failed creating slot mapping");
	}
    }

    public void createLabelSlotResolver(Layer layerModel) {
	Converter<Float, String> converter = new Converter<Float, String>() {
	    @Override
	    public String convert(Float value) throws ConversionException {
		if (value != null) {
		    int f = (value).intValue();
		    return "" + f;
		}

		return "";
	    }
	};

	SimplePropertyValueResolver resolver = new SimplePropertyValueResolver(
		"magnitude");

	layerModel.putResolver(SlotResolver.LABEL_SLOT_ID,
		new PropertyValueResolverConverterWrapper(resolver, converter));
    }

    public void createLocationSlotResolver(Layer layer) {
	layer.putResolver(SlotResolver.LOCATION_SLOT_ID,
		new SimplePropertyValueResolver("location"));

    }

}
