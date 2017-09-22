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

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class maintains a collection of document iterations with no more
 * than one {@link DocumentIteration} per each {@link DocumentRevision}.
 *
 * @author Taylor Labejof
 * @version 2.0, 25/08/14
 * @since V2.0
 */
@Table(name="DOCUMENTCOLLECTION")
@Entity
public class DocumentCollection implements Serializable {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "AUTHOR_LOGIN", referencedColumnName = "LOGIN"),
            @JoinColumn(name = "AUTHOR_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @MapKey(name="baselinedDocumentKey")
    @OneToMany(mappedBy="documentCollection", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
    private Map<BaselinedDocumentKey, BaselinedDocument> baselinedDocuments = new HashMap<>();

    public DocumentCollection() {
    }

    public Map<BaselinedDocumentKey, BaselinedDocument> getBaselinedDocuments() {
        return baselinedDocuments;
    }
    public void removeAllBaselinedDocuments() {
        baselinedDocuments.clear();
    }

    public BaselinedDocument addBaselinedDocument(DocumentIteration targetDocument){
        BaselinedDocument baselinedDocument = new BaselinedDocument(this, targetDocument);
        baselinedDocuments.put(baselinedDocument.getKey(), baselinedDocument);
        return baselinedDocument;
    }

    public BaselinedDocument getBaselinedDocument(BaselinedDocumentKey baselinedDocumentKey){
        return baselinedDocuments.get(baselinedDocumentKey);
    }
    public BaselinedDocument getBaselinedDocument(DocumentRevisionKey documentRevisionKey){
        BaselinedDocumentKey baselinedDocumentKey = getBaselinedDocumentKey(documentRevisionKey);
        return getBaselinedDocument(baselinedDocumentKey);
    }
    public boolean hasBaselinedDocument(DocumentRevisionKey documentRevisionKey){
        BaselinedDocumentKey baselinedDocumentKey = getBaselinedDocumentKey(documentRevisionKey);
        return baselinedDocuments.containsKey(baselinedDocumentKey);
    }

    public Date getCreationDate() {
        return (creationDate!=null) ? (Date) creationDate.clone() : null;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = (creationDate!=null) ? (Date) creationDate.clone() : null;
    }

    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    private BaselinedDocumentKey getBaselinedDocumentKey(DocumentRevisionKey documentRevisionKey) {
        return new BaselinedDocumentKey(id, documentRevisionKey.getWorkspaceId(), documentRevisionKey.getDocumentMasterId(), documentRevisionKey.getVersion());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentCollection)) {
            return false;
        }

        DocumentCollection collection = (DocumentCollection) o;
        return id == collection.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
