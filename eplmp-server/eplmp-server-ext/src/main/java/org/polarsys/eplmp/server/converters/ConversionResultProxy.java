/*******************************************************************************
 * Copyright (c) 2017-2019 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.server.converters;

import org.polarsys.eplmp.core.product.ConversionResult;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ConversionResultProxy extends ConversionResult {

    private Path convertedFile;

    public ConversionResultProxy() {
        super();
    }

    public ConversionResultProxy(Path convertedFile) {
        super();
        this.convertedFile  = convertedFile;
    }

    public ConversionResultProxy(Path convertedFile, List<Path> materials) {
        super();
        this.setConvertedFile(convertedFile);
        this.setMaterials(materials);
    }

    public ConversionResultProxy(Map<String, List<Position>> componentPositionMap) {
        super();
        this.setComponentPositionMap(componentPositionMap);
    }

    public Path getConvertedFile() {
        return convertedFile;
    }

    public void setConvertedFile(Path convertedFile) {
        this.convertedFile = convertedFile;
    }
}
