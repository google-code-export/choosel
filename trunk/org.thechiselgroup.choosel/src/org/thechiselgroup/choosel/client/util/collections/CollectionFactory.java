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
package org.thechiselgroup.choosel.client.util.collections;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;

public final class CollectionFactory {

    public static <T> LightweightList<T> createLightweightList() {
        if (GWT.isScript() || GWT.isClient()) {
            return new JavaScriptLightweightList<T>();
        }

        return new ArrayListToLightweightListAdapter<T>();
    }

    public static NumberArray createNumberArray() {
        if (GWT.isScript() || GWT.isClient()) {
            return JsDoubleArray.create();
        }

        return new DefaultNumberArray();
    }

    public static NumberArray createNumberArray(double... values) {
        NumberArray array = createNumberArray();
        for (double value : values) {
            array.push(value);
        }
        return array;
    }

    /**
     * Return a sorted String to value map.
     * 
     * @return Sorted String to value map. The Map does not implement SortedMap
     *         (TODO extend JavaScript implementation), however, the result from
     *         its iterator method is sorted.
     */
    public static <T> Map<String, T> createStringMap() {
        if (GWT.isScript() || GWT.isClient()) {
            return new JavaScriptStringToObjectMap<T>();
        }

        // return sorted Java map implementation
        return new TreeMap<String, T>();
    }

    public static Set<String> createStringSet() {
        if (GWT.isScript() || GWT.isClient()) {
            return new JavaScriptStringSet();
        }

        return new HashSet<String>();
    }

    private CollectionFactory() {
    }

}
