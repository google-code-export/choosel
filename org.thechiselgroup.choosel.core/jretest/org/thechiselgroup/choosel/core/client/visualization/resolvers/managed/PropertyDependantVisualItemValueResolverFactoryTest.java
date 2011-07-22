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

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.thechiselgroup.choosel.core.client.util.DataType;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollections;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.resolvers.PropertyDependantVisualItemValueResolver;

public class PropertyDependantVisualItemValueResolverFactoryTest {

    private PropertyDependantVisualItemValueResolverFactory underTest;

    @Test
    public void cannotCreateResolverForEmptyVisualItemCollection() {
        assertFalse(underTest.canCreateApplicableResolver(new Slot("id",
                "name", DataType.TEXT), LightweightCollections
                .<VisualItem> emptyCollection()));
    }

    @Before
    public void setUp() throws Exception {
        underTest = new PropertyDependantVisualItemValueResolverFactory(
                "resolverId", DataType.TEXT) {
            @Override
            protected PropertyDependantVisualItemValueResolver createUnmanagedResolver(
                    String property) {
                return null;
            }

            @Override
            public String getLabel() {
                return null;
            }
        };
    }

}
