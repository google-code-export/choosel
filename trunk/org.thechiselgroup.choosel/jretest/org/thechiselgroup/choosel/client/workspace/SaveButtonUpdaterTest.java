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
package org.thechiselgroup.choosel.client.workspace;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.ui.HasEnabledState;
import org.thechiselgroup.choosel.client.workspace.SaveButtonUpdater;
import org.thechiselgroup.choosel.client.workspace.Workspace;
import org.thechiselgroup.choosel.client.workspace.WorkspaceManager;
import org.thechiselgroup.choosel.client.workspace.WorkspaceSavingState;
import org.thechiselgroup.choosel.client.workspace.WorkspaceSwitchedEvent;
import org.thechiselgroup.choosel.client.workspace.WorkspaceSwitchedEventHandler;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasText;

public class SaveButtonUpdaterTest {

    private static interface TestButton extends HasText, HasEnabledState,
	    Focusable {
    }

    @Mock
    private TestButton button;

    private SaveButtonUpdater underTest;

    private Workspace workspace;

    private Workspace workspace2;

    @Mock
    private WorkspaceManager workspaceManager;

    @Test
    public void initNotSavedState() {
	workspace.setSavingState(WorkspaceSavingState.NOT_SAVED);
	underTest.init();

	verify(button).setEnabled(true);
	verify(button).setText("Save");
    }

    @Test
    public void initSavedState() {
	workspace.setSavingState(WorkspaceSavingState.SAVED);
	underTest.init();

	verify(button).setEnabled(false);
	verify(button).setFocus(false);
	verify(button).setText("Saved");
    }

    @Test
    public void initSavingState() {
	workspace.setSavingState(WorkspaceSavingState.SAVING);
	underTest.init();

	verify(button).setEnabled(false);
	verify(button).setFocus(false);
	verify(button).setText("Saving");
    }

    @Before
    public void setUp() {
	MockitoAnnotations.initMocks(this);

	workspace = spy(new Workspace());
	workspace2 = spy(new Workspace());

	underTest = new SaveButtonUpdater(workspaceManager, button, button) {
	    @Override
	    protected void delayedSwitchToNotSaved() {
	    }
	};

	when(workspaceManager.getWorkspace()).thenReturn(workspace);
    }

    @Test
    public void switchToSavedState() {
	workspace.setSavingState(WorkspaceSavingState.NOT_SAVED);
	underTest.init();
	workspace.setSavingState(WorkspaceSavingState.SAVED);

	verify(button).setEnabled(false);
	verify(button).setText("Saved");
    }

    @Test
    public void switchToSaveState() {
	workspace.setSavingState(WorkspaceSavingState.SAVED);
	underTest.init();
	workspace.setSavingState(WorkspaceSavingState.NOT_SAVED);

	verify(button).setEnabled(true);
	verify(button).setText("Save");
    }

    @Test
    public void switchToSavingState() {
	workspace.setSavingState(WorkspaceSavingState.NOT_SAVED);
	underTest.init();
	workspace.setSavingState(WorkspaceSavingState.SAVING);

	verify(button).setFocus(false);
	verify(button).setEnabled(false);
	verify(button).setText("Saving");
    }

    private void switchWorkspace() {
	ArgumentCaptor<WorkspaceSwitchedEventHandler> argument = ArgumentCaptor
		.forClass(WorkspaceSwitchedEventHandler.class);
	verify(workspaceManager).addSwitchedWorkspaceEventHandler(
		argument.capture());
	argument.getValue().onWorkspaceSwitched(
		new WorkspaceSwitchedEvent(workspace2));
    }

    @Test
    public void updateOnWorkspaceChange() {
	workspace.setSavingState(WorkspaceSavingState.NOT_SAVED);
	underTest.init();
	workspace2.setSavingState(WorkspaceSavingState.SAVED);

	switchWorkspace();

	verify(button).setEnabled(false);
	verify(button).setFocus(false);
	verify(button).setText("Saved");
    }

    @Test
    public void changeStateAfterWorkspaceChange() {
	workspace.setSavingState(WorkspaceSavingState.NOT_SAVED);
	underTest.init();
	workspace2.setSavingState(WorkspaceSavingState.NOT_SAVED);

	switchWorkspace();

	workspace2.setSavingState(WorkspaceSavingState.SAVED);

	verify(button).setEnabled(false);
	verify(button).setFocus(false);
	verify(button).setText("Saved");
    }

}
