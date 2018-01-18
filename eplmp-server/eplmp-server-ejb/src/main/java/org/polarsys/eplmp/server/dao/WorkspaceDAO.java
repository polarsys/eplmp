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

package org.polarsys.eplmp.server.dao;

import org.polarsys.eplmp.core.admin.WorkspaceBackOptions;
import org.polarsys.eplmp.core.admin.WorkspaceFrontOptions;
import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.configuration.PathDataIteration;
import org.polarsys.eplmp.core.configuration.PathDataMaster;
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentLink;
import org.polarsys.eplmp.core.document.DocumentMaster;
import org.polarsys.eplmp.core.meta.Folder;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartUsageLink;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.workflow.WorkflowModel;
import org.polarsys.eplmp.core.workflow.WorkspaceWorkflow;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class WorkspaceDAO {


    private EntityManager em;
    private Locale mLocale;
    private IBinaryStorageManagerLocal storageManager;

    public WorkspaceDAO(Locale pLocale, EntityManager pEM) {
        em = pEM;
        mLocale = pLocale;
    }

    public WorkspaceDAO(EntityManager pEM) {
        em = pEM;
        mLocale = Locale.getDefault();
    }

    public WorkspaceDAO(EntityManager pEM, IBinaryStorageManagerLocal pStorageManager) {
        em = pEM;
        storageManager = pStorageManager;
    }

    public void updateWorkspaceFrontOptions(WorkspaceFrontOptions settings){
        em.merge(settings);
    }

    public void updateWorkspaceBackOptions(WorkspaceBackOptions settings){
        em.merge(settings);
    }

    public WorkspaceFrontOptions loadWorkspaceFrontOptions(String pID){
        return em.find(WorkspaceFrontOptions.class, pID);
    }

    public WorkspaceBackOptions loadWorkspaceBackOptions(String pID){
        return em.find(WorkspaceBackOptions.class, pID);
    }

    public void createWorkspace(Workspace pWorkspace) throws WorkspaceAlreadyExistsException, CreationException, FolderAlreadyExistsException {
        try {
            //the EntityExistsException is thrown only when flush occurs
            em.persist(pWorkspace);
            em.flush();
            new FolderDAO(mLocale, em).createFolder(new Folder(pWorkspace.getId()));
        } catch (EntityExistsException pEEEx) {
            throw new WorkspaceAlreadyExistsException(mLocale, pWorkspace);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }


    public Workspace loadWorkspace(String pID) throws WorkspaceNotFoundException {
        Workspace workspace = em.find(Workspace.class, pID);
        if (workspace == null) {
            throw new WorkspaceNotFoundException(mLocale, pID);
        } else {
            return workspace;
        }
    }

    public long getDiskUsageForWorkspace(String pWorkspaceId) {
        Number result = (Number) em.createNamedQuery("BinaryResource.diskUsageInPath")
                .setParameter("path", pWorkspaceId + "/%")
                .getSingleResult();

        return result != null ? result.longValue() : 0L;
    }

    public List<Workspace> findWorkspacesWhereUserIsActive(String userLogin) {
        return em.createNamedQuery("Workspace.findWorkspacesWhereUserIsActive", Workspace.class)
                .setParameter("userLogin", userLogin)
                .getResultList();
    }

    public void removeWorkspace(Workspace workspace) throws IOException, StorageException, EntityConstraintException, FolderNotFoundException {


        String workspaceId = workspace.getId();
        String pathToMatch = workspaceId.replace("_", "\\_").replace("%", "\\%") + "/%";
        final int batchSize = 1000;
        int i;

        // SharedEntities
        em.createQuery("DELETE FROM SharedEntity s where s.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Subscriptions
        em.createQuery("DELETE FROM IterationChangeSubscription s where s.observedDocumentRevisionWorkspaceId = :workspaceId")
                .setParameter("workspaceId", workspaceId).executeUpdate();

        em.createQuery("DELETE FROM StateChangeSubscription s where s.observedDocumentRevisionWorkspaceId = :workspaceId")
                .setParameter("workspaceId", workspaceId).executeUpdate();

        // BaselinedPart
        em.createQuery("DELETE FROM BaselinedPart bp where bp.targetPart.partRevision.partMasterWorkspaceId = :workspaceId")
                .setParameter("workspaceId", workspaceId).executeUpdate();

        // BaselinedDocument
        em.createQuery("DELETE FROM BaselinedDocument bd where bd.targetDocument.documentRevision.documentMasterWorkspaceId = :workspaceId")
                .setParameter("workspaceId", workspaceId).executeUpdate();

        i = 0;
        while (true) {
        List<ProductInstanceIteration> productInstanceIterations =
                em.createQuery("SELECT pii FROM ProductInstanceIteration pii WHERE pii.productInstanceMaster.instanceOf.workspace = :workspace", ProductInstanceIteration.class)
                    .setParameter("workspace", workspace)
                    .setFirstResult(i)
                    .setMaxResults(batchSize)
                    .getResultList();
            if (productInstanceIterations == null || productInstanceIterations.isEmpty()) {
                break;
            }
            for (ProductInstanceIteration p : productInstanceIterations) {
                for (DocumentLink documentLink : p.getLinkedDocuments()) {
                    documentLink.removeTargetDocument();
                }
                p.setLinkedDocuments(new HashSet<>());
                for (PathDataMaster pathDataMaster : p.getPathDataMasterList()) {
                    for (PathDataIteration p2 : pathDataMaster.getPathDataIterations()) {
                        for (DocumentLink documentLink : p2.getLinkedDocuments()) {
                            documentLink.removeTargetDocument();
                        }
                        p2.setLinkedDocuments(new HashSet<>());
                    }
                }
            }
            em.flush();
            em.clear();
            i += batchSize;
        }

        // PathDatas
        em.createQuery("DELETE FROM PathDataIteration pdi " +
            "where pdi.pathDataMaster IN (" +
            "SELECT pdm FROM ProductInstanceIteration pii, PathDataMaster pdm " +
            "WHERE pdm = pii.pathDataMasterList " +
            "AND pii.productInstanceMaster.instanceOf.workspace = :workspace" +
            ")")
            .setParameter("workspace", workspace).executeUpdate();
        em.createQuery("DELETE FROM PathDataMaster pdm " +
            "where pdm IN (" +
            "SELECT pdm2 FROM ProductInstanceIteration pii, PathDataMaster pdm2 " +
            "WHERE pdm2 = pii.pathDataMasterList " +
            "AND pii.productInstanceMaster.instanceOf.workspace = :workspace" +
            ")")
            .setParameter("workspace", workspace).executeUpdate();

        // ProductInstances
        em.createQuery("DELETE FROM ProductInstanceIteration pii where pii.productInstanceMaster.instanceOf.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();
        em.createQuery("DELETE FROM ProductInstanceMaster pim where pim.instanceOf.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // ProductBaselines
        em.createQuery("DELETE FROM ProductBaseline b where b.configurationItem.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // ProductConfigurations
        em.createQuery("DELETE FROM ProductConfiguration c where c.configurationItem.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // DocumentBaselines
        em.createQuery("DELETE FROM DocumentBaseline b where b.author.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // PartCollection
        em.createQuery("DELETE FROM PartCollection pc where pc.author.workspaceId = :workspaceId")
                .setParameter("workspaceId", workspaceId).executeUpdate();

        // DocumentCollection
        em.createQuery("DELETE FROM DocumentCollection dc where dc.author.workspaceId = :workspaceId")
                .setParameter("workspaceId", workspaceId).executeUpdate();

        // Layers
        em.createQuery("DELETE FROM Layer l where l.configurationItem.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();
        // Markers
        em.createQuery("DELETE FROM Marker m where m.author.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Reset effectivities constraints on configuration items
        EffectivityDAO effectivityDAO = new EffectivityDAO(em);
        effectivityDAO.removeEffectivityConstraints(workspaceId);
        em.flush();
        em.clear();

        // ConfigurationItem
        em.createQuery("DELETE FROM ConfigurationItem c where c.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // DocumentMasterTemplate
        em.createQuery("DELETE FROM DocumentMasterTemplate d where d.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // PartMasterTemplate
        em.createQuery("DELETE FROM PartMasterTemplate p where p.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Conversions
        em.createQuery("DELETE FROM Conversion c where c.partIteration.partRevision.partMaster.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Notifications
        em.createQuery("DELETE FROM ModificationNotification m where m.impactedPart.partRevision.partMaster.workspace = :workspace or m.modifiedPart.partRevision.partMaster.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Change order
        em.createQuery("DELETE FROM ChangeOrder c where c.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Change requests
        em.createQuery("DELETE FROM ChangeRequest c where c.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Change issues
        em.createQuery("DELETE FROM ChangeIssue c where c.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Change issues / requests
        em.createQuery("DELETE FROM Milestone m where m.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        i = 0;
        while (true) {
            List<DocumentIteration> documentIterations =
                    em.createQuery("SELECT d FROM DocumentIteration d WHERE d.documentRevision.partMaster.workspace = :workspace", DocumentIteration.class)
                            .setParameter("workspace", workspace)
                            .setFirstResult(i)
                            .setMaxResults(batchSize)
                            .getResultList();
            if (documentIterations == null || documentIterations.isEmpty()) {
                break;
            }
            for (DocumentIteration d : documentIterations) {
                for (DocumentLink documentLink : d.getLinkedDocuments()) {
                    documentLink.removeTargetDocument();
                }
                d.setLinkedDocuments(new HashSet<>());
            }
            em.flush();
            em.clear();
            i += batchSize;
        }

        i = 0;
        while (true) {
            List<PartIteration> partIterations =
                    em.createQuery("SELECT p FROM PartIteration p WHERE p.partRevision.partMaster.workspace = :workspace", PartIteration.class)
                        .setParameter("workspace", workspace)
                        .setFirstResult(i)
                        .setMaxResults(batchSize)
                        .getResultList();
            if (partIterations == null || partIterations.isEmpty()) {
                break;
            }
            for (PartIteration p : partIterations) {
                for (DocumentLink documentLink : p.getLinkedDocuments()) {
                      documentLink.removeTargetDocument();
                }
                p.setLinkedDocuments(new HashSet<>());
                for (PartUsageLink pul : p.getComponents()) {
                    pul.setSubstitutes(new LinkedList<>());
                }
                p.setComponents(new LinkedList<>());
            }
            em.flush();
            em.clear();
            i += batchSize;
        }

        // Clear all part substitute links
        em.createQuery("DELETE FROM PartSubstituteLink psl WHERE psl.substitute.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Clear all part usage links
        em.createQuery("DELETE FROM PartUsageLink pul WHERE pul.component.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();


        WorkflowDAO workflowDAO = new WorkflowDAO(em);
        workflowDAO.removeWorkflowConstraints(workspace);
        em.flush();

        // remove documents
        while (true) {
            List<DocumentMaster> documentMasters =
                    em.createQuery("SELECT d FROM DocumentMaster d WHERE d.workspace = :workspace", DocumentMaster.class)
                            .setParameter("workspace", workspace)
                            .setFirstResult(0)
                            .setMaxResults(batchSize)
                            .getResultList();
            if (documentMasters == null || documentMasters.isEmpty()) {
                break;
            }
            for (DocumentMaster documentMaster : documentMasters) {
                em.remove(documentMaster);
            }
            em.flush();
            em.clear();
        }

        // remove parts
        while (true) {
            List<PartMaster> partMasters =
                em.createQuery("SELECT p FROM PartMaster p WHERE p.workspace = :workspace", PartMaster.class)
                        .setParameter("workspace", workspace)
                        .setFirstResult(0)
                        .setMaxResults(batchSize)
                        .getResultList();
            if (partMasters == null || partMasters.isEmpty()) {
                break;
            }
            for (PartMaster partMaster : partMasters) {
                em.remove(partMaster);
            }
            em.flush();
            em.clear();
        }


        // Delete folders
        em.createQuery("UPDATE Folder f SET f.parentFolder = NULL WHERE f.parentFolder.completePath = :workspaceId OR f.parentFolder.completePath LIKE :pathToMatch")
                .setParameter("workspaceId", workspaceId)
                .setParameter("pathToMatch", pathToMatch)
                .executeUpdate();

        em.createQuery("DELETE FROM Folder f where f.completePath = :workspaceId OR f.completePath LIKE :pathToMatch")
                .setParameter("workspaceId", workspaceId)
                .setParameter("pathToMatch", pathToMatch)
                .executeUpdate();

        em.flush();

        List<WorkflowModel> workflowModels =
                em.createQuery("SELECT w FROM WorkflowModel w WHERE w.workspace = :workspace", WorkflowModel.class)
                        .setParameter("workspace", workspace).getResultList();

        WorkflowModelDAO workflowModelDAO = new WorkflowModelDAO(Locale.getDefault(), em);
        for (WorkflowModel w : workflowModels) {
            workflowModelDAO.removeWorkflowModelConstraints(w);
            em.remove(w);
        }
        em.flush();

        List<WorkspaceWorkflow> workspaceWorkflowList =
                em.createQuery("SELECT ww FROM WorkspaceWorkflow ww WHERE ww.workspace = :workspace", WorkspaceWorkflow.class)
                        .setParameter("workspace", workspace).getResultList();

        for (WorkspaceWorkflow ww : workspaceWorkflowList) {
            em.remove(ww);
        }
        em.flush();
        em.clear();

        // Tags subscriptions
        em.createQuery("DELETE FROM TagUserSubscription t where t.tag.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        em.createQuery("DELETE FROM TagUserGroupSubscription t where t.tag.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Tags
        em.createQuery("DELETE FROM Tag t where t.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();
        // Roles
        em.createQuery("DELETE FROM Role r where r.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // LOV
        em.createQuery("DELETE FROM ListOfValuesAttributeTemplate lovat where lovat.lov.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();
        em.createQuery("DELETE FROM ListOfValues lov where lov.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Query
        em.createQuery("DELETE FROM QueryContext qc where qc.workspaceId = :workspaceId")
                .setParameter("workspaceId", workspaceId).executeUpdate();
        em.createQuery("DELETE FROM Query q where q.author.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // WorkspaceUserGroupMembership
        em.createQuery("DELETE FROM WorkspaceUserGroupMembership w where w.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // WorkspaceUserMembership
        em.createQuery("DELETE FROM WorkspaceUserMembership w where w.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // ACL User groups
        em.createQuery("DELETE FROM ACLUserGroupEntry acl where acl.principal.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // User groups
        em.createQuery("DELETE FROM UserGroup u where u.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        List<UserGroup> userGroups =
                em.createQuery("SELECT u FROM UserGroup u WHERE u.workspace = :workspace", UserGroup.class)
                        .setParameter("workspace", workspace).getResultList();

        for (UserGroup u : userGroups) {
            u.setUsers(new HashSet<>());
            em.flush();
            em.remove(u);
        }
        em.flush();
        em.clear();

        // Imports
        em.createQuery("DELETE FROM Import i where i.user.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // ACL Users
        em.createQuery("DELETE FROM ACLUserEntry acl where acl.principal.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // Users
        em.createQuery("DELETE FROM User u where u.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        em.flush();

        // Webhooks
        em.createQuery("DELETE FROM Webhook w where w.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // WorkspaceBackOptions
        em.createQuery("DELETE FROM WorkspaceBackOptions n where n.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        // WorkspaceFrontOptions
        em.createQuery("DELETE FROM WorkspaceFrontOptions wo where wo.workspace = :workspace")
                .setParameter("workspace", workspace).executeUpdate();

        //If em has been cleared workspace is not attached anymore and thus remove will fail
        if (!em.contains(workspace)) {
            workspace = em.merge(workspace);
        }
        // Finally delete the workspace
        em.remove(workspace);

        // Delete workspace files
        storageManager.deleteWorkspaceFolder(workspaceId);

        em.flush();
        em.clear();

    }

    public List<Workspace> getAll() {
        return em.createNamedQuery("Workspace.findAllWorkspaces", Workspace.class)
                .getResultList();
    }
}

