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

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.core.security.UserGroupMapping;
import org.polarsys.eplmp.core.services.*;
import org.polarsys.eplmp.server.config.AuthConfig;
import org.polarsys.eplmp.server.config.ServerConfig;
import org.polarsys.eplmp.server.converters.ConversionOrder;
import org.polarsys.eplmp.server.converters.ConverterUtils;
import org.polarsys.eplmp.server.converters.serialization.JsonbSerializer;
import org.polarsys.eplmp.server.dao.PartRevisionDAO;

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
import java.security.Key;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;

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

    @Inject
    private IProductManagerLocal productService;
    @Inject
    private ITokenManagerLocal tokenManager;
    @Inject
    private AuthConfig authConfig;
    @Inject
    private IContextManagerLocal contextManager;
    @Inject
    private PartRevisionDAO partRevisionDAO;
    @Inject
    private IUserManagerLocal userService;
    @Inject
    private IBinaryStorageManagerLocal storageManager;
    @Inject
    private ServerConfig serverConfig;


    private static final Logger LOGGER = Logger.getLogger(ConverterBean.class.getName());
    private static final String PRODUCER_TOPIC = "CONVERT";
    private static final String CONF_PROPERTIES = "/org/polarsys/eplmp/server/converters/utils/conf.properties";
    private KafkaProducer<String, ConversionOrder> producer;
    private static final float[] RATIO = new float[]{1f, 0.6f, 0.2f};
    private static final Properties CONF = new Properties();

    static {
        try (InputStream inputStream = ConverterBean.class.getResourceAsStream(CONF_PROPERTIES)) {
            CONF.load(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    @PostConstruct
    public void init (){
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "kafka:9092");
        producerProperties.put("acks", "0");
        producerProperties.put("retries", "1");
        producerProperties.put("batch.size", "20971520");
        producerProperties.put("linger.ms", "33");
        producerProperties.put("max.request.size", "2097152");
        producerProperties.put("compression.type", "gzip");
        producerProperties.put("key.serializer", StringSerializer.class.getName());
        producerProperties.put("value.serializer", JsonbSerializer.class.getName());
        producerProperties.put("kafka.topic", PRODUCER_TOPIC);

        producer = new KafkaProducer<>(producerProperties);
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

        // Send message in kafka queue
        String token = generateUserToken();
        ConversionOrder conversionOrder = new ConversionOrder(partIterationKey, cadBinaryResource, token);
        producer.send(new ProducerRecord<>(PRODUCER_TOPIC, partIterationKey.toString(), conversionOrder));

    }

    @Override
    @RolesAllowed({UserGroupMapping.REGULAR_USER_ROLE_ID})
    public void handleConversionResultCallback(PartRevisionKey partRevisionKey, ConversionResult conversionResult) throws UserNotFoundException, WorkspaceNotFoundException, WorkspaceNotEnabledException, AccessRightException, EntityConstraintException, UserNotActiveException, PartRevisionNotFoundException, NotAllowedException, DocumentRevisionNotFoundException, ListOfValuesNotFoundException, PartUsageLinkNotFoundException, PartMasterNotFoundException, PartIterationNotFoundException {

        userService.checkWorkspaceWriteAccess(partRevisionKey.getWorkspaceId());

        PartRevision partRevision = partRevisionDAO.loadPartR(partRevisionKey);

        if(null == partRevision) {
            LOGGER.severe("Cannot find part revision");
            return;
        }

        PartIteration partIteration = partRevision.getLastIteration();
        PartIterationKey partIterationKey = partIteration.getKey();

        if(!partRevision.isCheckedOut()) {
            LOGGER.severe("Cannot proceed as the part is not checked out");
            productService.endConversion(partIterationKey, false);
            return;
        }

        Map<String, List<ConversionResult.Position>> componentPositionMap = conversionResult.getComponentPositionMap();
        Path convertedFile = conversionResult.getConvertedFile();

        // No CAD file and no position map
        if(convertedFile == null && componentPositionMap == null) {
            LOGGER.severe("Converted file and component position map are null, conversion failed \nError output: " + conversionResult.getErrorOutput());
            productService.endConversion(partIterationKey, false);
            return;
        }

        // Handle component map
        if (componentPositionMap != null && !syncAssembly(componentPositionMap, partIteration)) {
            LOGGER.severe("Failed to sync assembly, conversion failed");
            productService.endConversion(partIterationKey, false);
            return;
        }

        // Handle converted file
        if(convertedFile != null) {
            // Temp dir : conversionPath/UUID
            String uuid = convertedFile.getParent().getFileName().toString();
            String fileName = convertedFile.getFileName().toString();
            Path tempDir = Paths.get(serverConfig.getConversionsPath() + "/" + uuid);

            Path convertedFileAbsolute = Paths.get(tempDir.toAbsolutePath() + "/" + fileName);

            if (!handleConvertedFile(tempDir, convertedFileAbsolute, conversionResult, partIterationKey)) {
                LOGGER.severe("Failed to hand converted fileconversion failed");
                productService.endConversion(partIterationKey, false);
                return;
            }


            double[] box = conversionResult.getBox();

            if (decimate(convertedFileAbsolute, tempDir, RATIO)) {
                for (int i = 0; i < RATIO.length; i++) {
                    Path geometryFile = tempDir
                            .resolve(convertedFileAbsolute.getFileName().toString().replaceAll("\\.obj$", Math.round((RATIO[i] * 100)) + ".obj"));
                    saveGeometryFile(partIterationKey, i, geometryFile, box);
                }
            } else {
                // Copy the converted file if decimation failed,
                saveGeometryFile(partIterationKey, 0, convertedFileAbsolute, box);
            }

            // Save materials files as attached files
            for (Path material : conversionResult.getMaterials()) {
                Path absolutePath = Paths.get(tempDir + "/" + material.getFileName());
                saveAttachedFile(partIterationKey, absolutePath);
            }

            try {
                LOGGER.log(Level.FINE, "Conversion ended with success");
                productService.endConversion(partIterationKey, true);
            } catch (ApplicationException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        }
    }

    private String generateUserToken() {
        String login = contextManager.getCallerPrincipalLogin();
        Key key = authConfig.getJWTKey();
        UserGroupMapping mapping = new UserGroupMapping(login, UserGroupMapping.REGULAR_USER_ROLE_ID);
        return tokenManager.createAuthToken(key, mapping);
    }

    private boolean handleConvertedFile(Path tempDir, Path convertedFile, ConversionResult conversionResult, PartIterationKey pPartIPK) {

        double[] box = conversionResult.getBox();

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
