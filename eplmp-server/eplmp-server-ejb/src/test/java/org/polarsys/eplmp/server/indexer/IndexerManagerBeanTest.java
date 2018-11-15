/*******************************************************************************
 * Copyright (c) 2017 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/
package org.polarsys.eplmp.server.indexer;

import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.cluster.Health;
import io.searchbox.core.Bulk;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Update;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.Mock;
import org.polarsys.eplmp.core.common.Account;
import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.exceptions.AccountNotFoundException;
import org.polarsys.eplmp.core.exceptions.IndexerNotAvailableException;
import org.polarsys.eplmp.core.exceptions.IndexerRequestException;
import org.polarsys.eplmp.core.exceptions.WorkspaceAlreadyExistsException;
import org.polarsys.eplmp.core.product.PartIteration;
import org.polarsys.eplmp.core.services.IAccountManagerLocal;
import org.polarsys.eplmp.core.services.INotifierLocal;
import org.powermock.core.classloader.annotations.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import static org.mockito.Mockito.times;

public class IndexerManagerBeanTest {

    @InjectMocks
    private IndexerManagerBean indexerManagerBean;

    @Mock
    private JestClient esClient;

    @Mock
    private IndexManagerBean indexManager;

    @Mock
    private IAccountManagerLocal accountManager;

    @Mock
    private INotifierLocal mailer;

    @Mock
    private IndexerQueryBuilder indexerQueryBuilder;

    private String workspaceId = "wks";

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void pingTest() throws IOException {
        Health health = new Health.Builder().build();
        JestResult result = new JestResult(new Gson());
        Mockito.when(esClient.execute(health))
                .thenReturn(result);

        result.setSucceeded(false);
        Assert.assertFalse(indexerManagerBean.ping());

        result.setSucceeded(true);
        Assert.assertTrue(indexerManagerBean.ping());

        Mockito.when(esClient.execute(health)).thenThrow(new IOException());
        Assert.assertFalse(indexerManagerBean.ping());
    }

    @Test
    public void createWorkspaceIndexTest() throws IndexerNotAvailableException {
        Mockito.when(indexManager.indicesExist(workspaceId)).thenReturn(true);

        try {
            indexerManagerBean.createWorkspaceIndex(workspaceId);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e.getClass().isAssignableFrom(WorkspaceAlreadyExistsException.class));
        }

        Mockito.when(indexManager.indicesExist(workspaceId)).thenReturn(false);

        try {
            indexerManagerBean.createWorkspaceIndex(workspaceId);
            Mockito.verify(indexManager, times(1)).createIndices(workspaceId);
        } catch (WorkspaceAlreadyExistsException | IndexerNotAvailableException | IndexerRequestException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void deleteWorkspaceIndexTest() throws IndexerNotAvailableException, AccountNotFoundException, IndexerRequestException {
        Account account = new Account("foo", "name", "mail", "en", new Date(), "CET");
        Mockito.when(accountManager.getMyAccount()).thenReturn(account);
        Mockito.doNothing().when(indexManager).deleteIndices(workspaceId);

        try {
            indexerManagerBean.deleteWorkspaceIndex(workspaceId);
            Mockito.verify(indexManager, times(1)).deleteIndices(workspaceId);
        } catch (Exception e) {
            Assert.assertTrue(e.getClass().isAssignableFrom(WorkspaceAlreadyExistsException.class));
        }

        Mockito.doThrow(new IndexerNotAvailableException()).when(indexManager).deleteIndices(workspaceId);

        indexerManagerBean.deleteWorkspaceIndex(workspaceId);
        Mockito.verify(mailer, times(1)).sendWorkspaceIndexationFailure(Matchers.any(Account.class), Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void indexDocumentIterationTest() throws IndexerNotAvailableException, AccountNotFoundException, IndexerRequestException {
        DocumentResult documentResult = new DocumentResult(new Gson());
        DocumentIteration documentIteration = new DocumentIteration();
        Mockito.when(indexManager.executeUpdate(Matchers.any(Update.Builder.class)))
            .thenReturn(documentResult);
        indexerManagerBean.indexDocumentIteration(documentIteration);
        Mockito.verify(indexManager,times(1)).executeUpdate(Matchers.any(Update.Builder.class));

    }

    @Test
    public void indexPartIterationTest() throws IndexerNotAvailableException, AccountNotFoundException, IndexerRequestException {
        DocumentResult documentResult = new DocumentResult(new Gson());
        PartIteration partIteration = new PartIteration();
        Mockito.when(indexManager.executeUpdate(Matchers.any(Update.Builder.class)))
                .thenReturn(documentResult);
        indexerManagerBean.indexPartIteration(partIteration);
        Mockito.verify(indexManager,times(1)).executeUpdate(Matchers.any(Update.Builder.class));

    }

    @Test
    public void indexDocumentIterationsTest() throws IndexerRequestException, IndexerNotAvailableException {
        List<DocumentIteration> documentIterations = new ArrayList<>();
        documentIterations.add(new DocumentIteration());
        documentIterations.add(new DocumentIteration());

        indexerManagerBean.indexDocumentIterations(documentIterations);

        Mockito.verify(indexManager,times(1))
                .sendBulk(Matchers.any(Bulk.Builder.class));

    }

    @Test
    public void indexPartIterationsTest() throws IndexerRequestException, IndexerNotAvailableException {
        List<PartIteration> partIterations = new ArrayList<>();
        partIterations.add(new PartIteration());
        partIterations.add(new PartIteration());

        indexerManagerBean.indexPartIterations(partIterations);

        Mockito.verify(indexManager,times(1))
                .sendBulk(Matchers.any(Bulk.Builder.class));

    }
}
