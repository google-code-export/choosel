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
package org.thechiselgroup.choosel.core.client.resources;

import static org.junit.Assert.assertEquals;
import static org.thechiselgroup.choosel.core.client.resources.TestResourceSetFactory.createResource;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ResourceByUriTypeCategorizerTest {

    @DataPoints
    public static int ids[] = { 2, 3, 4, 5, 6 };

    @DataPoints
    public static String types[] = { "a", "b", "type", "x1" };

    private ResourceByUriTypeCategorizer categorizer;

    @Theory
    public void getCategory(String type, int id) {
        Resource resource = createResource(type, id);
        assertEquals(type, categorizer.getCategory(resource));
    }

    @Before
    public void setUp() {
        this.categorizer = new ResourceByUriTypeCategorizer();
    }
}
