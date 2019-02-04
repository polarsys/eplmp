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
package org.polarsys.eplmp.core.configuration;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Wrapper for data of multiple forms
 * (attributes, binary files, document links...) localized on a precise part
 * inside a product structure.
 * Each evolution of data is tracked with the help of
 * {@link PathDataIteration}.
 *
 * @author Morgan Guimard
 */
@Table(name="PATHDATAMASTER")
@Entity
@NamedQueries({
        @NamedQuery(name="PathDataMaster.findByPathIdAndProductInstanceIteration", query="SELECT p FROM PathDataMaster p JOIN ProductInstanceIteration l WHERE p member of l.pathDataMasterList and p.id = :pathId and l = :productInstanceIteration"),
        @NamedQuery(name="PathDataMaster.findByPathAndProductInstanceIteration", query="SELECT p FROM PathDataMaster p JOIN ProductInstanceIteration l WHERE p member of l.pathDataMasterList and p.path = :path and l = :productInstanceIteration")
})
public class PathDataMaster implements Serializable{

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name="ID")
    private int id;

    private String path;

    @OneToMany(mappedBy = "pathDataMaster", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("iteration ASC")
    private List<PathDataIteration> pathDataIterations = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<PathDataIteration> getPathDataIterations() {
        return pathDataIterations;
    }

    public void setPathDataIterations(List<PathDataIteration> pathDataIterations) {
        this.pathDataIterations = pathDataIterations;
    }

    public PathDataIteration createNextIteration() {

        PathDataIteration lastPathIteration = this.getLastIteration();
        int iteration;
        if(lastPathIteration==null){
            iteration = 1;
        }else{
            iteration = lastPathIteration.getIteration()+1;
        }
        PathDataIteration pathIteration  = new PathDataIteration(iteration,this,new Date());
        this.pathDataIterations.add(pathIteration);
        return pathIteration;
    }

    public PathDataIteration getLastIteration() {
        int index = this.pathDataIterations.size()-1;
        if(index < 0) {
            return null;
        } else {
            return this.pathDataIterations.get(index);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathDataMaster that = (PathDataMaster) o;

        if (id != that.id){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
