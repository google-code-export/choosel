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
package org.thechiselgroup.choosel.client.command;

import com.google.gwt.event.shared.GwtEvent;

public class CommandRedoneEvent extends
	CommandManagerEvent<CommandRedoneEventHandler> {

    public static final GwtEvent.Type<CommandRedoneEventHandler> TYPE = new GwtEvent.Type<CommandRedoneEventHandler>();

    public CommandRedoneEvent(CommandManager commandManager,
	    UndoableCommand command) {
	super(commandManager, command);
    }

    @Override
    protected void dispatch(CommandRedoneEventHandler handler) {
	handler.onCommandRedone(this);
    }

    @Override
    public GwtEvent.Type<CommandRedoneEventHandler> getAssociatedType() {
	return TYPE;
    }

}