package com.nikhilmangali1.CourseSearchService.controller;

import com.nikhilmangali1.CourseSearchService.document.CourseDocument;
import com.nikhilmangali1.CourseSearchService.dto.SearchRequestDto;
import com.nikhilmangali1.CourseSearchService.dto.SearchResponseDto;
import com.nikhilmangali1.CourseSearchService.service.CourseSearchService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
public class CourseSearchController {
    private final CourseSearchService courseSearchService;

    public CourseSearchController(CourseSearchService courseSearchService) {
        this.courseSearchService = courseSearchService;
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResponseDto> searchCourses(@RequestBody SearchRequestDto request) {
        SearchResponseDto results = courseSearchService.searchCourses(request);
        return ResponseEntity.ok(results);
    }


}
