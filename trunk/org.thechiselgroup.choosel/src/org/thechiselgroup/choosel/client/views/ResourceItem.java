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
package org.thechiselgroup.choosel.client.views;

import java.util.Collection;

import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.ui.popup.PopupManager;

public interface ResourceItem {

    public static enum Status {

        DEFAULT, HIGHLIGHTED, HIGHLIGHTED_SELECTED, SELECTED, PARTIALLY_HIGHLIGHTED, PARTIALLY_HIGHLIGHTED_SELECTED, PARTIALLY_SELECTED

    }

    public static enum SubsetStatus {

        NONE, PARTIAL, COMPLETE

    }

    Object getDisplayObject();

    /**
     * @return all resources in this resource item that are highlighted.
     *         Resources that are not contained in this resource item are not
     *         included in the result.
     */
    ResourceSet getHighlightedResources();

    Collection<Resource> getHighlightedSelectedResources();

    /**
     * @return highlighting manager that manages the highlighting for this
     *         visual representation of the resource item. For the popup, there
     *         is a separate highlighting manager.
     */
    HighlightingManager getHighlightingManager();

    SubsetStatus getHighlightStatus();

    PopupManager getPopupManager();

    ResourceSet getResourceSet();

    Object getResourceValue(Slot slot);

    Collection<Resource> getSelectedResources();

    SubsetStatus getSelectionStatus();

    Status getStatus();

    /**
     * The display object is an arbitrary objects that can be set by a view
     * content display. Usually it would the visual representation of this
     * resource item to facilitate fast lookup operations.
     * 
     * @param displayObject
     * 
     * @see #getDisplayObject()
     */
    void setDisplayObject(Object displayObject);

}