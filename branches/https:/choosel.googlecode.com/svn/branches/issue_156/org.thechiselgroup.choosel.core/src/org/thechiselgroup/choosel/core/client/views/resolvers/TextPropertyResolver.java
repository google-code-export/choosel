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
package org.thechiselgroup.choosel.core.client.views.resolvers;

import org.thechiselgroup.choosel.core.client.resources.DataType;
import org.thechiselgroup.choosel.core.client.views.model.ViewItem;
import org.thechiselgroup.choosel.core.client.views.model.ViewItem.Subset;
import org.thechiselgroup.choosel.core.client.views.model.ViewItemValueResolverContext;

public class TextPropertyResolver extends FirstResourcePropertyResolver {

    public TextPropertyResolver(String property) {
        super("", property, DataType.TEXT);
    }

    @Override
    public String resolve(ViewItem viewItem,
            ViewItemValueResolverContext context, Subset subset) {
        // XXX can lead to weird effects (issue 115)
        if (viewItem.getResources().size() >= 2) {
            return viewItem.getViewItemID();
        }

        return (String) super.resolve(viewItem, context, subset);
    }

    @Override
    public String toString() {
        return getProperty() + " (text)";
    }

}