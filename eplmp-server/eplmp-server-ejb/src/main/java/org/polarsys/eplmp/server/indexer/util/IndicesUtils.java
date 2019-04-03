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

package org.polarsys.eplmp.server.indexer.util;

import org.polarsys.eplmp.core.util.Tools;
import org.polarsys.eplmp.server.indexer.config.IndexerConfig;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods for Search & Index operations using Elasticsearch API.
 *
 * @author Taylor Labejof
 */

@Stateless(name = "IndicesUtils")
public class IndicesUtils{

    @Inject
    IndexerConfig config;

    private static final Logger LOGGER = Logger.getLogger(IndicesUtils.class.getName());

    public String getIndexName(String indexName, String type){
        return config.getPrefixIndex() + IndexerMapping.INDEX_SEPARATOR +
               IndexerMapping.INDEX_PREFIX + IndexerMapping.INDEX_SEPARATOR +
                formatIndexName(indexName) + IndexerMapping. INDEX_SEPARATOR + type;
    }

    /**
     * Convert the workspaceId to a Elastic Search index name
     *
     * @param workspaceId Id to convert
     * @return The workspaceId without uppercase and space
     */
    private String formatIndexName(String workspaceId) {
        try {
            return URLEncoder.encode(Tools.unAccent(workspaceId), "UTF-8").toLowerCase();
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.FINEST, null, e);
            return null;
        }
    }
}
