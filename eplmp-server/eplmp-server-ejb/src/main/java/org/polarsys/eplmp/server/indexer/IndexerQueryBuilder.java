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

package org.polarsys.eplmp.server.indexer;

import org.polarsys.eplmp.core.query.DocumentSearchQuery;
import org.polarsys.eplmp.core.query.PartSearchQuery;
import org.polarsys.eplmp.core.query.SearchQuery;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Morgan Guimard
 */
public class IndexerQueryBuilder {

    public static QueryBuilder getSearchQueryBuilder(DocumentSearchQuery documentSearchQuery) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> documentQueries = getDocumentQueries(documentSearchQuery);
        documentQueries.forEach(boolQueryBuilder::must);
        return boolQueryBuilder;
    }

    public static QueryBuilder getSearchQueryBuilder(PartSearchQuery partSearchQuery) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> partQueries = getPartQueries(partSearchQuery);
        partQueries.forEach(boolQueryBuilder::must);
        return boolQueryBuilder;
    }


    private static List<QueryBuilder> getDocumentQueries(DocumentSearchQuery documentSearchQuery) {
        List<QueryBuilder> queries = new ArrayList<>();

        String docMId = documentSearchQuery.getDocMId();
        String title = documentSearchQuery.getTitle();
        String folder = documentSearchQuery.getFolder();

        if (docMId != null && !docMId.isEmpty()) {
            queries.add(QueryBuilders.multiMatchQuery(docMId, IndexerMapping.DOCUMENT_ID_KEY).fuzziness("AUTO"));
        }

        if (title != null && !title.isEmpty()) {
            queries.add(QueryBuilders.multiMatchQuery(title, IndexerMapping.TITLE_KEY).fuzziness("AUTO"));
        }

        if (folder != null && !folder.isEmpty()) {
            queries.add(QueryBuilders.multiMatchQuery(folder, IndexerMapping.FOLDER_KEY).fuzziness("AUTO"));
        }

        queries.addAll(getCommonQueries(documentSearchQuery));

        return queries;
    }

    private static List<QueryBuilder> getPartQueries(PartSearchQuery partSearchQuery) {
        List<QueryBuilder> queries = new ArrayList<>();

        String partNumber = partSearchQuery.getPartNumber();
        String partName = partSearchQuery.getName();

        if (partNumber != null && !partNumber.isEmpty()) {
            queries.add(QueryBuilders.multiMatchQuery(partNumber, IndexerMapping.PART_NUMBER_KEY).fuzziness("AUTO"));
        }

        if (partName != null && !partName.isEmpty()) {
            queries.add(QueryBuilders.multiMatchQuery(partName, IndexerMapping.PART_NAME_KEY).fuzziness("AUTO"));
        }

        queries.addAll(getCommonQueries(partSearchQuery));

        return queries;
    }

    private static List<QueryBuilder> getCommonQueries(SearchQuery searchQuery) {

        String[] tags = searchQuery.getTags();
        SearchQuery.AbstractAttributeQuery[] attributes = searchQuery.getAttributes();

        String fullText = searchQuery.getFullText();

        List<QueryBuilder> queries = new ArrayList<>();

        if (searchQuery.getVersion() != null) {
            queries.add(QueryBuilders.termQuery(IndexerMapping.VERSION_KEY, searchQuery.getVersion()));
        }
        if (searchQuery.getAuthor() != null) {
            BoolQueryBuilder authorQuery = QueryBuilders.boolQuery();
            authorQuery.should(QueryBuilders.multiMatchQuery(searchQuery.getAuthor(), IndexerMapping.AUTHOR_NAME_KEY).fuzziness("AUTO"));
            authorQuery.should(QueryBuilders.multiMatchQuery(searchQuery.getAuthor(), IndexerMapping.AUTHOR_LOGIN_KEY).fuzziness("AUTO"));
            queries.add(authorQuery);
        }
        if (searchQuery.getType() != null) {
            queries.add(QueryBuilders.multiMatchQuery(searchQuery.getType(), IndexerMapping.TYPE_KEY).fuzziness("AUTO"));
        }

        if (searchQuery.getCreationDateFrom() != null) {
            queries.add(QueryBuilders.rangeQuery(IndexerMapping.CREATION_DATE_KEY).from(searchQuery.getCreationDateFrom()));
        }

        if (searchQuery.getCreationDateTo() != null) {
            queries.add(QueryBuilders.rangeQuery(IndexerMapping.CREATION_DATE_KEY).to(searchQuery.getCreationDateTo()));
        }

        if (searchQuery.getModificationDateFrom() != null) {
            queries.add(QueryBuilders.rangeQuery(IndexerMapping.MODIFICATION_DATE_KEY).from(searchQuery.getModificationDateFrom()));
        }

        if (searchQuery.getModificationDateTo() != null) {
            queries.add(QueryBuilders.rangeQuery(IndexerMapping.MODIFICATION_DATE_KEY).to(searchQuery.getModificationDateTo()));
        }

        if (searchQuery.getContent() != null) {
            queries.add(QueryBuilders.matchQuery(IndexerMapping.CONTENT_KEY, searchQuery.getContent()));
        }

        if (tags != null && tags.length > 0) {
            queries.add(QueryBuilders.termsQuery(IndexerMapping.TAGS_KEY, tags));
        }

        if (attributes != null) {
            Stream.of(attributes)
                    .collect(Collectors.groupingBy(SearchQuery.AbstractAttributeQuery::getNameWithoutWhiteSpace))
                    .forEach((attributeName, attributeList) -> addAttributeToQueries(queries, attributeName, attributeList));
        }

        if (fullText != null && !fullText.isEmpty()) {
            queries.add(QueryBuilders.matchQuery(IndexerMapping.ALL_FIELDS, fullText));
        }

        return queries;
    }

    private static void addAttributeToQueries(List<QueryBuilder> queries, String attributeName, List<SearchQuery.AbstractAttributeQuery> attributeList) {

        BoolQueryBuilder attributeQueryBuilder = QueryBuilders.boolQuery();

        attributeQueryBuilder.must(QueryBuilders.nestedQuery(IndexerMapping.ATTRIBUTES_KEY,
                QueryBuilders.termQuery(IndexerMapping.ATTRIBUTES_KEY + "." + IndexerMapping.ATTRIBUTE_NAME, attributeName), ScoreMode.None));

        List<NestedQueryBuilder> nestedQueryBuilders = new ArrayList<>();
        BoolQueryBuilder valuesQuery = QueryBuilders.boolQuery();

        for (SearchQuery.AbstractAttributeQuery attr : attributeList) {
            String attributeValue = attr.toString();
            if (attributeValue != null && !attributeValue.isEmpty()) {
                nestedQueryBuilders.add(QueryBuilders.nestedQuery(IndexerMapping.ATTRIBUTES_KEY,
                        QueryBuilders.termQuery(IndexerMapping.ATTRIBUTES_KEY + "." + IndexerMapping.ATTRIBUTE_VALUE, attributeValue), ScoreMode.None));

            }
        }

        // Only request for attribute name if no values
        // Use bool must if only one value passed
        // Compound should queries if many values (but must not be empty)
        if (!nestedQueryBuilders.isEmpty()) {
            if (nestedQueryBuilders.size() == 1) {
                attributeQueryBuilder.must(nestedQueryBuilders.get(0));
            } else {
                nestedQueryBuilders.forEach(valuesQuery::should);
                attributeQueryBuilder.must(valuesQuery);
                attributeQueryBuilder.mustNot(QueryBuilders.nestedQuery(IndexerMapping.ATTRIBUTES_KEY,
                        QueryBuilders.termQuery(IndexerMapping.ATTRIBUTES_KEY + "." + IndexerMapping.ATTRIBUTE_VALUE, ""), ScoreMode.None));
            }
        }

        queries.add(attributeQueryBuilder);

    }

}
