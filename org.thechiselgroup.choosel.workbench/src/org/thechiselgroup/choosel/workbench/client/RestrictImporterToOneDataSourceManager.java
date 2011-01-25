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
package org.thechiselgroup.choosel.workbench.client;

import static org.thechiselgroup.choosel.core.client.configuration.ChooselInjectionConstants.DATA_SOURCES;

import org.thechiselgroup.choosel.core.client.resources.ResourceSetAddedEvent;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetAddedEventHandler;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetContainer;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetRemovedEvent;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetRemovedEventHandler;
import org.thechiselgroup.choosel.core.client.ui.HasEnabledState;
import org.thechiselgroup.choosel.core.client.util.Disposable;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.name.Named;

public class RestrictImporterToOneDataSourceManager implements Disposable {

    private HandlerRegistration handlerRegistration1;

    private HasEnabledState hasEnabledState;

    private final ResourceSetContainer dataSources;

    private HandlerRegistration handlerRegistration2;

    public RestrictImporterToOneDataSourceManager(
            @Named(DATA_SOURCES) ResourceSetContainer dataSources,
            HasEnabledState hasEnabledState) {

        assert dataSources != null;
        assert hasEnabledState != null;

        this.dataSources = dataSources;
        this.hasEnabledState = hasEnabledState;
    }

    @Override
    public void dispose() {
        handlerRegistration1.removeHandler();
        handlerRegistration2.removeHandler();
    }

    public void init() {
        handlerRegistration1 = dataSources
                .addEventHandler(new ResourceSetAddedEventHandler() {
                    @Override
                    public void onResourceSetAdded(ResourceSetAddedEvent e) {
                        updateEnabling();
                    }
                });
        handlerRegistration2 = dataSources
                .addEventHandler(new ResourceSetRemovedEventHandler() {
                    @Override
                    public void onResourceSetRemoved(ResourceSetRemovedEvent e) {
                        updateEnabling();
                    }
                });

        updateEnabling();
    }

    private void updateEnabling() {
        hasEnabledState.setEnabled(dataSources.getResourceSets().isEmpty());
    }
}
