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
package org.thechiselgroup.choosel.workbench.client.ui.dialog;

import com.google.gwt.user.client.ui.Widget;

/**
 * Default dialog implementation that displays a title, has a custom content,
 * and an OK and Cancel button. The OK and cancel buttons both close the window
 * after being pressed.
 * 
 * @author Lars Grammel
 * 
 */
public interface Dialog {

	void cancel();

	Widget getContent();

	String getHeader();

	String getOkayButtonLabel();

	String getWindowTitle();

	void handleException(Exception ex);

	public void init(DialogCallback callback);

	void okay() throws Exception;

}
