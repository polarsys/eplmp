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

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapping class for checked out entities stats
 *
 * @author Morgan Guimard
 */
@XmlRootElement
@ApiModel(value = "CheckedOutStatsResponseDTO",
        description = "This class is the representation of checked out entities stats, grouped by user and date")
public class CheckedOutStatsResponseDTO
        extends HashMap<String, List<Map<String, Long>>>
        implements Serializable {
}
