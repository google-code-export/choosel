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
package org.thechiselgroup.choosel.core.client.resources.ui;

import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;

public abstract class AbstractDetailsWidgetHelper implements
        DetailsWidgetHelper {

    protected ResourceSetAvatarFactory avatarFactory;

    protected ResourceSetFactory resourceSetFactory;

    // TODO use dragavatarfactory instead of provider
    @Inject
    public AbstractDetailsWidgetHelper(ResourceSetFactory resourceSetFactory,
            ResourceSetAvatarFactory dragAvatarFactory) {

        this.resourceSetFactory = resourceSetFactory;
        this.avatarFactory = dragAvatarFactory;
    }

    protected void addRow(Resource resource, VerticalPanel verticalPanel,
            String label, String property) {
        addRow(resource, verticalPanel, label, property, true);
    }

    protected void addRow(Resource resource, VerticalPanel verticalPanel,
            String label, String property, boolean nowrap) {
        Object resourceValue = resource.getValue(property);
        String value = (resourceValue == null) ? "" : resourceValue.toString();
        addRow(label, value, nowrap, verticalPanel);
    }

    protected void addRow(String label, String value, boolean nowrap,
            VerticalPanel verticalPanel) {
        HTML html = GWT.create(HTML.class);
        html.setHTML("<span " + (nowrap ? "style='white-space:nowrap;'" : "")
                + "><b>" + label + ":</b> " + value + " </span>");
        verticalPanel.add(html);
    }

}