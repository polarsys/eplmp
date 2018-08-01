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

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.polarsys.eplmp.core.security.ACLPermission;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
@ApiModel(value = "ACLDTO", description = "This class is the representation of an {@link org.polarsys.eplmp.core.security.ACL} entity")
public class ACLDTO implements Serializable {

    @XmlElement(nillable = true)
    @ApiModelProperty(value = "Users ACL entries")
    private List<ACLEntryDTO> userEntries = new ArrayList<>();

    @XmlElement(nillable = true)
    @ApiModelProperty(value = "Groups ACL entries")
    private List<ACLEntryDTO> groupEntries = new ArrayList<>();

    public ACLDTO() {
    }

    public void addUserEntry(String login, ACLPermission perm) {
        ACLEntryDTO aclEntryDTO = new ACLEntryDTO(login, perm);
        userEntries.add(aclEntryDTO);
    }

    public void addGroupEntry(String groupId, ACLPermission perm) {
        ACLEntryDTO aclEntryDTO = new ACLEntryDTO(groupId, perm);
        groupEntries.add(aclEntryDTO);
    }

    public List<ACLEntryDTO> getGroupEntries() {
        return groupEntries;
    }

    public void setGroupEntries(List<ACLEntryDTO> groupEntries) {
        this.groupEntries = groupEntries;
    }

    public List<ACLEntryDTO> getUserEntries() {
        return userEntries;
    }

    public void setUserEntries(List<ACLEntryDTO> userEntries) {
        this.userEntries = userEntries;
    }

    public boolean hasEntries(){
        return !userEntries.isEmpty() || !groupEntries.isEmpty();
    }

    public Map<String, String> getUserEntriesMap() {
        Map<String, String> map = new HashMap<>();
        for (ACLEntryDTO entry : getUserEntries()) {
            map.put(entry.getKey(), entry.getValue().name());
        }
        return map;
    }

    public Map<String, String> getUserGroupEntriesMap() {
        Map<String, String> map = new HashMap<>();
        for (ACLEntryDTO entry : getGroupEntries()) {
            map.put(entry.getKey(), entry.getValue().name());
        }
        return map;
    }

}
