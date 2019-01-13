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

package org.polarsys.eplmp.core.common;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Florent Garin
 */
public class Version implements Serializable, Comparable<Version>, Cloneable {

    private List<VersionUnit> mVersionUnits;

    public enum VersionUnit {
        A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z
    }

    public Version() {
        mVersionUnits = new LinkedList<>();
        mVersionUnits.add(VersionUnit.A);
    }

    public Version(String pStringVersion) {
        mVersionUnits = new LinkedList<>();

        for (int i = 0; i < pStringVersion.length(); i++) {
            try {
                mVersionUnits.add(VersionUnit.valueOf(pStringVersion.charAt(i) + ""));
            } catch (IllegalArgumentException pIAEx) {
                throw new VersionFormatException(pStringVersion, i);
            }
        }

    }

    public void increase() {

        for (int i = mVersionUnits.size() - 1; i > -1; i--) {
            VersionUnit unit = mVersionUnits.get(i);
            if (unit == VersionUnit.Z) {
                if (i == 0) {
                    mVersionUnits.add(0, VersionUnit.A);
                }
            } else {
                int ordinal = unit.ordinal();
                mVersionUnits.set(i, VersionUnit.values()[++ordinal]);
                break;
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder stringVersion = new StringBuilder();
        for (VersionUnit unit : mVersionUnits) {
            stringVersion.append(unit.toString());
        }

        return stringVersion.toString();
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof Version)) {
            return false;
        }
        Version version = (Version) pObj;
        //because of bug #6277781 Serialization of Enums over IIOP is broken.
        //we compare the string representation.
        //without this bug regular implementation would be: 
        //mVersionUnits.equals(version.mVersionUnits);
        return toString().equals(version.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public int compareTo(Version pVersion) {
        return toString().compareTo(pVersion.toString());
    }

    /**
     * perform a deep clone operation
     */
    @Override
    public Version clone() {
        Version clone;
        try {
            clone = (Version) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        //perform a deep copy
        clone.mVersionUnits = new LinkedList<>(mVersionUnits);
        return clone;
    }
}
