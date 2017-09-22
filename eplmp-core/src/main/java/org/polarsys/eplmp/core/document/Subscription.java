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

import org.polarsys.eplmp.core.common.User;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Abstract class for defining subscription created by users on documents.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@javax.persistence.IdClass(org.polarsys.eplmp.core.document.SubscriptionKey.class)
@javax.persistence.MappedSuperclass
public abstract class Subscription implements Serializable{
    

    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name="SUBSCRIBER_LOGIN", referencedColumnName="LOGIN"),
        @JoinColumn(name="SUBSCRIBER_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID")
    })
    protected User subscriber;
    
    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name="DOCUMENTMASTER_ID", referencedColumnName="DOCUMENTMASTER_ID"),
        @JoinColumn(name="DOCUMENTREVISION_VERSION", referencedColumnName="VERSION"),
        @JoinColumn(name="DOCUMENTMASTER_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID")
    })
    protected DocumentRevision observedDocumentRevision;
    
    @javax.persistence.Column(name = "SUBSCRIBER_WORKSPACE_ID", length=100, nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private String subscriberWorkspaceId="";
    
    @javax.persistence.Column(name = "SUBSCRIBER_LOGIN", nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private String subscriberLogin="";
    
    
    @javax.persistence.Column(name = "DOCUMENTMASTER_WORKSPACE_ID", length=100, nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private String observedDocumentRevisionWorkspaceId ="";
    
    @javax.persistence.Column(name = "DOCUMENTREVISION_VERSION", length=10, nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private String observedDocumentRevisionVersion ="";
    
    @javax.persistence.Column(name = "DOCUMENTMASTER_ID", length=100, nullable = false, insertable = false, updatable = false)
    @javax.persistence.Id
    private String observedDocumentRevisionId ="";
    
    
    public Subscription() {
    }
    
    public Subscription (User pSubscriber, DocumentRevision pObservedElement){
        setSubscriber(pSubscriber);
        setObservedDocumentRevision(pObservedElement);
    }

    public SubscriptionKey getKey(){
    return new SubscriptionKey(subscriberWorkspaceId, subscriberLogin, observedDocumentRevisionWorkspaceId, observedDocumentRevisionId, observedDocumentRevisionVersion);
    }
    
    public DocumentRevision getObservedDocumentRevision() {
        return observedDocumentRevision;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public String getSubscriberLogin() {
        return subscriberLogin;
    }


    public String getSubscriberWorkspaceId() {
        return subscriberWorkspaceId;
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


    public void setObservedDocumentRevision(DocumentRevision pObservedDocumentRevision) {
        observedDocumentRevision = pObservedDocumentRevision;
        observedDocumentRevisionId = observedDocumentRevision.getId();
        observedDocumentRevisionVersion = observedDocumentRevision.getVersion();
        observedDocumentRevisionWorkspaceId = observedDocumentRevision.getWorkspaceId();
        
    }

    public void setSubscriber(User pSubscriber) {
        subscriber = pSubscriber;
        subscriberLogin=subscriber.getLogin();
        subscriberWorkspaceId=subscriber.getWorkspaceId();
    }
    

}
