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
 * Subscription on the event that is triggered when the state of the workflow
 * attached to the document has changed.  
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name="STATECHANGESUBSCRIPTION")
@Entity
@NamedQueries({
    @NamedQuery(name="StateChangeSubscription.findSubscriptionByUserAndDocRevision", query="SELECT s FROM StateChangeSubscription s WHERE s.subscriber = :user AND s.observedDocumentRevision = :docR"),
})
public class StateChangeSubscription extends Subscription{
    

    public StateChangeSubscription() {
    }
    
    public StateChangeSubscription (User pSubscriber, DocumentRevision pObservedElement){
        super(pSubscriber,pObservedElement);
    }
    
}
