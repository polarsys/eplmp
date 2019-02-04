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

package org.polarsys.eplmp.core.configuration;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Identity class of {@link BaselinedPart} objects defined as an embeddable
 * object in order to be used inside the baselined parts map in
 * the {@link PartCollection} class.
 *
 * @author Florent Garin
 * @version 2.0, 25/08/14
 * @since V2.0
 */
@Embeddable
public class BaselinedPartKey implements Serializable{
    @Column(name = "PARTCOLLECTION_ID", nullable = false, insertable = false, updatable = false)
    private int partCollectionId;

    @Column(name = "TARGET_WORKSPACE_ID", length=100, nullable = false, insertable = false, updatable = false)
    private String targetPartWorkspaceId="";

    @Column(name = "TARGET_PARTMASTER_PARTNUMBER", length=100, nullable = false, insertable = false, updatable = false)
    private String targetPartNumber="";

    public BaselinedPartKey(){
    }

    public BaselinedPartKey(int partCollectionId, String targetPartWorkspaceId, String targetPartNumber) {
        this.partCollectionId = partCollectionId;
        this.targetPartWorkspaceId = targetPartWorkspaceId;
        this.targetPartNumber = targetPartNumber;
    }

    public int getPartCollectionId() {
        return partCollectionId;
    }

    public void setPartCollectionId(int partCollectionId) {
        this.partCollectionId = partCollectionId;
    }

    public String getTargetPartWorkspaceId() {
        return targetPartWorkspaceId;
    }

    public void setTargetPartWorkspaceId(String targetPartWorkspaceId) {
        this.targetPartWorkspaceId = targetPartWorkspaceId;
    }

    public String getTargetPartNumber() {
        return targetPartNumber;
    }

    public void setTargetPartNumber(String targetPartNumber) {
        this.targetPartNumber = targetPartNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaselinedPartKey)) {
            return false;
        }

        BaselinedPartKey that = (BaselinedPartKey) o;

        return partCollectionId == that.partCollectionId &&
               targetPartNumber.equals(that.targetPartNumber) &&
               targetPartWorkspaceId.equals(that.targetPartWorkspaceId);

    }

    @Override
    public int hashCode() {
        int result = partCollectionId;
        result = 31 * result + targetPartWorkspaceId.hashCode();
        result = 31 * result + targetPartNumber.hashCode();
        return result;
    }
}
