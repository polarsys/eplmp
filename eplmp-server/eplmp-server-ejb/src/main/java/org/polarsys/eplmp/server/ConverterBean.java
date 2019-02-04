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
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.IBinaryStorageManagerLocal;
import org.polarsys.eplmp.core.services.IConverterManagerLocal;
import org.polarsys.eplmp.core.services.IProductManagerLocal;
import org.polarsys.eplmp.core.util.FileIO;
import org.polarsys.eplmp.server.converters.CADConverter;
import org.polarsys.eplmp.server.converters.CADConverter.ConversionException;
import org.polarsys.eplmp.server.converters.ConversionResult;
import org.polarsys.eplmp.server.converters.ConverterUtils;
import org.polarsys.eplmp.server.geometry.GeometryParser;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Asynchronous;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * CAD File converter
 *
 * @author Florent.Garin
 */
@DeclareRoles({UserGroupMapping.REGULAR_USER_ROLE_ID})
@RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
@Local(IConverterManagerLocal.class)
@Stateless(name = "ConverterBean")
public class ConverterBean implements IConverterManagerLocal {

    private List<CADConverter> converters = new ArrayList<>();

    @Inject
    private IProductManagerLocal productService;

    @Inject
    private IBinaryStorageManagerLocal storageManager;

    @Inject
    private BeanLocator beanLocator;

    @Inject
    private GeometryParser geometryParser;

    private static final String CONF_PROPERTIES = "/org/polarsys/eplmp/server/converters/utils/conf.properties";
    private static final Properties CONF = new Properties();
    private static final float[] RATIO = new float[]{1f, 0.6f, 0.2f};

    private static final Logger LOGGER = Logger.getLogger(ConverterBean.class.getName());

