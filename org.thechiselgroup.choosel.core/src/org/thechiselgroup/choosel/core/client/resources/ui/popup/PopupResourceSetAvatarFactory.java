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
package org.thechiselgroup.choosel.core.client.resources.ui.popup;

import java.util.ArrayList;
import java.util.List;

import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ui.DelegatingResourceSetAvatarFactory;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatar;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarEnabledStatusEvent;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarEnabledStatusEventHandler;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarFactory;
import org.thechiselgroup.choosel.core.client.resources.ui.popup.ResourceSetAvatarPopupWidgetFactory.HeaderUpdatedEventHandler;
import org.thechiselgroup.choosel.core.client.resources.ui.popup.ResourceSetAvatarPopupWidgetFactory.ResourceSetAvatarPopupWidgetFactoryAction;
import org.thechiselgroup.choosel.core.client.ui.popup.DefaultPopupManager;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupManager;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.core.client.util.Disposable;
import org.thechiselgroup.choosel.core.client.views.View;
import org.thechiselgroup.choosel.core.client.views.ViewAccessor;

import com.google.gwt.event.shared.HandlerRegistration;

public class PopupResourceSetAvatarFactory extends
        DelegatingResourceSetAvatarFactory {

    public static interface Action {

        void execute(ResourceSet resources, View view);

        String getLabel();

    }

    public static class ActionToDragAvatarPopupWidgetFactoryActionAdapter
            implements ResourceSetAvatarPopupWidgetFactoryAction {

        private ResourceSetAvatar avatar;

        private Action delegate;

        private PopupManager popupManager;

        private ViewAccessor viewAccessor;

        public ActionToDragAvatarPopupWidgetFactoryActionAdapter(
                ViewAccessor viewAccessor, ResourceSetAvatar avatar,
                Action delegate) {

            this.viewAccessor = viewAccessor;
            this.avatar = avatar;
            this.delegate = delegate;
        }

        @Override
        public void execute() {
            delegate.execute(avatar.getResourceSet(),
                    viewAccessor.findView(avatar));
            popupManager.hidePopup();
        }

        @Override
        public String getLabel() {
            return delegate.getLabel();
        }

        public void setPopupManager(PopupManager popupManager) {
            this.popupManager = popupManager;
        }
    }

    private List<Action> actions;

    private String infoText;

    private final PopupManagerFactory popupManagerFactory;

    private final boolean resourceLabelModifiable;

    private String subHeaderText;

    private final ViewAccessor viewAccessor;

    public PopupResourceSetAvatarFactory(ResourceSetAvatarFactory delegate,
            ViewAccessor viewAccessor, PopupManagerFactory popupManagerFactory,
            List<Action> actions, String subHeaderText, String infoText,
            boolean resourceLabelModifiable) {

        super(delegate);

        this.viewAccessor = viewAccessor;
        this.popupManagerFactory = popupManagerFactory;
        this.infoText = infoText;
        this.actions = actions;
        this.subHeaderText = subHeaderText;
        this.resourceLabelModifiable = resourceLabelModifiable;
    }

    @Override
    public ResourceSetAvatar createAvatar(final ResourceSet resources) {
        final ResourceSetAvatar avatar = delegate.createAvatar(resources);

        List<ResourceSetAvatarPopupWidgetFactoryAction> actionAdapters = new ArrayList<ResourceSetAvatarPopupWidgetFactoryAction>();
        for (Action action : actions) {
            actionAdapters
                    .add(new ActionToDragAvatarPopupWidgetFactoryActionAdapter(
                            viewAccessor, avatar, action));
        }

        ResourceSetAvatarPopupWidgetFactory widgetFactory = new ResourceSetAvatarPopupWidgetFactory(
                avatar.getText(), subHeaderText, actionAdapters, infoText,
                resourceLabelModifiable ? new HeaderUpdatedEventHandler() {
                    @Override
                    public void headerLabelChanged(String newLabel) {
                        resources.setLabel(newLabel);
                    }
                } : null);

        final PopupManager popupManager = popupManagerFactory
                .createPopupManager(widgetFactory);

        for (ResourceSetAvatarPopupWidgetFactoryAction action : actionAdapters) {
            ((ActionToDragAvatarPopupWidgetFactoryActionAdapter) action)
                    .setPopupManager(popupManager);
        }

        final Disposable link = DefaultPopupManager.linkManagerToSource(
                popupManager, avatar);

        popupManager.setEnabled(avatar.isEnabled());
        final HandlerRegistration handlerRegistration = avatar
                .addEnabledStatusHandler(new ResourceSetAvatarEnabledStatusEventHandler() {
                    @Override
                    public void onDragAvatarEnabledStatusChange(
                            ResourceSetAvatarEnabledStatusEvent event) {
                        popupManager.setEnabled(avatar.isEnabled());
                    }
                });

        avatar.addDisposable(new Disposable() {
            @Override
            public void dispose() {
                handlerRegistration.removeHandler();
                link.dispose();
            }
        });

        return avatar;
    }
}
