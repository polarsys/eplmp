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

package org.polarsys.eplmp.core.configuration;

import java.io.Serializable;

/**
 * Identity class of {@link PathDataIteration} objects.
 *
 * @author Florent Garin
 */
public class PathDataIterationKey implements Serializable{

    private int pathDataMaster;
    private int iteration;

    public PathDataIterationKey(){
    }


    public PathDataIterationKey(int pathDataMaster, int iteration) {
        this.pathDataMaster = pathDataMaster;
        this.iteration = iteration;
    }

    public int getPathDataMaster() {
        return pathDataMaster;
    }

    public void setPathDataMaster(int pathDataMaster) {
        this.pathDataMaster = pathDataMaster;
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PathDataIterationKey that = (PathDataIterationKey) o;

        if (iteration != that.iteration) {
            return false;
        }
        if (pathDataMaster != that.pathDataMaster) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = pathDataMaster;
        result = 31 * result + iteration;
        return result;
    }
}
