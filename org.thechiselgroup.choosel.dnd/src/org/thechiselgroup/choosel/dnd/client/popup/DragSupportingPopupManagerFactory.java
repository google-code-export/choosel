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
package org.thechiselgroup.choosel.dnd.client.popup;

import org.thechiselgroup.choosel.core.client.ui.WidgetFactory;
import org.thechiselgroup.choosel.core.client.ui.popup.DefaultPopupManagerFactory;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupManager;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupManagerFactory;

/**
 * This factory should be used instead of {@link DefaultPopupManagerFactory} if
 * there are elements inside the popup that can be dragged out of it.
 * 
 * @author Lars Grammel
 */
public class DragSupportingPopupManagerFactory implements PopupManagerFactory {

    @Override
    public PopupManager createPopupManager(WidgetFactory widgetFactory) {
        return new DragSupportingPopupManager(widgetFactory);
    }

}