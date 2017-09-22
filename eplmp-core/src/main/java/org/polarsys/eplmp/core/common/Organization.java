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

package org.polarsys.eplmp.core.common;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Organization class represents an entity which groups users
 * or more precisely {@link Account}
 * as its validity spreads across workspaces.
 *
 * @author Florent Garin
 * @version 2.0, 30/05/14
 * @since V2.0
 */
@Table(name = "ORGANIZATION")
@javax.persistence.Entity
@NamedQueries ({
        @NamedQuery(name="Organization.ofAccount", query = "SELECT orga FROM Organization orga WHERE :account MEMBER OF orga.members")
})
public class Organization implements Serializable {

    @Column(length = 100)
    @javax.persistence.Id
    private String name = "";

    @javax.persistence.OneToOne(optional = false, fetch = FetchType.EAGER)
    private Account owner;

    @Lob
    private String description;

    @JoinTable(name="ORGANIZATION_ACCOUNT",
            inverseJoinColumns={
                    @JoinColumn(name="ACCOUNT_LOGIN", referencedColumnName="LOGIN")
            },
            joinColumns={
                    @JoinColumn(name="ORGANIZATION_NAME", referencedColumnName="NAME")
            })
    @OrderColumn(name="ACCOUNT_ORDER")
    @OneToMany(fetch = FetchType.EAGER)
    private List<Account> members = new ArrayList<>();

    public Organization() {

    }
    public Organization(String pName, Account pOwner, String pDescription) {
        name = pName;
        owner = pOwner;
        description = pDescription;
    }

    public List<Account> getMembers() {
        return members;
    }
    public boolean addMember(Account pAccount){
        return members.add(pAccount);
    }
    public boolean removeMember(Account pAccount){
        return members.remove(pAccount);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Account getOwner() {
        return owner;
    }
    public void setOwner(Account owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Organization that = (Organization) o;

         return  name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
