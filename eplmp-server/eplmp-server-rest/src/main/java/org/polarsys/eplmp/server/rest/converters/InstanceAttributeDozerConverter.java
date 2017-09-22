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

package org.polarsys.eplmp.server.rest.converters;

import org.polarsys.eplmp.core.meta.*;
import org.polarsys.eplmp.core.product.InstancePartNumberAttribute;
import org.polarsys.eplmp.core.product.PartMasterKey;
import org.polarsys.eplmp.core.util.DateUtils;
import org.polarsys.eplmp.server.rest.dto.InstanceAttributeDTO;
import org.polarsys.eplmp.server.rest.dto.NameValuePairDTO;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.DozerConverter;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Florent Garin
 */
public class InstanceAttributeDozerConverter extends DozerConverter<InstanceAttribute, InstanceAttributeDTO> {

    private Mapper mapper;

    public InstanceAttributeDozerConverter() {
        super(InstanceAttribute.class, InstanceAttributeDTO.class);
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }


    @Override
    public InstanceAttributeDTO convertTo(InstanceAttribute source, InstanceAttributeDTO dto) {
        if (dto == null)
            dto = new InstanceAttributeDTO();

        InstanceAttributeDTO.Type type;
        String value = "";

        if (source instanceof InstanceBooleanAttribute) {
            type = InstanceAttributeDTO.Type.BOOLEAN;
            value = source.getValue() + "";
        } else if (source instanceof InstanceTextAttribute) {
            type = InstanceAttributeDTO.Type.TEXT;
            value = source.getValue() + "";
        } else if (source instanceof InstanceNumberAttribute) {
            type = InstanceAttributeDTO.Type.NUMBER;
            value = source.getValue() + "";
        } else if (source instanceof InstanceDateAttribute) {
            type = InstanceAttributeDTO.Type.DATE;
            Date date = ((InstanceDateAttribute) source).getDateValue();
            if (date != null) {
                value = DateUtils.format(date);
            }
        } else if (source instanceof InstanceURLAttribute) {
            type = InstanceAttributeDTO.Type.URL;
            value = source.getValue() + "";
        } else if (source instanceof InstanceListOfValuesAttribute) {
            type = InstanceAttributeDTO.Type.LOV;
            value = ((InstanceListOfValuesAttribute) source).getIndexValue() + "";

            List<NameValuePair> items = ((InstanceListOfValuesAttribute) source).getItems();
            List<NameValuePairDTO> itemsDTO = new ArrayList<>();
            for (NameValuePair item : items) {
                itemsDTO.add(mapper.map(item, NameValuePairDTO.class));
            }
            dto.setItems(itemsDTO);
        } else if (source instanceof InstanceLongTextAttribute) {
            type = InstanceAttributeDTO.Type.LONG_TEXT;
            value = source.getValue() + "";
        } else if (source instanceof InstancePartNumberAttribute) {
            type = InstanceAttributeDTO.Type.PART_NUMBER;
            InstancePartNumberAttribute attribute = (InstancePartNumberAttribute) source;
            value = attribute.getPartMasterValue() == null ? "" : attribute.getPartMasterValue().getNumber();
        } else {
            throw new IllegalArgumentException("Instance attribute not supported");
        }
        dto.setName(source.getName());
        dto.setMandatory(source.isMandatory());
        dto.setLocked(source.isLocked());
        dto.setType(type);
        dto.setValue(value);

        return dto;
    }

    @Override
    public InstanceAttribute convertFrom(InstanceAttributeDTO source, InstanceAttribute destination) {
        InstanceAttribute attr;
        switch (source.getType()) {
            case BOOLEAN:
                attr = new InstanceBooleanAttribute();
                attr.setValue(source.getValue());
                break;
            case TEXT:
                attr = new InstanceTextAttribute();
                attr.setValue(source.getValue());
                break;
            case NUMBER:
                attr = new InstanceNumberAttribute();
                attr.setValue(source.getValue());
                break;
            case DATE:
                attr = new InstanceDateAttribute();
                attr.setValue(source.getValue());
                break;
            case URL:
                attr = new InstanceURLAttribute();
                attr.setValue(source.getValue());
                break;
            case LOV:
                attr = new InstanceListOfValuesAttribute();
                List<NameValuePairDTO> itemsDTO = source.getItems();
                List<NameValuePair> items = new ArrayList<>();
                if (itemsDTO != null) {
                    for (NameValuePairDTO itemDTO : itemsDTO) {
                        items.add(mapper.map(itemDTO, NameValuePair.class));
                    }
                }
                ((InstanceListOfValuesAttribute) attr).setItems(items);
                attr.setValue(source.getValue());
                break;
            case LONG_TEXT:
                attr = new InstanceLongTextAttribute();
                attr.setValue(source.getValue());
                break;
            case PART_NUMBER:
                attr = new InstancePartNumberAttribute();
                attr.setValue(new PartMasterKey(source.getWorkspaceId(), source.getValue()));
                break;
            default:
                throw new IllegalArgumentException("Instance attribute not supported");
        }

        attr.setName(source.getName());
        attr.setLocked(source.isLocked());
        attr.setMandatory(source.isMandatory());
        return attr;
    }

}
