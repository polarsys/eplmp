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

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConversionResultProxy extends ConversionResult {
    public ConversionResultProxy() {
        super();
    }

    public ConversionResultProxy(Path convertedFile) {
        super(convertedFile);
    }

    public ConversionResultProxy(Path convertedFile, List<Path> materials) {
        super(convertedFile, materials);
    }

    public ConversionResultProxy(Map<String, List<Position>> componentPositionMap) {
        super(componentPositionMap);
    }
}
