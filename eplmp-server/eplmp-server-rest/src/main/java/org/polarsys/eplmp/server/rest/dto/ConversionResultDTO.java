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

package org.polarsys.eplmp.server.rest.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.polarsys.eplmp.core.product.PartIterationKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@ApiModel(value = "ConversionResultDTO", description = "")
public class ConversionResultDTO implements Serializable {

    @ApiModelProperty(value = "PartIterationKey")
    private PartIterationKey partIterationKey;

    @ApiModelProperty(value = "Temp dir")
    private String tempDir;

    public static class PositionDTO implements Serializable {

        private double[] translation;
        private double[][] rotationmatrix;

        public PositionDTO(double[][] rm, double[] o) {
            this.translation = o;
            this.rotationmatrix = rm;
        }

        public double[] getTranslation() {
            return translation;
        }

        public void setTranslation(double[] translation) {
            this.translation = translation;
        }

        public double[][] getRotationmatrix() {
            return rotationmatrix;
        }

        public void setRotationmatrix(double[][] rotationmatrix) {
            this.rotationmatrix = rotationmatrix;
        }
    }

    @ApiModelProperty(value = "Converted file")
    private String convertedFile;

    @ApiModelProperty(value = "Materials")
    private List<String> materials = new ArrayList<>();

    @ApiModelProperty(value = "Std output")
    private String stdOutput;

    @ApiModelProperty(value = "Error output")
    private String errorOutput;

    @ApiModelProperty(value = "Component position map")
    private Map<String, List<PositionDTO>> componentPositionMap;

    public PartIterationKey getPartIterationKey() {
        return partIterationKey;
    }

    public void setPartIterationKey(PartIterationKey partIterationKey) {
        this.partIterationKey = partIterationKey;
    }

    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    public String getConvertedFile() {
        return convertedFile;
    }

    public void setConvertedFile(String convertedFile) {
        this.convertedFile = convertedFile;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public String getStdOutput() {
        return stdOutput;
    }

    public void setStdOutput(String stdOutput) {
        this.stdOutput = stdOutput;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public void setErrorOutput(String errorOutput) {
        this.errorOutput = errorOutput;
    }

    public Map<String, List<PositionDTO>> getComponentPositionMap() {
        return componentPositionMap;
    }

    public void setComponentPositionMap(Map<String, List<PositionDTO>> componentPositionMap) {
        this.componentPositionMap = componentPositionMap;
    }
}
