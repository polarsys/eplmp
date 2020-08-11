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

package org.polarsys.server.importers.attributes;

/**
 * Exception raised when comments are not present or in a wrong order
 *
 * @author Morgan Guimard
 * @version 1.0.0
 * @since 05/04/16
 */
public class WrongCellCommentException extends Exception {
    public WrongCellCommentException(){
        super();
    }
}
