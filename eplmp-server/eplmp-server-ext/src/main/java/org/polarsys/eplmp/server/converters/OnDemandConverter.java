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

package org.polarsys.eplmp.server.converters;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.exceptions.ConvertedResourceException;
import org.polarsys.eplmp.core.product.PartIteration;

import java.io.InputStream;
import java.util.Locale;

/**
 * OnDemandConverter plugin interface
 * Extension point for attached files conversion
 */
public interface OnDemandConverter {

    /**
     * Determine if plugin is able to convert given resource in given output format
     *
     * @param outputFormat   the output format
     * @param binaryResource the resource to convert
     * @return true if plugin can handle the conversion, false otherwise
     */
    boolean canConvert(String outputFormat, BinaryResource binaryResource);

    /**
     * Get the converted resource in given output format for a document iteration
     *
     * @param outputFormat      the output format
     * @param binaryResource    the resource to convert
     * @param documentIteration the document iteration concerned
     * @param locale            the locale to use for conversion
     * @return the converted resource input stream
     */
    InputStream getConvertedResource(String outputFormat, BinaryResource binaryResource, DocumentIteration documentIteration, Locale locale) throws ConvertedResourceException;

    /**
     * Get the converted resource in given output format for a part iteration
     *
     * @param outputFormat   the output format
     * @param binaryResource the resource to convert
     * @param partIteration  the part iteration concerned
     * @param locale         the locale to use for conversion
     * @return the converted resource input stream
     */
    InputStream getConvertedResource(String outputFormat, BinaryResource binaryResource, PartIteration partIteration, Locale locale) throws ConvertedResourceException;
}
