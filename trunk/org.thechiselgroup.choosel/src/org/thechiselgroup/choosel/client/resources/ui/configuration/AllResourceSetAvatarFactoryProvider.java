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
package org.thechiselgroup.choosel.client.resources.ui.configuration;

import static org.thechiselgroup.choosel.client.configuration.MashupInjectionConstants.*;

import java.util.Collections;

import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ResourceSetContainer;
import org.thechiselgroup.choosel.client.resources.ui.AbstractResourceSetAvatarFactoryProvider;
import org.thechiselgroup.choosel.client.resources.ui.DefaultResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.DisableIfEmptyResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.FixedLabelResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.HighlightingResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarType;
import org.thechiselgroup.choosel.client.resources.ui.popup.PopupResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.ui.dnd.DragEnableResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.ui.dnd.DropTargetResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.ui.dnd.ResourceSetAvatarDragController;
import org.thechiselgroup.choosel.client.ui.dnd.ResourceSetAvatarDropTargetManager;
import org.thechiselgroup.choosel.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.client.views.ViewAccessor;

import com.google.inject.Inject;
import com.google.inject.name.Named;

//TODO refactor see TypeDragAvatarFactoryProvider for example
public class AllResourceSetAvatarFactoryProvider extends
	AbstractResourceSetAvatarFactoryProvider {

    @Inject
    public AllResourceSetAvatarFactoryProvider(
	    ResourceSetAvatarDragController dragController,
	    @Named(HOVER_MODEL) ResourceSet hoverModel,
	    @Named(HOVER_MODEL) ResourceSetContainer setHoverModel,
	    @Named(AVATAR_FACTORY_ALL_RESOURCES) ResourceSetAvatarDropTargetManager dropTargetManager,
	    ViewAccessor viewAccessor, PopupManagerFactory popupManagerFactory) {

	super(
		new PopupResourceSetAvatarFactory(
			new HighlightingResourceSetAvatarFactory(
				new DropTargetResourceSetAvatarFactory(
					new DragEnableResourceSetAvatarFactory(
						new FixedLabelResourceSetAvatarFactory(
							new DisableIfEmptyResourceSetAvatarFactory(
								new DefaultResourceSetAvatarFactory(
									"avatar-allResources",
									ResourceSetAvatarType.ALL)),
							"All"), dragController),
					dropTargetManager), hoverModel,
				setHoverModel, dragController), viewAccessor,
			popupManagerFactory, Collections.EMPTY_LIST,
			"All resources in this view",
			"<p><b>Drag</b> to add all resources "
				+ "from this view to other views "
				+ "(by dropping on 'All' set), "
				+ "to create filtered views containing "
				+ "all resources from this view "
				+ "(by dropping on view content) " + "or to"
				+ " select all resources from "
				+ "this view (by dropping on selection).</p>",
			false));
    }
}