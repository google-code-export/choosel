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

/**
 * TODO improve type identification as using the first part of the URI is a poor
 * way to do this. Using semantic web technologies like RDF-S might be a better
 * approach.
 * 
 * @return first part of the URI until the ':'.
 */
public class ResourceByUriTypeCategorizer implements ResourceCategorizer {

    @Override
    public String getCategory(Resource resource) {
        return Resource.getTypeFromURI(resource.getUri());
    }

}