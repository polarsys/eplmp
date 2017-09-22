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
 * Identity class of {@link DocumentIteration} objects.
 *
 * @author Florent Garin
 */
public class DocumentIterationKey implements Serializable {

    private DocumentRevisionKey documentRevision;
    private int iteration;
    
    public DocumentIterationKey() {
    }

    public DocumentIterationKey(String pWorkspaceId, String pId, String pVersion, int pIteration) {
        documentRevision= new DocumentRevisionKey(pWorkspaceId, pId, pVersion);
        iteration=pIteration;
    }

    public DocumentIterationKey(DocumentRevisionKey pDocumentRevisionKey, int pIteration) {
        documentRevision=pDocumentRevisionKey;
        iteration=pIteration;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + documentRevision.hashCode();
        hash = 31 * hash + iteration;
        return hash;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof DocumentIterationKey))
            return false;
        DocumentIterationKey key = (DocumentIterationKey) pObj;
        return key.documentRevision.equals(documentRevision) && key.iteration==iteration;
    }

    @Override
    public String toString() {
        return documentRevision + "-" + iteration;
    }

    public int getIteration(){
        return iteration;
    }
    
    public void setIteration(int pIteration){
        iteration=pIteration;
    }

    public DocumentRevisionKey getDocumentRevision() {
        return documentRevision;
    }

    public void setDocumentRevision(DocumentRevisionKey documentRevision) {
        this.documentRevision = documentRevision;
    }


    public String getWorkspaceId() {
        return documentRevision.getDocumentMaster().getWorkspace();
    }

    public String getDocumentMasterId() {
        return documentRevision.getDocumentMaster().getId();
    }

    public String getDocumentRevisionVersion(){
        return documentRevision.getVersion();
    }
}
