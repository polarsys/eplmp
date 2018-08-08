/*******************************************************************************
  * Copyright (c) 2017 DocDoku.
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

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Mapping class ArrayList<String>
 *
 * @author Morgan Guimard
 */
@XmlRootElement
@ApiModel(value = "StringListDTO",
        description = "This class is the representation of a list of strings")
public class StringListDTO
        extends ArrayList<String>
        implements Serializable {
}
