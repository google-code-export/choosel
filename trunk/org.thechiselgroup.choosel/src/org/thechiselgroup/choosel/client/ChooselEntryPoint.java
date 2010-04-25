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
package org.thechiselgroup.choosel.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class ChooselEntryPoint implements EntryPoint {

    public final void onModuleLoad() {
	ChooselGinjector injector = createGinjector();

	// TODO what is this for?
	// resolves initialization cycles
	injector.getProxyViewFactoryResolver().setDelegate(
		injector.getViewFactory());

	injector.getApplication().init();
    }

    /**
     * Choosel applications should override to implement their own
     * ChooseGinjector that links their custom configuration module.
     */
    protected ChooselGinjector createGinjector() {
	return GWT.create(ChooselGinjector.class);
    }

}