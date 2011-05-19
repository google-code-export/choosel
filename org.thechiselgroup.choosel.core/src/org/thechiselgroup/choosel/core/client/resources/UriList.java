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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class UriList implements Serializable, Iterable<String> {

    private static final long serialVersionUID = -3207445518794182555L;

    // is an ArrayList so that Serialization does not fail
    private ArrayList<String> delegate = new ArrayList<String>();

    private boolean isLoaded;

    // this constructor is required by GWT for serialization of the object
    public UriList() {

    }

    public UriList(String... uris) {
        for (String uri : uris) {
            add(uri);
        }
    }

    public void add(String uri) {
        assert uri != null;

        if (delegate.contains(uri)) {
            return;
        }

        delegate.add(uri);
    }

    public boolean contains(String uri) {
        assert uri != null;
        return delegate.contains(uri);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public Iterator<String> iterator() {
        return delegate.iterator();
    }

    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    public int size() {
        return delegate.size();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
