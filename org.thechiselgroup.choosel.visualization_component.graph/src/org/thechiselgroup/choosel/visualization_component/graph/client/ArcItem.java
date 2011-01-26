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


// TODO set visible method
public class ArcItem {

    private String arcColor;

    /**
     * One of the valid arc styles (ARC_STYLE_DASHED or ARC_STYLE_SOLID).
     * 
     * @see ArcSettings#ARC_STYLE_DASHED
     * @see ArcSettings#ARC_STYLE_SOLID
     */
    private String arcStyle;

    private Arc arc;

    private int arcThickness;

    private boolean visible = false;

    private final GraphDisplay graphDisplay;

    public ArcItem(Arc arc, String arcColor, String arcStyle, int arcThickness,
            GraphDisplay graphDisplay) {

        assert arc != null;
        assert arcColor != null;
        assert arcStyle != null;
        assert arcThickness > 0;
        assert graphDisplay != null;

        this.arc = arc;
        this.arcColor = arcColor;
        this.arcStyle = arcStyle;
        this.arcThickness = arcThickness;
        this.graphDisplay = graphDisplay;
    }

    private void applyArcColor() {
        graphDisplay.setArcStyle(arc, ArcSettings.ARC_COLOR, arcColor);
    }

    private void applyArcStyle() {
        graphDisplay.setArcStyle(arc, ArcSettings.ARC_STYLE, arcStyle);
    }

    private void applyArcThickness() {
        graphDisplay.setArcStyle(arc, ArcSettings.ARC_THICKNESS, ""
                + arcThickness);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ArcItem other = (ArcItem) obj;
        if (arc == null) {
            if (other.arc != null) {
                return false;
            }
        } else if (!arc.equals(other.arc)) {
            return false;
        }
        if (arcColor == null) {
            if (other.arcColor != null) {
                return false;
            }
        } else if (!arcColor.equals(other.arcColor)) {
            return false;
        }
        if (arcStyle == null) {
            if (other.arcStyle != null) {
                return false;
            }
        } else if (!arcStyle.equals(other.arcStyle)) {
            return false;
        }
        if (arcThickness != other.arcThickness) {
            return false;
        }
        return true;
    }

    public Arc getArc() {
        return arc;
    }

    public String getColor() {
        return arcColor;
    }

    public String getId() {
        return arc.getId();
    }

    public String getStyle() {
        return arcStyle;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arc == null) ? 0 : arc.hashCode());
        result = prime * result
                + ((arcColor == null) ? 0 : arcColor.hashCode());
        result = prime * result
                + ((arcStyle == null) ? 0 : arcStyle.hashCode());
        result = prime * result + arcThickness;
        return result;
    }

    public void setArcStyle(String arcStyle) {
        assert arcStyle != null;

        this.arcStyle = arcStyle;

        if (visible) {
            applyArcStyle();
        }
    }

    public void setArcThickness(int arcThickness) {
        assert arcThickness > 0;

        this.arcThickness = arcThickness;

        if (visible) {
            applyArcThickness();
        }
    }

    public void setColor(String arcColor) {
        assert arcColor != null;

        this.arcColor = arcColor;

        if (visible) {
            applyArcColor();
        }
    }

    public void setVisible(boolean visible) {
        if (this.visible == visible) {
            return;
        }

        this.visible = visible;

        if (visible) {
            graphDisplay.addArc(arc);

            applyArcStyle();
            applyArcColor();
            applyArcThickness();
        } else {
            graphDisplay.removeArc(arc);
        }
    }

    @Override
    public String toString() {
        return "ArcItem [arcColor=" + arcColor + ", arcStyle=" + arcStyle
                + ", arc=" + arc + ", arcThickness=" + arcThickness + "]";
    }

}
