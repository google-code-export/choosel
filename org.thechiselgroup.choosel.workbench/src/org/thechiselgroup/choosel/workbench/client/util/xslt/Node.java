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
/**
 * Created on Sep 1, 2006
 */
package org.thechiselgroup.choosel.workbench.client.util.xslt;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;

public class Node {

    private static Node create(JavaScriptObject nativeNode) {
        return new Node(nativeNode);
    }

    private JavaScriptObject nativeNode;

    public Node(JavaScriptObject nativeNode) {
        this.nativeNode = nativeNode;
    }

    // @formatter:off
    native List<Node> getNodes(String xpathExpression, List<Node> result) /*-{
        var nativeNode = this.@org.thechiselgroup.choosel.workbench.client.util.xslt.Node::nativeNode;

        var nodes = nativeNode.selectNodes(xpathExpression);
        for (var i = 0; i < nodes.length; i++) {
            result.@java.util.List::add(Ljava/lang/Object;)( @org.thechiselgroup.choosel.workbench.client.util.xslt.Node::create(Lcom/google/gwt/core/client/JavaScriptObject;)(nodes[i]));
        }

        return result;
    }-*/;

    native String getValue() /*-{
        var node = this.@org.thechiselgroup.choosel.workbench.client.util.xslt.Node::nativeNode;
        if ( !node ) {
            return null;
        } else if ( node.childNodes.length == 1 ) {
            return node.childNodes[ 0 ].nodeValue;
        } else if ( !node.nodeValue ) {
            return "";
        } else {
            return node.nodeValue;
        }
    }-*/;
    // @formatter:on

    @Override
    public String toString() {
        return getValue();
    }

}
