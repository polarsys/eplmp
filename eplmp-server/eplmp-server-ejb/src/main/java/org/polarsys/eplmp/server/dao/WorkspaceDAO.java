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
import org.polarsys.eplmp.core.common.UserGroup;
import org.polarsys.eplmp.core.common.Workspace;
import org.polarsys.eplmp.core.configuration.PathDataIteration;
import org.polarsys.eplmp.core.configuration.PathDataMaster;
import org.polarsys.eplmp.core.configuration.ProductInstanceIteration;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.document.DocumentLink;
import org.polarsys.eplmp.core.document.DocumentMaster;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.meta.Folder;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.product.PartMaster;
import org.polarsys.eplmp.core.product.PartUsageLink;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.workflow.WorkflowModel;
import org.polarsys.eplmp.core.workflow.WorkspaceWorkflow;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Stateless(name = "WorkspaceDAO")
public class WorkspaceDAO {

    public static final String WORKSPACE = "workspace";
    public static final String WORKSPACE_ID = "workspaceId";
    @PersistenceContext
    private EntityManager em;

    @Inject
    private EffectivityDAO effectivityDAO;

    @Inject
    private FolderDAO folderDAO;

    @Inject
    private WorkflowModelDAO workflowModelDAO;

    @Inject
    private IBinaryStorageManagerLocal storageManager;

    @Inject
    private WorkflowDAO workflowDAO;

    private Locale mLocale;

