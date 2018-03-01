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

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This GeometryParser class allows to compute geometric data for given file
 *
 * @author Morgan Guimard
 */
public class GeometryParser {


    private Path path;
    private static final Logger LOGGER = Logger.getLogger(GeometryParser.class.getName());

    public GeometryParser(Path convertedFile) {
        path = convertedFile;
    }


    /**
     * Computes the bounding box of given 3D OBJ file
     *
     * @return an array of double representing the bounding box min and max values
     */
    public double[] calculateBox() {

        double[] result = new double[6];
        int i = 0;
        double xMin = 0.0, xMax = 0.0, yMin = 0.0, yMax = 0.0, zMin = 0.0, zMax = 0.0;

        try (InputStream inputStream = new FileInputStream(path.toFile())) {

            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
            FloatBuffer vertices = ObjData.getVertices(obj);

            while (vertices.hasRemaining()) {
                float v = vertices.get();
                if (i == 0) {
                    if (v < xMin) {
                        xMin = v;
                    } else if (v > xMax) {
                        xMax = v;
                    }
                    i++;
                } else if (i == 1) {
                    if (v < yMin) {
                        yMin = v;
                    } else if (v > yMax) {
                        yMax = v;
                    }
                    i++;
                } else if (i == 2) {
                    if (v < zMin) {
                        zMin = v;
                    } else if (v > zMax) {
                        zMax = v;
                    }
                    i = 0;
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cannot parse vertices from obj", e);
        }

        result[0] = xMin;
        result[1] = yMin;
        result[2] = zMin;
        result[3] = xMax;
        result[4] = yMax;
        result[5] = zMax;
        return result;
    }

}
