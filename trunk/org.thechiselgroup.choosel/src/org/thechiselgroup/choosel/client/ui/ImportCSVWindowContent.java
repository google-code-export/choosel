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
package org.thechiselgroup.choosel.client.ui;

import java.io.Serializable;

import org.thechiselgroup.choosel.client.command.CommandManager;
import org.thechiselgroup.choosel.client.configuration.ChooselInjectionConstants;
import org.thechiselgroup.choosel.client.importer.CSVImporter;
import org.thechiselgroup.choosel.client.importer.ImportException;
import org.thechiselgroup.choosel.client.importer.ImportResult;
import org.thechiselgroup.choosel.client.importer.Importer;
import org.thechiselgroup.choosel.client.resources.DefaultResourceSet;
import org.thechiselgroup.choosel.client.resources.Resource;
import org.thechiselgroup.choosel.client.resources.ResourceSet;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarFactory;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetAvatarResourceSetsPresenter;
import org.thechiselgroup.choosel.client.resources.ui.ResourceSetsPresenter;
import org.thechiselgroup.choosel.client.views.map.MapViewContentDisplay;
import org.thechiselgroup.choosel.client.windows.AbstractWindowContent;
import org.thechiselgroup.choosel.client.windows.CreateWindowCommand;
import org.thechiselgroup.choosel.client.windows.Desktop;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class ImportCSVWindowContent extends AbstractWindowContent {

    private static final String IMPORT_CSV_CSS = "importCSV";

    // TODO test
    // TODO move --> resource set parser interface
    /*
     * TODO later: resource set not the perfect result, rather structure and
     * table of String values (since they should not be parsed at this point)
     */
    public static ResourceSet parseResourcesFromCSV(String csvText)
            throws ImportException {

        Importer importer = new CSVImporter();
        ImportResult importresult = importer.doImport(csvText);

        ResourceSet resources = new DefaultResourceSet();
        resources.setLabel("import"); // TODO changeable, inc number

        for (int row = 0; row < importresult.getRowCount(); row++) {
            // XXX this is a bug because uri's are used for caching
            String uri = "import:" + row; // TODO improved uri generation
            Resource resource = new Resource(uri);

            for (int column = 0; column < importresult.getColumnCount(); column++) {
                String stringValue = importresult.getValue(row, column);

                /*
                 * TODO should not be parsed at this point - change once setting
                 * property types possible
                 */
                Serializable value = stringValue;

                // number
                if (stringValue.matches("^[-+]?[0-9]*\\.?[0-9]+")) {
                    value = new Double(stringValue);
                }

                // date
                if (stringValue
                        .matches("^(0[1-9]|[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012]|[1-9])/\\d{4}")) {

                    value = DateTimeFormat.getFormat("dd/MM/yyyy").parse(
                            stringValue);
                }

                // location (long/lat)
                if (stringValue
                        .matches("^[-+]?[0-9]*\\.?[0-9]+\\/[-+]?[0-9]*\\.?[0-9]+")) {

                    Resource r = new Resource();

                    String[] split = stringValue.split("\\/");

                    r.putValue(MapViewContentDisplay.LATITUDE,
                            Double.parseDouble(split[0]));
                    r.putValue(MapViewContentDisplay.LONGITUDE,
                            Double.parseDouble(split[1]));

                    value = r;

                }

                resource.putValue(importresult.getColumnName(column), value);
            }

            resources.add(resource);
        }

        return resources;
    }

    private TextArea pasteArea;

    private DialogPanel panel;

    private ResourceSetAvatarFactory defaultDragAvatarFactory;

    private CommandManager commandManager;

    private Desktop desktop;

    public ImportCSVWindowContent(
            ResourceSetAvatarFactory defaultDragAvatarFactory,
            CommandManager commandManager, Desktop desktop) {

        super("Import CSV", ChooselInjectionConstants.WINDOW_CONTENT_CSV_IMPORT);

        this.defaultDragAvatarFactory = defaultDragAvatarFactory;
        this.commandManager = commandManager;
        this.desktop = desktop;
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    // TODO use dialog panel
    @Override
    public void init() {
        super.init();

        panel = new DialogPanel();
        panel.setHeader("Paste CSV content");

        pasteArea = new TextArea();
        panel.setContent(pasteArea);
        pasteArea.addStyleName(IMPORT_CSV_CSS);

        Button button = panel.createButton("parse");
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                try {
                    ResourceSet resources = parseResourcesFromCSV(pasteArea
                            .getText());

                    // TODO show dnd window with parsed data
                    resources.toString();

                    String title = "CSV Import";
                    final ResourceSetsPresenter presenter = new ResourceSetAvatarResourceSetsPresenter(
                            defaultDragAvatarFactory);
                    presenter.init();

                    commandManager.execute(new CreateWindowCommand(desktop,
                            new AbstractWindowContent(title, "TODO") {
                                @Override
                                public Widget asWidget() {
                                    return presenter.asWidget();
                                }
                            }));

                    presenter.addResourceSet(resources);

                } catch (ImportException e) {
                    // TODO choosel exception handling
                    e.printStackTrace();
                }
            }

        });

    }
}