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
package org.thechiselgroup.choosel.core.client.visualization.model.managed;

import org.thechiselgroup.choosel.core.client.util.collections.LightweightList;
import org.thechiselgroup.choosel.core.client.visualization.model.Slot;

public interface ManagedSlotMappingConfiguration {

    void addManagedSlotMappingConfigurationChangedEventHandler(
            ManagedSlotMappingConfigurationChangedEventHandler handler);

    ManagedVisualItemValueResolver getCurrentResolver(Slot slot);

    ManagedSlotMapping getManagedSlotMapping(Slot slot);

    LightweightList<ManagedSlotMapping> getManagedSlotMappings();

    Slot getSlotById(String slotId);

    Slot[] getSlots();

    boolean isConfigured(Slot slot);

    void setCurrentResolver(Slot slot, ManagedVisualItemValueResolver resolver);

}