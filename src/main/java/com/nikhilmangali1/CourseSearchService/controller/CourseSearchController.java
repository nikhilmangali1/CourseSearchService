package com.nikhilmangali1.CourseSearchService.controller;

import com.nikhilmangali1.CourseSearchService.document.CourseDocument;
import com.nikhilmangali1.CourseSearchService.dto.SearchRequestDto;
import com.nikhilmangali1.CourseSearchService.dto.SearchResponseDto;
import com.nikhilmangali1.CourseSearchService.service.CourseSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api")
public class CourseSearchController {
    private final CourseSearchService courseSearchService;

    public CourseSearchController(CourseSearchService courseSearchService) {
        this.courseSearchService = courseSearchService;
    }

    @PostMapping("/courses/search")
    public ResponseEntity<SearchResponseDto> searchCourses(@RequestBody SearchRequestDto request) {
        SearchResponseDto results = courseSearchService.searchCourses(request);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponseDto> searchCoursesGet(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "minAge", required = false) Integer minAge,
            @RequestParam(value = "maxAge", required = false) Integer maxAge,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        SearchRequestDto request = new SearchRequestDto();
        request.setKeyword(q);
        request.setMinAge(minAge);
        request.setMaxAge(maxAge);
        request.setCategory(category);
        request.setType(type);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setPage(page);
        request.setSize(size);

        // Parse startDate if provided
        if (startDate != null && !startDate.trim().isEmpty()) {
            try {
                OffsetDateTime parsedDate = OffsetDateTime.parse(startDate);
                request.setNextSessionDate(parsedDate);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        // Handle sort parameter mapping
        if (sort != null) {
            switch (sort.toLowerCase()) {
                case "upcoming":
                    request.setSort(null); // Default sort is by nextSessionDate
                    break;
                case "priceasc":
                    request.setSort("priceAsc");
                    break;
                case "pricedesc":
                    request.setSort("priceDesc");
                    break;
                default:
                    request.setSort(null); // Default sort
                    break;
            }
        }

        SearchResponseDto results = courseSearchService.searchCourses(request);
        return ResponseEntity.ok(results);
    }


}
