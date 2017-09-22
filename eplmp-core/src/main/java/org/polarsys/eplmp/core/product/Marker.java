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

import org.polarsys.eplmp.core.common.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a marker on the 3D scene, actually a {@link ConfigurationItem}.
 * May be attached to one or several {@link PartMaster}s.
 * 
 * @author Florent Garin
 * @version 1.1, 14/08/12
 * @since   V1.1
 */
@Table(name="MARKER")
@Entity
public class Marker implements Serializable{

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private int id;

    @OneToMany(orphanRemoval=true, cascade= CascadeType.ALL, fetch= FetchType.EAGER)
    @JoinTable(name="MARKER_EFFECTIVITY",
    inverseJoinColumns={
        @JoinColumn(name="EFFECTIVITY_ID", referencedColumnName="ID")
    },
    joinColumns={
        @JoinColumn(name="MARKER_ID", referencedColumnName="ID")
    })
    private Set<Effectivity> effectivities = new HashSet<>();
        
    @ManyToMany(fetch= FetchType.LAZY)
    @JoinTable(name="MARKER_PARTMASTER",
    inverseJoinColumns={
        @JoinColumn(name="RELATEDPART_WORKSPACE_ID", referencedColumnName="WORKSPACE_ID"),
        @JoinColumn(name="RELATEDPART_PARTNUMBER", referencedColumnName="PARTNUMBER")
    },
    joinColumns={
        @JoinColumn(name="MARKER_ID", referencedColumnName="ID")
    })
    private Set<PartMaster> relatedParts = new HashSet<>();
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "AUTHOR_LOGIN", referencedColumnName = "LOGIN"),
        @JoinColumn(name = "AUTHOR_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private User author;
    
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date creationDate;
    
    private String title;
    
    @Lob
    private String description;
    
    /**
     * Position on x axis.
     */
    private double x;
    
    /**
     * Position on y axis.
     */
    private double y;
    
    /**
     * Position on z axis.
     */
    private double z;
    
    public Marker() {
    }

    public Marker(String pTitle, User pAuthor, String pDescription, double pX, double pY, double pZ) {
        this.title=pTitle;
        this.author=pAuthor;
        this.description=pDescription;
        this.x=pX;
        this.y=pY;
        this.z=pZ;
    }
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Effectivity> getEffectivities() {
        return effectivities;
    }

    public void setEffectivities(Set<Effectivity> effectivities) {
        this.effectivities = effectivities;
    }

    public Set<PartMaster> getRelatedParts() {
        return relatedParts;
    }

    public void setRelatedParts(Set<PartMaster> relatedParts) {
        this.relatedParts = relatedParts;
    }


    public void setX(double x) {
        this.x = x;
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getZ() {
        return z;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
   
    
    
}
