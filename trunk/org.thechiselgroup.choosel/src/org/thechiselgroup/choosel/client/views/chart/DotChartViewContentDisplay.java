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
package org.thechiselgroup.choosel.client.views.chart;

import org.thechiselgroup.choosel.client.ui.Colors;
import org.thechiselgroup.choosel.client.ui.widget.protovis.Alignment;
import org.thechiselgroup.choosel.client.ui.widget.protovis.Dot;
import org.thechiselgroup.choosel.client.ui.widget.protovis.DoubleFunction;
import org.thechiselgroup.choosel.client.ui.widget.protovis.Label;
import org.thechiselgroup.choosel.client.ui.widget.protovis.ProtovisEventHandler;
import org.thechiselgroup.choosel.client.ui.widget.protovis.Rule;
import org.thechiselgroup.choosel.client.ui.widget.protovis.Scale;
import org.thechiselgroup.choosel.client.ui.widget.protovis.StringFunction;
import org.thechiselgroup.choosel.client.ui.widget.protovis.StringFunctionIntArg;
import org.thechiselgroup.choosel.client.views.DragEnablerFactory;

import com.google.inject.Inject;

public class DotChartViewContentDisplay extends ChartViewContentDisplay {

    private static final int BORDER_HEIGHT = 20;

    private static final int BORDER_WIDTH = 20;

    private static final String GRIDLINE_SCALE_COLOR = Colors.GRAY_1;

    private static final String AXIS_SCALE_COLOR = Colors.GRAY_2;

    private double[] dotCounts;

    protected int chartHeight;

    protected int chartWidth;

    private DoubleFunction<ChartItem> dotLeft = new DoubleFunction<ChartItem>() {

        @Override
        public double f(ChartItem value, int index) {
            return index * chartWidth / chartItems.size() + chartWidth
                    / (chartItems.size() * 2);
        }
    };

    private ProtovisFunctionDoubleWithCache<ChartItem> dotBottom = new ProtovisFunctionDoubleWithCache<ChartItem>() {

        @Override
        public void beforeRender() {
            if (chartItems.isEmpty()) {
                return;
            }

            dotCounts = new double[chartItems.size()];

            for (int i = 0; i < chartItems.size(); i++) {
                dotCounts[i] = calculateAllResources(i);
            }

        }

        @Override
        public double f(ChartItem value, int i) {
            return dotCounts[i] * chartHeight / getMaximumChartItemValue();
        }

    };

    private int baselineLabelBottom = -15;

    private StringFunctionIntArg scaleStrokeStyle = new StringFunctionIntArg() {
        @Override
        public String f(int value, int i) {
            return value == 0 ? AXIS_SCALE_COLOR : GRIDLINE_SCALE_COLOR;
        }
    };

    private Dot regularDot;

    private DoubleFunction<ChartItem> baselineLabelLeft = new DoubleFunction<ChartItem>() {
        @Override
        public double f(ChartItem value, int i) {
            return dotLeft.f(value, i);
        }
    };

    private String baselineLabelTextAlign = Alignment.CENTER;

    private StringFunction<ChartItem> baselineLabelText = new StringFunction<ChartItem>() {
        @Override
        public String f(ChartItem value, int i) {
            return value
                    .getResourceItem()
                    .getResourceValue(
                            BarChartViewContentDisplay.CHART_LABEL_SLOT)
                    .toString();
        }
    };

    @Inject
    public DotChartViewContentDisplay(DragEnablerFactory dragEnablerFactory) {
        super(dragEnablerFactory);
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();
        dotBottom.beforeRender();
    }

    private void calculateChartVariables() {
        chartWidth = width - BORDER_WIDTH * 2;
        chartHeight = height - BORDER_HEIGHT * 2;
    }

    private void dehighlightResources(int i) {
        chartItems.get(i).getResourceItem().getHighlightingManager()
                .setHighlighting(false);
    }

    private void deselectResources(int i) {
        chartItems
                .get(i)
                .getView()
                .getCallback()
                .switchSelection(
                        chartItems.get(i).getResourceItem().getResourceSet());
    }

