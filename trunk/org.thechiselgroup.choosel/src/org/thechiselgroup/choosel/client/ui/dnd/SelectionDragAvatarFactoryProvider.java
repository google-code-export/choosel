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
package org.thechiselgroup.choosel.client.ui.dnd;

import static org.thechiselgroup.choosel.client.configuration.MashupInjectionConstants.*;

import java.util.ArrayList;
import java.util.List;

import org.thechiselgroup.choosel.client.command.CommandManager;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSetContainer;
import org.thechiselgroup.choosel.client.resources.action.RemoveSelectionSetAction;
import org.thechiselgroup.choosel.client.resources.ui.DefaultResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.DelegatingResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.HighlightingResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatar;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarFactoryProvider;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarType;
import org.thechiselgroup.choosel.client.resources.ui.UpdateResourceSetAvatarLabelFactory;
import org.thechiselgroup.choosel.client.resources.ui.popup.PopupResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.popup.PopupResourceSetAvatarFactory.Action;
import org.thechiselgroup.choosel.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.client.views.View;
import org.thechiselgroup.choosel.client.views.ViewAccessor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SelectionDragAvatarFactoryProvider implements
	ResourceSetAvatarFactoryProvider {

    private final ResourceSetAvatarDragController dragController;
    private final PopupManagerFactory popupManagerFactory;
    private final ViewAccessor viewAccessor;
    private final ResourceSetContainer setHoverModel;
    private final ResourceSet hoverModel;
    private final CommandManager commandManager;

    @Inject
    public SelectionDragAvatarFactoryProvider(
	    ResourceSetAvatarDragController dragController,
	    @Named(HOVER_MODEL) ResourceSet hoverModel,
	    @Named(HOVER_MODEL) ResourceSetContainer setHoverModel,
	    ViewAccessor viewAccessor, PopupManagerFactory popupManagerFactory,
	    CommandManager commandManager) {

	this.dragController = dragController;
	this.hoverModel = hoverModel;
	this.setHoverModel = setHoverModel;
	this.viewAccessor = viewAccessor;
	this.popupManagerFactory = popupManagerFactory;
	this.commandManager = commandManager;
    }

    @Override
    public ResourceSetAvatarFactory get() {
	ResourceSetAvatarFactory defaultFactory = new DefaultResourceSetAvatarFactory(
		"avatar-selection", ResourceSetAvatarType.SELECTION);

	ResourceSetAvatarFactory updateFactory = new UpdateResourceSetAvatarLabelFactory(
		defaultFactory);

	ResourceSetAvatarFactory dragEnableFactory = new DragEnableResourceSetAvatarFactory(
		updateFactory, dragController);

	ResourceSetAvatarFactory highlightingFactory = new HighlightingResourceSetAvatarFactory(
		dragEnableFactory, hoverModel, setHoverModel, dragController);

	// XXX refactor
	ResourceSetAvatarFactory clickFactory = new DelegatingResourceSetAvatarFactory(
		highlightingFactory) {
	    @Override
	    public ResourceSetAvatar createAvatar(ResourceSet resources) {
		final ResourceSetAvatar avatar = delegate
			.createAvatar(resources);

		avatar.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent event) {
			View v = viewAccessor.findView(avatar);
			if (avatar.getResourceSet().equals(v.getSelection())) {
			    v.setSelection(null);
			} else {
			    v.setSelection(avatar.getResourceSet());
			}
		    }
		});

		return avatar;
	    }
	};

	List<Action> actions = new ArrayList<Action>();
	actions.add(new RemoveSelectionSetAction(commandManager));
	// actions.add(new CreateSetAction(resourceSetFactory,
	// resourceSetLabelFactory, commandManager));

	return new PopupResourceSetAvatarFactory(clickFactory, viewAccessor,
		popupManagerFactory, actions, "View selection",
		"<p><b>Drag</b> this selection to create filtered views"
			+ " (by dropping on view content), to"
			+ " synchronize the selection in multiple views "
			+ "(by dropping on other selections) "
			+ "or to add resources"
			+ " to a different set or view "
			+ "(by dropping on 'All').</p>", true);
    }
}