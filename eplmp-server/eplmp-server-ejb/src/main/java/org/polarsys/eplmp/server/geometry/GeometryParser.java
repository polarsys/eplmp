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

package org.polarsys.eplmp.server.geometry;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.polarsys.eplmp.server.converters.ConverterUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This GeometryParser class allows to compute geometric data for given file
 * It relies on Nashorn to compute the bounding box
 *
 * @author Morgan Guimard
 */
public class GeometryParser {

    private static final Logger LOGGER = Logger.getLogger(GeometryParser.class.getName());
    private final Path convertedFile;

    private final ScriptEngineManager factory = new ScriptEngineManager();
    private final ScriptEngine engine = factory.getEngineByName("nashorn");

    private final ClassLoader loader = Thread.currentThread().getContextClassLoader();


    public GeometryParser(Path convertedFile) {

        this.convertedFile = convertedFile;

        List<String> scripts = Arrays.asList(
                "org/polarsys/eplmp/server/converters/box-calculator.js",
                "META-INF/resources/webjars/three.js/r70/three.js"
        );

        scripts.stream().forEach((script) -> {
            try {
                loadFile(script);
            } catch (ScriptException e) {
                LOGGER.log(Level.SEVERE, "Cannot load script : " + script, e);
            }
        });

    }


    /**
     * Computes the bounding box of given 3D OBJ file
     *
     * @return an array of double representing the bounding box min and max values
     */
    public double[] calculateBox() throws IOException, ScriptException, NoSuchMethodException {
        final Invocable invocable = (Invocable) engine;
        final InputStream inputStream = Files.newInputStream(convertedFile);
        String data = ConverterUtils.inputStreamToString(inputStream);
        ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) invocable.invokeFunction("calculateBox", data);
        return scriptObjectMirror.to(double[].class);
    }


    private void loadFile(String script) throws ScriptException {
        final InputStream inputStream = loader.getResourceAsStream(script);
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        engine.eval(inputStreamReader);
    }
}
