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
package org.thechiselgroup.choosel.core.client.visualization.resolvers.ui;

import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class EmptyWidgetUIController implements
        VisualItemValueResolverUIController {

    private String id;

    public EmptyWidgetUIController(String id) {
        assert id != null;

        this.id = id;
    }

    @Override
    public Widget asWidget() {
        return new Label("");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void update(LightweightCollection<VisualItem> visualItems) {
        return;
    }

}
