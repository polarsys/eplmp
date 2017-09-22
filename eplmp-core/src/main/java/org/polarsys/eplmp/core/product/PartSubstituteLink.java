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
import java.util.LinkedList;
import java.util.List;

/**
 * This class is related to a {@link PartUsageLink}
 * to indicate a replacement part that could be used instead.
 * 
 * @author Florent Garin
 * @version 1.1, 16/10/11
 * @since   V1.1
 */
@Table(name="PARTSUBSTITUTELINK")
@Entity
@NamedQueries({
        @NamedQuery(name="PartSubstituteLink.findBySubstitute",query="SELECT u FROM PartSubstituteLink u WHERE u.substitute.number LIKE :partNumber AND u.substitute.workspace.id = :workspaceId"),
})
public class PartSubstituteLink implements Serializable, Cloneable, PartLink {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private double amount;
    private String unit;


    private String referenceDescription;
    
    @Column(name="COMMENTDATA")
    private String comment;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "SUBSTITUTE_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID"),
        @JoinColumn(name = "SUBSTITUTE_PARTNUMBER", referencedColumnName = "PARTNUMBER")
    })
    private PartMaster substitute;

    @OrderColumn(name = "CADINSTANCE_ORDER")
    @JoinTable(name = "PARTSUBSTITUTELINK_CADINSTANCE",
    inverseJoinColumns = {
        @JoinColumn(name = "CADINSTANCE_ID", referencedColumnName = "ID")
    },
    joinColumns = {
        @JoinColumn(name = "PARTSUBSTITUTELINK_ID", referencedColumnName = "ID")
    })
    @OneToMany(orphanRemoval=true, cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<CADInstance> cadInstances = new LinkedList<CADInstance>();

    public PartSubstituteLink() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public boolean isOptional() {
        // A substitute cannot be optional
        return false;
    }

    @Override
    public PartMaster getComponent() {
        return substitute;
    }

    @Override
    public List<PartSubstituteLink> getSubstitutes() {
        // A substitute cannot have substitutes
        return null;
    }

    @Override
    public String getReferenceDescription() {
        return referenceDescription;
    }

    @Override
    public Character getCode() {
        return 's';
    }

    @Override
    public String getFullId() {
        return getCode()+""+getId();
    }

    public PartMaster getSubstitute() {
        return substitute;
    }

    @Override
    public List<CADInstance> getCadInstances() {
        return cadInstances;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setReferenceDescription(String referenceDescription) {
        this.referenceDescription = referenceDescription;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSubstitute(PartMaster substitute) {
        this.substitute = substitute;
    }

    public void setCadInstances(List<CADInstance> cadInstances) {
        this.cadInstances = cadInstances;
    }

    @Override
    public PartSubstituteLink clone() {
        PartSubstituteLink clone = null;
        try {
            clone = (PartSubstituteLink) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }

        //perform a deep copy
        List<CADInstance> clonedCADInstances = new LinkedList<CADInstance>();
        for (CADInstance cadInstance : cadInstances) {
            CADInstance clonedCADInstance = cadInstance.clone();
            clonedCADInstances.add(clonedCADInstance);
        }
        clone.cadInstances = clonedCADInstances;

        return clone;
    }

}
