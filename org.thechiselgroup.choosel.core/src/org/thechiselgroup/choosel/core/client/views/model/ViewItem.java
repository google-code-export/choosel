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
package org.thechiselgroup.choosel.core.client.views.model;

import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.views.View;

/**
 * Model of several resources that should be displayed as a visual item in the
 * view. The <code>ViewItem</code> provides the highlighting and selection
 * status, enables {@link ViewContentDisplay}s to store a display object, and
 * facilitates the resolution of {@link Slot}s.
 * 
 * @author Lars Grammel
 * 
 * @see View
 */
// TODO rename to VisualItem or VisualElement or VisualizationElement?
public interface ViewItem {

    public static enum Status {

        NONE, PARTIAL, FULL

    }

    public static enum Subset {

        ALL, SELECTED, HIGHLIGHTED

    }

    /**
     * @see #setDisplayObject(Object)
     */
    Object getDisplayObject();

    /**
     * @return All resources that are contained in this {@link ViewItem}.
     */
    ResourceSet getResources();

    ResourceSet getResources(Subset subset);

    // TODO move to super interface
    Slot[] getSlots();

    Status getStatus(Subset subset);

    <T> T getValue(Slot slot);

    double getValueAsDouble(Slot slot);

    /**
     * Returns the identifier of the view item.
     */
    String getViewItemID();

    /**
     * @return true, if the status of the given subset is any of the provided
     *         status codes.
     */
    boolean isStatus(Subset subset, Status... status);

    /**
     * Events from the visual representations of view items in concrete
     * visualizations must be forwarded to their corresponding view item. This
     * is especially important for mouse events, and also for keyboard events.
     * Separating the visual representation from the event handling facilitates
     * customization and maintenance.
     */
    void reportInteraction(ViewItemInteraction interaction);

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