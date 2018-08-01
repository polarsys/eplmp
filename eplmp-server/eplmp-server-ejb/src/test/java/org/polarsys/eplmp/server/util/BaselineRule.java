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

package org.polarsys.eplmp.server.util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.common.User;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.configuration.ProductBaselineType;
import org.polarsys.eplmp.core.meta.RevisionStatus;
import org.polarsys.eplmp.core.product.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Asmae Chadid
 * @version 2.0, 19/12/14
 * @since V2.0
 */
public class BaselineRule implements TestRule {

    private String name;
    private ProductBaselineType type;
    private String description;
    private Workspace workspace;
    private User user;
    private PartMaster partMaster;
    private ConfigurationItemKey configurationItemKey;
    private ConfigurationItem configurationItem;
    private List<String> substituteLinks = new ArrayList<>();
    private List<String> optionalUsageLinks = new ArrayList<>();


    public BaselineRule(String baselineName, ProductBaselineType type, String description, String workspaceId, String login, String partId, String productId, boolean released) {
        name = baselineName;
        this.type = type;
        this.description = description;
        this.workspace = new Workspace(workspaceId);
        user = new User(workspace, new Account(login, login, login + "@docdoku.com", "en", new Date(), null));
        partMaster = new PartMaster(workspace, partId, user);
        configurationItemKey = new ConfigurationItemKey("workspace1", productId);
        configurationItem = new ConfigurationItem(user, workspace, productId, "description");
        partMaster.setPartRevisions(new ArrayList<>());
        if (released) {
            List<PartRevision> revisions = new ArrayList<>();
            List<PartIteration> iterationLists = new ArrayList<>();
            PartRevision revision = new PartRevision(partMaster, "A", user);
            iterationLists.add(new PartIteration(revision, user));
            revision.setPartIterations(iterationLists);
            revision.setStatus(RevisionStatus.RELEASED);
            revisions.add(revision);
            partMaster.setPartRevisions(revisions);
        }

        configurationItem.setDesignItem(partMaster);
    }

    public BaselineRule(String baselineName, ProductBaselineType type, String description, String workspaceId, String login, String partId, String productId, boolean released, boolean checkedOut) {
        this(baselineName, type, description, workspaceId, login, partId, productId, released);
        if (checkedOut) {
            this.partMaster.getLastReleasedRevision().getIteration(1).getPartRevision().setCheckOutUser(this.user);
        }
    }


    @Override
    public Statement apply(Statement statement, Description description) {
        return new BaselineStatement(statement);
    }

    public PartLink getRootPartUsageLink() {
        return new PartLink() {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public Character getCode() {
                return '-';
            }

            @Override
            public String getFullId() {
                return "-1";
            }

            @Override
            public double getAmount() {
                return 1;
            }

            @Override
            public String getUnit() {
                return null;
            }

            @Override
            public String getComment() {
                return null;
            }

            @Override
            public boolean isOptional() {
                return false;
            }

            @Override
            public PartMaster getComponent() {
                return configurationItem.getDesignItem();
            }

            @Override
            public List<PartSubstituteLink> getSubstitutes() {
                return null;
            }

            @Override
            public String getReferenceDescription() {
                return null;
            }

            @Override
            public List<CADInstance> getCadInstances() {
                List<CADInstance> cads = new ArrayList<>();
                CADInstance cad = new CADInstance(0d, 0d, 0d, 0d, 0d, 0d);
                cad.setId(0);
                cads.add(cad);
                return cads;
            }
        };
    }

    public class BaselineStatement extends Statement {
        private final Statement statement;

        public BaselineStatement(Statement s) {
            this.statement = s;
        }

        @Override
        public void evaluate() throws Throwable {
            statement.evaluate();
        }
    }

    public ConfigurationItem getConfigurationItem() {
        return this.configurationItem;
    }

    public String getName() {
        return name;
    }

    public ProductBaselineType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public User getUser() {
        return user;
    }

    public PartMaster getPartMaster() {
        return partMaster;
    }

    public ConfigurationItemKey getConfigurationItemKey() {
        return configurationItemKey;
    }

    public List<String> getOptionalUsageLinks() {
        return optionalUsageLinks;
    }

    public List<String> getSubstituteLinks() {
        return substituteLinks;
    }
}
