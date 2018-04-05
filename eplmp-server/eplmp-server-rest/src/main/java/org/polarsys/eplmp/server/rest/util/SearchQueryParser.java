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

import org.polarsys.eplmp.core.query.DocumentSearchQuery;
import org.polarsys.eplmp.core.query.PartSearchQuery;
import org.polarsys.eplmp.core.query.SearchQuery;
import org.polarsys.eplmp.core.util.DateUtils;

import javax.ws.rs.core.MultivaluedMap;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchQueryParser {

    private static final Logger LOGGER = Logger.getLogger(SearchQueryParser.class.getName());
    private static final String ATTRIBUTES_DELIMITER = ";";
    private static final String ATTRIBUTES_SPLITTER = ":";
    private static final char CHAR_DELIMITER = ';';
    private static final char QUOTE_CHAR_DELIMITER = '~';
    private static final char OTHER_CHAR_DELIMITER = 's';
    private static final char CHAR_SPLITTER = ':';
    private static final char QUOTE_SPLITTER = '!';
    private static final char OTHER_CHAR_SPLITTER = 'c';

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


        for (String filter : query.keySet()) {
            List<String> values = query.get(filter);
            if (values.size() == 1) {
                String value = values.get(0);
                switch (filter) {
                    case "q":
                        fullText = value;
                        break;
                    case "id":
                        pDocMId = value;
                        break;
                    case "title":
                        pTitle = value;
                        break;
                    case "version":
                        pVersion = value;
                        break;
                    case "author":
                        pAuthor = value;
                        break;
                    case "type":
                        pType = value;
                        break;
                    case "folder":
                        folder = value;
                        break;
                    case "createdFrom":
                        try {
                            pCreationDateFrom = DateUtils.parse(value);
                        } catch (ParseException e) {
                            LOGGER.log(Level.FINEST, null, e);
                        }
                        break;
                    case "createdTo":
                        try {
                            pCreationDateTo = DateUtils.parse(value);
                        } catch (ParseException e) {
                            LOGGER.log(Level.FINEST, null, e);
                        }
                        break;
                    case "modifiedFrom":
                        try {
                            pModificationDateFrom = DateUtils.parse(value);
                        } catch (ParseException e) {
                            LOGGER.log(Level.FINEST, null, e);
                        }
                        break;
                    case "modifiedTo":
                        try {
                            pModificationDateTo = DateUtils.parse(value);
                        } catch (ParseException e) {
                            LOGGER.log(Level.FINEST, null, e);
                        }
                        break;
                    case "tags":
                        if (null != value) {
                            pTags = value.split(",");
                        }
                        break;
                    case "content":
                        pContent = value;
                        break;
                    case "attributes":
                        if (null != value) {
                            pAttributes = parseAttributeStringQuery(value);
                        }
                        break;
                    case "fetchHeadOnly":
                        fetchHeadOnly = Boolean.valueOf(value);
                        break;
                    default:
                        break;

                }
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

        for (String filter : query.keySet()) {
            List<String> values = query.get(filter);
            if (values.size() == 1) {
                String value = values.get(0);
                switch (filter) {
                    case "q":
                        fullText = value;
                        break;
                    case "number":
                        pNumber = value;
                        break;
                    case "name":
                        pName = value;
                        break;
                    case "version":
                        pVersion = value;
                        break;
                    case "author":
                        pAuthor = value;
                        break;
                    case "type":
                        pType = value;
                        break;
                    case "createdFrom":
                        try {
                            pCreationDateFrom = DateUtils.parse(value);
                        } catch (ParseException e) {
                            LOGGER.log(Level.FINEST, null, e);
                        }
                        break;
                    case "createdTo":
                        try {
                            pCreationDateTo = DateUtils.parse(value);
                        } catch (ParseException e) {
                            LOGGER.log(Level.FINEST, null, e);
                        }
                        break;
                    case "modifiedFrom":
                        try {
                            pModificationDateFrom = DateUtils.parse(value);
                        } catch (ParseException e) {
                            LOGGER.log(Level.FINEST, null, e);
                        }
                        break;
                    case "modifiedTo":
                        try {
                            pModificationDateTo = DateUtils.parse(value);
                        } catch (ParseException e) {
                            LOGGER.log(Level.FINEST, null, e);
                        }
                        break;
                    case "tags":
                        if (null != value) {
                            pTags = value.split(",");
                        }
                        break;
                    case "standardPart":
                        standardPart = Boolean.valueOf(value);
                        break;
                    case "content":
                        content = value;
                        break;
                    case "attributes":
                        if (null != value) {
                            pAttributes = parseAttributeStringQuery(value);
                        }
                        break;
                    case "fetchHeadOnly":
                        fetchHeadOnly = Boolean.valueOf(value);
                        break;
                }
            }
        }

        PartSearchQuery.AbstractAttributeQuery[] pAttributesArray = pAttributes.toArray(new PartSearchQuery.AbstractAttributeQuery[pAttributes.size()]);

        return new PartSearchQuery(workspaceId, fullText, pNumber, pName, pVersion, pAuthor, pType,
                pCreationDateFrom, pCreationDateTo, pModificationDateFrom, pModificationDateTo,
                pAttributesArray, pTags, standardPart, content, fetchHeadOnly);

    }

    private static List<SearchQuery.AbstractAttributeQuery> parseAttributeStringQuery(String attributeQuery) {
        List<SearchQuery.AbstractAttributeQuery> pAttributes = new ArrayList<>();
        String[] attributesString = attributeQuery.split(ATTRIBUTES_DELIMITER);

        for (String attributeString : attributesString) {

            String attributeType = null;
            String attributeName = null;
            String attributeValue = null;
            String[] tokens = attributeString.split(ATTRIBUTES_SPLITTER);
            if (tokens != null && tokens.length != 3) {
                LOGGER.log(Level.FINEST, "Parsing Error : " + attributeString, new ParseException("Parsing Error : " + attributeString, 0));
                continue;
            }else{
                attributeType = tokens[0];
                attributeName = UnquoteSeparator(tokens[1], CHAR_DELIMITER, QUOTE_CHAR_DELIMITER, OTHER_CHAR_DELIMITER);
                attributeName = UnquoteSeparator(attributeName, CHAR_SPLITTER, QUOTE_SPLITTER, OTHER_CHAR_SPLITTER);
                attributeValue = UnquoteSeparator(tokens[2], CHAR_DELIMITER, QUOTE_CHAR_DELIMITER, OTHER_CHAR_DELIMITER);
                attributeValue = UnquoteSeparator(attributeValue, CHAR_SPLITTER, QUOTE_SPLITTER, OTHER_CHAR_SPLITTER);
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

    private static String UnquoteSeparator(String str, char separator, char quoteChar, char otherChar) // "~~" -> "~"     "~s" -> ";"
    {

        StringBuilder sb = new StringBuilder(str.length());
        boolean isQuoted = false;
        for (char c : str.toCharArray())
        {
            if (isQuoted)
            {
                if (c == otherChar)
                    sb.append(separator);
                else
                    sb.append(c);
                isQuoted = false;
            }
            else
            {
                if (c == quoteChar)
                    isQuoted = true;
                else
                    sb.append(c);
            }
        }
        if (isQuoted){
            LOGGER.log(Level.FINEST, "input string is not correctly quoted", new ParseException("input string is not correctly quoted", 0));
        }
        return sb.toString(); // ";" are restored
    }
}
