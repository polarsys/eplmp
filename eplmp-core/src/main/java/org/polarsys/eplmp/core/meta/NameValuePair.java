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

package org.polarsys.eplmp.core.meta;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Embeddable name-value pair.
 *
 * @author Florent Garin
 * @version 2.0, 27/02/15
 * @since V2.0
 */
@Embeddable
public class NameValuePair implements Serializable {

    private String name, value;

    public NameValuePair() {
    }

    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
