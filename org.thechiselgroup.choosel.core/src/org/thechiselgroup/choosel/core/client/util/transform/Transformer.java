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
package org.thechiselgroup.choosel.core.client.util.transform;

/**
 * Interface for transforming one type of value into another.
 * 
 * @author Lars Grammel
 * 
 * @param <From>
 *            Type of input values
 * @param <To>
 *            Type of output values
 */
// TODO merge Converter into this class
public interface Transformer<From, To> {

    To transform(From value) throws Exception;

}