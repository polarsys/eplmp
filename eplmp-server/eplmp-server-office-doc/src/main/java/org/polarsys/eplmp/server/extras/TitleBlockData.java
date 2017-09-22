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

import org.polarsys.eplmp.core.meta.InstanceAttribute;
import org.polarsys.eplmp.core.workflow.Workflow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


/**
 * @author Morgan Guimard
 *         <p>
 *         This class define the default pdf generation for both part and document.
 *         This behaviour can be overridden:
 * @see PartTitleBlockData
 * @see DocumentTitleBlockData
 */
public abstract class TitleBlockData {

    protected static final String PROPERTIES_BASE_NAME = "/org/polarsys/eplmp/server/extras/localization/TitleBlockData";

    protected Properties properties;
    protected SimpleDateFormat dateFormat;
    protected String title;
    protected String subject;
    protected String authorName;
    protected String version;
    protected String creationDate;
    protected String iterationDate;
    protected String keywords;
    protected String description;
    protected List<InstanceAttribute> instanceAttributes;
    protected String currentIteration;
    protected Workflow workflow;
    protected Locale locale;
    protected String revisionNote;
    protected String lifeCycleState;

    public String getBundleString(String key) {
        return properties.getProperty(key);
    }

    public String format(Date date) {
        return dateFormat.format(date);
    }

    public String getLifeCycleState() {
        return lifeCycleState;
    }

    public String getTitle() {
        return title;
    }

    public String getSubject() {
        return subject;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getVersion() {
        return version;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getIterationDate() {
        return iterationDate;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getDescription() {
        return description;
    }

    public List<InstanceAttribute> getInstanceAttributes() {
        return instanceAttributes;
    }

    public String getCurrentIteration() {
        return currentIteration;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getRevisionNote() {
        return revisionNote;
    }
}
