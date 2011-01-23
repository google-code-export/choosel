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

import org.thechiselgroup.choosel.core.client.views.DelegatingViewContentDisplay;
import org.thechiselgroup.choosel.core.client.views.ViewContentDisplay;
import org.thechiselgroup.choosel.core.client.views.ViewContentDisplayCallback;

public class DropEnabledViewContentDisplay extends DelegatingViewContentDisplay {

    private ResourceSetAvatarDropTargetManager dropTargetManager;

    public DropEnabledViewContentDisplay(ViewContentDisplay delegate,
            ResourceSetAvatarDropTargetManager dropTargetManager) {

        super(delegate);

        assert dropTargetManager != null;

        this.dropTargetManager = dropTargetManager;
    }

    @Override
    public void dispose() {
        dropTargetManager.disableDropTarget(asWidget());
        dropTargetManager = null;

        super.dispose();
    }

    @Override
    public void init(ViewContentDisplayCallback callback) {
        super.init(callback);

        dropTargetManager.enableDropTarget(asWidget());
    }

}
