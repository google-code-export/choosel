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

import static org.thechiselgroup.choosel.server.workspace.WorkspaceSharingServiceImplementation.*;

import java.io.IOException;
import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thechiselgroup.choosel.client.authentication.AuthorizationException;
import org.thechiselgroup.choosel.server.PMF;

import com.allen_sauer.gwt.log.client.Log;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

// TODO refactor
public class AcceptInvitationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	UserService userService = UserServiceFactory.getUserService();
	WorkspaceSecurityManager securityManager = new WorkspaceSecurityManager(
		userService);

	// parse parameters
	Key invitationUid = KeyFactory.stringToKey(request
		.getParameter(PARAM_INVITATION));
	Long workspaceUid = Long.parseLong(request
		.getParameter(PARAM_WORKSPACE_ID));
	String password = request.getParameter(PARAM_PASSWORD);

	// check that user is logged in
	if (!userService.isUserLoggedIn()) {
	    // we need to add the parameters manually
	    String requestURL = request.getRequestURL().toString() + "?"
		    + PARAM_WORKSPACE_ID + "=" + workspaceUid + "&"
		    + PARAM_INVITATION + "="
		    + KeyFactory.keyToString(invitationUid) + "&"
		    + PARAM_PASSWORD + "=" + password;

	    String loginURL = userService.createLoginURL(requestURL);
	    response.sendRedirect(response.encodeRedirectURL(loginURL));
	    return;
	}

	try {
	    PersistenceManager manager = PMF.get().getPersistenceManager();
	    PersistentWorkspace workspace = manager.getObjectById(
		    PersistentWorkspace.class, workspaceUid);

	    try {
		checkInvitation(invitationUid, workspace, password, manager);

		// create permission & remove invitation
		securityManager.createWorkspacePermissionForCurrentUser(
			workspace, manager);
		removeInvitation(invitationUid, workspace, password, manager);
	    } catch (AuthorizationException e) {
		/*
		 * Users might use the link from the email to access the
		 * document after they accepted the invitation. Thus, as a
		 * fallback we need to check if they already have permission to
		 * access the workspace.
		 */
		securityManager.checkAuthorization(workspace, manager);
	    }

	    // redirect to main with load default workspace
	    String targetURL = WorkspaceSharingServiceServlet.constructURL(
		    "index.html?" + PARAM_WORKSPACE_ID + "=" + workspaceUid,
		    request, getServletContext());

	    response.sendRedirect(response.encodeRedirectURL(targetURL));
	} catch (AuthorizationException e) {
	    Log.warn("AuthorizationException: "
		    + userService.getCurrentUser().getEmail() + " - "
		    + invitationUid + " - " + password);
	    response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}

    }

    private void removeInvitation(Key invitationUid,
	    PersistentWorkspace workspace, String password,
	    PersistenceManager manager) {

	createInvitationQuery(manager).deletePersistentAll(workspace,
		invitationUid, password);
    }

    public void checkInvitation(Key invitationUid,
	    PersistentWorkspace workspace, String password,
	    PersistenceManager manager) throws AuthorizationException {

	Collection<PersistentSharingInvitation> invitations = getSharingInvitation(
		invitationUid, workspace, password, manager);

	if (invitations.isEmpty()) {
	    throw new AuthorizationException(
		    "User not authorized to access workspace");
	}
    }

    public Collection<PersistentSharingInvitation> getSharingInvitation(
	    Key invitationUid, PersistentWorkspace workspace, String password,
	    PersistenceManager manager) {

	return (Collection<PersistentSharingInvitation>) createInvitationQuery(
		manager).execute(workspace, invitationUid, password);
    }

    private Query createInvitationQuery(PersistenceManager manager) {
	Query query = manager.newQuery(PersistentSharingInvitation.class,
		"workspace == workspaceParam" + " && " + "uid == uidParam"
			+ " && " + " password == passwordParam");
	query.declareParameters("Long workspaceParam" + ", " + "Long uidParam"
		+ ", " + "String passwordParam");
	return query;
    }
}
