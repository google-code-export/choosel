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
package org.thechiselgroup.choosel.core.client.persistence;

import org.thechiselgroup.choosel.core.client.resources.persistence.ResourceSetAccessor;

/**
 * Optional factory restores {@link Persistable} objects from a {@link Memento}.
 * 
 * @author Lars Grammel
 */
public interface PersistableObjectFactory {

    String getFactoryId();

    /**
     * Restores a {@link Persistable} object from a {@link Memento}. The memento
     * must have the same factory id as this factory.
     */
    // TODO can we somehow remove accessor?
    Persistable restore(Memento memento, ResourceSetAccessor accessor);

}