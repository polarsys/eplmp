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

package org.polarsys.eplmp.core.configuration;

import org.polarsys.eplmp.core.common.BinaryResource;

import java.io.Serializable;
import java.util.*;

/**
 * @author Elisabel Généreux
 */
public class BaselinedDocumentBinaryResourceCollection implements Serializable {

    private String rootFolderName;
    private Set<BinaryResource> attachedFiles = new HashSet<>();

    public BaselinedDocumentBinaryResourceCollection() {
    }

    public BaselinedDocumentBinaryResourceCollection(String rootFolderName) {
        this.rootFolderName = rootFolderName;
    }

    public String getRootFolderName() {
        return rootFolderName;
    }

    public void setRootFolderName(String rootFolderName) {
        this.rootFolderName = rootFolderName;
    }

    public Set<BinaryResource> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(Set<BinaryResource> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public boolean hasNoFiles() {
        return attachedFiles.isEmpty();
    }
}
