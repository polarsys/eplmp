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
package org.polarsys.eplmp.core.product;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

/**
 * Effectivity is an abstract class which is a kind of qualification object.
 * <p>
 * Effectivities are primarily applied to {@link PartRevision} objects.
 *
 * @author Florent Garin
 * @version 1.1, 14/10/11
 * @since V1.1
 */
@Table(name = "EFFECTIVITY")
@XmlSeeAlso({DateBasedEffectivity.class, SerialNumberBasedEffectivity.class, LotBasedEffectivity.class})
@Inheritance()
@Entity
@NamedQueries({
        @NamedQuery(name = "Effectivity.removeEffectivitiesFromConfigurationItem", query = "DELETE FROM Effectivity e WHERE e.configurationItem.id = :configurationItemId AND e.configurationItem.workspace.id = :workspaceId"),
        @NamedQuery(name = "Effectivity.findPartRevisionHolder", query = "SELECT p FROM PartRevision p, Effectivity e WHERE e member of p.effectivities AND e.id = :effectivityId"),
        @NamedQuery(name = "Effectivity.getEffectivitiesInWorkspace", query = "SELECT e FROM PartRevision p, Effectivity e WHERE e member of p.effectivities AND p.partMaster.workspace.id = :workspaceId")
})

public abstract class Effectivity implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    protected String name;

    @Lob
    private String description;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "CONFIGURATIONITEM_ID", referencedColumnName = "ID"),
            @JoinColumn(name = "CONFIGURATIONITEM_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private ConfigurationItem configurationItem;

    public Effectivity() {
    }

    public Effectivity(String pName) {
        name = pName;
    }

    public int getId() {
        return id;
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

    /*
     * May be optional for a DateBasedEffectivity.
     */
    public void setConfigurationItem(ConfigurationItem configurationItem) {
        this.configurationItem = configurationItem;
    }

    public ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }

}
