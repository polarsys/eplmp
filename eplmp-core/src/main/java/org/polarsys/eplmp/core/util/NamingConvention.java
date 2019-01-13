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

package org.polarsys.eplmp.core.util;

/**
 * Document IDs, part numbers and other object identifiers
 * and must conform to naming conventions.
 *
 * This class checks that these conventions are respected.
 *
 * @author Florent Garin
 */
public class NamingConvention {

    private static final char[] DOCUMENTS_FORBIDDEN_CHARS = {
            '%'
    };

    private static final char[] PARTS_FORBIDDEN_CHARS = {
            '%'
    };

    private static final char[] FORBIDDEN_CHARS = {
            '$','&','+',',','/',':',';','=','?','@','"', '<', '>', '#','%','{','}','|','\\','^','~','[',']',' ', '*','`'
    };

    private static final char[] FORBIDDEN_CHARS_MASK = {
            '$','&','+',',','/',':',';','=','?','@','"', '<', '>','%','{','}','|','\\','^','~','[',']',' ','`'
    };

    private static final char[] FORBIDDEN_CHARS_FILE = {
            '/', '\\', ':', '*', '?','"', '<', '>', '|', '~', '#',
            '^', '%', '{', '}','&','$','+',',', ';', '@', '\'', '`','=', '[', ']'
    };
    
    private static final String[] FORBIDDEN_NAMES = {"",".."};

    private NamingConvention() {
    }
    
    private static boolean forbidden(char pChar, char[] forbiddenChars) {
        for (char forbiddenChar : forbiddenChars) {
            if (pChar == forbiddenChar) {
                return true;
            }
        }
        return false;
    }

    private static boolean correct(String pShortName, char[] forbiddenChars) {
        if (pShortName == null) {
            return false;
        }

        for (String forbiddenName : FORBIDDEN_NAMES) {
            if (pShortName.equals(forbiddenName)) {
                return false;
            }
        }

        for (int i = 0; i < pShortName.length(); i++) {
            if (forbidden(pShortName.charAt(i), forbiddenChars)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean correct(String pShortName) {
        return correct(pShortName, FORBIDDEN_CHARS);
    }


    public static boolean correctNameFile(String pShortName) {
        return correct(pShortName, FORBIDDEN_CHARS_FILE);
    }
    public static boolean correctNameMask(String mask) {
        return correct(mask, FORBIDDEN_CHARS_MASK);
    }

    public static boolean correctDocumentId(String documentId) {
        return correct(documentId, DOCUMENTS_FORBIDDEN_CHARS);
    }

    public static boolean correctPartNumber(String documentId) {
        return correct(documentId, PARTS_FORBIDDEN_CHARS);
    }
}
