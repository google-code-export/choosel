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
package org.thechiselgroup.choosel.dnd.client.resources;

import org.thechiselgroup.choosel.core.client.command.CommandManager;
import org.thechiselgroup.choosel.core.client.views.ViewAccessor;

import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractResourceSetAvatarDropTargetManager implements
        ResourceSetAvatarDropTargetManager {

    private DropTargetCapabilityChecker capabilityChecker;

    private CommandManager commandManager;

    private ResourceSetAvatarDragController dragController;

    private ViewAccessor viewAccessor;

    public AbstractResourceSetAvatarDropTargetManager(
            CommandManager commandManager,
            ResourceSetAvatarDragController dragController,
            ViewAccessor viewAccessor,
            DropTargetCapabilityChecker capabilityChecker) {

        assert commandManager != null;
        assert dragController != null;
        assert viewAccessor != null;
        assert capabilityChecker != null;

        this.commandManager = commandManager;
        this.dragController = dragController;
        this.viewAccessor = viewAccessor;
        this.capabilityChecker = capabilityChecker;
    }

    protected abstract ResourceSetAvatarDropCommandFactory createCommandFactory(
            Widget dropTarget, ViewAccessor viewAccessor);

    @Override
    public void disableDropTarget(Widget dropTarget) {
        assert dropTarget != null;

        dragController.unregisterDropControllerFor(dropTarget);
    }

    @Override
    public void enableDropTarget(Widget dropTarget) {
        assert dropTarget != null;

        ResourceSetAvatarDropController controller = new ResourceSetAvatarDropController(
                dropTarget, createCommandFactory(dropTarget, viewAccessor),
                commandManager, viewAccessor, capabilityChecker);

        dragController.registerDropController(controller);
    }
}
