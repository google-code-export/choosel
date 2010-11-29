package org.thechiselgroup.choosel.client.views;

import org.thechiselgroup.choosel.client.ui.WidgetAdaptable;
import org.thechiselgroup.choosel.client.workspace.ViewSaver;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ShareConfiguration implements WidgetAdaptable {

    private VerticalPanel sharePanel;

    private DefaultView view;

    private final ViewSaver viewPersistence;

    private Button button;

    private Label label;

    private TextBox textBox;

    private final String EMBED_POSTTEXT = "Created with <a href=\"http://choosel-mashups.appspot.com\">Choosel</a>";

    private final int EMBED_HEIGHT = 400;

    private final int EMBED_WIDTH = 480;

    private TextArea textArea;

    private Label embedLabel;

    public ShareConfiguration(DefaultView view, ViewSaver viewPersistence) {
        this.view = view;
        this.viewPersistence = viewPersistence;

    }

    @Override
    public Widget asWidget() {
        if (sharePanel == null) {
            init();
        }
        return sharePanel;
    }

    public DefaultView getView() {
        return view;
    }

    private void init() {
        sharePanel = new VerticalPanel();

        initShareControls();
    }

    private void initShareControls() {

        button = new Button("Share this");
        label = new Label("Generating Share Information...");
        label.setVisible(false);
        textBox = new TextBox();
        textBox.setVisible(false);
        embedLabel = new Label();
        embedLabel.setVisible(false);
        textArea = new TextArea();
        textArea.setVisible(false);

        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                label.setVisible(false);
                textBox.setVisible(false);
                embedLabel.setVisible(false);
                textArea.setVisible(false);

                label.setText("Generating Share Information...");
                label.setVisible(true);

                viewPersistence.saveView(ShareConfiguration.this);

            }
        });
        sharePanel.add(button);
        sharePanel.add(label);
        sharePanel.add(textBox);
        sharePanel.add(embedLabel);
        sharePanel.add(textArea);

    }

    public void updateSharePanel(Long id) {
        String url = Window.Location.getHref()
                + (Window.Location.getParameterMap().size() == 0 ? "?" : "&")
                + "viewId=" + id.toString();

        String embed = "<iframe src=\""
                + url
                + "\" width=\""
                + EMBED_WIDTH
                + "\" height=\""
                + EMBED_HEIGHT
                + "\">Sorry, your browser doesn't support iFrames</iframe><br /><a href=\""
                + url + "\">Full Size</a>. " + EMBED_POSTTEXT;

        // Hide things while we change them
        label.setVisible(false);

        label.setText("Share Link:");
        textBox.setText(url);
        label.setText("Embed Source:");
        textArea.setText(embed);

        label.setVisible(true);
        textBox.setVisible(true);
        embedLabel.setVisible(true);
        textArea.setVisible(true);

    }

}
