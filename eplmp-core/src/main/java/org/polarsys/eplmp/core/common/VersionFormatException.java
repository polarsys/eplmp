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

package org.polarsys.eplmp.core.common;

/**
 *
 * @author Florent Garin
 */
public class VersionFormatException extends IllegalArgumentException {

    private int mErrorOffset;
    private String mInputString;


    public VersionFormatException(String pInputString, int pErrorOffset) {
        super("Error trying to parse the string \"" + pInputString + "\" at the character position " + pErrorOffset);
        mErrorOffset = pErrorOffset;
        mInputString = pInputString;
    }

    public int getErrorOffset() {
        return mErrorOffset;
    }

    public String getInputString() {
        return mInputString;
    }

}
