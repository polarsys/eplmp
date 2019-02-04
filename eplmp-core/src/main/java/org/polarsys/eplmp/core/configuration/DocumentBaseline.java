/*******************************************************************************
  * Copyright (c) 2017-2019 DocDoku.
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
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentRevisionKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Baselines could be seen as "snapshots in time" of a set of documents.
 * More concretely, baselines are collections of documents
 * at a given iteration plus various metadata like {@code author},
 * {@code type} or {@code description} for instance.
 *
 * @author Taylor Labejof
 * @version 2.0, 25/08/14
 * @since V2.0
 */
@Table(name="DOCUMENTBASELINE")
@Entity
public class DocumentBaseline implements Serializable {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    private DocumentBaselineType type = DocumentBaselineType.LATEST;

    @Lob
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private DocumentCollection documentCollection = new DocumentCollection();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "AUTHOR_LOGIN", referencedColumnName = "LOGIN"),
            @JoinColumn(name = "AUTHOR_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private User author;

    public DocumentBaseline() {
    }

    public DocumentBaseline(User author, String name, DocumentBaselineType type, String description) {
        this.author = author;
        this.name = name;
        this.type = type;
        this.description = description;
        this.creationDate = new Date();
    }

    public void addBaselinedDocument(DocumentIteration targetDocument){
        documentCollection.addBaselinedDocument(targetDocument);

    }
    public boolean hasBaselinedDocument(DocumentRevisionKey documentRevisionKey){
        if(documentCollection != null){
            return documentCollection.hasBaselinedDocument(documentRevisionKey);
        }
        return false;
    }
    public BaselinedDocument getBaselinedDocument(DocumentRevisionKey documentRevisionKey){
        return (documentCollection!=null) ? documentCollection.getBaselinedDocument(documentRevisionKey) : null;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public DocumentBaselineType getType() {
        return type;
    }

    public void setType(DocumentBaselineType type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return (creationDate!=null) ? (Date) creationDate.clone(): null;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = (Date) creationDate.clone();
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public DocumentCollection getDocumentCollection() {
        return documentCollection;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentBaseline)) {
            return false;
        }

        DocumentBaseline baseline = (DocumentBaseline) o;
        return id == baseline.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
