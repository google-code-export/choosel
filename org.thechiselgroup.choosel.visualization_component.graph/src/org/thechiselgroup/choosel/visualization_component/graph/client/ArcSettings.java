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
package org.thechiselgroup.choosel.visualization_component.graph.client;

/**
 * Constants for arc properties and values.
 */
public interface ArcSettings {

    /**
     * Arc property that defines the color of the arc. Valid values are HTML
     * color values (e.g. '#ff0000').
     */
    String ARC_COLOR = "normalLineColor";

    /**
     * Arc property that defines the thickness of the line.
     */
    String ARC_THICKNESS = "normalLineThickness";

    /**
     * Alpha value for arcs. Can be between 0 and 1.
     */
    String ARC_ALPHA = "normalLineAlpha";

    /**
     * Arc property that defines the style of the arc (solid or dashed).
     * 
     * @see #ARC_STYLE_DASHED
     * @see #ARC_STYLE_SOLID
     */
    String ARC_STYLE = "normalLineStyle";

    /**
     * Style constant for dashed arcs.
     * 
     * @see #ARC_STYLE
     */
    String ARC_STYLE_DASHED = "dashed";

    /**
     * Style constant for solid arcs.
     * 
     * @see #ARC_STYLE
     */
    String ARC_STYLE_SOLID = "solid";

}
