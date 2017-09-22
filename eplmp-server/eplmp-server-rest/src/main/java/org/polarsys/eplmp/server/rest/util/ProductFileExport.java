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

package org.polarsys.eplmp.server.rest.util;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.configuration.ProductStructureFilter;
import org.polarsys.eplmp.core.product.ConfigurationItemKey;

import java.util.Map;
import java.util.Set;

/**
 * This class holds the context for a product export
 * See {link org.polarsys.eplmp.server.rest.writers.DocumentBaselineFileExportMessageBodyWriter} for response implementation
 *
 * @author morgan on 29/04/15.
 */
public class ProductFileExport {

    private ConfigurationItemKey configurationItemKey;
    private ProductStructureFilter psFilter;

    private String serialNumber;
    private Integer baselineId;

    private boolean exportNativeCADFile;
    private boolean exportDocumentLinks;
    private Map<String, Set<BinaryResource>> binariesInTree;


    public ProductFileExport() {
    }

    public ProductStructureFilter getPsFilter() {
        return psFilter;
    }

    public void setPsFilter(ProductStructureFilter psFilter) {
        this.psFilter = psFilter;
    }

    public ConfigurationItemKey getConfigurationItemKey() {
        return configurationItemKey;
    }

    public void setConfigurationItemKey(ConfigurationItemKey configurationItemKey) {
        this.configurationItemKey = configurationItemKey;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getBaselineId() {
        return baselineId;
    }

    public void setBaselineId(Integer baselineId) {
        this.baselineId = baselineId;
    }

    public boolean isExportNativeCADFile() {
        return exportNativeCADFile;
    }

    public void setExportNativeCADFile(boolean exportNativeCADFile) {
        this.exportNativeCADFile = exportNativeCADFile;
    }

    public boolean isExportDocumentLinks() {
        return exportDocumentLinks;
    }

    public void setExportDocumentLinks(boolean exportDocumentLinks) {
        this.exportDocumentLinks = exportDocumentLinks;
    }

    public void setBinariesInTree(Map<String, Set<BinaryResource>> binariesInTree) {
        this.binariesInTree = binariesInTree;
    }

    public Map<String, Set<BinaryResource>> getBinariesInTree() {
        return binariesInTree;
    }
}
