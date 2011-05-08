/*******************************************************************************
 * Copyright (C) 2011 Lars Grammel 
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
package org.thechiselgroup.choosel.core.shared.util.date;

import java.util.Date;

/**
 * Interfaces that allows for reusing date time format on client and server, as
 * well as for testing.
 * 
 * @author Lars Grammel
 */
public interface DateTimeFormat {

    String format(Date date);

    /**
     * @throws IllegalArgumentException
     *             thrown if unable to parse text
     */
    Date parse(String text) throws IllegalArgumentException;

}