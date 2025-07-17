package com.nikhilmangali1.CourseSearchService.dto;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class SearchRequestDto {
    private String keyword;
    private String category;
    private String type;
    private Integer minAge;
    private Integer maxAge;
    private Double minPrice;
    private Double maxPrice;
    private OffsetDateTime nextSessionDate;
    private String sort;
    private int page;
    private int size;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public OffsetDateTime getNextSessionDate() {
        return nextSessionDate;
    }

    public void setNextSessionDate(OffsetDateTime nextSessionDate) {
        this.nextSessionDate = nextSessionDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public String getStartDate() {
        return nextSessionDate != null ? nextSessionDate.toLocalDate().toString() : null;
    }

    public String getEndDate() {
        return nextSessionDate != null ? nextSessionDate.toLocalDate().plusDays(1).toString() : null;
    }
}
