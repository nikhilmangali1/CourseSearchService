package com.nikhilmangali1.CourseSearchService.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nikhilmangali1.CourseSearchService.document.CourseDocument;
import com.nikhilmangali1.CourseSearchService.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class CourseDataLoader implements CommandLineRunner {
    private final CourseRepository courseRepository;

    public CourseDataLoader(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        InputStream inputStream = new ClassPathResource("sample-courses.json").getInputStream();
        List<CourseDocument> courses = mapper.readValue(inputStream, new TypeReference<>() {});
        courseRepository.saveAll(courses);
        System.out.println("Courses indexed: " + courses.size());
    }

}
