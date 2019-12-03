/*******************************************************************************
 * Copyright (c) 2017-2019 DocDoku.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * Contributors:
 * DocDoku - initial API and implementation
 *******************************************************************************/
package org.polarsys.eplmp.server;

import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.exceptions.*;
import org.polarsys.eplmp.core.product.*;
import org.polarsys.eplmp.server.converters.ConverterUtils;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.DoubleStream;

/**
 * @author Morgan Guimard
 */
@ApplicationScoped
@Singleton
public class ConversionResultListener {
/*
    @Inject
    private IProductManagerLocal productService;

    @Inject
    private GeometryParser geometryParser;

    @Inject
    private IBinaryStorageManagerLocal storageManager;

    private final static Logger LOGGER = Logger.getLogger(ConversionResultListener.class.getName());

    private KafkaConsumer<String, ConversionResultWrapper> consumer;

    private static final String CONF_PROPERTIES = "/org/polarsys/eplmp/server/converters/utils/conf.properties";
    private static final Properties CONF = new Properties();
    private static final float[] RATIO = new float[]{1f, 0.6f, 0.2f};

    static {
        try (InputStream inputStream = ConverterBean.class.getResourceAsStream(CONF_PROPERTIES)) {
            CONF.load(inputStream);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    @PostConstruct
    public void init() {
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", "kafka:9092");
        consumerProperties.put("kafka.topic", "RESULT");
        consumerProperties.put("compression.type", "gzip");
        consumerProperties.put("key.deserializer", StringDeserializer.class.getName());
        consumerProperties.put("value.deserializer", ConversionResultDeserializer.class.getName());
        consumerProperties.put("max.partition.fetch.bytes", "2097152");
        consumerProperties.put("max.poll.records", "500");
        consumerProperties.put("group.id", "my-group");
        consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Collections.singletonList(consumerProperties.getProperty("kafka.topic")));
    }

    @Asynchronous
    public void start() {
        while (true) {
            ConsumerRecords<String, ConversionResultWrapper> records = consumer.poll(Duration.ofDays(2));
            records.forEach(this::handleRecord);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void handleRecord(ConsumerRecord<String, ConversionResultWrapper> record) {
        ConversionResultWrapper resultWrapper = record.value();
        Callback cb = new Callback();
        try {
            cb.handleResult(resultWrapper);
        } catch (ApplicationException e) {
            cb.handleError(resultWrapper, e);
        }
    }

    @RunAs(UserGroupMapping.REGULAR_USER_ROLE_ID)
    private class Callback {

        private void handleError(ConversionResultWrapper resultWrapper, ApplicationException e) {
            try {
                LOGGER.log(Level.FINE, "Conversion ended with error ");
                productService.endConversion(resultWrapper.getPartIterationKey(), false);
            } catch (ApplicationException e1) {
                LOGGER.log(Level.SEVERE, null, e1);
            }
        }

        private void handleResult(ConversionResultWrapper conversionResultWrapper) throws UserNotActiveException, PartRevisionNotFoundException, WorkspaceNotFoundException, UserNotFoundException, PartIterationNotFoundException, NotAllowedException, AccessRightException, WorkspaceNotEnabledException, EntityConstraintException, ListOfValuesNotFoundException, PartMasterNotFoundException, PartUsageLinkNotFoundException, DocumentRevisionNotFoundException {
            boolean result = false;

            ConversionResult conversionResult = conversionResultWrapper.getConversionResult();
            PartIterationKey pPartIPK = conversionResultWrapper.getPartIterationKey();
            Path tempDir = conversionResultWrapper.getTempDir();

            Map<String, List<ConversionResult.Position>> componentPositionMap = conversionResult.getComponentPositionMap();

            if (componentPositionMap != null) {
                result = syncAssembly(componentPositionMap, productService.getPartIteration(pPartIPK));
            }

            if (conversionResult.getConvertedFile() != null) {
                result = handleConvertedFile(conversionResult, pPartIPK, tempDir);
            }

            try {
                LOGGER.log(Level.FINE, "Conversion ended");
                productService.endConversion(pPartIPK, result);
            } catch (ApplicationException e) {
                LOGGER.log(Level.SEVERE, null, e);
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

*/
}
