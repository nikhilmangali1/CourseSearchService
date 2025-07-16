package com.nikhilmangali1.CourseSearchService.repository;

import com.nikhilmangali1.CourseSearchService.document.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends ElasticsearchRepository<CourseDocument, String> {

}