    public WorkspaceDAO() {
        mLocale = Locale.getDefault();
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
            folderDAO.createFolder(mLocale, new Folder(pWorkspace.getId()));
        } catch (EntityExistsException pEEEx) {
            throw new WorkspaceAlreadyExistsException(mLocale, pWorkspace);
        } catch (PersistenceException pPEx) {
            //EntityExistsException is case sensitive
            //whereas MySQL is not thus PersistenceException could be
            //thrown instead of EntityExistsException
            throw new CreationException(mLocale);
        }
    }

    public void createWorkspace(Locale pLocale, Workspace pWorkspace) throws WorkspaceAlreadyExistsException, CreationException, FolderAlreadyExistsException {
        mLocale = pLocale;
        createWorkspace(pWorkspace);
    }


    public Workspace loadWorkspace(String pID) throws WorkspaceNotFoundException {
        Workspace workspace = em.find(Workspace.class, pID);
        if (workspace == null) {
            throw new WorkspaceNotFoundException(mLocale, pID);
        } else {
            return workspace;
        }
    }

    public Workspace loadWorkspace(Locale pLocale, String pID) throws WorkspaceNotFoundException {
        mLocale = pLocale;
        return loadWorkspace(pID);
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

    public void removeWorkspace(Workspace workspace) throws StorageException {

        String workspaceId = workspace.getId();
        String pathToMatch = workspaceId.replace("_", "\\_").replace("%", "\\%") + "/%";
        final int batchSize = 1000;
        int i;

        // SharedEntities
        em.createQuery("DELETE FROM SharedEntity s where s.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Subscriptions
        em.createQuery("DELETE FROM IterationChangeSubscription s where s.observedDocumentRevisionWorkspaceId = :workspaceId")
                .setParameter(WORKSPACE_ID, workspaceId).executeUpdate();

        em.createQuery("DELETE FROM StateChangeSubscription s where s.observedDocumentRevisionWorkspaceId = :workspaceId")
                .setParameter(WORKSPACE_ID, workspaceId).executeUpdate();

        // BaselinedPart
        em.createQuery("DELETE FROM BaselinedPart bp where bp.targetPart.partRevision.partMasterWorkspaceId = :workspaceId")
                .setParameter(WORKSPACE_ID, workspaceId).executeUpdate();

        // BaselinedDocument
        em.createQuery("DELETE FROM BaselinedDocument bd where bd.targetDocument.documentRevision.documentMasterWorkspaceId = :workspaceId")
                .setParameter(WORKSPACE_ID, workspaceId).executeUpdate();

        i = 0;
        while (true) {
        List<ProductInstanceIteration> productInstanceIterations =
                em.createQuery("SELECT pii FROM ProductInstanceIteration pii WHERE pii.productInstanceMaster.instanceOf.workspace = :workspace", ProductInstanceIteration.class)
                    .setParameter(WORKSPACE, workspace)
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

        // ProductInstances
        em.createQuery("DELETE FROM ProductInstanceIteration pii where pii.productInstanceMaster.instanceOf.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();
        em.createQuery("DELETE FROM ProductInstanceMaster pim where pim.instanceOf.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // ProductBaselines
        em.createQuery("DELETE FROM ProductBaseline b where b.configurationItem.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // ProductConfigurations
        em.createQuery("DELETE FROM ProductConfiguration c where c.configurationItem.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // DocumentBaselines
        em.createQuery("DELETE FROM DocumentBaseline b where b.author.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // PartCollection
        em.createQuery("DELETE FROM PartCollection pc where pc.author.workspaceId = :workspaceId")
                .setParameter(WORKSPACE_ID, workspaceId).executeUpdate();

        // DocumentCollection
        em.createQuery("DELETE FROM DocumentCollection dc where dc.author.workspaceId = :workspaceId")
                .setParameter(WORKSPACE_ID, workspaceId).executeUpdate();

        // Layers
        em.createQuery("DELETE FROM Layer l where l.configurationItem.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();
        // Markers
        em.createQuery("DELETE FROM Marker m where m.author.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Reset effectivities constraints on configuration items
        effectivityDAO.removeEffectivityConstraints(workspaceId);
        em.flush();
        em.clear();

        // ConfigurationItem
        em.createQuery("DELETE FROM ConfigurationItem c where c.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // DocumentMasterTemplate
        em.createQuery("DELETE FROM DocumentMasterTemplate d where d.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // PartMasterTemplate
        em.createQuery("DELETE FROM PartMasterTemplate p where p.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Conversions
        em.createQuery("DELETE FROM Conversion c where c.partIteration.partRevision.partMaster.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Notifications
        em.createQuery("DELETE FROM ModificationNotification m where m.impactedPart.partRevision.partMaster.workspace = :workspace or m.modifiedPart.partRevision.partMaster.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Change order
        em.createQuery("DELETE FROM ChangeOrder c where c.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Change requests
        em.createQuery("DELETE FROM ChangeRequest c where c.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Change issues
        em.createQuery("DELETE FROM ChangeIssue c where c.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Change issues / requests
        em.createQuery("DELETE FROM Milestone m where m.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        i = 0;
        while (true) {
            List<DocumentIteration> documentIterations =
                    em.createQuery("SELECT d FROM DocumentIteration d WHERE d.documentRevision.documentMaster.workspace = :workspace", DocumentIteration.class)
                            .setParameter(WORKSPACE, workspace)
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
                        .setParameter(WORKSPACE, workspace)
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
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Clear all part usage links
        em.createQuery("DELETE FROM PartUsageLink pul WHERE pul.component.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        workflowDAO.removeWorkflowConstraints(workspace);
        em.flush();

        // remove documents
        while (true) {
            List<DocumentMaster> documentMasters =
                    em.createQuery("SELECT d FROM DocumentMaster d WHERE d.workspace = :workspace", DocumentMaster.class)
                            .setParameter(WORKSPACE, workspace)
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
                        .setParameter(WORKSPACE, workspace)
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
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter("pathToMatch", pathToMatch)
                .executeUpdate();

        em.createQuery("DELETE FROM Folder f where f.completePath = :workspaceId OR f.completePath LIKE :pathToMatch")
                .setParameter(WORKSPACE_ID, workspaceId)
                .setParameter("pathToMatch", pathToMatch)
                .executeUpdate();

        em.flush();

        List<WorkflowModel> workflowModels =
                em.createQuery("SELECT w FROM WorkflowModel w WHERE w.workspace = :workspace", WorkflowModel.class)
                        .setParameter(WORKSPACE, workspace).getResultList();

        for (WorkflowModel w : workflowModels) {
            workflowModelDAO.removeWorkflowModelConstraints(w);
            em.remove(w);
        }
        em.flush();

        List<WorkspaceWorkflow> workspaceWorkflowList =
                em.createQuery("SELECT ww FROM WorkspaceWorkflow ww WHERE ww.workspace = :workspace", WorkspaceWorkflow.class)
                        .setParameter(WORKSPACE, workspace).getResultList();

        for (WorkspaceWorkflow ww : workspaceWorkflowList) {
            em.remove(ww);
        }
        em.flush();
        em.clear();

        // Tags subscriptions
        em.createQuery("DELETE FROM TagUserSubscription t where t.tag.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        em.createQuery("DELETE FROM TagUserGroupSubscription t where t.tag.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Tags
        em.createQuery("DELETE FROM Tag t where t.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();
        // Roles
        em.createQuery("DELETE FROM Role r where r.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // LOV
        em.createQuery("DELETE FROM ListOfValuesAttributeTemplate lovat where lovat.lov.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();
        em.createQuery("DELETE FROM ListOfValues lov where lov.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Query
        em.createQuery("DELETE FROM QueryContext qc where qc.workspaceId = :workspaceId")
                .setParameter(WORKSPACE_ID, workspaceId).executeUpdate();
        em.createQuery("DELETE FROM Query q where q.author.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // WorkspaceUserGroupMembership
        em.createQuery("DELETE FROM WorkspaceUserGroupMembership w where w.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // WorkspaceUserMembership
        em.createQuery("DELETE FROM WorkspaceUserMembership w where w.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // ACL User groups
        em.createQuery("DELETE FROM ACLUserGroupEntry acl where acl.principal.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // User groups
        em.createQuery("DELETE FROM UserGroup u where u.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        List<UserGroup> userGroups =
                em.createQuery("SELECT u FROM UserGroup u WHERE u.workspace = :workspace", UserGroup.class)
                        .setParameter(WORKSPACE, workspace).getResultList();

        for (UserGroup u : userGroups) {
            u.setUsers(new HashSet<>());
            em.flush();
            em.remove(u);
        }
        em.flush();
        em.clear();

        // Imports
        em.createQuery("DELETE FROM Import i where i.user.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // ACL Users
        em.createQuery("DELETE FROM ACLUserEntry acl where acl.principal.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // Users
        em.createQuery("DELETE FROM User u where u.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        em.flush();

        // Webhooks
        em.createQuery("DELETE FROM Webhook w where w.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // WorkspaceBackOptions
        em.createQuery("DELETE FROM WorkspaceBackOptions n where n.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

        // WorkspaceFrontOptions
        em.createQuery("DELETE FROM WorkspaceFrontOptions wo where wo.workspace = :workspace")
                .setParameter(WORKSPACE, workspace).executeUpdate();

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

