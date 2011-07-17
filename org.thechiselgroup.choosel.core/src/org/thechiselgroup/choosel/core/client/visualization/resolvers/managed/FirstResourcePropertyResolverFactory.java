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
import org.thechiselgroup.choosel.core.client.visualization.resolvers.FirstResourcePropertyResolver;

public class FirstResourcePropertyResolverFactory extends
        PropertyDependantVisualItemValueResolverFactory {

    public FirstResourcePropertyResolverFactory(DataType dataType,
            String resolverID) {
        super(resolverID, dataType);
    }

    @Override
    protected FirstResourcePropertyResolver createUnmanagedResolver(String property) {
        return new FirstResourcePropertyResolver(property, getValidDataType());
    }

    // TODO Perhaps a better value for this
    @Override
    public String getLabel() {
        return "Property Selector";
    }

}