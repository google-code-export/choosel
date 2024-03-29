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
package org.thechiselgroup.choosel.protovis.client;

import static org.thechiselgroup.choosel.protovis.client.PVAlignment.BOTTOM;
import static org.thechiselgroup.choosel.protovis.client.PVAlignment.LEFT;

import org.thechiselgroup.choosel.protovis.client.jsutil.JsArgs;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsArrayGeneric;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsBooleanFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsDoubleFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsStringFunction;
import org.thechiselgroup.choosel.protovis.client.jsutil.JsUtils;

import com.google.gwt.user.client.ui.Widget;

/**
 * Protovis/GWT implementation of <a
 * href="http://vis.stanford.edu/protovis/ex/stack.html">Protovis stacked chart
 * example</a>.
 * 
 * @author Lars Grammel
 */
public class StackedChartExample extends ProtovisWidget implements
        ProtovisExample {
    public static class Point {

        public double x;

        public double y;

        private Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private void createVisualization(JsArrayGeneric<JsArrayGeneric<Point>> data) {
        /* Sizing and scales. */
        int w = 400;
        int h = 200;
        final PVLinearScale x = PV.Scale.linear(0, 9.9).range(0, w);
        final PVLinearScale y = PV.Scale.linear(0, 14).range(0, h);

        /* The root panel. */
        PVPanel vis = getPVPanel().width(w).height(h).bottom(20).left(20)
                .right(10).top(5);

        /* X-axis and ticks. */
        vis.add(PV.Rule).data(x.ticks()).visible(new JsBooleanFunction() {
            public boolean f(JsArgs args) {
                double d = args.getDouble(0);
                return d != 0;
            }
        }).left(x).bottom(-5).height(5).anchor(BOTTOM).add(PV.Label)
                .text(x.tickFormat());

        /* The stack layout. */
        vis.add(PV.Layout.Stack()).layers(data).x(new JsDoubleFunction() {
            public double f(JsArgs args) {
                Point d = args.getObject(0);
                return x.fd(d.x);
            }
        }).y(new JsDoubleFunction() {
            public double f(JsArgs args) {
                Point d = args.getObject(0);
                return y.fd(d.y);
            }
        }).layer().add(PV.Area);

        /* Y-axis and ticks. */
        vis.add(PV.Rule).data(y.ticks(3)).bottom(y)
                .strokeStyle(new JsStringFunction() {
                    public String f(JsArgs args) {
                        double d = args.getDouble(0);
                        return d != 0 ? "rgba(128,128,128,.2)" : "#000";
                    }
                }).anchor(LEFT).add(PV.Label).text(y.tickFormat());
    }

    private JsArrayGeneric<JsArrayGeneric<Point>> generateData() {
        JsArrayGeneric<JsArrayGeneric<Point>> data = JsUtils
                .createJsArrayGeneric();
        for (int j = 0; j < 4; j++) {
            JsArrayGeneric<Point> series = JsUtils.createJsArrayGeneric();
            for (int i = 0; i < 100; i++) {
                double xValue = i / 10d;
                series.push(new Point(xValue, Math.sin(xValue) + Math.random()
                        * .5 + 2));
            }
            data.push(series);
        }
        return data;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getProtovisExampleURL() {
        return "http://vis.stanford.edu/protovis/ex/stack.html";
    }

    public String getSourceCodeFile() {
        return "StackedChartExample.java";
    }

    protected void onAttach() {
        super.onAttach();
        initPVPanel();
        createVisualization(generateData());
        getPVPanel().render();
    }

    public String toString() {
        return "Stacked Area Chart";
    }

}