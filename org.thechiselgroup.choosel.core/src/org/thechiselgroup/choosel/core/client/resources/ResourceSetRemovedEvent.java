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

import com.google.gwt.event.shared.GwtEvent;

public class ResourceSetRemovedEvent extends
        GwtEvent<ResourceSetRemovedEventHandler> {

    public static final GwtEvent.Type<ResourceSetRemovedEventHandler> TYPE = new GwtEvent.Type<ResourceSetRemovedEventHandler>();

    private final ResourceSet resourceSet;

    public ResourceSetRemovedEvent(ResourceSet resourceSet) {
        this.resourceSet = resourceSet;
    }

    @Override
    protected void dispatch(ResourceSetRemovedEventHandler handler) {
        handler.onResourceSetRemoved(this);
    }

    @Override
    public GwtEvent.Type<ResourceSetRemovedEventHandler> getAssociatedType() {
        return TYPE;
    }

    public ResourceSet getResourceSet() {
        return resourceSet;
    }

}