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

/**
 * @author Yassine Belouad
 */

@XmlRootElement
@ApiModel(value = "FolderDTO", description = "This class is a representation of a {@link org.polarsys.eplmp.core.meta.Folder} entity")
public class FolderDTO implements Serializable {

    @ApiModelProperty(value = "Folder full path")
    private String path;

    @ApiModelProperty(value = "Folder id")
    private String id;

    @ApiModelProperty(value = "Folder name")
    private String name;

    @ApiModelProperty(value = "Folder home flag")
    private boolean home;

    public FolderDTO() {

    }

    public FolderDTO(String parentFolder, String name) {
        this.name = name.trim();
        path = parentFolder + "/" + this.name;
    }

    public static String replaceSlashWithColon(String completePathWithSlashes) {
        return completePathWithSlashes.replaceAll("/", ":");
    }

    public static String replaceColonWithSlash(String completePathWithColons) {
        return completePathWithColons.replaceAll(":", "/");
    }

    public static String extractName(String slashedCompletePath) {
        stripTrailingSlash(slashedCompletePath);
        int lastSlash = slashedCompletePath.lastIndexOf('/');
        return slashedCompletePath.substring(lastSlash, slashedCompletePath.length());
    }

    public static String extractParentFolder(String slashedCompletePath) {
        stripTrailingSlash(slashedCompletePath);
        int lastSlash = slashedCompletePath.lastIndexOf('/');
        return slashedCompletePath.substring(0, lastSlash);
    }

    private static String stripTrailingSlash(String completePath) {
        if (completePath.charAt(completePath.length() - 1) == '/') {
            return completePath.substring(0, completePath.length() - 1);
        } else {
            return completePath;
        }
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
