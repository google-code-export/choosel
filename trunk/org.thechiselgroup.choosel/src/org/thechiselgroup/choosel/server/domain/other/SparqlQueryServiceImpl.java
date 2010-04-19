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
package org.thechiselgroup.choosel.server.domain.other;

import org.thechiselgroup.choosel.client.domain.other.SparqlQueryService;
import org.thechiselgroup.choosel.client.services.ServiceException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class SparqlQueryServiceImpl extends RemoteServiceServlet implements
	SparqlQueryService {

    @Override
    public String runSparqlQuery() throws ServiceException {
	// try {
	// String urlString = "";
	//
	// http://dbpedia.org/sparql
	//	    
	// URL url = new URL(urlString);
	// BufferedReader reader = new BufferedReader(new InputStreamReader(
	// url.openStream()));
	//
	String content = "";
	// String line;
	// while ((line = reader.readLine()) != null) {
	// content += line + "\n"; // TODO use linebreak property
	// }
	// reader.close();

	return content;

	// } catch (MalformedURLException e) {
	// throw new MashupException(e.getMessage());
	// } catch (IOException e) {
	// throw new MashupException(e.getMessage());
	// }
    }
}