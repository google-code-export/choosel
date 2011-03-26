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
package org.thechiselgroup.choosel.visualization_component.chart.client.functions;

import org.thechiselgroup.choosel.core.client.views.filter.ViewItemPredicate;
import org.thechiselgroup.choosel.core.client.views.model.ViewItem;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsArgs;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsBooleanFunction;

public class ViewItemPredicateJsBooleanFunction implements JsBooleanFunction {

    private final ViewItemPredicate predicate;

    public ViewItemPredicateJsBooleanFunction(ViewItemPredicate predicate) {
        assert predicate != null;
        this.predicate = predicate;
    }

    @Override
    public boolean f(JsArgs args) {
        return predicate.matches(args.<ViewItem> getObject());
    }

}