/*******************************************************************************
  * Copyright (c) 2017-2019 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Morgan Guimard
 */
@XmlRootElement
@ApiModel(value="StatsOverviewDTO", description="This class is a representation of workspace metrics")
public class StatsOverviewDTO implements Serializable {

    @ApiModelProperty(value = "Documents count")
    private int documents;

    @ApiModelProperty(value = "Parts count")
    private int parts;

    @ApiModelProperty(value = "Products count")
    private int products;

    @ApiModelProperty(value = "Users count")
    private int users;

    public StatsOverviewDTO() {
    }

    public int getDocuments() {
        return documents;
    }

    public void setDocuments(int documents) {
        this.documents = documents;
    }

    public int getParts() {
        return parts;
    }

    public void setParts(int parts) {
        this.parts = parts;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }
}
