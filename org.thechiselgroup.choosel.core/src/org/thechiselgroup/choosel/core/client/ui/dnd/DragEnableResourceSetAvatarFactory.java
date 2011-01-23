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
package org.thechiselgroup.choosel.core.client.ui.dnd;

import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ui.DelegatingResourceSetAvatarFactory;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatar;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarEnabledStatusEvent;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarEnabledStatusEventHandler;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarFactory;
import org.thechiselgroup.choosel.core.client.util.Disposable;

import com.google.gwt.event.shared.HandlerRegistration;

public class DragEnableResourceSetAvatarFactory extends
        DelegatingResourceSetAvatarFactory {

    private ResourceSetAvatarDragController dragController;

    public DragEnableResourceSetAvatarFactory(
            ResourceSetAvatarFactory delegate,
            ResourceSetAvatarDragController dragController) {

        super(delegate);

        assert dragController != null;
        this.dragController = dragController;
    }

    @Override
    public ResourceSetAvatar createAvatar(ResourceSet resources) {
        final ResourceSetAvatar avatar = delegate.createAvatar(resources);

        final HandlerRegistration registration = avatar
                .addEnabledStatusHandler(new ResourceSetAvatarEnabledStatusEventHandler() {
                    @Override
                    public void onDragAvatarEnabledStatusChange(
                            ResourceSetAvatarEnabledStatusEvent event) {
                        updateAvatar(avatar);
                    }
                });

        avatar.addDisposable(new Disposable() {
            @Override
            public void dispose() {
                registration.removeHandler();
            }
        });

        // throws exception if we try to remove drag handle from avatar without
        // drag handle, so we need to check if enabled.
        if (avatar.isEnabled()) {
            updateAvatar(avatar);
        }

        return avatar;
    }

    private void updateAvatar(final ResourceSetAvatar avatar) {
        dragController.setDraggable(avatar, avatar.isEnabled());
    }
}
