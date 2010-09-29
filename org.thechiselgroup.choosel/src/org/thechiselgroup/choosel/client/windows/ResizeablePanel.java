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
package org.thechiselgroup.choosel.client.windows;

public interface ResizeablePanel {

    /**
     * WindowPanel direction constant, used in
     * {@link WindowResizeController#makeDraggable(com.google.gwt.user.client.ui.Widget, org.thechiselgroup.mashups.client.windows.demo.client.example.resize.WindowPanel.DirectionConstant)}
     * .
     */
    public static class DirectionConstant {

        public final int directionBits;

        public final String directionLetters;

        private DirectionConstant(int directionBits, String directionLetters) {
            this.directionBits = directionBits;
            this.directionLetters = directionLetters;
        }
    }

    /**
     * Specifies that resizing occur at the east edge.
     */
    int DIRECTION_EAST = 0x0001;

    /**
     * Specifies that resizing occur at the both edge.
     */
    int DIRECTION_NORTH = 0x0002;

    /**
     * Specifies that resizing occur at the south edge.
     */
    int DIRECTION_SOUTH = 0x0004;

    /**
     * Specifies that resizing occur at the west edge.
     */
    int DIRECTION_WEST = 0x0008;

    /**
     * Specifies that resizing occur at the east edge.
     */
    DirectionConstant EAST = new DirectionConstant(DIRECTION_EAST, "e");

    DirectionConstant EAST_TOP = new DirectionConstant(DIRECTION_EAST, "et");

    /**
     * Specifies that resizing occur at the both edge.
     */
    DirectionConstant NORTH = new DirectionConstant(DIRECTION_NORTH, "n");

    /**
     * Specifies that resizing occur at the north-east edge.
     */
    DirectionConstant NORTH_EAST = new DirectionConstant(DIRECTION_NORTH
            | DIRECTION_EAST, "ne");

    /**
     * Specifies that resizing occur at the north-west edge.
     */
    DirectionConstant NORTH_WEST = new DirectionConstant(DIRECTION_NORTH
            | DIRECTION_WEST, "nw");

    /**
     * Specifies that resizing occur at the south edge.
     */
    DirectionConstant SOUTH = new DirectionConstant(DIRECTION_SOUTH, "s");

    /**
     * Specifies that resizing occur at the south-east edge.
     */
    DirectionConstant SOUTH_EAST = new DirectionConstant(DIRECTION_SOUTH
            | DIRECTION_EAST, "se");

    /**
     * Specifies that resizing occur at the south-west edge.
     */
    DirectionConstant SOUTH_WEST = new DirectionConstant(DIRECTION_SOUTH
            | DIRECTION_WEST, "sw");

    /**
     * Specifies that resizing occur at the west edge.
     */
    DirectionConstant WEST = new DirectionConstant(DIRECTION_WEST, "w");

    DirectionConstant WEST_TOP = new DirectionConstant(DIRECTION_WEST, "wt");

    int getHeight();

    int getMinimumWidth();

    int getWidth();

    void moveBy(int right, int down);

    void setPixelSize(int width, int height);

}