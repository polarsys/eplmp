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

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * @author Morgan Guimard
 */
@XmlRootElement
@ApiModel(value = "LeafDTO", description = "This class is the representation of a leaf in InstanceCollection")
public class LeafDTO implements Serializable {

    @ApiModelProperty(value = "Instance id")
    private String id;

    @ApiModelProperty(value = "Part iteration id")
    private String partIterationId;

    @ApiModelProperty(value = "Complete path in structure")
    private String path;

    @ApiModelProperty(value = "Transformation matrix")
    private List<Double> matrix;

    @ApiModelProperty(value = "Level of details available")
    private Integer qualities;

    @ApiModelProperty(value = "Bounding box X axis lower value")
    private Double xMin;

    @ApiModelProperty(value = "Bounding box Y axis lower value")
    private Double yMin;

    @ApiModelProperty(value = "Bounding box Z axis lower value")
    private Double zMin;

    @ApiModelProperty(value = "Bounding box X axis upper value")
    private Double xMax;

    @ApiModelProperty(value = "Bounding box Y axis upper value")
    private Double yMax;

    @ApiModelProperty(value = "Bounding box Z axis upper value")
    private Double zMax;

    @ApiModelProperty(value = "All available files")
    private List<BinaryResourceDTO> files;

    public LeafDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartIterationId() {
        return partIterationId;
    }

    public void setPartIterationId(String partIterationId) {
        this.partIterationId = partIterationId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Double> getMatrix() {
        return matrix;
    }

    public void setMatrix(List<Double> matrix) {
        this.matrix = matrix;
    }

    public Integer getQualities() {
        return qualities;
    }

    public void setQualities(Integer qualities) {
        this.qualities = qualities;
    }

    public Double getxMin() {
        return xMin;
    }

    public void setxMin(Double xMin) {
        this.xMin = xMin;
    }

    public Double getyMin() {
        return yMin;
    }

    public void setyMin(Double yMin) {
        this.yMin = yMin;
    }

    public Double getzMin() {
        return zMin;
    }

    public void setzMin(Double zMin) {
        this.zMin = zMin;
    }

    public Double getxMax() {
        return xMax;
    }

    public void setxMax(Double xMax) {
        this.xMax = xMax;
    }

    public Double getyMax() {
        return yMax;
    }

    public void setyMax(Double yMax) {
        this.yMax = yMax;
    }

    public Double getzMax() {
        return zMax;
    }

    public void setzMax(Double zMax) {
        this.zMax = zMax;
    }

    public List<BinaryResourceDTO> getFiles() {
        return files;
    }

    public void setFiles(List<BinaryResourceDTO> files) {
        this.files = files;
    }
}
