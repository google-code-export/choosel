/*******************************************************************************
 * Copyright (C) 2011 Lars Grammel 
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
package org.thechiselgroup.choosel.core.client.visualization.model;

public class SlotMappingResolutionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SlotMappingResolutionException(Slot slot, VisualItem visualItem,
            Exception cause) {

        super("slot resolution failed [view item=" + visualItem + ";slot=" + slot
                + "]", cause);
    }

}