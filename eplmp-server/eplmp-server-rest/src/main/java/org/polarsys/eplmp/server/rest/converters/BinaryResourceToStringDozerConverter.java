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

package org.polarsys.eplmp.server.rest.converters;

import org.dozer.DozerConverter;
import org.polarsys.eplmp.core.common.BinaryResource;

/**
 * @author Florent Garin
 */
public class BinaryResourceToStringDozerConverter extends DozerConverter<BinaryResource, String> {

    public BinaryResourceToStringDozerConverter() {
        super(BinaryResource.class, String.class);
    }

    @Override
    public String convertTo(BinaryResource source, String destination) {
        return (source != null) ? source.getFullName() : null;
    }

    @Override
    public BinaryResource convertFrom(String source, BinaryResource destination) {
        if (source != null) {
            BinaryResource bin = new BinaryResource();
            bin.setFullName(source);
            return bin;
        } else {
            return null;
        }
    }

}
