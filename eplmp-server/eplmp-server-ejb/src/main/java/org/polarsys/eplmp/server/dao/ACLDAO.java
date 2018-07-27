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
package org.polarsys.eplmp.server.dao;


import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.security.ACL;
import org.polarsys.eplmp.core.security.ACLUserEntry;
import org.polarsys.eplmp.core.security.ACLUserGroupEntry;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Map;

@RequestScoped
public class ACLDAO {

    @PersistenceContext
    private EntityManager em;

    public ACLDAO() {

    }

    public void createACL(ACL acl) {
        //Hack to prevent a bug inside the JPA implementation (Eclipse Link)
        Map<UserGroup,ACLUserGroupEntry> groupEntries = acl.getGroupEntries();
        Map<User,ACLUserEntry> userEntries = acl.getUserEntries();
        acl.setGroupEntries(null);
        acl.setUserEntries(null);
        em.persist(acl);
        em.flush();
        acl.setGroupEntries(groupEntries);
        acl.setUserEntries(userEntries);
    }

    public void removeACLEntries(ACL acl){
        em.createNamedQuery("ACL.removeUserEntries").setParameter("aclId",acl.getId()).executeUpdate();
        em.createNamedQuery("ACL.removeUserGroupEntries").setParameter("aclId",acl.getId()).executeUpdate();
        em.flush();
    }

    public void removeAclUserEntries(User pUser) {
        Query query = em.createQuery("DELETE FROM ACLUserEntry a WHERE a.principal = :user");
        query.setParameter("user", pUser).executeUpdate();

    }
}
