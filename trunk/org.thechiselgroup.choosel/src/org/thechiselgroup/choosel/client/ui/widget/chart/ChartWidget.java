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
package org.thechiselgroup.choosel.client.ui.widget.chart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.thechiselgroup.choosel.client.ui.widget.chart.protovis.Panel;
import org.thechiselgroup.choosel.client.ui.widget.chart.protovis.ProtovisEventHandler;
import org.thechiselgroup.choosel.client.ui.widget.chart.protovis.ProtovisFunctionString;
import org.thechiselgroup.choosel.client.ui.widget.chart.protovis.ProtovisFunctionStringToString;
import org.thechiselgroup.choosel.client.ui.widget.chart.protovis.Scale;
import org.thechiselgroup.choosel.client.util.ArrayUtils;
import org.thechiselgroup.choosel.client.views.SlotResolver;
import org.thechiselgroup.choosel.client.views.chart.ChartItem;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Bradley Blashko
 * 
 */
public abstract class ChartWidget extends Widget {

    public static class ChartItemComparator implements Comparator<ChartItem> {
        @Override
        public int compare(ChartItem item1, ChartItem item2) {
            return getDescriptionString(item1).compareTo(
                    getDescriptionString(item2));
        }

        private String getDescriptionString(ChartItem item) {
            return item.getResourceItem()
                    .getResourceValue(SlotResolver.DESCRIPTION_SLOT).toString();
        }
    }

    public static final String EVENT_TYPE_MOUSEUP = "mouseup";

    public static final String EVENT_TYPE_MOUSEOVER = "mouseover";

    public static final String EVENT_TYPE_MOUSEOUT = "mouseout";

    public static final String EVENT_TYPE_MOUSEMOVE = "mousemove";

    public static final String EVENT_TYPE_MOUSEDOWN = "mousedown";

    public static final String EVENT_TYPE_CLICK = "click";

    protected Panel chart;

    protected List<ChartItem> chartItems = new ArrayList<ChartItem>();

    protected int height;

    protected int width;

    public boolean partialHighlightingChange = false;

    protected String[] eventTypes = { EVENT_TYPE_CLICK, EVENT_TYPE_MOUSEDOWN,
            EVENT_TYPE_MOUSEMOVE, EVENT_TYPE_MOUSEOUT, EVENT_TYPE_MOUSEOVER,
            EVENT_TYPE_MOUSEUP };

    private ProtovisEventHandler handler = new ProtovisEventHandler() {
        @Override
        public void handleEvent(Event e, int index) {
            onEvent(e, index);
        }
    };

    protected Scale scale;

    protected ProtovisFunctionStringToString scaleLabelText = new ProtovisFunctionStringToString() {
        @Override
        public String f(String o, int index) {
            return scale.tickFormat(o.toString());
        }
    };

    /**
     * Flags status that chart widget is rendering. While rendering, events are
     * discarded.
     */
    protected boolean isRendering;

    protected ProtovisFunctionString chartFillStyle = new ProtovisFunctionString() {
        @Override
        public String f(ChartItem value, int i) {
            return value.getColour();
        }
    };

    public ChartWidget() {
        setElement(DOM.createDiv());
        // TODO extract + move to CSS
        getElement().getStyle().setProperty("backgroundColor", "white");
    }

    public void addChartItem(ChartItem resourceItem) {
        chartItems.add(resourceItem);
    }

    /**
     * Is called before the chart is rendered. Subclasses can override this
     * method to recalculate values that are used for all resource item specific
     * calls from protovis.
     */
    protected void beforeRender() {
    }

    protected double calculateAllResources(int i) {
        return Double.parseDouble(chartItems.get(i).getResourceItem()
                .getResourceValue(SlotResolver.FONT_SIZE_SLOT).toString());
    }

    protected double calculateHighlightedResources(int i) {
        return chartItems.get(i).getResourceItem().getHighlightedResources()
                .size();
    }

    public void checkResize() {
        if (chart != null) {
            resize(getOffsetWidth(), getOffsetHeight());
        }
    }

    /**
     * <code>drawChart</code> is only called if there are actual data items that
     * can be rendered ( jsChartItems.length >= 1 ).
     */
    protected abstract void drawChart();

    public ChartItem getChartItem(int index) {
        assert chartItems != null;
        assert 0 <= index;
        assert index < chartItems.size();

        return chartItems.get(index);
    }

    public List<ChartItem> getChartItems() {
        return chartItems;
    }

    protected JavaScriptObject getJsDataArray(List<Double> dataArray) {
        return ArrayUtils.toJsArray(ArrayUtils.toDoubleArray(dataArray));
    }

    protected JavaScriptObject getJsDataArrayForObject(List<Object> dataArray) {
        return ArrayUtils.toJsArray(dataArray.toArray());
    }

    protected SlotValues getSlotValues(String slot) {
        double[] slotValues = new double[chartItems.size()];

        for (int i = 0; i < chartItems.size(); i++) {
            Object value = chartItems.get(i).getResourceItem()
                    .getResourceValue(slot);

            if (value instanceof Double) {
                slotValues[i] = (Double) value;
            } else if (value instanceof Number) {
                slotValues[i] = ((Number) value).doubleValue();
            } else {
                slotValues[i] = Double.valueOf(value.toString());
            }
        }

        return new SlotValues(slotValues);
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        if (chart == null) {
            updateChart();
        }
    }

    protected void onEvent(Event e, int index) {
        getChartItem(index).onEvent(e);

        // TODO remove once selection is fixed
        if (e.getTypeInt() == Event.ONCLICK) {
            renderChart();
        }
    }

    protected abstract void registerEventHandler(String eventType,
            ProtovisEventHandler handler);

    protected void registerEventHandlers() {
        for (String eventType : eventTypes) {
            registerEventHandler(eventType, handler);
        }
    }

    public void removeChartItem(ChartItem chartItem) {
        chartItems.remove(chartItem);
    }

    public void renderChart() {
        // TODO instead of isRendering flag, remove event listeners before
        // rendering starts and add them again after rendering is finished.
        try {
            beforeRender();
            chart.render();
        } finally {
        }
    }

    private void resize(int width, int height) {
        if (width == this.width && height == this.height) {
            return;
        }

        this.width = width;
        this.height = height;
        updateChart(); // TODO render chart good enough?
    }

    // re-rendering requires reset?
    // see
    // http://groups.google.com/group/protovis/browse_thread/thread/b9032215a2f5ac25
    public void updateChart() {
        if (chartItems.size() == 0) {
            chart = Panel.createWindowPanel().canvas(getElement())
                    .height(height).width(width);
        } else {
            chart = Panel.createWindowPanel().canvas(getElement());
            Collections.sort(chartItems, new ChartItemComparator());
            drawChart();
            registerEventHandlers();
        }

        // XXX how often are event listeners assigned? are they removed?
        renderChart();
    }
}
