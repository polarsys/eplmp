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

package org.polarsys.eplmp.server.util;

import javax.servlet.http.Part;
import java.io.*;
import java.util.Collection;

/**
 * @author Asmae Chadid on 12/01/15.
 */
public class PartImpl implements Part {

    private File fileToUpload;

    public PartImpl(File file){
        this.fileToUpload = file;
    }
    @Override
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(this.fileToUpload));
    }

    @Override
    public String getContentType() {
        return ResourceUtil.FILE_TYPE;
    }

    @Override
    public String getName() {
        return this.fileToUpload.getName();
    }

    @Override
    public String getSubmittedFileName() {
        return fileToUpload.getPath();
    }

    @Override
    public long getSize() {
        return fileToUpload.getTotalSpace();
    }

    @Override
    public void write(String s) throws IOException {

    }

    @Override
    public void delete() throws IOException {

    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }
}
