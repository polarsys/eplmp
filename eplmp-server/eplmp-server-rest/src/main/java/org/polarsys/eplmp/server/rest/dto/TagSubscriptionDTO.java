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

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author Florent Garin
 */
@XmlRootElement
@ApiModel(value="TagSubscriptionDTO", description="This is a representation of a {@link org.polarsys.eplmp.core.meta.TagUserSubscription} or {@link org.polarsys.eplmp.core.meta.TagUserGroupSubscription} entity")
public class TagSubscriptionDTO implements Serializable {

    @ApiModelProperty(value = "Tag name")
    private String tag;

    @ApiModelProperty(value = "Iteration change flag")
    private boolean onIterationChange;

    @ApiModelProperty(value = "State change flag")
    private boolean onStateChange;

    public TagSubscriptionDTO() {

    }

    public TagSubscriptionDTO(String tag, boolean onIterationChange, boolean onStateChange) {
        this.tag = tag;
        this.onIterationChange = onIterationChange;
        this.onStateChange = onStateChange;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isOnIterationChange() {
        return onIterationChange;
    }

    public void setOnIterationChange(boolean onIterationChange) {
        this.onIterationChange = onIterationChange;
    }

    public boolean isOnStateChange() {
        return onStateChange;
    }

    public void setOnStateChange(boolean onStateChange) {
        this.onStateChange = onStateChange;
    }
}
