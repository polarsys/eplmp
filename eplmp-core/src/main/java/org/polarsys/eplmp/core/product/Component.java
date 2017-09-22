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
package org.polarsys.eplmp.core.product;

import org.polarsys.eplmp.core.common.User;

import java.io.Serializable;
import java.util.List;

/**
 * Convenience class to construct a resolved Product Structure.
 *
 * @author Morgan Guimard
 */

public class Component implements Serializable {

    private User user;
    private PartMaster partMaster;
    private PartIteration retainedIteration;
    private List<PartLink> path;
    private List<Component> components;
    private boolean isVirtual = false;

    public Component() {
    }

    public Component(User user, PartMaster partMaster, List<PartLink> path, List<Component> components) {
        this.user = user;
        this.partMaster = partMaster;
        this.path = path;
        this.components = components;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<PartLink> getPath() {
        return path;
    }
    public PartLink getPartLink() {
        return path.get(path.size()-1);
    }
    public void setPath(List<PartLink> path) {
        this.path = path;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
    public void addComponent(Component component) {
        this.components.add(component);
    }
    public PartMaster getPartMaster() {
        return partMaster;
    }

    public void setPartMaster(PartMaster partMaster) {
        this.partMaster = partMaster;
    }

    public PartIteration getRetainedIteration() {
        return retainedIteration;
    }

    public void setRetainedIteration(PartIteration retainedIteration) {
        this.retainedIteration = retainedIteration;
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public void setVirtual(boolean isVirtual) {
        this.isVirtual = isVirtual;
    }
}
