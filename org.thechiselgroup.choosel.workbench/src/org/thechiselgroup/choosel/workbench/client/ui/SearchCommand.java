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
package org.thechiselgroup.choosel.workbench.client.ui;

import org.thechiselgroup.choosel.core.client.command.CommandManager;
import org.thechiselgroup.choosel.core.client.ui.HasTextParameter;
import org.thechiselgroup.choosel.core.client.windows.CreateWindowCommand;
import org.thechiselgroup.choosel.core.client.windows.Desktop;
import org.thechiselgroup.choosel.core.client.windows.WindowContent;
import org.thechiselgroup.choosel.core.client.windows.WindowContentProducer;

import com.google.gwt.user.client.Command;

public class SearchCommand implements Command, HasTextParameter {

    private CommandManager commandManager;

    private String contentType;

    private Desktop desktop;

    private String searchTerm;

    private WindowContentProducer viewFactory;

    public SearchCommand(CommandManager commandManager, Desktop desktop,
            WindowContentProducer viewFactory, String contentType) {

        this.commandManager = commandManager;
        this.desktop = desktop;
        this.viewFactory = viewFactory;
        this.contentType = contentType;
    }

    @Override
    public void execute() {
        WindowContent content = viewFactory.createWindowContent(contentType);

        assert content instanceof HasTextParameter;

        ((HasTextParameter) content).initParameter(searchTerm);

        commandManager.execute(new CreateWindowCommand(desktop, content));
    }

    @Override
    public void initParameter(String parameter) {
        this.searchTerm = parameter;
    }

}
