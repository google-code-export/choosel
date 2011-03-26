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
package org.thechiselgroup.choosel.core.client.resources.command;

import org.thechiselgroup.choosel.core.client.command.UndoableCommand;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.util.HasDescription;
import org.thechiselgroup.choosel.core.client.views.model.ResourceModel;

/**
 * Removes a labeled resource set from a view as an explicitly displayed
 * resource set, and removes all resources from this set from the view if they
 * are not explicitly contained in another user set for this view.
 */
public class RemoveResourceSetFromResourceModelCommand implements
        UndoableCommand, HasDescription {

    private String description;

    private ResourceSet resourceSet;

    protected ResourceModel resourceModel;

    public RemoveResourceSetFromResourceModelCommand(
            ResourceModel resourceModel, ResourceSet resourceSet) {
        this(resourceModel, resourceSet, "Remove resource set '"
                + resourceSet.getLabel() + "' from view");
    }

    public RemoveResourceSetFromResourceModelCommand(
            ResourceModel resourceModel, ResourceSet resourceSet,
            String description) {

        assert resourceModel != null;
        assert resourceSet != null;
        assert resourceSet.hasLabel();
        assert description != null;

        this.description = description;
        this.resourceModel = resourceModel;
        this.resourceSet = resourceSet;
    }

    @Override
    public void execute() {
        assert resourceModel.containsResourceSet(resourceSet);
        resourceModel.removeResourceSet(resourceSet);
        assert !resourceModel.containsResourceSet(resourceSet);
    }

    // TODO add view name / label once available
    @Override
    public String getDescription() {
        return description;
    }

    public ResourceModel getResourceModel() {
        return resourceModel;
    }

    public ResourceSet getResourceSet() {
        return resourceSet;
    }

    @Override
    public void undo() {
        assert !resourceModel.containsResourceSet(resourceSet);
        resourceModel.addResourceSet(resourceSet);
        assert resourceModel.containsResourceSet(resourceSet);
    }

}