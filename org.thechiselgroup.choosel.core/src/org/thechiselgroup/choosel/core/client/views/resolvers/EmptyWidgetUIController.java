package org.thechiselgroup.choosel.core.client.views.resolvers;

import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.views.model.VisualItem;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class EmptyWidgetUIController implements
        ViewItemValueResolverUIController {

    @Override
    public Widget asWidget() {
        return new Label("");
    }

    @Override
    public void update(LightweightCollection<VisualItem> viewItems) {
        return;
    }

}
