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

import java.net.URI;

import javax.ejb.Remote;

/**
 * CADConverter Extension point interface for 3D files conversion.
 * 
 * Converters are supposed to be (standalone) remote EJB module that can be
 * deployed independently of DocdokuPLM application.
 */
@Remote
public interface CADConverter {

    /**
     * Exception reporting a unrecoverable problem during conversion process.
     */
    class ConversionException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConversionException(String message) {
	    super(message);
	}

	public ConversionException(Throwable cause) {
	    super(cause);
	}

	public ConversionException(String message, Throwable cause) {
	    super(message, cause);
	}
    }

    /**
     * Convert the given CAD file to Wavefront OBJ format
     *
     * @param cadFileName
     *            the CAD file to convert
     * @param tempDir
     *            a given temporary directory for converter operations
     * @return the conversion result
     * @throws ConversionException
     * 
     */
    ConversionResult convert(URI cadFileName, URI tempDir)
	    throws ConversionException;

    /**
     * Determine if this converter is able to convert given CAD file format
     * (identified by it's extension) to Wavefront OBJ format
     *
     * @param cadFileExtension
     *            the extension of the cadFile
     * @return true if the converter can handle the conversion, false otherwise
     */
    boolean canConvertToOBJ(String cadFileExtension);
}
