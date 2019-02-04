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
package org.polarsys.eplmp.server.indexer.config;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import java.util.Properties;

/**
 * Retrieves Elasticsearch config from custom jndi resource
 *
 * @author Morgan Guimard
 */
@ApplicationScoped
public class IndexerConfig {

    public static final String NUMBER_OF_SHARDS = "number_of_shards";
    public static final String NUMBER_OF_REPLICAS = "number_of_replicas";
    public static final String AUTO_EXPAND_REPLICAS = "auto_expand_replicas";

    @Resource(name = "elasticsearch.config")
    private Properties properties;

    public String getServerUri() {
        return properties.getProperty("serverUri");
    }

    public String getUserName() { return properties.getProperty("username"); }

    public String getPassword() { return properties.getProperty("password"); }

    public String getAWSService() { return properties.getProperty("awsService"); }

    public String getAWSRegion() { return properties.getProperty("awsRegion"); }

    public String getAWSAccessKey() { return properties.getProperty("awsAccessKey"); }

    public String getAWSSecretKey() { return properties.getProperty("awsSecretKey"); }

}
