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
package org.polarsys.eplmp.server.extras;

import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.i18n.PropertiesLoader;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This class should be used to override the default PDF layout applied
 * when generating document.
 *
 * @author Charles Fallourd
 * @version 2.5, 05/01/16
 * @see TitleBlockGenerator
 */
class DocumentTitleBlockData extends TitleBlockData {

    DocumentTitleBlockData(DocumentIteration documentIteration, Locale pLocale) {
        locale = pLocale;
        properties = PropertiesLoader.loadLocalizedProperties(locale, PROPERTIES_BASE_NAME, getClass());
        dateFormat = new SimpleDateFormat(properties.getProperty("date.format"));
        authorName = documentIteration.getAuthor().getName();
        version = documentIteration.getVersion();
        creationDate = dateFormat.format(documentIteration.getDocumentRevision().getCreationDate());
        iterationDate = dateFormat.format(documentIteration.getCreationDate());
        keywords = documentIteration.getDocumentRevision().getTags().toString();
        description = documentIteration.getDocumentRevision().getDescription();
        instanceAttributes = documentIteration.getInstanceAttributes();
        currentIteration = String.valueOf(documentIteration.getIteration());
        workflow = documentIteration.getDocumentRevision().getWorkflow();
        revisionNote = documentIteration.getRevisionNote();
        lifeCycleState = documentIteration.getDocumentRevision().getLifeCycleState();
        title = documentIteration.getId() + "-" + version;
        subject = documentIteration.getTitle();
    }

}