    @Override
    public void drawChart() {
        assert chartItems.size() >= 1;

        calculateChartVariables();
        setChartParameters();

        calculateMaximumChartItemValue();
        Scale scale = Scale.linear(0, getMaximumChartItemValue()).range(0,
                chartHeight);
        drawScales(scale);
        drawSelectionBox();
        drawDot();

    }

    private void drawDot() {
        regularDot = getChart()
                .add(Dot.createDot())
                .data(chartItemJsArray)
                .bottom(dotBottom)
                .left(dotLeft)
                .size(Math.min(chartHeight, chartWidth)
                        / (chartItems.size() * 2)).fillStyle(chartFillStyle)
                .strokeStyle(Colors.STEELBLUE);

        regularDot.add(Label.createLabel()).left(baselineLabelLeft)
                .textAlign(baselineLabelTextAlign).bottom(baselineLabelBottom)
                .text(baselineLabelText);

    }

    protected void drawScales(Scale scale) {
        this.scale = scale;
        getChart().add(Rule.createRule()).data(scale.ticks()).bottom(scale)
                .strokeStyle(scaleStrokeStyle).width(chartWidth)
                .anchor(Alignment.LEFT).add(Label.createLabel())
                .text(scaleLabelText);

        getChart().add(Rule.createRule()).left(0).bottom(0)
                .strokeStyle(AXIS_SCALE_COLOR).height(chartHeight);
    }

