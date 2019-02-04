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

package org.polarsys.eplmp.core.query;

import org.polarsys.eplmp.core.configuration.PathDataIteration;
import org.polarsys.eplmp.core.product.PartLink;
import org.polarsys.eplmp.core.product.PartLinkList;
import org.polarsys.eplmp.core.product.PartRevision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Value object to represent one row of a {@link Query} result set.
 *
 * Instances of this class are not persisted.
 *
 * @author Morgan Guimard
 */
public class QueryResultRow {

    private PartRevision partRevision;
    private int depth;
    private Map<String, List<PartLinkList>> sources = new HashMap<>();
    private Map<String, List<PartLinkList>> targets = new HashMap<>();
    private PathDataIteration pathDataIteration;
    private double[] results;
    private QueryContext context;
    private double amount;
    private String path;

    public QueryResultRow() {
    }

    public QueryResultRow(PartRevision partRevision) {
        this.partRevision = partRevision;
    }

    public QueryResultRow(PartRevision partRevision, int depth, double[] results) {
        this.partRevision = partRevision;
        this.depth = depth;
        this.results = results;
    }

    public PartRevision getPartRevision() {
        return partRevision;
    }

    public void setPartRevision(PartRevision partRevision) {
        this.partRevision = partRevision;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public double[] getResults() {
        return results;
    }

    public void setResults(double[] results) {
        this.results = results;
    }

    public QueryContext getContext() {
        return context;
    }

    public void setContext(QueryContext context) {
        this.context = context;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Map<String, List<PartLinkList>> getSources() {
        return sources;
    }

    public void setSources(Map<String, List<PartLinkList>> sources) {
        this.sources = sources;
    }

    public Map<String, List<PartLinkList>> getTargets() {
        return targets;
    }

    public void setTargets(Map<String, List<PartLinkList>> targets) {
        this.targets = targets;
    }

    public void addTarget(String type, List<PartLink> targetPath) {
        List<PartLinkList> paths = targets.get(type) != null ? targets.get(type) : new ArrayList<>();
        paths.add(new PartLinkList(targetPath));
        targets.put(type, paths);
    }

    public void addSource(String type, List<PartLink> sourcePath) {
        List<PartLinkList> paths = sources.get(type) != null ? sources.get(type) : new ArrayList<>();
        paths.add(new PartLinkList(sourcePath));
        sources.put(type, paths);
    }

    public PathDataIteration getPathDataIteration() {
        return pathDataIteration;
    }

    public void setPathDataIteration(PathDataIteration pathDataIteration) {
        this.pathDataIteration = pathDataIteration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
