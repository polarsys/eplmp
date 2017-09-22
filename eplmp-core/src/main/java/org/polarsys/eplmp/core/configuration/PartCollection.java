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
package org.polarsys.eplmp.core.configuration;

import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartMaster;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class maintains a collection of part iterations with no more
 * than one {@link PartIteration} per each {@link PartMaster}.
 *
 * PartCollection is a foundation for the definition of {@link ProductBaseline}
 * and {@link ProductInstanceIteration}.
 *
 * @author Florent Garin
 * @version 2.0, 25/02/14
 * @since V2.0
 */
@Table(name="PARTCOLLECTION")
@Entity
public class PartCollection implements Serializable {


    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "AUTHOR_LOGIN", referencedColumnName = "LOGIN"),
            @JoinColumn(name = "AUTHOR_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private User author;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date creationDate;

    @MapKey(name="baselinedPartKey")
    @OneToMany(mappedBy="partCollection", cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
    private Map<BaselinedPartKey, BaselinedPart> baselinedParts=new HashMap<>();

    public PartCollection() {
    }

    public void removeAllBaselinedParts() {
        baselinedParts.clear();
    }

    public Map<BaselinedPartKey, BaselinedPart> getBaselinedParts() {
        return baselinedParts;
    }

    public void addBaselinedPart(PartIteration targetPart){
        BaselinedPart baselinedPart = new BaselinedPart(this, targetPart);
        baselinedParts.put(baselinedPart.getBaselinedPartKey(),baselinedPart);
    }

    public BaselinedPart getBaselinedPart(BaselinedPartKey baselinedPartKey){
        return baselinedParts.get(baselinedPartKey);
    }

    public boolean hasBaselinedPart(BaselinedPartKey baselinedPartKey){
        return baselinedParts.containsKey(baselinedPartKey);
    }

    public Date getCreationDate() {
        return (creationDate!=null) ? (Date) creationDate.clone() : null;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = (creationDate!=null) ? (Date) creationDate.clone() : null;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartCollection)) {
            return false;
        }

        PartCollection collection = (PartCollection) o;
        return id == collection.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
