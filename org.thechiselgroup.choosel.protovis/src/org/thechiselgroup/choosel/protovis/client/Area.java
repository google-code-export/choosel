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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * 
 * @author Bradley Blashko
 * 
 */
// @formatter:off        
public class Area extends Mark {

    public static final native Area createArea() /*-{
        return $wnd.pv.Area;
    }-*/;

    protected Area() {
    }

    public final native <T extends Mark> T add(T mark) /*-{
        return this.add(mark);
    }-*/;

    public final native Area anchor(String anchor) /*-{
        return this.anchor(anchor);
    }-*/;

    public final native Area bottom(double bottom) /*-{
        return this.bottom(bottom);
    }-*/;

    public final native Area bottom(DoubleFunction<?> f) /*-{
        return this.bottom(@org.thechiselgroup.choosel.protovis.client.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/DoubleFunction;)(this,f));
    }-*/;

    public final native Area childIndex(int childIndex) /*-{
        return this.childIndex(childIndex);
    }-*/;

    public final native Area cursor(String cursor) /*-{
        return this.cursor(cursor);
    }-*/;

    public final native Area data(JavaScriptObject data) /*-{
        return this.data(data);
    }-*/;

    public final native Area def(String name) /*-{
        return this.def(name);
    }-*/;

    // TODO Likely needs some fixing
    public final native Area def(String name, DoubleFunction<?> f) /*-{
        return this.def(name, @org.thechiselgroup.choosel.protovis.client.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/DoubleFunction;)(this,f));
    }-*/;

    public final native Area def(String name, String constant) /*-{
        return this.def(name, constant);
    }-*/;

    public final native Area defaults(Mark mark) /*-{
        return this.defaults(mark);
    }-*/;

    public final native Area event(String eventType, ProtovisEventHandler handler) /*-{
        return this.event(eventType, @org.thechiselgroup.choosel.protovis.client.Mark::registerEvent(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/ProtovisEventHandler;)(this, handler));
    }-*/;

    public final native Area events(String events) /*-{
        return this.events(events);
    }-*/;

    public final native Area fillStyle(StringFunction<?> f) /*-{
        return this.fillStyle(@org.thechiselgroup.choosel.protovis.client.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/StringFunction;)(this,f));
    }-*/;

    public final native Area fillStyle(String colour) /*-{
        return this.fillStyle(colour);
    }-*/;

    public final native Area font(String font) /*-{
        return this.font(font);
    }-*/;

    public final native Area height(double height) /*-{
        return this.height(height);
    }-*/;

    public final native Area height(DoubleFunction<?> f) /*-{
        return this.height(@org.thechiselgroup.choosel.protovis.client.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/DoubleFunction;)(this,f));
    }-*/;

    public final native Area index(int index) /*-{
        return this.index(index);
    }-*/;

    public final native Area interpolate(String interpolate) /*-{
        return this.interpolate(interpolate);
    }-*/;

    public final native Area left(double left) /*-{
        return this.left(left);
    }-*/;

    public final native Area left(DoubleFunction<?> f) /*-{
        return this.left(@org.thechiselgroup.choosel.protovis.client.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/DoubleFunction;)(this,f));
    }-*/;

    public final native Area lineWidth(double lineWidth) /*-{
        return this.lineWidth(lineWidth);
    }-*/;

    public final native Area parent(Panel panel) /*-{
        return this.parent(panel);
    }-*/;

    public final native Area proto(Mark mark) /*-{
        return this.proto(mark);
    }-*/;

    public final native void render() /*-{
        return this.render();
    }-*/;

    public final native Area reverse(boolean reverse) /*-{
        return this.reverse(reverse);
    }-*/;

    public final native Area right(double right) /*-{
        return this.right(right);
    }-*/;

    public final native Area right(DoubleFunction<?> f) /*-{
        return this.right(@org.thechiselgroup.choosel.protovis.client.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/DoubleFunction;)(this,f));
    }-*/;

    public final native Area root(Panel panel) /*-{
        return this.root(panel);
    }-*/;

    public final native Area scale(double scale) /*-{
        return this.scale(scale);
    }-*/;

    public final native Area segmented(boolean segmented) /*-{
        return this.segmented(segmented);
    }-*/;

    public final native Area strokeStyle(String strokeStyle) /*-{
        return this.strokeStyle(strokeStyle);
    }-*/;

    public final native Area tension(double tension) /*-{
        return this.tension(tension);
    }-*/;

    public final native Area text(String text) /*-{
        return this.text(text);
    }-*/;

    public final native Area textAlign(String textAlign) /*-{
        return this.textAlign(textAlign);
    }-*/;
    
    public final native Area textAngle(double textAngle) /*-{
        return this.textAngle(textAngle);
    }-*/;
    
    public final native Area textBaseline(String textBaseline) /*-{
        return this.textBaseline(textBaseline);
    }-*/;
    
    public final native Area textDecoration(String textDecoration) /*-{
        return this.textDecoration(textDecoration);
    }-*/;
    
    public final native Area textMargin(double textMargin) /*-{
        return this.textMargin(textMargin);
    }-*/;
    
    public final native Area textShadow(String textShadow) /*-{
        return this.textShadow(textShadow);
    }-*/;
    
    public final native Area textStyle(String textStyle) /*-{
        return this.textStyle(textStyle);
    }-*/;
    
    public final native Area title(String title) /*-{
        return this.title(title);
    }-*/;
    
    public final native Area top(double top) /*-{
        return this.top(top);
    }-*/;
    
    public final native Area top(DoubleFunction<?> f) /*-{
        return this.top(@org.thechiselgroup.choosel.protovis.client.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/DoubleFunction;)(this,f));
    }-*/;
    
    public final native Area type(String type) /*-{
        return this.type(type);
    }-*/;
    
    public final native Area visible(boolean visible) /*-{
        return this.visible(visible);
    }-*/;
    
    public final native Area width(double width) /*-{
        return this.width(width);
    }-*/;
    
    public final native Area width(DoubleFunction<?> f) /*-{
        return this.width(@org.thechiselgroup.choosel.protovis.client.Mark::toJavaScriptFunction(Lcom/google/gwt/core/client/JavaScriptObject;Lorg/thechiselgroup/choosel/protovis/client/DoubleFunction;)(this,f));
    }-*/;
    // @formatter:on

}