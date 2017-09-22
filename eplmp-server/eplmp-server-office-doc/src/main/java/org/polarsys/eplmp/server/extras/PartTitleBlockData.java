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

package org.polarsys.eplmp.server.extras;

import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.i18n.PropertiesLoader;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author kelto on 05/01/16.
 *         <p>
 *         This class should be used to override the default Pdf generation for parts.
 * @see org.polarsys.eplmp.server.extras.TitleBlockGenerator
 */
public class PartTitleBlockData extends TitleBlockData {

    PartTitleBlockData(PartIteration partIteration, Locale pLocale) {
        locale = pLocale;
        properties = PropertiesLoader.loadLocalizedProperties(locale, PROPERTIES_BASE_NAME, getClass());
        dateFormat = new SimpleDateFormat(properties.getProperty("date.format"));
        authorName = partIteration.getAuthor().getName();
        version = partIteration.getVersion();
        creationDate = dateFormat.format(partIteration.getPartRevision().getCreationDate());
        iterationDate = dateFormat.format(partIteration.getCreationDate());
        keywords = partIteration.getPartRevision().getTags().toString();
        description = partIteration.getPartRevision().getDescription();
        instanceAttributes = partIteration.getInstanceAttributes();
        currentIteration = String.valueOf(partIteration.getIteration());
        workflow = partIteration.getPartRevision().getWorkflow();
        revisionNote = partIteration.getIterationNote();
        lifeCycleState = partIteration.getPartRevision().getLifeCycleState();
        title = partIteration.getNumber() + "-" + version;
        subject = partIteration.getName();
    }
}
