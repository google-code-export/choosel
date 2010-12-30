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
package org.thechiselgroup.choosel.protovis.client;

import org.thechiselgroup.choosel.protovis.client.functions.PVDoubleFunction;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Wrapper for
 * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Layout.Stack.html">pv.Layout.Stack</a></code>
 * .
 * 
 * @author Lars Grammel
 */
public final class PVStackLayout extends PVAbstractBar<PVStackLayout> {

    protected PVStackLayout() {
    }

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Layout.Stack.html#layer">pv.Layout.Stack.layer</a></code>
     * .
     */
    public final native PVMark layer() /*-{
        return this.layer;
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Layout.Stack.html#layers">pv.Layout.Stack.layers()</a></code>
     * .
     */
    public final native PVStackLayout layers(JavaScriptObject data) /*-{
        return this.layers(data);
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Layout.Stack.html#offset">pv.Layout.Stack.offset()</a></code>
     * .
     */
    public final native PVStackLayout offset(String offset) /*-{
        return this.offset(offset);
    }-*/;

    /**
     * Wrapper for
     * <code><a href="http://vis.stanford.edu/protovis/jsdoc/symbols/pv.Layout.Stack.html#order">pv.Layout.Stack.order()</a></code>
     * .
     */
    public final native PVStackLayout order(String order) /*-{
        return this.order(order);
    }-*/;

    public final native PVStackLayout x(PVDoubleFunction<PVMark> f) /*-{
        return this.x(@org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVDoubleFunction;)(f));
    }-*/;

    public final native PVStackLayout y(PVDoubleFunction<PVMark> f) /*-{
        return this.y(@org.thechiselgroup.choosel.protovis.client.functions.JsFunctionUtils::toJavaScriptFunction(Lorg/thechiselgroup/choosel/protovis/client/functions/PVDoubleFunction;)(f));
    }-*/;

}