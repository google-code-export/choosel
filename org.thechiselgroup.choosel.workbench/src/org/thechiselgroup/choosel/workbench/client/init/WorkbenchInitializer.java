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
package org.thechiselgroup.choosel.workbench.client.init;

import static org.thechiselgroup.choosel.core.client.configuration.ChooselInjectionConstants.DATA_SOURCES;

import org.thechiselgroup.choosel.core.client.command.AsyncCommandExecutor;
import org.thechiselgroup.choosel.core.client.command.AsyncCommandToCommandAdapter;
import org.thechiselgroup.choosel.core.client.command.CommandManager;
import org.thechiselgroup.choosel.core.client.error_handling.ErrorHandler;
import org.thechiselgroup.choosel.core.client.importer.Importer;
import org.thechiselgroup.choosel.core.client.resources.ResourceSet;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetAddedEvent;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetAddedEventHandler;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetContainer;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetFactory;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetRemovedEvent;
import org.thechiselgroup.choosel.core.client.resources.ResourceSetRemovedEventHandler;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarFactory;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetAvatarResourceSetsPresenter;
import org.thechiselgroup.choosel.core.client.resources.ui.ResourceSetsPresenter;
import org.thechiselgroup.choosel.core.client.test.Benchmark;
import org.thechiselgroup.choosel.core.client.test.CreateBenchmarkResourcesCommand;
import org.thechiselgroup.choosel.core.client.ui.Action;
import org.thechiselgroup.choosel.core.client.ui.ActionBar;
import org.thechiselgroup.choosel.core.client.ui.TextCommandPresenter;
import org.thechiselgroup.choosel.core.client.ui.popup.PopupManagerFactory;
import org.thechiselgroup.choosel.core.client.util.BrowserDetect;
import org.thechiselgroup.choosel.dnd.client.windows.AbstractWindowContent;
import org.thechiselgroup.choosel.dnd.client.windows.CreateWindowCommand;
import org.thechiselgroup.choosel.dnd.client.windows.Desktop;
import org.thechiselgroup.choosel.dnd.client.windows.WindowContentProducer;
import org.thechiselgroup.choosel.workbench.client.InfoDialog;
import org.thechiselgroup.choosel.workbench.client.ToolbarPanel;
import org.thechiselgroup.choosel.workbench.client.authentication.AuthenticationManager;
import org.thechiselgroup.choosel.workbench.client.authentication.ui.AuthenticationBar;
import org.thechiselgroup.choosel.workbench.client.authentication.ui.AuthenticationBasedEnablingStateWrapper;
import org.thechiselgroup.choosel.workbench.client.command.ui.RedoActionStateController;
import org.thechiselgroup.choosel.workbench.client.command.ui.UndoActionStateController;
import org.thechiselgroup.choosel.workbench.client.ui.dialog.Dialog;
import org.thechiselgroup.choosel.workbench.client.ui.dialog.DialogManager;
import org.thechiselgroup.choosel.workbench.client.workspace.SaveActionStateController;
import org.thechiselgroup.choosel.workbench.client.workspace.ViewLoader;
import org.thechiselgroup.choosel.workbench.client.workspace.WorkspaceManager;
import org.thechiselgroup.choosel.workbench.client.workspace.WorkspacePersistenceManager;
import org.thechiselgroup.choosel.workbench.client.workspace.WorkspacePresenter;
import org.thechiselgroup.choosel.workbench.client.workspace.WorkspacePresenter.DefaultWorkspacePresenterDisplay;
import org.thechiselgroup.choosel.workbench.client.workspace.command.LoadViewAsWorkspaceCommand;
import org.thechiselgroup.choosel.workbench.client.workspace.command.LoadWorkspaceCommand;
import org.thechiselgroup.choosel.workbench.client.workspace.command.LoadWorkspaceDialogCommand;
import org.thechiselgroup.choosel.workbench.client.workspace.command.NewWorkspaceCommand;
import org.thechiselgroup.choosel.workbench.client.workspace.command.SaveWorkspaceCommand;
import org.thechiselgroup.choosel.workbench.client.workspace.command.ShareWorkspaceCommand;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public abstract class WorkbenchInitializer implements ApplicationInitializer {

    public static final String WINDOW_CONTENT_HELP = "help";

    public static final String WINDOW_CONTENT_NOTE = "note";

    public static final String WORKSPACE_ID = "workspaceId";

    public static final String DATA_PANEL = "data";

    public static final String EDIT_PANEL = "edit";

    public static final String HELP_PANEL = "help";

    public static final String VIEWS_PANEL = "views";

    public static final String WORKSPACE_PANEL = "workspace";

    public static final String DEVELOPER_MODE_PANEL = "developer_mode";

    public static final String VIEW_ID = "viewId";

    @Inject
    protected ActionBar actionBar;

    /**
     * Manages and provides access to the data sources (i.e. the imported and
     * loaded ResourceSets) that are available in this workspace.
     */
    @Inject
    protected @Named(DATA_SOURCES)
    ResourceSetContainer dataSources;

    @Inject
    private AuthenticationBar authenticationBar;

    @Inject
    protected Importer importer;

    @Inject
    protected AuthenticationManager authenticationManager;

    @Inject
    private AsyncCommandExecutor asyncCommandExecutor;

    @Inject
    protected ErrorHandler errorHandler;

    @Inject
    protected CommandManager commandManager;

    @Inject
    protected ResourceSetAvatarFactory defaultDragAvatarFactory;

    @Inject
    protected Desktop desktop;

    @Inject
    protected DialogManager dialogManager;

    @Inject
    private InfoDialog infoDialog;

    @Inject
    private LoadWorkspaceDialogCommand loadWorkspaceDialogCommand;

    @Inject
    private NewWorkspaceCommand newWorkspaceCommand;

    @Inject
    protected ResourceSetFactory resourceSetFactory;

    @Inject
    private SaveWorkspaceCommand saveWorkspaceCommand;

    @Inject
    private ShareWorkspaceCommand shareWorkspaceCommand;

    @Inject
    protected WindowContentProducer windowContentProducer;

    @Inject
    private WorkspaceManager workspaceManager;

    @Inject
    private WorkspacePersistenceManager workspacePersistenceManager;

    @Inject
    private PopupManagerFactory popupManagerFactory;

    @Inject
    private DefaultWorkspacePresenterDisplay workspacePresenterDisplay;

    @Inject
    private ViewLoader viewLoader;

    @Inject
    private ResourceSetAvatarFactory resourceSetAvatarFactory;

    protected void addActionToToolbar(String panelId, Action action) {
        getToolbarPanel(panelId).addAction(action);
    }

    protected Action addActionToToolbar(String panelId, String label,
            String iconName, Command command) {

        Action action = new Action(label, command, iconName);
        addActionToToolbar(panelId, action);
        return action;
    }

    protected void addCreateWindowActionToToolbar(String panelId, String label,
            final String contentType) {

        addCreateWindowActionToToolbar(panelId, label, null, contentType);
    }

    protected void addCreateWindowActionToToolbar(String panelId, String label,
            String iconName, final String contentType) {

        addActionToToolbar(panelId, label, iconName, new Command() {
            @Override
            public void execute() {
                createWindow(contentType);
            }
        });
    }

    protected Action addDialogActionToToolbar(String panelId, String label,
            Dialog dialog) {
        return addDialogActionToToolbar(panelId, label, null, dialog);
    }

    protected Action addDialogActionToToolbar(String panelId, String label,
            String iconName, final Dialog dialog) {

        return addActionToToolbar(panelId, label, iconName, new Command() {
            @Override
            public void execute() {
                dialogManager.show(dialog);
            }
        });
    }

    protected void addToolbarPanel(String panelId, String title) {
        assert panelId != null;
        assert title != null;

        actionBar
                .addPanel(new ToolbarPanel(panelId, title, popupManagerFactory));
    }

    protected void addWidget(String panelId, Widget widget) {
        assert panelId != null;
        assert widget != null;

        ((HorizontalPanel) actionBar.getPanel(panelId).getContentWidget())
                .add(widget);
    }

    // hook for subclasses
    protected void afterInit() {

    }

    private DockPanel createMainPanel() {
        DockPanel mainPanel = new DockPanel();
        RootPanel.get().add(mainPanel);
        return mainPanel;
    }

    protected ResourceSet createResourceSet() {
        return resourceSetFactory.createResourceSet();
    }

    protected ResourceSetsPresenter createResourceSetsPresenter() {
        final ResourceSetsPresenter dataSourcesPresenter = new ResourceSetAvatarResourceSetsPresenter(
                defaultDragAvatarFactory);
        dataSourcesPresenter.init();
        return dataSourcesPresenter;
    }

    protected void createWindow(AbstractWindowContent content) {
        commandManager.execute(new CreateWindowCommand(desktop, content));
    }

    private void createWindow(String contentType) {
        commandManager.execute(new CreateWindowCommand(desktop,
                windowContentProducer.createWindowContent(contentType)));
    }

    public ToolbarPanel getToolbarPanel(String panelId) {
        return (ToolbarPanel) actionBar.getPanel(panelId);
    }

    @Override
    public void init() throws Exception {
        initGlobalErrorHandler();
        BrowserDetect.checkBrowser();

        initWindowClosingConfirmationDialog();

        DockPanel mainPanel = createMainPanel();

        initDesktop(mainPanel);
        initActionBar(mainPanel);
        initAuthenticationBar();
        initActionBarContent();

        /*
         * TODO 3 options: new workspace, load workspace, load view as workspace
         * 
         * There should be a parameter to model this, with new workspace as
         * fallback mode that is used in case of errors.
         */

        String viewIdParam = Window.Location.getParameter(VIEW_ID);
        if (viewIdParam != null) {
            Long viewID = Long.parseLong(viewIdParam);
            // XXX potential parsing exception

            LoadViewAsWorkspaceCommand loadWorkspaceCommand = new LoadViewAsWorkspaceCommand(
                    viewID, viewLoader);
            asyncCommandExecutor.execute(loadWorkspaceCommand);
        } else {
            String workspaceIdParam = Window.Location
                    .getParameter(WORKSPACE_ID);

            if (workspaceIdParam != null) {
                long workspaceID = Long.parseLong(workspaceIdParam);

                LoadWorkspaceCommand loadWorkspaceCommand = new LoadWorkspaceCommand(
                        workspaceID, workspacePersistenceManager);
                asyncCommandExecutor.execute(loadWorkspaceCommand);
            }
        }

        afterInit();
    }

    protected void initAboutAction() {
        addDialogActionToToolbar(HELP_PANEL, "About", "help-about", infoDialog);
    }

    protected void initActionBar(DockPanel mainPanel) {
        mainPanel.add(actionBar.asWidget(), DockPanel.NORTH);

        initWorkspaceTitlePresenter();
        initActionBarPanels();
    }

    protected void initActionBarContent() {
        initWorkspacePanel();
        initEditPanel();
        initDataPanel();
        initHelpPanel();
        initDeveloperModePanel();

        initCustomActions();
    }

    protected void initActionBarPanels() {
        addToolbarPanel(WORKSPACE_PANEL, "Workspace");
        addToolbarPanel(EDIT_PANEL, "Edit");
        addToolbarPanel(DATA_PANEL, "Data");
        addToolbarPanel(VIEWS_PANEL, "Views");
        initCustomPanels();
        addToolbarPanel(HELP_PANEL, "Help");

        if (runsInDevelopmentMode()) {
            addToolbarPanel(DEVELOPER_MODE_PANEL, "Dev Mode");
        }
    }

    protected void initAuthenticationBar() {
        ((VerticalPanel) actionBar.asWidget()).add(authenticationBar);
    }

    protected void initBenchmarkResourceCreator() {
        if (Benchmark.isBenchmarkEnabled() || runsInDevelopmentMode()) {
            TextCommandPresenter presenter = new TextCommandPresenter(
                    new CreateBenchmarkResourcesCommand(resourceSetFactory,
                            dataSources), "Add");
            presenter.init();
            TextBox textBox = presenter.getTextBox();
            textBox.setMaxLength(6);
            textBox.setWidth("50px");
            addWidget(DATA_PANEL, textBox);
            addWidget(DATA_PANEL, presenter.getExecuteButton());
        }
    }

    /**
     * Override to add custom actions and elements to the different panels.
     */
    protected void initCustomActions() {
    }

    /**
     * Override to insert custom panels between the views and the help panel.
     */
    protected void initCustomPanels() {
    }

    protected void initDataPanel() {
        initDataSourcesPresenter();
        initBenchmarkResourceCreator();
    }

    protected void initDataSourcesPresenter() {
        final ResourceSetAvatarResourceSetsPresenter presenter = new ResourceSetAvatarResourceSetsPresenter(
                resourceSetAvatarFactory);
        presenter.init();
        addWidget(DATA_PANEL, presenter.asWidget());
        dataSources.addEventHandler(new ResourceSetAddedEventHandler() {
            @Override
            public void onResourceSetAdded(ResourceSetAddedEvent e) {
                presenter.addResourceSet(e.getResourceSet());
            }
        });
        dataSources.addEventHandler(new ResourceSetRemovedEventHandler() {
            @Override
            public void onResourceSetRemoved(ResourceSetRemovedEvent e) {
                presenter.removeResourceSet(e.getResourceSet());
            }
        });
    }

    private void initDesktop(DockPanel mainPanel) {
        /*
         * Absolute root panel required for drag & drop into windows using
         * Firefox
         */
        desktop.asWidget().setPixelSize(Window.getClientWidth(),
                Window.getClientHeight() - ActionBar.ACTION_BAR_HEIGHT_PX);

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                desktop.asWidget().setPixelSize(event.getWidth(),
                        event.getHeight() - ActionBar.ACTION_BAR_HEIGHT_PX);
                // TODO windows need to be moved if they are out of the
                // range
            }
        });

        mainPanel.add(desktop.asWidget(), DockPanel.CENTER);
    }

    protected void initDeveloperModePanel() {
    }

    protected void initEditPanel() {
        initRedoAction();
        initUndoAction();
    }

    private void initGlobalErrorHandler() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable e) {
                errorHandler.handleError(e);
            }
        });
    }

    protected void initHelpAction() {
        addCreateWindowActionToToolbar(HELP_PANEL, "Help", "help",
                WINDOW_CONTENT_HELP);
    }

    protected void initHelpPanel() {
        initHelpAction();
        initAboutAction();
    }

    protected void initLoadWorkspaceAction() {
        Action loadAction = addActionToToolbar(WORKSPACE_PANEL,
                "Load Workspace", "workspace-open",
                new AsyncCommandToCommandAdapter(loadWorkspaceDialogCommand,
                        asyncCommandExecutor));

        new AuthenticationBasedEnablingStateWrapper(authenticationManager,
                loadAction).init();
    }

    protected void initNewWorkspaceAction() {
        addActionToToolbar(WORKSPACE_PANEL, "New Workspace", "workspace-new",
                newWorkspaceCommand);
    }

    protected void initRedoAction() {
        Action redoAction = addActionToToolbar(EDIT_PANEL, "Redo", "edit-redo",
                new Command() {
                    @Override
                    public void execute() {
                        assert commandManager.canRedo();
                        commandManager.redo();
                    }
                });

        new RedoActionStateController(commandManager, redoAction).init();
    }

    protected void initSaveWorkspaceAction() {
        Action saveAction = addActionToToolbar(WORKSPACE_PANEL,
                SaveActionStateController.MESSAGE_SAVE_WORKSPACE,
                "workspace-save", saveWorkspaceCommand);
        AuthenticationBasedEnablingStateWrapper authWrapper = new AuthenticationBasedEnablingStateWrapper(
                authenticationManager, saveAction);
        authWrapper.init();

        new SaveActionStateController(workspaceManager, saveAction, authWrapper)
                .init();
    }

    protected void initShareWorkspaceAction() {
        Action action = addActionToToolbar(WORKSPACE_PANEL, "Share Workspace",
                "workspace-share", new AsyncCommandToCommandAdapter(
                        shareWorkspaceCommand, asyncCommandExecutor));

        new AuthenticationBasedEnablingStateWrapper(authenticationManager,
                action).init();
    }

    protected void initUndoAction() {
        Action undoAction = addActionToToolbar(EDIT_PANEL, "Undo", "edit-undo",
                new Command() {
                    @Override
                    public void execute() {
                        assert commandManager.canUndo();
                        commandManager.undo();
                    }
                });

        new UndoActionStateController(commandManager, undoAction).init();
    }

    protected void initWindowClosingConfirmationDialog() {
        Window.addWindowClosingHandler(new ClosingHandler() {
            @Override
            public void onWindowClosing(ClosingEvent event) {
                event.setMessage("Unsaved changes to the workspace will be lost.");
            }
        });
    }

    protected void initWorkspacePanel() {
        initNewWorkspaceAction();
        initLoadWorkspaceAction();
        initSaveWorkspaceAction();
        initShareWorkspaceAction();
    }

    protected void initWorkspaceTitlePresenter() {
        // TODO refactor title area part
        WorkspacePresenter presenter = new WorkspacePresenter(workspaceManager,
                workspacePresenterDisplay);
        presenter.init();
        // TODO replace with stuff from workspace presenter
        workspacePresenterDisplay.getTextBox().addStyleName(
                "actionbar-titleArea-text");
        actionBar.getActionBarTitleArea().add(
                workspacePresenterDisplay.getTextBox());
    }

    protected boolean runsInDevelopmentMode() {
        return !GWT.isScript();
    }

}
