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

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A concrete implementation of a subscription.
 * With the use of this class, the user manifests his interest of being informed
 * when a new iteration has been made on a specific document.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="ITERATIONCHANGESUBSCRIPTION")
@Entity
@NamedQueries({
    @NamedQuery(name="IterationChangeSubscription.findSubscriptionByUserAndDocRevision", query="SELECT s FROM IterationChangeSubscription s WHERE s.subscriber = :user AND s.observedDocumentRevision = :docR"),
})
public class IterationChangeSubscription extends Subscription{
    

    public IterationChangeSubscription() {
    }
    
    public IterationChangeSubscription (User pSubscriber, DocumentRevision pObservedElement){
        super(pSubscriber,pObservedElement);
    }
}
