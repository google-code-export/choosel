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
package org.thechiselgroup.choosel.server.workspace;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.services.ServiceException;
import org.thechiselgroup.choosel.client.test.Assert2;
import org.thechiselgroup.choosel.client.workspace.dto.ResourceSetDTO;
import org.thechiselgroup.choosel.client.workspace.dto.WindowDTO;
import org.thechiselgroup.choosel.client.workspace.dto.WorkspaceDTO;
import org.thechiselgroup.choosel.client.workspace.dto.WorkspacePreviewDTO;
import org.thechiselgroup.choosel.client.workspace.service.WorkspacePersistenceService;
import org.thechiselgroup.choosel.server.workspace.PersistentWorkspace;
import org.thechiselgroup.choosel.server.workspace.PersistentWorkspacePermission;
import org.thechiselgroup.choosel.server.workspace.WorkspacePersistenceServiceImplementation;
import org.thechiselgroup.choosel.server.workspace.WorkspaceSecurityManager;

public class WorkspacePersistenceServiceImplementationTest {

    private static final Long ID = new Long(101);

    private static final String NAME = "aa";

    private WorkspacePersistenceService service;

    @Mock
    private PersistenceManagerFactory pmf;

    @Mock
    private PersistenceManager manager;

    private PersistentWorkspace persistentWorkspace;

    private WorkspaceDTO[] workspaceDTOs;

    @Mock
    private WorkspaceSecurityManager securityManager;

    @Before
    public void setUp() {
	MockitoAnnotations.initMocks(this);

	workspaceDTOs = new WorkspaceDTO[] { new WorkspaceDTO(null, NAME),
		new WorkspaceDTO() };

	service = new WorkspacePersistenceServiceImplementation(pmf,
		securityManager);

	persistentWorkspace = new PersistentWorkspace();
	persistentWorkspace.setId(ID);
	persistentWorkspace.setName(NAME);

	when(pmf.getPersistenceManager()).thenReturn(manager);
    }

    // TODO test DTO update

    @Test
    public void saveNewWorkspaceDTO() throws ServiceException {
	WindowDTO windowDTO = new WindowDTO();
	workspaceDTOs[0].setWindows(new WindowDTO[] { windowDTO });
	workspaceDTOs[0].setResources(new Resource[0]);
	workspaceDTOs[0].setResourceSets(new ResourceSetDTO[0]);
	persistentWorkspace.setWindows(new WindowDTO[] { windowDTO });

	when(manager.makePersistent(Matchers.any())).thenReturn(
		persistentWorkspace);

	Long result = service.saveWorkspace(workspaceDTOs[0]);

	verify(pmf).getPersistenceManager();
	verify(manager).makePersistent(any(PersistentWorkspace.class));
	verify(manager).close();
	assertEquals(ID, result);
    }

    @Test
    public void getAllWorkspaces() throws ServiceException {
	PersistentWorkspacePermission permission = new PersistentWorkspacePermission();
	permission.setWorkspace(persistentWorkspace);

	Collection<PersistentWorkspacePermission> permissions = new ArrayList<PersistentWorkspacePermission>();
	permissions.add(permission);
	when(securityManager.getWorkspacePermissionsForCurrentUser(manager))
		.thenReturn(permissions);
	when(manager.getObjectById(PersistentWorkspace.class, ID)).thenReturn(
		persistentWorkspace);

	List<WorkspacePreviewDTO> result = service.loadWorkspacePreviews();

	List<WorkspacePreviewDTO> expected = new ArrayList<WorkspacePreviewDTO>();
	expected.add(new WorkspacePreviewDTO(ID, NAME));
	Assert2.assertEquals(expected, result);
    }
}
