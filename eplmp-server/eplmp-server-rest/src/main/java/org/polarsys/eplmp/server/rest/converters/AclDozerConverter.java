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

package org.polarsys.eplmp.server.rest.converters;

import org.dozer.DozerConverter;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.security.ACL;
import org.polarsys.eplmp.core.security.ACLUserEntry;
import org.polarsys.eplmp.core.security.ACLUserGroupEntry;
import org.polarsys.eplmp.server.rest.dto.ACLDTO;

import java.util.Map;


public class AclDozerConverter extends DozerConverter<ACL, ACLDTO> {


    public AclDozerConverter() {
        super(ACL.class, ACLDTO.class);
    }

    @Override
    public ACLDTO convertTo(ACL acl, ACLDTO aclDTO) {

        aclDTO = new ACLDTO();

        if (acl != null) {

            for (Map.Entry<User, ACLUserEntry> entry : acl.getUserEntries().entrySet()) {
                ACLUserEntry aclEntry = entry.getValue();
                aclDTO.addUserEntry(aclEntry.getPrincipalLogin(), aclEntry.getPermission());
            }

            for (Map.Entry<UserGroup, ACLUserGroupEntry> entry : acl.getGroupEntries().entrySet()) {
                ACLUserGroupEntry aclEntry = entry.getValue();
                aclDTO.addGroupEntry(aclEntry.getPrincipalId(), aclEntry.getPermission());
            }
            return aclDTO;
        }

        return null;
    }

    @Override
    public ACL convertFrom(ACLDTO aclDTO, ACL acl) {
        return acl;
    }

}
