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

package org.polarsys.eplmp.server.geometry;


import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeometryParserTest {

    @Test
    public void getBoundingBoxTest() throws IOException {

        /*
        Three.js calculated box
        * Box3 {min: Vector3, max: Vector3}
        * min: Vector3 {
        * x: -7.60359001159668,
        * y: 0.17896200716495514,
        * z: -4.584129810333252
        * }
        * max: Vector3 {
        * x: 7.60359001159668,
        * y: 1.9977799654006958,
        * z: 3.896850109100342
        * }
        */

        Path path = Paths.get("src/test/resources/fake.obj");
        GeometryParser geometryParser = new GeometryParser(path);
        double[] doubles = geometryParser.calculateBox();

        assert doubles[0] == -7.60359001159668;
        assert doubles[1] == 0.0;
        assert doubles[2] == -4.584129810333252;
        assert doubles[3] == 7.60359001159668;
        assert doubles[4] == 1.9977799654006958;
        assert doubles[5] == 3.896850109100342;


    }

}
