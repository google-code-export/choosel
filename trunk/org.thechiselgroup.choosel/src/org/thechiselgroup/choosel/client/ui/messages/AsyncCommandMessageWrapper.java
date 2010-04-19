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
package org.thechiselgroup.choosel.client.ui.messages;

import org.thechiselgroup.choosel.client.command.AsyncCommand;
import org.thechiselgroup.choosel.client.services.ForwardingAsyncCallback;
import org.thechiselgroup.choosel.client.util.HasDescription;
import org.thechiselgroup.choosel.client.util.RemoveHandle;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AsyncCommandMessageWrapper implements AsyncCommand {

    private MessageManager messageManager;

    private String message;

    private AsyncCommand delegate;

    @Inject
    public AsyncCommandMessageWrapper(MessageManager messageManager,
	    String message, AsyncCommand delegate) {

	assert delegate != null;
	assert messageManager != null;

	this.messageManager = messageManager;
	this.message = message;
	this.delegate = delegate;
    }

    public <T extends AsyncCommand & HasDescription> AsyncCommandMessageWrapper(
	    MessageManager messageManager, T delegate) {

	this(messageManager, delegate.getDescription(), delegate);
    }

    @Override
    public void execute(AsyncCallback<Void> callback) {
	final RemoveHandle messageHandle = messageManager.showMessage(message);

	delegate.execute(new ForwardingAsyncCallback<Void>(callback) {
	    @Override
	    public void onFailure(Throwable caught) {
		messageHandle.remove();
		super.onFailure(caught);
	    }

	    @Override
	    public void onSuccess(Void v) {
		messageHandle.remove();
		super.onSuccess(v);
	    }
	});
    }
}
