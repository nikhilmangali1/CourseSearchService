package com.nikhilmangali1.CourseSearchService.dto;

import com.nikhilmangali1.CourseSearchService.document.CourseDocument;

import java.util.List;

public class SearchResponseDto {
    private long total;
    private List<CourseDocument> courses;
    private int page;
    private int size;
    private int totalPages;

    public SearchResponseDto(long total, List<CourseDocument> courses, int page, int size, int totalPages) {
        this.total = total;
        this.courses = courses;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<CourseDocument> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseDocument> courses) {
        this.courses = courses;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
