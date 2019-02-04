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

import org.polarsys.eplmp.core.common.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A Layer is a collection of {@link Marker}s that can be manipulated as a whole.
 * Layers belong to a {@link ConfigurationItem}.
 * 
 * @author Florent Garin
 * @version 1.1, 14/08/12
 * @since   V1.1
 */
@Table(name="LAYER")
@Entity
@NamedQueries({
    @NamedQuery(name="Layer.findLayersByConfigurationItem",query="SELECT DISTINCT l FROM Layer l WHERE l.configurationItem.id = :configurationItemId AND l.configurationItem.workspace.id = :workspaceId"),
    @NamedQuery(name="Layer.removeLayersFromConfigurationItem",query="DELETE FROM Layer l WHERE l.configurationItem.id = :configurationItemId AND l.configurationItem.workspace.id = :workspaceId")
})
public class Layer implements Serializable{

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
    
    private String name;
    
    @ManyToMany(fetch= FetchType.LAZY)
    @JoinTable(name="LAYER_MARKER",
    inverseJoinColumns={
        @JoinColumn(name="MARKER_ID", referencedColumnName="ID")
    },
    joinColumns={
        @JoinColumn(name="LAYER_ID", referencedColumnName="ID")
    })
    private Set<Marker> markers = new HashSet<>();
        
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "CONFIGURATIONITEM_ID", referencedColumnName = "ID"),
        @JoinColumn(name = "CONFIGURATIONITEM_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private ConfigurationItem configurationItem;

    private String color;

    public Layer() {
    }

    public Layer(String pName, User pAuthor, ConfigurationItem pConfigurationItem, String color) {
        this.name=pName;
        this.author=pAuthor;
        this.configurationItem=pConfigurationItem;
        this.color=color;
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

    public Set<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(Set<Marker> markers) {
        this.markers = markers;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }

    public void setConfigurationItem(ConfigurationItem configurationItem) {
        this.configurationItem = configurationItem;
    }

    public void addMarker(Marker marker) {
        this.markers.add(marker);
    }

    public void removeMarker(Marker marker) {
        this.markers.remove(marker);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
