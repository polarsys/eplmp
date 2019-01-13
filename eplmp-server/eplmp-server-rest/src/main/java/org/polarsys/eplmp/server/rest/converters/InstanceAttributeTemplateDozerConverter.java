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
import org.polarsys.eplmp.core.meta.DefaultAttributeTemplate;
import org.polarsys.eplmp.core.meta.InstanceAttributeTemplate;
import org.polarsys.eplmp.core.meta.ListOfValuesAttributeTemplate;
import org.polarsys.eplmp.core.product.PartNumberAttributeTemplate;
import org.polarsys.eplmp.server.rest.dto.InstanceAttributeTemplateDTO;
import org.polarsys.eplmp.server.rest.dto.InstanceAttributeType;

/**
 * @author Florent Garin
 */
public class InstanceAttributeTemplateDozerConverter extends DozerConverter<InstanceAttributeTemplate, InstanceAttributeTemplateDTO> {

    public InstanceAttributeTemplateDozerConverter() {
        super(InstanceAttributeTemplate.class, InstanceAttributeTemplateDTO.class);
    }


    @Override
    public InstanceAttributeTemplateDTO convertTo(InstanceAttributeTemplate instanceAttributeTemplate, InstanceAttributeTemplateDTO dto) {
        if(dto==null)
            dto=new InstanceAttributeTemplateDTO();

        dto.setLocked(instanceAttributeTemplate.isLocked());
        dto.setName(instanceAttributeTemplate.getName());
        dto.setMandatory(instanceAttributeTemplate.isMandatory());
        if(instanceAttributeTemplate instanceof DefaultAttributeTemplate){
            DefaultAttributeTemplate defaultIA = (DefaultAttributeTemplate)instanceAttributeTemplate;
            dto.setAttributeType(InstanceAttributeType.valueOf(defaultIA.getAttributeType().name()));

        }else if(instanceAttributeTemplate instanceof ListOfValuesAttributeTemplate){
            ListOfValuesAttributeTemplate lovIA=(ListOfValuesAttributeTemplate)instanceAttributeTemplate;
            dto.setLovName(lovIA.getLovName());
            dto.setAttributeType(InstanceAttributeType.LOV);
        }else if(instanceAttributeTemplate instanceof PartNumberAttributeTemplate){
            dto.setAttributeType(InstanceAttributeType.PART_NUMBER);
        }
        return dto;
    }

    @Override
    public InstanceAttributeTemplate convertFrom(InstanceAttributeTemplateDTO dto, InstanceAttributeTemplate instanceAttributeTemplate) {

        InstanceAttributeTemplate data;
        if (InstanceAttributeType.LOV.equals(dto.getAttributeType())) {
            data = new ListOfValuesAttributeTemplate();
        }else if (InstanceAttributeType.PART_NUMBER.equals(dto.getAttributeType())){
            data = new PartNumberAttributeTemplate();
        }
        else {
            DefaultAttributeTemplate defaultIA = new DefaultAttributeTemplate();
            defaultIA.setAttributeType(DefaultAttributeTemplate.AttributeType.valueOf(dto.getAttributeType().name()));
            data = defaultIA;
        }

        data.setName(dto.getName());
        data.setMandatory(dto.isMandatory());
        data.setLocked(dto.isLocked());
        return data;


    }
}
