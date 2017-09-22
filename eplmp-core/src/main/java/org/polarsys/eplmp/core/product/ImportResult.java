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

package org.polarsys.eplmp.core.product;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Value object that wraps information about the result of an import.
 *
 * Instances of this class are not persisted.
 *
 * @author Laurent Le Van
 *
 * @version 2.5, 29/06/2016
 * @since   V2.5
 */
public class ImportResult implements Serializable {

    private File importedFile;
    private List<String> warnings = new ArrayList<>();
    private List<String> errors = new ArrayList<>();
    private String stdOutput;
    private String errorOutput;


    public ImportResult(File importedFile, List<String> warnings, List<String> errors) {
        this.importedFile = importedFile;
        this.warnings = warnings;
        this.errors = errors;
    }


    public File getImportedFile() {
        return importedFile;
    }

    public void setImportedFile(File importedFile) {
        this.importedFile = importedFile;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
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

    public boolean isSucceed() {
        return errors == null || errors.isEmpty();
    }

}
