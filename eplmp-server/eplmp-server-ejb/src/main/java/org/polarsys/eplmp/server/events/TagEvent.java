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
package org.polarsys.eplmp.server.events;

import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.meta.Tag;
import org.polarsys.eplmp.core.product.PartRevision;

/**
 * @author Florent Garin
 */
public class TagEvent {

    private Tag observedTag;

    private DocumentRevision taggableDocument;
    private PartRevision taggablePart;



    public TagEvent(Tag observedTag, DocumentRevision taggableDocument) {
        this.observedTag = observedTag;
        this.taggableDocument = taggableDocument;
    }

    public TagEvent(Tag observedTag, PartRevision taggablePart) {
        this.observedTag = observedTag;
        this.taggablePart = taggablePart;
    }


    public TagEvent(Tag observedTag) {
        this.observedTag = observedTag;
    }

    public PartRevision getTaggablePart() {
        return taggablePart;
    }

    public void setTaggablePart(PartRevision taggablePart) {
        this.taggablePart = taggablePart;
    }

    public DocumentRevision getTaggableDocument() {
        return taggableDocument;
    }

    public void setTaggableDocument(DocumentRevision taggableDocument) {
        this.taggableDocument = taggableDocument;
    }

    public Tag getObservedTag() {
        return observedTag;
    }

    public void setObservedTag(Tag observedTag) {
        this.observedTag = observedTag;
    }
}
