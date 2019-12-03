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

package org.polarsys.eplmp.server.rest.converters;

import org.dozer.DozerConverter;
import org.polarsys.eplmp.core.common.BinaryResource;
import org.polarsys.eplmp.core.product.ConversionResult;
import org.polarsys.eplmp.server.rest.dto.ConversionResultDTO;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Morgan Guimard
 */
public class ConversionResultDozerConverter extends DozerConverter<ConversionResult, ConversionResultDTO> {

    public ConversionResultDozerConverter() {
        super(ConversionResult.class, ConversionResultDTO.class);
    }

    @Override
    public ConversionResultDTO convertTo(ConversionResult conversionResult, ConversionResultDTO pConversionResultDTO) {
        return new ConversionResultDTO();
    }

    @Override
    public ConversionResult convertFrom(ConversionResultDTO conversionResultDTO, ConversionResult pConversionResult) {
        ConversionResult conversionResult = new ConversionResult();

        if( null != conversionResultDTO.getConvertedFile()) {
            Path convertedFile = Paths.get(conversionResultDTO.getConvertedFile());
            conversionResult.setConvertedFile(convertedFile);
        }

        conversionResult.setErrorOutput(conversionResultDTO.getErrorOutput());
        conversionResult.setStdOutput(conversionResultDTO.getStdOutput());

        if(null != conversionResultDTO.getMaterials()) {
            List<Path> materials = conversionResultDTO.getMaterials().stream().map(s -> Paths.get(s)).collect(Collectors.toList());
            conversionResult.setMaterials(materials);
        }

        if( null != conversionResultDTO.getComponentPositionMap()){
            Map<String, List<ConversionResultDTO.PositionDTO>> componentPositionMap = conversionResultDTO.getComponentPositionMap();
            Map<String, List<ConversionResult.Position>> positionMap = new HashMap<>();

            for(Map.Entry<String, List<ConversionResultDTO.PositionDTO>> entry : componentPositionMap.entrySet()){
                String key = entry.getKey();
                List<ConversionResultDTO.PositionDTO> value = entry.getValue();
                List<ConversionResult.Position> positions = value.stream().map(v -> new ConversionResult.Position(v.getRotationmatrix(), v.getTranslation())).collect(Collectors.toList());
                positionMap.put(key,positions);
            }
            conversionResult.setComponentPositionMap(positionMap);
        }

        return conversionResult;
    }
}