    // @formatter:off
    public native void drawSelectionBox() /*-{
        var chart = this.@org.thechiselgroup.choosel.client.views.chart.ChartViewContentDisplay::getChart(),
        eventToggle = true,
        fade,
        s, 
        selectBoxAlpha = .42,
        selectBoxData = [{x:0, y:0, dx:0, dy:0}],
        thisChart = this;

        var selectBox = chart.add($wnd.pv.Panel)
            .data(selectBoxData)
            .events("all")
            .event("mousedown", $wnd.pv.Behavior.select())
            .event("selectstart", removeBoxes)
            .event("select", update)
            .event("selectend", addBoxes)
          .add($wnd.pv.Bar)
            .visible(function() {return s;})
            .left(function(d) {return d.x;})
            .top(function(d) {return d.y;})
            .width(function(d) {return d.dx;})
            .height(function(d) {return d.dy;})
            .fillStyle("rgba(193,217,241,"+selectBoxAlpha+")")
            .strokeStyle("rgba(0,0,0,"+selectBoxAlpha+")")
            .lineWidth(.5);

        var minusBox = selectBox.add($wnd.pv.Bar)
            .left(function(d) {return d.x + d.dx - 15;})
            .top(function(d) {return d.y + d.dy - 15;})
            .width(15)
            .height(15)
            .strokeStyle("rgba(0,0,0,"+selectBoxAlpha+")")
            .lineWidth(.5)
            .events("all")
            .event("mousedown", function(d) {
                var doReturn;
                for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.client.views.chart.ChartViewContentDisplay::chartItems.@java.util.ArrayList::size()(); i++) {
                    if(isInSelectionBox(d,i)) {
                        updateMinusPlus(d,i);
                        thisChart.@org.thechiselgroup.choosel.client.views.chart.DotChartViewContentDisplay::deselectResources(I)(i);
                        doReturn = true;
                    }
                }
                if(doReturn == true) {
                    removeBoxes(d);
                    return (s = null, chart);
                }
            })
            .event("mouseover", function(d) {
                for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.client.views.chart.ChartViewContentDisplay::chartItems.@java.util.ArrayList::size()(); i++) {
                    if(isInSelectionBox(d,i)) {
                        return this.fillStyle("FFFFE1");
                    }
                }
            })
            .event("mouseout", function() {return this.fillStyle("rgba(193,217,241,"+selectBoxAlpha+")");});

        var minus = minusBox.anchor("center").add($wnd.pv.Label)
            .visible(false)
            .text("-")
            .textStyle("black")
            .font("bold");

        var plusBox = selectBox.add($wnd.pv.Bar)
            .left(function(d) {return d.x + d.dx - 30;})
            .top(function(d) {return d.y + d.dy - 15;})
            .width(15)
            .height(15)
            .strokeStyle("rgba(0,0,0,.35)")
            .lineWidth(.5)
            .events("all")
            .event("mousedown", function(d) {
                var doReturn;
                for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.client.views.chart.ChartViewContentDisplay::chartItems.@java.util.ArrayList::size()(); i++) {
                    if(isInSelectionBox(d,i)) {
                        updateMinusPlus(d,i);
                        thisChart.@org.thechiselgroup.choosel.client.views.chart.DotChartViewContentDisplay::selectResources(I)(i);
                        doReturn = true;
                    }
                }
                if(doReturn == true) {
                    removeBoxes(d);
                    return (s = null, chart);
                }
            })
            .event("mouseover", function(d) {
                for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.client.views.chart.ChartViewContentDisplay::chartItems.@java.util.ArrayList::size()(); i++) {
                    if(isInSelectionBox(d,i)) {
                        return this.fillStyle("FFFFE1");
                    }
                }
            })
            .event("mouseout", function() {return this.fillStyle("rgba(193,217,241,"+selectBoxAlpha+")");});

        var plus = plusBox.anchor("center").add($wnd.pv.Label)
            .visible(false)
            .text("+")
            .textStyle("black")
            .font("bold");

        function addBoxes(d) {
            for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.client.views.chart.ChartViewContentDisplay::chartItems.@java.util.ArrayList::size()(); i++) {
                if(isInSelectionBox(d,i)) {
                    plusBox.visible(true);
                    plus.visible(true);
                    minusBox.visible(true);
                    minus.visible(true);
                    eventToggle = false;
                    update(d);
                    this.render();
                }
            }
        }

        function isInSelectionBox(d,i) {
            return thisChart.@org.thechiselgroup.choosel.client.views.chart.DotChartViewContentDisplay::isInSelectionBox(IIIII)(d.x, d.y, d.dx, d.dy, i);
        }

        function removeBoxes(d) {
            plusBox.visible(false);
            plus.visible(false);
            plus.textStyle("black");
            minusBox.visible(false);
            minus.visible(false);
            minus.textStyle("black");

            d.dx = 0;
            d.dy = 0;
            eventToggle = true;

            update(d);
        }

        function update(d) {
            s = d;
            s.x1 = d.x;
            s.x2 = d.x + d.dx;
            s.y1 = d.y;
            s.y2 = d.y + d.dy;

            for(var i = 0; i < thisChart.@org.thechiselgroup.choosel.client.views.chart.ChartViewContentDisplay::chartItems.@java.util.ArrayList::size()(); i++) {
                if(isInSelectionBox(d,i)) {
                    thisChart.@org.thechiselgroup.choosel.client.views.chart.DotChartViewContentDisplay::highlightResources(I)(i);
                } else {
                    thisChart.@org.thechiselgroup.choosel.client.views.chart.DotChartViewContentDisplay::dehighlightResources(I)(i);
                }
            }

            chart.context(null, 0, function() {return this.render();});
        }

        function updateMinusPlus(d,i) {
            update(d);
            thisChart.@org.thechiselgroup.choosel.client.views.chart.DotChartViewContentDisplay::onEvent(Lcom/google/gwt/user/client/Event;I)($wnd.pv.event,i);
        }
    }-*/;
    // @formatter:on

    private void highlightResources(int i) {
        chartItems.get(i).getResourceItem().getHighlightingManager()
                .setHighlighting(true);
    }

    private boolean isInSelectionBox(int x, int y, int dx, int dy, int i) {
        return dotLeft.f(chartItems.get(i), i) >= x
                && dotLeft.f(chartItems.get(i), i) <= x + dx
                && chartHeight - dotBottom.f(chartItems.get(i), i) + 20 >= y
                && chartHeight - dotBottom.f(chartItems.get(i), i) + 20 <= y
                        + dy;
    }

    private boolean isSelected(int i) {
        return !chartItems.get(i).getResourceItem().getSelectedResources()
                .isEmpty();
    }

    @Override
    protected void registerEventHandler(String eventType,
            ProtovisEventHandler handler) {
        regularDot.event(eventType, handler);
    }

    private void selectResources(int i) {
        chartItems
                .get(i)
                .getView()
                .getCallback()
                .switchSelection(
                        chartItems.get(i).getResourceItem().getResourceSet());
    }

    private void setChartParameters() {
        getChart().left(BORDER_WIDTH).bottom(BORDER_HEIGHT);
    }

}