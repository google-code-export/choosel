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
package org.thechiselgroup.choosel.core.client.util;

import com.google.gwt.core.client.JavaScriptObject;

public final class JavaScriptUtils {

    // @formatter:off
    public static native double toDouble(JavaScriptObject jso) /*-{
        return jso;
    }-*/;

    public static native int toInt(JavaScriptObject jso) /*-{
        return jso;
    }-*/;

    public static native String toString(JavaScriptObject jso) /*-{
        return jso;
    }-*/;
    // @formatter:on

    private JavaScriptUtils() {

    }

}
