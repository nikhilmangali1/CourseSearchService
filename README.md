# CourseSearchService

A Spring Boot application for searching and filtering educational courses using Elasticsearch.  
Indexes a rich sample of course data and exposes a flexible REST API for advanced querying.

---

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Setup & Running](#setup--running)
  - [1. Start Elasticsearch](#1-start-elasticsearch)
  - [2. Configure Application](#2-configure-application)
  - [3. Build & Run Spring Boot App](#3-build--run-spring-boot-app)
  - [4. Sample Data Bootstrapping](#4-sample-data-bootstrapping)
- [API Usage](#api-usage)
  - [GET /api/search](#get-apisearch)
  - [Example Queries](#example-queries)
  - [Response Format](#response-format)
  - [Expected Behavior](#expected-behavior)
- [Project Structure](#project-structure)
- [Author](#author)

---

## Features

- **Elasticsearch-backed**: Fast, flexible search on course data.
- **Bulk sample data**: Over 50 varied courses auto-indexed at startup.
- **Advanced filtering**: Full-text, category, type, age/price/date ranges, etc.
- **Sorting & Pagination**: Flexible sorting and robust paging support.
- **RESTful API**: Simple, powerful GET endpoint.
- **Self-contained**: Works out-of-the-box with Docker Compose and Maven.

---

## Requirements

- Java 17+
- Maven 3.6+
- Docker & Docker Compose

---

## Setup & Running

### 1. Start Elasticsearch

Spin up Elasticsearch locally (version 7.17.9):

```bash
docker-compose up -d
```

This runs a single-node cluster at [http://localhost:9200](http://localhost:9200).

**To verify:**
```bash
curl http://localhost:9200
```

You should see JSON with cluster info.

---

### 2. Configure Application

The application is preconfigured to connect to `localhost:9200` with no authentication.  
No extra changes are needed in `application.properties` for out-of-the-box local use.

---

### 3. Build & Run Spring Boot App

```bash
mvn clean spring-boot:run
```

The app will:

- Read `src/main/resources/sample-courses.json`
- Auto-bulk-index all courses to the `courses` index in Elasticsearch

---

### 4. Sample Data Bootstrapping

No manual trigger is needed.  
Courses are loaded and indexed automatically at application startup.

---

## API Usage

### GET /api/search

Search for courses with flexible filters and sorting.

#### Query Parameters

| Name        | Type    | Description                                                                                   |
|-------------|---------|-----------------------------------------------------------------------------------------------|
| q           | string  | Full-text search (matches title & description)                                                |
| category    | string  | Exact match for course category (e.g., `Math`)                                                |
| type        | string  | Exact match for course type (`ONE_TIME`, `COURSE`, `CLUB`)                                    |
| minAge      | int     | Minimum age filter (inclusive)                                                                |
| maxAge      | int     | Maximum age filter (inclusive)                                                                |
| minPrice    | double  | Minimum price (inclusive)                                                                     |
| maxPrice    | double  | Maximum price (inclusive)                                                                     |
| startDate   | string  | Only courses with `nextSessionDate` on/after this ISO-8601 date/time (e.g. `2025-08-01T00:00:00Z`) |
| sort        | string  | `upcoming` (default), `priceAsc`, `priceDesc`                                                 |
| page        | int     | Page number (default: 0)                                                                      |
| size        | int     | Results per page (default: 10)                                                                |

---

### Example Queries

#### 1. Search Math Courses of Type CLUB

```bash
curl "http://localhost:8080/api/search?category=Math&type=CLUB"
```

#### 2. Search by Age, Price, and Date

```bash
curl "http://localhost:8080/api/search?minAge=8&maxAge=10&minPrice=100&maxPrice=250&startDate=2025-08-01T00:00:00Z"
```

#### 3. Full-text Search and Lowest Price First

```bash
curl "http://localhost:8080/api/search?q=chess&sort=priceAsc"
```

#### 4. All Filters Combined (with Pagination)

```bash
curl "http://localhost:8080/api/search?q=math&category=Math&type=CLUB&minAge=7&maxAge=11&minPrice=100&maxPrice=200&sort=priceAsc&page=0&size=10&startDate=2025-07-17T18:30:00Z"
```

---

### Response Format

```json
{
  "total": 2,
  "courses": [
    {
      "id": "22",
      "title": "Numbers & Patterns",
      "category": "Math",
      "type": "CLUB",
      "price": 150.0,
      "nextSessionDate": "2025-07-31T09:00:00Z"
    },
    {
      "id": "14",
      "title": "Math Wizards",
      "category": "Math",
      "type": "CLUB",
      "price": 190.0,
      "nextSessionDate": "2025-08-02T12:00:00Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalPages": 1
}
```

---

### Expected Behavior

- **Filtering**: All filters apply in combination. Only courses matching all criteria are returned.
- **Sorting**: Results are sorted by `nextSessionDate` (upcoming) by default, or by price as requested.
- **Pagination**: Results are paged. `total` is the total number of hits for your query.
- **Field Coverage**: Each course includes at least `id`, `title`, `category`, `type`, `price`, and `nextSessionDate`.

---

## Project Structure

- `src/main/resources/sample-courses.json` – 50+ course objects for bulk indexing
- `src/main/java/com/nikhilmangali1/CourseSearchService/document` – Course document mapping
- `src/main/java/com/nikhilmangali1/CourseSearchService/service` – Search service logic
- `src/main/java/com/nikhilmangali1/CourseSearchService/controller` – REST API endpoints
- `docker-compose.yml` – Spins up Elasticsearch 7.17.9

---

## Author

- [nikhilmangali1](https://github.com/nikhilmangali1)
---
