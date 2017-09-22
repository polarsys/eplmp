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

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This ConversionResult class represents the conversion status done by a
 * CADConverter plugin.
 * <p>
 * It holds the converted file and its materials.
 */
public class ConversionResult implements Closeable, Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConversionResult.class.getName());

    private static final long serialVersionUID = 1L;

    /**
     * The converted file for succeed conversions
     */
    private URI convertedFile;
    /**
     * The list of materials files if any
     */
    private List<URI> materials = new ArrayList<>();
    /**
     * The output of conversion program
     */
    private String stdOutput;
    /**
     * The error output of conversion program
     */
    private String errorOutput;

    /**
     * Default constructor
     */
    public ConversionResult() {
    }

    /**
     * Constructor with converted file
     *
     * @param convertedFile the converted file
     */
    public ConversionResult(Path convertedFile) {
        this.convertedFile = convertedFile.toUri();
    }

    /**
     * Constructor with converted file and materials
     *
     * @param convertedFile the converted file
     */
    public ConversionResult(Path convertedFile, List<Path> materials) {
        this.convertedFile = convertedFile.toUri();
        this.materials = new ArrayList<>();
        materials.forEach((path) -> this.materials.add(path.toUri()));
    }

    public Path getConvertedFile() {
        return Paths.get(convertedFile);
    }

    public void setConvertedFile(Path convertedFile) {
        this.convertedFile = convertedFile.toUri();
    }

    public List<Path> getMaterials() {
        return materials.stream().map((uri) -> {
            return Paths.get(uri);
        }).collect(Collectors.toList());
    }

    public void setMaterials(List<Path> materials) {
        this.materials = new ArrayList<>();
        materials.forEach((path) -> this.materials.add(path.toUri()));
    }

    public String getStdOutput() {
        return stdOutput;
    }

    public void setStdOutput(String stdOutput) {
        this.stdOutput = stdOutput;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public void setErrorOutput(String errorOutput) {
        this.errorOutput = errorOutput;
    }

    public void close() {
        try {
            if (convertedFile != null) {
                Files.deleteIfExists(Paths.get(convertedFile));
            }
            if (materials != null) {
                for (URI m : materials) {
                    Files.deleteIfExists(Paths.get(m));
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }
}
