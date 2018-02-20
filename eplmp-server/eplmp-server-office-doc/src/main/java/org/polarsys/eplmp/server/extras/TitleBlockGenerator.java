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

package org.polarsys.eplmp.server.extras;

import org.polarsys.eplmp.core.document.DocumentIteration;
import org.polarsys.eplmp.core.product.PartIteration;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * This class define the default pdf generation for both part and document.
 * This behaviour can be overridden.
 *
 * @author Morgan Guimard
 *
 * @see PartTitleBlockData
 * @see DocumentTitleBlockData
 */
public abstract class TitleBlockGenerator {

    private static final Logger LOGGER = Logger.getLogger(TitleBlockGenerator.class.getName());

    /**
     * Generate a block title pdf page and add it to the pdf given in the input stream
     */
    public static InputStream addBlockTitleToPDF(InputStream pdfDocument, DocumentIteration docI, Locale pLocale) throws IOException {
        DocumentTitleBlockData data =
                new DocumentTitleBlockData(docI, pLocale);
        return merge(pdfDocument, new TitleBlockWriter(data).createTitleBlock());
    }

    /**
     * Generate a block title pdf page and add it to the pdf given in the input stream
     */
    public static InputStream addBlockTitleToPDF(InputStream pdfDocument, PartIteration partIteration, Locale pLocale) throws IOException {
        PartTitleBlockData data =
                new PartTitleBlockData(partIteration, pLocale);
        return merge(pdfDocument, new TitleBlockWriter(data).createTitleBlock());
    }

    public static InputStream merge(InputStream originalPDF, byte[] titleBlock) throws IOException {

        ByteArrayOutputStream tempOutStream = new ByteArrayOutputStream();
        PDFMergerUtility mergedDoc = new PDFMergerUtility();

        InputStream titleBlockStream = new ByteArrayInputStream(titleBlock);

        mergedDoc.addSource(titleBlockStream);
        mergedDoc.addSource(originalPDF);

        mergedDoc.setDestinationStream(tempOutStream);
        mergedDoc.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

        return new ByteArrayInputStream(tempOutStream.toByteArray());


    }
}
