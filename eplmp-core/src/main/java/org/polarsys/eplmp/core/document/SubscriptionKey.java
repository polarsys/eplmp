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
 * Identity class of {@link Subscription} objects.
 *
 * @author Florent Garin
 */
public class SubscriptionKey implements Serializable {

    private String subscriberWorkspaceId;
    private String subscriberLogin;
    private String observedDocumentRevisionWorkspaceId;
    private String observedDocumentRevisionVersion;
    private String observedDocumentRevisionId;

    public SubscriptionKey() {
    }

    public SubscriptionKey(String pSubscriberWorkspaceId, String pSubscriberLogin, String pObservedDocumentRevisionWorkspaceId, String pObservedDocumentRevisionId, String pObservedDocumentRevisionVersion) {
        subscriberWorkspaceId = pSubscriberWorkspaceId;
        subscriberLogin = pSubscriberLogin;
        observedDocumentRevisionWorkspaceId = pObservedDocumentRevisionWorkspaceId;
        observedDocumentRevisionId = pObservedDocumentRevisionId;
        observedDocumentRevisionVersion = pObservedDocumentRevisionVersion;
    }

    public String getObservedDocumentRevisionId() {
        return observedDocumentRevisionId;
    }

    public String getObservedDocumentRevisionVersion() {
        return observedDocumentRevisionVersion;
    }

    public String getObservedDocumentRevisionWorkspaceId() {
        return observedDocumentRevisionWorkspaceId;
    }

    public String getSubscriberLogin() {
        return subscriberLogin;
    }

    public String getSubscriberWorkspaceId() {
        return subscriberWorkspaceId;
    }

    public void setObservedDocumentRevisionId(String observedDocumentRevisionId) {
        this.observedDocumentRevisionId = observedDocumentRevisionId;
    }

    public void setObservedDocumentRevisionVersion(String observedDocumentRevisionVersion) {
        this.observedDocumentRevisionVersion = observedDocumentRevisionVersion;
    }

    public void setObservedDocumentRevisionWorkspaceId(String observedDocumentRevisionWorkspaceId) {
        this.observedDocumentRevisionWorkspaceId = observedDocumentRevisionWorkspaceId;
    }

    public void setSubscriberLogin(String subscriberLogin) {
        this.subscriberLogin = subscriberLogin;
    }

    public void setSubscriberWorkspaceId(String subscriberWorkspaceId) {
        this.subscriberWorkspaceId = subscriberWorkspaceId;
    }

    @Override
    public String toString() {
        return subscriberWorkspaceId + "-" + subscriberLogin + "/" + observedDocumentRevisionWorkspaceId + "-" + observedDocumentRevisionId + "-" + observedDocumentRevisionVersion;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof SubscriptionKey)) {
            return false;
        }
        SubscriptionKey key = (SubscriptionKey) pObj;
        return key.subscriberWorkspaceId.equals(subscriberWorkspaceId) && key.subscriberLogin.equals(subscriberLogin) && key.observedDocumentRevisionId.equals(observedDocumentRevisionId) && key.observedDocumentRevisionWorkspaceId.equals(observedDocumentRevisionWorkspaceId) && key.observedDocumentRevisionVersion.equals(observedDocumentRevisionVersion);
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + subscriberWorkspaceId.hashCode();
        hash = 31 * hash + subscriberLogin.hashCode();
        hash = 31 * hash + observedDocumentRevisionWorkspaceId.hashCode();
        hash = 31 * hash + observedDocumentRevisionId.hashCode();
        hash = 31 * hash + observedDocumentRevisionVersion.hashCode();
        return hash;
    }
}
