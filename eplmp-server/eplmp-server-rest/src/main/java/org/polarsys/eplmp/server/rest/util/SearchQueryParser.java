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

package org.polarsys.eplmp.server.rest.util;

import org.apache.commons.codec.binary.Base64;
import org.polarsys.eplmp.core.query.DocumentSearchQuery;
import org.polarsys.eplmp.core.query.PartSearchQuery;
import org.polarsys.eplmp.core.query.SearchQuery;
import org.polarsys.eplmp.core.util.DateUtils;

import javax.json.*;
import javax.ws.rs.core.MultivaluedMap;
import java.io.StringReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.sun.corba.se.spi.activation.IIOP_CLEAR_TEXT.value;

public class SearchQueryParser {

    private static final Logger LOGGER = Logger.getLogger(SearchQueryParser.class.getName());

    private SearchQueryParser() {
        super();
    }

    public static DocumentSearchQuery parseDocumentStringQuery(String workspaceId, MultivaluedMap<String, String> query) {

        String fullText = null;
        String pDocMId = null;
        String pTitle = null;
        String pVersion = null;
        String pAuthor = null;
        String pType = null;
        Date pCreationDateFrom = null;
        Date pCreationDateTo = null;
        Date pModificationDateFrom = null;
        Date pModificationDateTo = null;
        List<DocumentSearchQuery.AbstractAttributeQuery> pAttributes = new ArrayList<>();
        String[] pTags = null;
        String pContent = null;
        String folder = null;
        boolean fetchHeadOnly = false;


        String encodedQueryString = (String) query.keySet().toArray()[0];
        String decodedQueryString = new String(Base64.decodeBase64(encodedQueryString));

        JsonReader reader = Json.createReader(new StringReader(decodedQueryString));
        JsonObject queryObject = reader.readObject();
        reader.close();

        for (String filter : queryObject.keySet()) {
            switch (filter) {
                case "q":
                    fullText = queryObject.getString("q");
                    break;
                case "id":
                    pDocMId = queryObject.getString("id");
                    break;
                case "title":
                    pTitle = queryObject.getString("title");
                    break;
                case "version":
                    pVersion = queryObject.getString("version");
                    break;
                case "author":
                    pAuthor = queryObject.getString("author");
                    break;
                case "type":
                    pType = queryObject.getString("type");
                    break;
                case "folder":
                    folder = queryObject.getString("folder");
                    break;
                case "createdFrom":
                    try {
                        pCreationDateFrom = DateUtils.parse(queryObject.getString("createdFrom"));
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "createdTo":
                    try {
                        pCreationDateTo = DateUtils.parse(queryObject.getString("createdTo"));
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "modifiedFrom":
                    try {
                        pModificationDateFrom = DateUtils.parse(queryObject.getString("modifiedFrom"));
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "modifiedTo":
                    try {
                        pModificationDateTo = DateUtils.parse(queryObject.getString("modifiedTo"));
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "tags":
                    if (null != queryObject.getString("tags")) {
                        pTags = queryObject.getString("tags").split(",");
                    }
                    break;
                case "content":
                    pContent = queryObject.getString("content");
                    break;
                case "attributes":
                    JsonArray attributesArray = queryObject.getJsonArray("attributes");
                    if (null != attributesArray) {
                        pAttributes = parseAttributeStringQuery(attributesArray);
                    }
                    break;
                case "fetchHeadOnly":
                    fetchHeadOnly = Boolean.valueOf(queryObject.getString("fetchHeadOnly"));
                    break;
                default:
                    break;

            }
        }

        DocumentSearchQuery.AbstractAttributeQuery[] pAttributesArray = pAttributes.toArray(new DocumentSearchQuery.AbstractAttributeQuery[pAttributes.size()]);

        return new DocumentSearchQuery(workspaceId, fullText, pDocMId, pTitle, pVersion, pAuthor, pType,
                pCreationDateFrom, pCreationDateTo, pModificationDateFrom, pModificationDateTo,
                pAttributesArray, pTags, pContent, folder, fetchHeadOnly);

    }

    public static PartSearchQuery parsePartStringQuery(String workspaceId, MultivaluedMap<String, String> query) {
        String fullText = null;
        String pNumber = null;
        String pName = null;
        String pVersion = null;
        String pAuthor = null;
        String pType = null;
        Date pCreationDateFrom = null;
        Date pCreationDateTo = null;
        Date pModificationDateFrom = null;
        Date pModificationDateTo = null;
        List<PartSearchQuery.AbstractAttributeQuery> pAttributes = new ArrayList<>();
        String[] pTags = null;
        Boolean standardPart = null;
        String content = null;
        boolean fetchHeadOnly = false;

        String encodedQueryString = (String) query.keySet().toArray()[0];
        String decodedQueryString = new String(Base64.decodeBase64(encodedQueryString));

        JsonReader reader = Json.createReader(new StringReader(decodedQueryString));
        JsonObject queryObject = reader.readObject();
        reader.close();

        for (String filter : queryObject.keySet()) {
            switch (filter) {
                case "q":
                    fullText = queryObject.getString("q");
                    break;
                case "number":
                    pNumber = queryObject.getString("number");
                    break;
                case "name":
                    pName = value;
                    break;
                case "version":
                    pVersion = queryObject.getString("version");
                    break;
                case "author":
                    pAuthor = queryObject.getString("author");
                    break;
                case "type":
                    pType = queryObject.getString("type");
                    break;
                case "createdFrom":
                    try {
                        pCreationDateFrom = DateUtils.parse(queryObject.getString("createdFrom"));
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "createdTo":
                    try {
                        pCreationDateTo = DateUtils.parse(queryObject.getString("createdTo"));
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "modifiedFrom":
                    try {
                        pModificationDateFrom = DateUtils.parse(queryObject.getString("modifiedFrom"));
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "modifiedTo":
                    try {
                        pModificationDateTo = DateUtils.parse(queryObject.getString("modifiedTo"));
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "tags":
                    if (null != queryObject.getString("tags")) {
                        pTags = queryObject.getString("tags").split(",");
                    }
                    break;
                case "standardPart":
                    standardPart = Boolean.valueOf(queryObject.getString("standardPart"));
                    break;
                case "content":
                    content = queryObject.getString("content");
                    break;
                case "attributes":
                    JsonArray attributesArray = queryObject.getJsonArray("attributes");
                    if (null != attributesArray) {
                        pAttributes = parseAttributeStringQuery(attributesArray);
                    }
                    break;
                case "fetchHeadOnly":
                    fetchHeadOnly = Boolean.valueOf(queryObject.getString("fetchHeadOnly"));
                    break;
            }
        }


        PartSearchQuery.AbstractAttributeQuery[] pAttributesArray = pAttributes.toArray(new PartSearchQuery.AbstractAttributeQuery[pAttributes.size()]);

        return new PartSearchQuery(workspaceId, fullText, pNumber, pName, pVersion, pAuthor, pType,
                pCreationDateFrom, pCreationDateTo, pModificationDateFrom, pModificationDateTo,
                pAttributesArray, pTags, standardPart, content, fetchHeadOnly);

    }

    private static List<SearchQuery.AbstractAttributeQuery> parseAttributeStringQuery(JsonArray attributeQuery) {

        List<SearchQuery.AbstractAttributeQuery> pAttributes = new ArrayList<>();
        for (JsonValue attributeString : attributeQuery) {

            String attributeType = ((JsonObject) attributeString).getString("type");
            String attributeName = ((JsonObject) attributeString).getString("name");
            ;
            String attributeValue = ((JsonObject) attributeString).getString("value");
            if (attributeType == null || attributeValue == null) {
                continue;
            }

            switch (attributeType) {
                case "BOOLEAN":
                    SearchQuery.BooleanAttributeQuery baq = new SearchQuery.BooleanAttributeQuery(attributeName, Boolean.valueOf(attributeValue));
                    pAttributes.add(baq);
                    break;
                case "DATE":
                    SearchQuery.DateAttributeQuery daq = new SearchQuery.DateAttributeQuery();
                    daq.setName(attributeName);
                    try {
                        daq.setDate(DateUtils.parse(attributeValue));
                        pAttributes.add(daq);
                    } catch (ParseException e) {
                        LOGGER.log(Level.FINEST, null, e);
                    }
                    break;
                case "TEXT":
                    SearchQuery.TextAttributeQuery taq = new SearchQuery.TextAttributeQuery(attributeName, attributeValue);
                    pAttributes.add(taq);
                    break;
                case "NUMBER":
                    try {
                        SearchQuery.NumberAttributeQuery naq = new SearchQuery.NumberAttributeQuery(attributeName, NumberFormat.getInstance().parse(attributeValue).floatValue());
                        pAttributes.add(naq);
                    } catch (ParseException e) {
                        LOGGER.log(Level.INFO, null, e);
                    }
                    break;
                case "URL":
                    SearchQuery.URLAttributeQuery uaq = new SearchQuery.URLAttributeQuery(attributeName, attributeValue);
                    pAttributes.add(uaq);
                    break;

                case "LOV":
                    SearchQuery.LovAttributeQuery laq = new SearchQuery.LovAttributeQuery(attributeName, attributeValue);
                    pAttributes.add(laq);
                    break;

                default:
                    break;
            }

        }
        return pAttributes;
    }
}
