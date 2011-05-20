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
package org.thechiselgroup.choosel.core.client.command;

import java.util.LinkedList;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class DefaultCommandManager implements CommandManager {

    private HandlerManager eventBus;

    private LinkedList<UndoableCommand> executedCommands = new LinkedList<UndoableCommand>();

    private LinkedList<UndoableCommand> undoneCommands = new LinkedList<UndoableCommand>();

    public DefaultCommandManager() {
        this.eventBus = new HandlerManager(this);
    }

    @Override
    public <H extends CommandManagerEventHandler> HandlerRegistration addHandler(
            Type<H> type, H handler) {
        return eventBus.addHandler(type, handler);
    }

    @Override
    public boolean canRedo() {
        return !undoneCommands.isEmpty();
    }

    @Override
    public boolean canUndo() {
        return !executedCommands.isEmpty();
    }

    @Override
    public void clear() {
        undoneCommands.clear();
        executedCommands.clear();
        fireEvent(new CommandManagerClearedEvent(this));
    }

    @Override
    public void execute(UndoableCommand command) {
        assert command != null;

        if (!command.hasExecuted()) {
            command.execute();
        }

        executedCommands.add(command);
        undoneCommands.clear();
        fireEvent(new CommandAddedEvent(this, command));

        assert canUndo();
        assert !canRedo();
    }

    private void fireEvent(CommandManagerEvent<?> event) {
        eventBus.fireEvent(event);
    }

    @Override
    public UndoableCommand getRedoCommand() {
        assert canRedo();
        return undoneCommands.getLast();
    }

    @Override
    public UndoableCommand getUndoCommand() {
        assert canUndo();
        return executedCommands.getLast();
    }

    @Override
    public void redo() {
        assert canRedo();

        UndoableCommand command = undoneCommands.removeLast();
        command.execute();
        executedCommands.add(command);
        fireEvent(new CommandRedoneEvent(this, command));

        assert canUndo();
    }

    @Override
    public void undo() {
        assert canUndo();

        UndoableCommand command = executedCommands.removeLast();
        command.undo();
        undoneCommands.add(command);
        fireEvent(new CommandUndoneEvent(this, command));

        assert canRedo();
    }

}
