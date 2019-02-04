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

package org.polarsys.eplmp.server;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.Properties;

/**
 * Get config from resources
 *
 * @author Morgan Guimard
 */
@ApplicationScoped
public class ConfigManager {

    @Resource(name="docdokuplm.config")
    private Properties properties;

    public String getCodebase(){
        return properties.getProperty("codebase");
    }

    public String getVaultPath(){
        return properties.getProperty("vaultPath");
    }

    public String getDigestAlgorithm() {
        return Optional.ofNullable(properties.getProperty("digestAlgorithm")).orElse("MD5");
    }

}