    static {
        try (InputStream inputStream = ConverterBean.class.getResourceAsStream(CONF_PROPERTIES)) {
            CONF.load(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    @PostConstruct
    void init() {
        // add external converters
        converters.addAll(beanLocator.search(CADConverter.class));
    }

    @Override
    @Asynchronous
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public void convertCADFileToOBJ(PartIterationKey partIterationKey, BinaryResource cadBinaryResource) {

        try {

            Conversion existingConversion = productService.getConversion(partIterationKey);

            // Don't try to convert if any conversion pending
            if (existingConversion != null && existingConversion.isPending()) {
                LOGGER.log(Level.SEVERE, "Conversion already running for part iteration {0}", partIterationKey);
                return;
            }

            // Clean old non pending conversions
            if (existingConversion != null) {
                LOGGER.log(Level.FINE, "Cleaning previous ended conversion");
                productService.removeConversion(partIterationKey);
            }

        } catch (ApplicationException e) {
            LOGGER.log(Level.SEVERE, null, e);
            return;
        }

        // Creates the new one
        try {
            LOGGER.log(Level.FINE, "Creating a new conversion");
            productService.createConversion(partIterationKey);
        } catch (ApplicationException e) {
            // Abort if any error (this should not happen though)
            LOGGER.log(Level.SEVERE, null, e);
            return;
        }

        CADConverter selectedConverter = selectConverter(cadBinaryResource);

        boolean succeed = false;

        if (selectedConverter != null) {
            try {
                succeed = doConversion(cadBinaryResource, selectedConverter, partIterationKey);
            } catch (StorageException e) {
                LOGGER.log(Level.WARNING, "Unable to read from storage", e);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            } catch (ConversionException e) {
                LOGGER.log(Level.WARNING, "Cannot convert " + cadBinaryResource.getName(), e);
            }
        } else {
            LOGGER.log(Level.WARNING, "No CAD converter able to handle " + cadBinaryResource.getName());
        }

        try {
            LOGGER.log(Level.FINE, "Conversion ended");
            productService.endConversion(partIterationKey, succeed);
        } catch (ApplicationException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }

    }

    private boolean doConversion(BinaryResource cadBinaryResource, CADConverter selectedConverter,
                                 PartIterationKey pPartIPK) throws IOException, StorageException, ConversionException {

        boolean result = false;

        UUID uuid = UUID.randomUUID();
        Path tempDir = Files.createDirectory(Paths.get("docdoku-" + uuid));
        Path tmpCadFile = tempDir.resolve(cadBinaryResource.getName().trim());

        // copy resource content to temp directory
        try (InputStream in = storageManager.getBinaryResourceInputStream(cadBinaryResource)) {
            Files.copy(in, tmpCadFile);
            // convert file
            try (ConversionResult conversionResult = selectedConverter.convert(tmpCadFile.toUri(), tempDir.toUri())) {
                Map<String, List<ConversionResult.Position>> componentPositionMap = conversionResult.getComponentPositionMap();
                if (componentPositionMap != null) {
                    result = syncAssembly(componentPositionMap, productService.getPartIteration(pPartIPK));
                }
                if (conversionResult.getConvertedFile() != null) {
                    result = handleConvertedFile(conversionResult, pPartIPK, tempDir);
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            } finally {
                deleteTempDirectory(tempDir);
            }
        } finally {
            Files.deleteIfExists(tempDir);
        }
        return result;
    }

    private void deleteTempDirectory(Path tempDir) {
        try (Stream<Path> s = Files.list(tempDir)) {
            s.forEach((path) -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    LOGGER.warning("Unable to delete " + path.getFileName());
                }
            });
        } catch (IOException e) {
            LOGGER.warning("Unable to delete " + tempDir.getFileName());
        }
    }

    private boolean handleConvertedFile(ConversionResult conversionResult, PartIterationKey pPartIPK, Path tempDir) {

        // manage converted file
        Path convertedFile = conversionResult.getConvertedFile();
        double[] box = geometryParser.calculateBox(convertedFile);

        if (decimate(convertedFile, tempDir, RATIO)) {
            String fileName = convertedFile.getFileName().toString();
            for (int i = 0; i < RATIO.length; i++) {
                Path geometryFile = tempDir
                        .resolve(fileName.replaceAll("\\.obj$", Math.round((RATIO[i] * 100)) + ".obj"));
                saveGeometryFile(pPartIPK, i, geometryFile, box);
            }
        } else {
            // Copy the converted file if decimation failed,
            saveGeometryFile(pPartIPK, 0, convertedFile, box);
        }

        // manage materials
        for (Path material : conversionResult.getMaterials()) {
            saveAttachedFile(pPartIPK, material);
        }

        return true;

    }

    /**
     * Update the current Part from the imported assembly description
     *
     * @param componentPositionMap Assembly description root component.
     * @param partToConvert        Current Part ID
     * @throws UserNotFoundException
     * @throws UserNotActiveException
     * @throws WorkspaceNotEnabledException
     * @throws WorkspaceNotFoundException
     * @throws EntityConstraintException
     * @throws NotAllowedException
     * @throws AccessRightException
     * @throws DocumentRevisionNotFoundException
     * @throws PartUsageLinkNotFoundException
     * @throws ListOfValuesNotFoundException
     * @throws PartMasterNotFoundException
     * @throws PartRevisionNotFoundException
     */
    private boolean syncAssembly(Map<String, List<ConversionResult.Position>> componentPositionMap, PartIteration partToConvert)
            throws UserNotFoundException, UserNotActiveException, WorkspaceNotEnabledException,
            WorkspaceNotFoundException, PartRevisionNotFoundException, PartMasterNotFoundException,
            ListOfValuesNotFoundException, PartUsageLinkNotFoundException, DocumentRevisionNotFoundException,
            AccessRightException, NotAllowedException, EntityConstraintException {

        boolean succeed = true;

        List<PartUsageLink> partUsageLinks = new ArrayList<>();
        for (Map.Entry<String, List<ConversionResult.Position>> entry : componentPositionMap.entrySet()) {
            // Component name
            String cadFileName = entry.getKey();
            // Linked component positioning
            List<ConversionResult.Position> positions = entry.getValue();
            // Retrieve this part master ID
            PartMaster partMaster = productService.findPartMasterByCADFileName(partToConvert.getWorkspaceId(),
                    cadFileName);

            if (partMaster != null) {
                PartUsageLink partUsageLink = new PartUsageLink();
                partUsageLink.setAmount(positions.size());
                partUsageLink.setComponent(partMaster);
                partUsageLink.setCadInstances(toCADInstances(positions));
                partUsageLinks.add(partUsageLink);
            } else {
                LOGGER.log(Level.WARNING, "No Part found for {0}", cadFileName);
                succeed = false;
            }
        }
        // Replace usage links (erase old structure)
        partToConvert.setComponents(partUsageLinks);
        productService.updatePartIteration(partToConvert.getKey(), partToConvert.getIterationNote(),
                partToConvert.getSource(), partToConvert.getComponents(), partToConvert.getInstanceAttributes(),
                partToConvert.getInstanceAttributeTemplates(), null, null, null);
        if (succeed) {
            LOGGER.log(Level.INFO, "Assembly synchronized");
        }
        return succeed;
    }

    List<CADInstance> toCADInstances(List<ConversionResult.Position> positions) {
        List<CADInstance> instances = new ArrayList<>();
        for (ConversionResult.Position p : positions) {
            double[] rm = DoubleStream
                    .concat(Arrays.stream(p.getRotationMatrix()[0]), DoubleStream
                            .concat(Arrays.stream(p.getRotationMatrix()[1]), Arrays.stream(p.getRotationMatrix()[2])))
                    .toArray();
            instances
                    .add(new CADInstance(new RotationMatrix(rm), p.getTranslation()[0], p.getTranslation()[1], p.getTranslation()[2]));
        }
        return instances;
    }

    private CADConverter selectConverter(BinaryResource cadBinaryResource) {
        String ext = FileIO.getExtension(cadBinaryResource.getName());
        for (CADConverter converter : converters) {
            try {
                if (converter.canConvertToOBJ(ext)) {
                    return converter;
                }
            } catch (Exception e) {
                // javax.ejb.CreateException can be thrown when static initialization fail inside plugin
                LOGGER.log(Level.SEVERE, "Something gone wrong with converter instantiation " + converter, e);
            }
        }
        return null;
    }

    private boolean decimate(Path file, Path tempDir, float[] ratio) {

        LOGGER.log(Level.INFO, "Decimate file in progress : {0}", ratio);

        // sanity checks
        String decimater = CONF.getProperty("decimater");
        Path executable = Paths.get(decimater);
        if (!executable.toFile().exists()) {
            LOGGER.log(Level.WARNING, "Cannot decimate file \"{0}\", decimater \"{1}\" is not available",
                    new Object[]{file.getFileName(), decimater});
            return false;
        }
        if (!Files.isExecutable(executable)) {
            LOGGER.log(Level.WARNING, "Cannot decimate file \"{0}\", decimater \"{1}\" has no execution rights",
                    new Object[]{file.getFileName(), decimater});
            return false;
        }

        boolean decimateSucceed = false;

        try {
            String[] args = {decimater, "-i", file.toAbsolutePath().toString(), "-o",
                    tempDir.toAbsolutePath().toString(), String.valueOf(ratio[0]), String.valueOf(ratio[1]),
                    String.valueOf(ratio[2])};

            LOGGER.log(Level.INFO, "Decimate command\n{0}", args);

            // Add redirectErrorStream, fix process hang up
            ProcessBuilder pb = new ProcessBuilder(args).redirectErrorStream(true);

            Process proc = pb.start();

            String stdOutput = ConverterUtils.inputStreamToString(proc.getInputStream());

            proc.waitFor();

            if (proc.exitValue() == 0) {
                LOGGER.log(Level.INFO, "Decimation done");
                decimateSucceed = true;
            } else {
                LOGGER.log(Level.SEVERE, "Decimation failed with code = {0} {1}", new Object[]{proc.exitValue(), stdOutput});
            }

        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Decimation failed for " + file.toAbsolutePath(), e);
        }
        return decimateSucceed;
    }

    private void saveGeometryFile(PartIterationKey partIPK, int quality, Path file, double[] box) {
        try {
            Geometry lod = (Geometry) productService.saveGeometryInPartIteration(partIPK, file.getFileName().toString(),
                    quality, Files.size(file), box);
            try (OutputStream os = storageManager.getBinaryResourceOutputStream(lod)) {
                Files.copy(file, os);
                LOGGER.log(Level.INFO, "geometry saved");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to get geometry file's size", e);
        } catch (UserNotFoundException | WorkspaceNotFoundException | WorkspaceNotEnabledException | CreationException
                | FileAlreadyExistsException | PartRevisionNotFoundException | NotAllowedException
                | UserNotActiveException | StorageException e) {
            LOGGER.log(Level.SEVERE, "Cannot save geometry to part iteration", e);
        }
    }

    private void saveAttachedFile(PartIterationKey partIPK, Path file) {
        try {
            BinaryResource binaryResource = productService.saveFileInPartIteration(partIPK,
                    file.getFileName().toString(), PartIteration.ATTACHED_FILES_SUBTYPE, Files.size(file));
            try (OutputStream os = storageManager.getBinaryResourceOutputStream(binaryResource)) {
                Files.copy(file, os);
                LOGGER.log(Level.INFO, "Attached file copied");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Unable to save attached file", e);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to get attached file's size", e);
        } catch (UserNotFoundException | WorkspaceNotFoundException | WorkspaceNotEnabledException | CreationException
                | FileAlreadyExistsException | PartRevisionNotFoundException | NotAllowedException
                | UserNotActiveException | StorageException e) {
            LOGGER.log(Level.SEVERE, "Cannot save attached file to part iteration", e);
        }
    }
}
