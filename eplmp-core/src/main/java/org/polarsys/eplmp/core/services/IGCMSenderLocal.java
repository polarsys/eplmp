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

package org.polarsys.eplmp.core.services;

import org.polarsys.eplmp.core.document.DocumentRevision;
import org.polarsys.eplmp.core.gcm.GCMAccount;

/**
 *
 * @author Morgan Guimard
 */
public interface IGCMSenderLocal {
    void sendStateNotification(GCMAccount[] pGCGcmAccounts, DocumentRevision pDocumentRevision);
    void sendIterationNotification(GCMAccount[] pGCGcmAccounts, DocumentRevision pDocumentRevision);
}
