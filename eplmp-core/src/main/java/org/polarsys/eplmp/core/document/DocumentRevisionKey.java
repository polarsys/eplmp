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

package org.polarsys.eplmp.core.document;


import java.io.Serializable;

/**
 * Identity class of {@link DocumentRevision} objects.
 *
 * @author Florent Garin
 */
public class DocumentRevisionKey implements Serializable, Comparable<DocumentRevisionKey>, Cloneable {

    private DocumentMasterKey documentMaster;
    private String version;


    public DocumentRevisionKey() {
    }

    public DocumentRevisionKey(String pWorkspaceId, String pId, String pVersion) {
        documentMaster = new DocumentMasterKey(pWorkspaceId, pId);
        version = pVersion;
    }

    public DocumentRevisionKey(DocumentMasterKey pDocumentMasterKey, String pVersion) {
        documentMaster = pDocumentMasterKey;
        version = pVersion;
    }


    public String getWorkspaceId() {
        return documentMaster.getWorkspace();
    }

    public String getDocumentMasterId() {
        return documentMaster.getId();
    }

    public DocumentMasterKey getDocumentMaster() {
        return documentMaster;
    }

    public void setDocumentMaster(DocumentMasterKey documentMaster) {
        this.documentMaster = documentMaster;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String pVersion) {
        version = pVersion;
    }

    @Override
    public String toString() {
        return documentMaster + "-" + version;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof DocumentRevisionKey)) {
            return false;
        }
        DocumentRevisionKey key = (DocumentRevisionKey) pObj;
        return key.documentMaster.equals(documentMaster) && key.version.equals(version);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + documentMaster.hashCode();
        hash = 31 * hash + version.hashCode();
        return hash;
    }

    public int compareTo(DocumentRevisionKey pKey) {
        int wksMaster = documentMaster.compareTo(pKey.documentMaster);
        if (wksMaster != 0) {
            return wksMaster;
        } else {
            return version.compareTo(pKey.version);
        }
    }

    @Override
    public DocumentRevisionKey clone() {
        DocumentRevisionKey clone;
        try {
            clone = (DocumentRevisionKey) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return clone;
    }

}
