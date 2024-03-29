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
package org.thechiselgroup.choosel.workbench.client.workspace;

import java.util.Date;

public class ViewPreview {

    private Long id;

    private String title;

    private String type;

    private Date date;

    public ViewPreview(Long id, String title, String type, Date date) {
        super();
        this.id = id;
        this.title = title;
        this.type = type;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

}