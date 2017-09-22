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
package org.polarsys.eplmp.core.product;

import javax.persistence.*;
import java.io.Serializable;

/**
 * An Alternate object is a part that is interchangeable with another part
 * with respect to function and physical properties.
 * 
 * Beware that this link is neither transitive nor even bidirectional.
 * That means if we want to express that two parts are alternates of each other
 * we must create two links.
 * 
 * @author Florent Garin
 * @version 1.1, 15/10/11
 * @since   V1.1
 */
@Embeddable
public class PartAlternateLink implements Serializable {


    private String referenceDescription;
    
    @Column(name="COMMENTDATA")
    private String comment;
    
    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name="ALTERNATE_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID"),
        @JoinColumn(name="ALTERNATE_PARTNUMBER", referencedColumnName="PARTNUMBER")})
    private PartMaster alternate;


    public PartAlternateLink() {
    }

    public PartMaster getAlternate() {
        return alternate;
    }

    public void setAlternate(PartMaster alternate) {
        this.alternate = alternate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReferenceDescription() {
        return referenceDescription;
    }

    public void setReferenceDescription(String referenceDescription) {
        this.referenceDescription = referenceDescription;
    }
    
}
