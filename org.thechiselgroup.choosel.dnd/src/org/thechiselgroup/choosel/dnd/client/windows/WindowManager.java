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
package org.thechiselgroup.choosel.dnd.client.windows;

import org.thechiselgroup.choosel.core.client.geometry.Point;

import com.google.gwt.user.client.ui.AbsolutePanel;

public interface WindowManager {

    void bringToFront(WindowPanel window);

    void close(WindowPanel window);

    AbsolutePanel getBoundaryPanel();

    Point getLocation(WindowPanel window);

    WindowMoveController getMoveDragController();

    WindowResizeController getResizeDragController();

    void setLocation(WindowPanel window, int x, int y);

}