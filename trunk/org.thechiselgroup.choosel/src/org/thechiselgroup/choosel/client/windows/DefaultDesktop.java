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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.thechiselgroup.choosel.client.command.CommandManager;
import org.thechiselgroup.choosel.client.geometry.HasSize;
import org.thechiselgroup.choosel.client.geometry.Point;
import org.thechiselgroup.choosel.client.ui.ZIndex;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

// TODO wrapper similar to action bar
public class DefaultDesktop extends AbsolutePanel implements Desktop, HasSize {

    private static final String CSS_DESKTOP = "desktop";

    private PositionManager positionManager;

    private final DesktopWindowController windowController;

    private List<WindowPanel> windows = new LinkedList<WindowPanel>();

    @Inject
    public DefaultDesktop(CommandManager commandManager) {
	addStyleName(CSS_DESKTOP);

	// TODO extract constants
	positionManager = new PositionManager(this, 7, 13, 10);
	windowController = new DesktopWindowController(this, commandManager);

	initBranding();
    }

    private void addWindowInternal(WindowPanel window) {
	windows.add(window);
	window.setZIndex(ZIndex.DESKTOP_WINDOW_BASE + windows.indexOf(window));
    }

    @Override
    public AbsolutePanel asWidget() {
	return this;
    }

    public void bringToFront(WindowPanel window) {
	removeWindowInternal(window);
	addWindowInternal(window);
    }

    public void clearWindows() {
	List<WindowPanel> windows = new ArrayList<WindowPanel>(getWindows());
	for (WindowPanel w : windows) {
	    removeWindow(w);
	}
    }

    private WindowPanel createWindow(String title, Widget widget, int x, int y) {
	WindowPanel window = new WindowPanel();

	window.init(windowController, title, widget);

	addWindowInternal(window);
	add(window, x, y);

	return window;
    }

    // TODO what about views // view factories??
    public WindowPanel createWindow(WindowContent content) {
	content.init();

	// FIXME better prediction of window size
	Point point = positionManager.getNextLocation(500, 400);

	WindowPanel window = createWindow(content.getLabel(), content
		.asWidget(), point.x, point.y);

	window.setViewContent(content);

	window.adjustSize(); // required for search view

	return window;
    }

    public WindowPanel createWindow(WindowContent content, int x, int y,
	    int windowOffsetWidth, int windowOffsetHeight) {

	content.init();

	WindowPanel w = createWindow(content.getLabel(), content.asWidget(), x,
		y);
	w.setAbsoluteSize(windowOffsetWidth, windowOffsetHeight);
	w.setViewContent(content);

	return w;
    }

    @Override
    public int getHeight() {
	return getOffsetHeight();
    }

    @Override
    public int getWidth() {
	return getOffsetWidth();
    }

    public List<WindowPanel> getWindows() {
	return windows;
    }

    // TODO refactor: extract
    private void initBranding() {
	Label appTitleLabel = new Label("Bio-Mixer");
	appTitleLabel.addStyleName("branding-app-title");
	add(appTitleLabel);

	Label copyRightLabel = new Label(
		"(C) 2010 The CHISEL Group (www.thechiselgroup.org)");
	copyRightLabel.addStyleName("branding-copy-right");
	add(copyRightLabel);
    }

    public void removeWindow(WindowPanel window) {
	removeWindowInternal(window);
	remove(window);
    }

    private void removeWindowInternal(WindowPanel window) {
	int index = windows.indexOf(window);
	windows.remove(window);

	// lower z-index of previously higher windows
	for (int i = index; i < windows.size(); i++) {
	    windows.get(i).setZIndex(ZIndex.DESKTOP_WINDOW_BASE + i);
	}
    }

}