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

package org.polarsys.eplmp.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Morgan Guimard
 */
public class HashUtils {
    public static String md5Sum(String pText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return digest(pText, "MD5");
    }

    public static String sha256Sum(String pText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return digest(pText,"SHA-256");
    }

    /**
     * Computes a hash function using the supplied algorithm and s
     * the result as a string representation using
     * hex as the encoding and UTF-8 as the character set.
     *
     * @param pText
     * @param pAlgorithm
     *
     * @return hashed string result.
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String digest(String pText, String pAlgorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] digest = MessageDigest.getInstance(pAlgorithm).digest(pText.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();
        for (byte aDigest : digest) {
            String hex = Integer.toHexString(0xFF & aDigest);
            if (hex.length() == 1) {
                hexString.append("0").append(hex);
            } else {
                hexString.append(hex);
            }
        }
        return hexString.toString();
    }
}
