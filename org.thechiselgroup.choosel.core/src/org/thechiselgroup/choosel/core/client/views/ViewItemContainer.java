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
package org.thechiselgroup.choosel.core.client.views;

import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;

/**
 * Provides read access to {@link ViewItem}s.
 * 
 * @author Lars Grammel
 * 
 * @see ViewItem
 */
public interface ViewItemContainer {

    /**
     * @return <code>true</code>, if there is a {@link ViewItem} with the
     *         specified <code>viewItemId</code> in this container.
     */
    boolean containsViewItem(String viewItemId);

    /**
     * @Return {@link ViewItem} with the given ID.
     */
    ViewItem getViewItem(String viewItemId);

    /**
     * @return All {@link ViewItem}s in this container.
     */
    LightweightCollection<ViewItem> getViewItems();

    /**
     * @return {@link ViewItem}s that contain at least one of the given
     *         {@link Resource}s.
     */
    LightweightCollection<ViewItem> getViewItems(Iterable<Resource> resources);

}