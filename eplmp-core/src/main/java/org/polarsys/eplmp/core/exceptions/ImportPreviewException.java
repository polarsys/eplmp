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

package org.polarsys.eplmp.core.exceptions;


/**
 * @author Morgan Guimard
 *
 * @version 2.5, 02/06/15
 * @since V2.5
 */
public class ImportPreviewException extends Exception {

    public ImportPreviewException() {
    }

    public ImportPreviewException(String s) {
        super(s);
    }

    public ImportPreviewException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ImportPreviewException(Throwable throwable) {
        super(throwable);
    }
}
