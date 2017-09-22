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
package org.polarsys.eplmp.server.indexer;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import java.util.Properties;

/**
 * Retrieves Elasticsearch config from custom jndi resource
 *
 * @author Morgan Guimard
 */
@ApplicationScoped
public class IndexerConfigManager {

    @Resource(name = "elasticsearch.config")
    private Properties properties;

    public Integer getNumberOfShards(){
        return Integer.parseInt(properties.getProperty("number_of_shards"));
    }

    public Integer getNumberOfReplicas(){
        return Integer.parseInt(properties.getProperty("number_of_replicas"));
    }

    public String getAutoExpandReplicas(){
        return properties.getProperty("auto_expand_replicas");
    }

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
