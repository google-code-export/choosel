package org.thechiselgroup.choosel.core.client.visualization.model.managed;

import com.google.gwt.event.shared.EventHandler;

public interface ManagedSlotMappingConfigurationChangedEventHandler extends
        EventHandler {

    void onSlotMappingStateChanged(
            ManagedSlotMappingConfigurationChangedEvent event);
}
