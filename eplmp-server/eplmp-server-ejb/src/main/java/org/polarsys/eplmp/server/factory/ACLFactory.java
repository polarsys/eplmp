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
package org.polarsys.eplmp.server.factory;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.common.UserGroupKey;
import org.polarsys.eplmp.core.common.UserKey;
import org.polarsys.eplmp.core.security.ACL;
import org.polarsys.eplmp.core.security.ACLPermission;
import org.polarsys.eplmp.server.dao.ACLDAO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Asmae CHADID on 26/02/15.
 */
@Stateless(name = "ACLFactory")
public class ACLFactory {

    @Inject
    private EntityManager em;

    @Inject
    private ACLDAO aclDAO;

    @Inject
    private ACLFactory aclFactory;

    public ACLFactory() {

    }

    public ACL createACL(String pWorkspaceId, Map<String, String> pUserEntries, Map<String, String> pGroupEntries) {
        ACL acl = new ACL();
        if (pUserEntries != null) {
            for (Map.Entry<String, String> entry : pUserEntries.entrySet()) {
                acl.addEntry(em.find(User.class, new UserKey(pWorkspaceId, entry.getKey())),
                        ACLPermission.valueOf(entry.getValue()));
            }
        }
        if (pGroupEntries != null) {
            for (Map.Entry<String, String> entry : pGroupEntries.entrySet()) {
                acl.addEntry(em.find(UserGroup.class, new UserGroupKey(pWorkspaceId, entry.getKey())),
                        ACLPermission.valueOf(entry.getValue()));
            }
        }
        aclDAO.createACL(acl);
        return acl;
    }

    public ACL updateACL(String workspaceId, ACL acl, Map<String, String> pUserEntries, Map<String, String> pGroupEntries) {

        if (acl != null) {
            aclDAO.removeACLEntries(acl);
            acl.setUserEntries(new HashMap<>());
            acl.setGroupEntries(new HashMap<>());
            for (Map.Entry<String, String> entry : pUserEntries.entrySet()) {
                acl.addEntry(em.getReference(User.class, new UserKey(workspaceId, entry.getKey())), ACLPermission.valueOf(entry.getValue()));
            }

            for (Map.Entry<String, String> entry : pGroupEntries.entrySet()) {
                acl.addEntry(em.getReference(UserGroup.class, new UserGroupKey(workspaceId, entry.getKey())), ACLPermission.valueOf(entry.getValue()));
            }
        }
        return acl;
    }
}
