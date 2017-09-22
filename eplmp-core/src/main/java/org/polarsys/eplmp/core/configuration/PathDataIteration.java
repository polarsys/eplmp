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


import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.common.FileHolder;
import org.polarsys.eplmp.core.document.DocumentLink;
import org.polarsys.eplmp.core.meta.InstanceAttribute;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

/**
 * A state at a moment of time of a piece of data represented by
 * a {@link PathDataMaster}.
 *
 * @author Chadid Asmae
 */
@Table(name="PATHDATAITERATION")
@Entity
@IdClass(PathDataIterationKey.class)
@NamedQueries({
    @NamedQuery(name = "PathDataIteration.findDistinctInstanceAttributes", query = "SELECT DISTINCT i FROM ProductInstanceMaster pim JOIN pim.productInstanceIterations pi JOIN pi.pathDataMasterList pdm JOIN pdm.pathDataIterations pdi JOIN pdi.instanceAttributes i WHERE pim.instanceOf.workspace.id = :workspaceId"),
    @NamedQuery(name = "PathDataIteration.findLastIterationFromProductInstanceIteration", query = "SELECT DISTINCT pdi FROM ProductInstanceIteration pi JOIN pi.pathDataMasterList pdm JOIN pdm.pathDataIterations pdi WHERE pi = :productInstanceIteration AND pdi.iteration = (select max(otherPdi.iteration) from pdm.pathDataIterations otherPdi)")
})
public class PathDataIteration implements Serializable, FileHolder {

    @Id
    @ManyToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name="PATHDATAMASTER_ID", referencedColumnName="ID")
    })
    private PathDataMaster pathDataMaster;

    @Id
    @Column(name="ITERATION")
    private int iteration;

    @Lob
    private String iterationNote;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date  dateIteration;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn(name="ATTRIBUTE_ORDER")
    @JoinTable(name = "PATHDATAITERATION_ATTRIBUTE",
            inverseJoinColumns = {
                    @JoinColumn(name = "INSTANCEATTRIBUTE_ID", referencedColumnName = "ID")
            },
            joinColumns = {
                    @JoinColumn(name="PATHDATA_ITERATION", referencedColumnName="ITERATION"),
                    @JoinColumn(name="PATHDATAMASTER_ID", referencedColumnName="PATHDATAMASTER_ID")
            })
    private List<InstanceAttribute> instanceAttributes = new ArrayList<>();


    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "PATHDATAITERATION_DOCUMENTLINK",
            inverseJoinColumns = {
                    @JoinColumn(name = "DOCUMENTLINK_ID", referencedColumnName = "ID")
            },
            joinColumns = {
                    @JoinColumn(name="PATHDATA_ITERATION", referencedColumnName="ITERATION"),
                    @JoinColumn(name="PATHDATAMASTER_ID", referencedColumnName="PATHDATAMASTER_ID")
            })
    private Set<DocumentLink> linkedDocuments = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "PATHDATAITERATION_BINRES",
            inverseJoinColumns = {
                    @JoinColumn(name = "ATTACHEDFILE_FULLNAME", referencedColumnName = "FULLNAME")
            },
            joinColumns = {
                    @JoinColumn(name="PATHDATA_ITERATION", referencedColumnName="ITERATION"),
                    @JoinColumn(name="PATHDATAMASTER_ID", referencedColumnName="PATHDATAMASTER_ID")
            })
    private Set<BinaryResource> attachedFiles = new HashSet<>();

    public PathDataIteration() {
    }

    public PathDataIteration(int iteration, PathDataMaster pathDataMaster,Date date) {
        setIteration(iteration);
        setPathDataMaster(pathDataMaster);
        setDateIteration(date);
    }

    @XmlTransient
    public PathDataMaster getPathDataMaster() {
        return pathDataMaster;
    }

    public void setPathDataMaster(PathDataMaster pathDataMaster) {
        this.pathDataMaster = pathDataMaster;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public Date getDateIteration() {
        return dateIteration;
    }

    public void setDateIteration(Date dateIteration) {
        this.dateIteration = dateIteration;
    }

    public Set<DocumentLink> getLinkedDocuments() {
        return linkedDocuments;
    }

    public void setLinkedDocuments(Set<DocumentLink> linkedDocuments) {
        this.linkedDocuments = linkedDocuments;
    }

    public List<InstanceAttribute> getInstanceAttributes() {
        return instanceAttributes;
    }

    public void setInstanceAttributes(List<InstanceAttribute> instanceAttributes) {
        this.instanceAttributes = instanceAttributes;
    }

    public String getIterationNote() {
        return iterationNote;
    }

    public void setIterationNote(String iterationNote) {
        this.iterationNote = iterationNote;
    }

    @Override
    public Set<BinaryResource> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(Set<BinaryResource> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public void addFile(BinaryResource binaryResource) {
        attachedFiles.add(binaryResource);
    }

    public void removeFile(BinaryResource file) {
        attachedFiles.remove(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathDataIteration that = (PathDataIteration) o;

        if (iteration != that.iteration) {
            return false;
        }
        if (pathDataMaster != null ? !pathDataMaster.equals(that.pathDataMaster) : that.pathDataMaster != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = pathDataMaster != null ? pathDataMaster.hashCode() : 0;
        result = 31 * result + iteration;
        return result;
    }
}
