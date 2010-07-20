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
package org.thechiselgroup.choosel.client.resources;

import java.util.Iterator;
import java.util.List;

import org.thechiselgroup.choosel.client.label.LabelChangedEventHandler;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;

public class DelegatingResourceSet implements ResourceSet {

    private final ResourceSet delegate;

    public DelegatingResourceSet(ResourceSet delegate) {
        this.delegate = delegate;
    }

    @Override
    public void add(Resource resource) {
        delegate.add(resource);
    }

    @Override
    public void addAll(Iterable<Resource> resources) {
        delegate.addAll(resources);
    }

    @Override
    public <H extends ResourceEventHandler> HandlerRegistration addHandler(
            Type<H> type, H handler) {
        return delegate.addHandler(type, handler);
    }

    @Override
    public HandlerRegistration addLabelChangedEventHandler(
            LabelChangedEventHandler eventHandler) {
        return delegate.addLabelChangedEventHandler(eventHandler);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public boolean contains(Resource resource) {
        return delegate.contains(resource);
    }

    @Override
    public boolean containsAll(Iterable<Resource> resources) {
        return delegate.containsAll(resources);
    }

    @Override
    public boolean containsEqualResources(ResourceSet other) {
        return delegate.containsEqualResources(other);
    }

    @Override
    public boolean containsResourceWithUri(String uri) {
        return delegate.containsResourceWithUri(uri);
    }

    @Override
    public Resource getByUri(String uri) {
        return delegate.getByUri(uri);
    }

    public ResourceSet getDelegate() {
        return delegate;
    }

    @Override
    public Resource getFirstResource() {
        return delegate.getFirstResource();
    }

    @Override
    public String getLabel() {
        return delegate.getLabel();
    }

    @Override
    public boolean hasLabel() {
        return delegate.hasLabel();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean isModifiable() {
        return delegate.isModifiable();
    }

    @Override
    public Iterator<Resource> iterator() {
        return delegate.iterator();
    }

    @Override
    public void remove(Resource resource) {
        delegate.remove(resource);
    }

    @Override
    public void removeAll(Iterable<Resource> resources) {
        delegate.removeAll(resources);
    }

    @Override
    public void setLabel(String label) {
        delegate.setLabel(label);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void switchContainment(Resource resource) {
        delegate.switchContainment(resource);
    }

    @Override
    public void switchContainment(ResourceSet resources) {
        delegate.switchContainment(resources);
    }

    @Override
    public List<Resource> toList() {
        return delegate.toList();
    }

}