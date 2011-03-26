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
package org.thechiselgroup.choosel.core.client.views.model;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;

/**
 * User interaction with the visual representation of a {@link ViewItem}. This
 * should wrap mouse and potentially keyboard interaction. A separate class was
 * introduced to facilitate the integration of components using 3rd party
 * technology, e.g. Flash, Silverlight.
 */
public class ViewItemInteraction {

    /**
     * Separate event types are used to enable simulation of events that are not
     * supported by the GWT event library (as of GWT 2.1), e.g. HTML5 events
     * such as DRAG_START.
     */
    public static enum Type {

        CLICK(Event.ONCLICK), DOUBLE_CLICK(Event.ONDBLCLICK), MOUSE_OVER(
                Event.ONMOUSEOVER), MOUSE_MOVE(Event.ONMOUSEMOVE), MOUSE_OUT(
                Event.ONMOUSEOUT), MOUSE_DOWN(Event.ONMOUSEDOWN), MOUSE_UP(
                Event.ONMOUSEUP), DRAG_START, DRAG_END, UNDEFINED;

        private static final Map<Integer, Type> typesByCode = new HashMap<Integer, ViewItemInteraction.Type>();

        static {
            Type[] values = values();
            for (Type type : values) {
                if (type.getEventCode() != -1) {
                    typesByCode.put(type.getEventCode(), type);
                }
            }
        }

        /**
         * @return Type or {@link #UNDEFINED}, if there is no such event.
         * 
         * @see Event
         */
        private static Type byCode(int eventCode) {
            if (!typesByCode.containsKey(eventCode)) {
                return UNDEFINED;
            }
            return typesByCode.get(eventCode);
        }

        /**
         * Event code from {@link Event}, if available, -1 otherwise.
         */
        private int eventCode;

        private Type() {
            this(-1);
        }

        private Type(int eventCode) {
            this.eventCode = eventCode;
        }

        public int getEventCode() {
            return eventCode;
        }

    }

    /**
     * Unknown coordinate.
     * 
     * @see #getClientX()
     * @see #getClientY()
     */
    public final static int UNKNOWN = -2;

    private NativeEvent nativeEvent;

    private int clientX;

    private int clientY;

    private Type eventType;

    public ViewItemInteraction(NativeEvent e) {
        assert e != null;

        this.nativeEvent = e;
        this.clientX = e.getClientX();
        this.clientY = e.getClientY();
        this.eventType = Type.byCode(Event.as(e).getTypeInt());
    }

    public ViewItemInteraction(Type eventType) {
        this(eventType, UNKNOWN, UNKNOWN);
    }

    public ViewItemInteraction(Type eventType, int clientX, int clientY) {
        this.eventType = eventType;
        this.clientX = clientX;
        this.clientY = clientY;
    }

    /**
     * X-Coordinate of the event in the browser client area.Can be
     * {@link #UNKNOWN}.
     */
    public int getClientX() {
        return clientX;
    }

    /**
     * Y-Coordinate of the event in the browser client area. Can be
     * {@link #UNKNOWN}.
     */
    public int getClientY() {
        return clientY;
    }

    /**
     * Type of this event.
     * 
     * @see Type
     */
    public Type getEventType() {
        return eventType;
    }

    /**
     * Native event, if available.
     */
    public NativeEvent getNativeEvent() {
        return nativeEvent;
    }

    public boolean hasNativeEvent() {
        return nativeEvent != null;
    }

    @Override
    public String toString() {
        return "ViewItemInteraction [clientX=" + clientX + ", clientY="
                + clientY + ", eventType=" + eventType + "]";
    }

}