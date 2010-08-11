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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.thechiselgroup.choosel.client.label.LabelChangedEventHandler;
import org.thechiselgroup.choosel.client.util.NullHandlerRegistration;
import org.thechiselgroup.choosel.client.util.NullIterator;

import com.google.gwt.event.shared.HandlerRegistration;

public final class NullResourceSet implements ResourceSet {

    public final static ResourceSet NULL_RESOURCE_SET = new NullResourceSet();

    public static boolean isNullResourceSet(ResourceSet resourceSet) {
        return resourceSet instanceof NullResourceSet;
    }

    private NullResourceSet() {

    }

    @Override
    public boolean add(Resource resource) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Resource> resources) {
        return false;
    }

    @Override
    public HandlerRegistration addEventHandler(
            ResourcesAddedEventHandler handler) {

        return NullHandlerRegistration.NULL_HANDLER_REGISTRATION;
    }

    @Override
    public HandlerRegistration addEventHandler(
            ResourcesRemovedEventHandler handler) {

        return NullHandlerRegistration.NULL_HANDLER_REGISTRATION;
    }

    @Override
    public HandlerRegistration addLabelChangedEventHandler(
            LabelChangedEventHandler eventHandler) {

        return NullHandlerRegistration.NULL_HANDLER_REGISTRATION;
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> resources) {
        return false;
    }

    @Override
    public boolean containsEqualResources(ResourceSet other) {
        return false;
    }

    @Override
    public boolean containsResourceWithUri(String uri) {
        return false;
    }

    @Override
    public Resource getByUri(String uri) {
        return null;
    }

    @Override
    public Resource getFirstResource() {
        return null;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public boolean hasLabel() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isModifiable() {
        return false;
    }

    @Override
    public Iterator<Resource> iterator() {
        return NullIterator.nullIterator();
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void setLabel(String label) {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void switchContainment(Resource resource) {
    }

    @Override
    public void switchContainment(ResourceSet resources) {
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) new Object[0];
    }

    @Override
    public List<Resource> toList() {
        return Collections.emptyList();
    }

}
