package com.nikhilmangali1.CourseSearchService.service;

import com.nikhilmangali1.CourseSearchService.document.CourseDocument;
import com.nikhilmangali1.CourseSearchService.dto.SearchRequestDto;
import com.nikhilmangali1.CourseSearchService.dto.SearchResponseDto;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public CourseSearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public SearchResponseDto searchCourses(SearchRequestDto request) {
        try {
            List<Query> mustQueries = new ArrayList<>();
            List<Query> filterQueries = new ArrayList<>();

            // Full-text search on title and description (multi-match)
            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                mustQueries.add(Query.of(q -> q.multiMatch(mm -> mm
                        .query(request.getKeyword())
                        .fields("title", "description"))));
            }

            // Range filters for minAge/maxAge - check for age range overlap
            if (request.getMinAge() != null) {
                filterQueries.add(Query.of(q -> q.range(r -> r
                        .field("maxAge")
                        .gte(co.elastic.clients.json.JsonData.of(request.getMinAge())))));
            }
            if (request.getMaxAge() != null) {
                filterQueries.add(Query.of(q -> q.range(r -> r
                        .field("minAge")
                        .lte(co.elastic.clients.json.JsonData.of(request.getMaxAge())))));
            }

            // Range filters for minPrice/maxPrice
            if (request.getMinPrice() != null) {
                filterQueries.add(Query.of(q -> q.range(r -> r
                        .field("price")
                        .gte(co.elastic.clients.json.JsonData.of(request.getMinPrice())))));
            }
            if (request.getMaxPrice() != null) {
                filterQueries.add(Query.of(q -> q.range(r -> r
                        .field("price")
                        .lte(co.elastic.clients.json.JsonData.of(request.getMaxPrice())))));
            }

            // Exact filters for category and type
            if (request.getCategory() != null && !request.getCategory().trim().isEmpty()) {
                filterQueries.add(Query.of(q -> q.term(t -> t
                        .field("category")
                        .value(request.getCategory()))));
            }
            if (request.getType() != null && !request.getType().trim().isEmpty()) {
                filterQueries.add(Query.of(q -> q.term(t -> t
                        .field("type")
                        .value(request.getType()))));
            }

            // Date filter for nextSessionDate (on or after given date)
            if (request.getNextSessionDate() != null) {
                filterQueries.add(Query.of(q -> q.range(r -> r
                        .field("nextSessionDate")
                        .gte(co.elastic.clients.json.JsonData.of(request.getNextSessionDate().toString())))));
            }

            // Build the main query
            Query mainQuery;
            if (mustQueries.isEmpty() && filterQueries.isEmpty()) {
                mainQuery = Query.of(q -> q.matchAll(ma -> ma));
            } else {
                BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
                mustQueries.forEach(boolQueryBuilder::must);
                filterQueries.forEach(boolQueryBuilder::filter);
                mainQuery = Query.of(q -> q.bool(boolQueryBuilder.build()));
            }

            // Build search request
            SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder()
                    .index("courses")
                    .query(mainQuery)
                    .from(request.getPage() * request.getSize())
                    .size(request.getSize());

            // Apply sorting
            if (request.getSort() != null && request.getSort().equals("priceAsc")) {
                searchRequestBuilder.sort(s -> s.field(f -> f.field("price").order(SortOrder.Asc)));
            } else if (request.getSort() != null && request.getSort().equals("priceDesc")) {
                searchRequestBuilder.sort(s -> s.field(f -> f.field("price").order(SortOrder.Desc)));
            } else {
                // Default sort: ascending by nextSessionDate (soonest upcoming first)
                searchRequestBuilder.sort(s -> s.field(f -> f.field("nextSessionDate").order(SortOrder.Asc)));
            }

            SearchResponse<CourseDocument> searchResponse = elasticsearchClient.search(
                    searchRequestBuilder.build(), CourseDocument.class);

            List<CourseDocument> courses = searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());

            assert searchResponse.hits().total() != null;
            long totalHits = searchResponse.hits().total().value();
            int totalPages = (int) Math.ceil((double) totalHits / request.getSize());

            return new SearchResponseDto(totalHits, courses, request.getPage(), request.getSize(), totalPages);

        } catch (Exception e) {
            throw new RuntimeException("Error executing search query", e);
        }
    }
}