/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/

package org.polarsys.eplmp.core.configuration;

import java.util.Set;

public interface ResolvedCollection {

    PartCollection getPartCollection();
    Set<String> getOptionalUsageLinks();
    Set<String> getSubstituteLinks();
}