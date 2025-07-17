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
output(POSTMAN - GET : http://localhost:8080/api/search?category=Math&type=CLUB ):
```bash
{
    "total": 4,
    "courses": [
        {
            "id": "8",
            "title": "Chess for Kids",
            "description": "Strategic thinking through chess basics and tactics.",
            "category": "Math",
            "type": "CLUB",
            "gradeRange": "2nd–5th",
            "minAge": 7,
            "maxAge": 10,
            "price": 145.0,
            "nextSessionDate": 1753621200.000000000
        },
        {
            "id": "22",
            "title": "Numbers & Patterns",
            "description": "Math activities focusing on sequences and logic.",
            "category": "Math",
            "type": "CLUB",
            "gradeRange": "2nd–4th",
            "minAge": 7,
            "maxAge": 9,
            "price": 150.0,
            "nextSessionDate": 1753952400.000000000
        },
        {
            "id": "14",
            "title": "Math Wizards",
            "description": "Crack math puzzles and logic challenges.",
            "category": "Math",
            "type": "CLUB",
            "gradeRange": "4th–6th",
            "minAge": 9,
            "maxAge": 11,
            "price": 190.0,
            "nextSessionDate": 1754136000.000000000
        },
        {
            "id": "42",
            "title": "Brain Boosters",
            "description": "Games and exercises to improve memory and focus.",
            "category": "Math",
            "type": "CLUB",
            "gradeRange": "3rd–4th",
            "minAge": 8,
            "maxAge": 9,
            "price": 175.0,
            "nextSessionDate": 1756305000.000000000
        }
    ],
    "page": 0,
    "size": 10,
    "totalPages": 1
}
```

#### 2. Search by Age, Price, and Date

```bash
curl "http://localhost:8080/api/search?minAge=8&maxAge=10&minPrice=100&maxPrice=250&startDate=2025-08-01T00:00:00Z"
```
output(POSTMAN - GET : output(POSTMAN - GET : http://localhost:8080/api/search?category=Math&type=CLUB ):
```bash
{
    "total": 23,
    "courses": [
        {
            "id": "3",
            "title": "Science Explorers",
            "description": "Hands-on science experiments for curious young minds.",
            "category": "Science",
            "type": "ONE_TIME",
            "gradeRange": "3rd–5th",
            "minAge": 8,
            "maxAge": 11,
            "price": 149.0,
            "nextSessionDate": 1754046000.000000000
        },
        {
            "id": "23",
            "title": "Science of Food",
            "description": "Explore nutrition, digestion, and kitchen science.",
            "category": "Science",
            "type": "ONE_TIME",
            "gradeRange": "3rd–5th",
            "minAge": 8,
            "maxAge": 10,
            "price": 175.5,
            "nextSessionDate": 1754051400.000000000
        },
        {
            "id": "14",
            "title": "Math Wizards",
            "description": "Crack math puzzles and logic challenges.",
            "category": "Math",
            "type": "CLUB",
            "gradeRange": "4th–6th",
            "minAge": 9,
            "maxAge": 11,
            "price": 190.0,
            "nextSessionDate": 1754136000.000000000
        },
        {
            "id": "7",
            "title": "Little Coders",
            "description": "Fun with basic coding concepts for beginners.",
            "category": "Technology",
            "type": "COURSE",
            "gradeRange": "3rd–5th",
            "minAge": 8,
            "maxAge": 10,
            "price": 210.5,
            "nextSessionDate": 1754211600.000000000
        },
        {
            "id": "25",
            "title": "Geometry in Motion",
            "description": "Learn shapes, symmetry, and angles using movement.",
            "category": "Math",
            "type": "COURSE",
            "gradeRange": "4th–6th",
            "minAge": 9,
            "maxAge": 11,
            "price": 220.0,
            "nextSessionDate": 1754227800.000000000
        },
        {
            "id": "20",
            "title": "Ecology Adventures",
            "description": "Learn ecosystems and wildlife preservation hands-on.",
            "category": "Science",
            "type": "COURSE",
            "gradeRange": "4th–6th",
            "minAge": 9,
            "maxAge": 11,
            "price": 230.0,
            "nextSessionDate": 1754560800.000000000
        },
        {
            "id": "16",
            "title": "Physics in Real Life",
            "description": "Daily life physics explored with fun demonstrations.",
            "category": "Science",
            "type": "COURSE",
            "gradeRange": "5th–6th",
            "minAge": 10,
            "maxAge": 12,
            "price": 250.0,
            "nextSessionDate": 1754821800.000000000
        },
        {
            "id": "34",
            "title": "Math Around Us",
            "description": "Real-life math through shopping, cooking, and games.",
            "category": "Math",
            "type": "COURSE",
            "gradeRange": "3rd–5th",
            "minAge": 8,
            "maxAge": 10,
            "price": 225.0,
            "nextSessionDate": 1755086400.000000000
        },
        {
            "id": "26",
            "title": "Astronomy Nights",
            "description": "Virtual telescope sessions and astronomy basics.",
            "category": "Science",
            "type": "ONE_TIME",
            "gradeRange": "5th–6th",
            "minAge": 10,
            "maxAge": 12,
            "price": 199.0,
            "nextSessionDate": 1755280800.000000000
        },
        {
            "id": "31",
            "title": "Plant Biology",
            "description": "Study photosynthesis, germination, and plant ecosystems.",
            "category": "Science",
            "type": "COURSE",
            "gradeRange": "3rd–5th",
            "minAge": 8,
            "maxAge": 10,
            "price": 210.0,
            "nextSessionDate": 1755342000.000000000
        }
    ],
    "page": 0,
    "size": 10,
    "totalPages": 3
}
```

#### 3. Full-text Search and Lowest Price First

```bash
curl "http://localhost:8080/api/search?q=chess&sort=priceAsc"
```
output(POSTMAN - GET : http://localhost:8080/api/search?q=chess&sort=priceAsc ):
```bash
{
    "total": 1,
    "courses": [
        {
            "id": "8",
            "title": "Chess for Kids",
            "description": "Strategic thinking through chess basics and tactics.",
            "category": "Math",
            "type": "CLUB",
            "gradeRange": "2nd–5th",
            "minAge": 7,
            "maxAge": 10,
            "price": 145.0,
            "nextSessionDate": 1753621200.000000000
        }
    ],
    "page": 0,
    "size": 10,
    "totalPages": 1
}
```

#### 4. All Filters Combined (with Pagination)

```bash
curl "http://localhost:8080/api/search?q=math&category=Math&type=CLUB&minAge=7&maxAge=11&minPrice=100&maxPrice=200&sort=priceAsc&page=0&size=10&startDate=2025-07-17T18:30:00Z"
```
output(POSTMAN - GET : output(POSTMAN - GET : http://localhost:8080/api/search?category=Math&type=CLUB ):
```bash
{
    "total": 2,
    "courses": [
        {
            "id": "22",
            "title": "Numbers & Patterns",
            "description": "Math activities focusing on sequences and logic.",
            "category": "Math",
            "type": "CLUB",
            "gradeRange": "2nd–4th",
            "minAge": 7,
            "maxAge": 9,
            "price": 150.0,
            "nextSessionDate": 1753952400.000000000
        },
        {
            "id": "14",
            "title": "Math Wizards",
            "description": "Crack math puzzles and logic challenges.",
            "category": "Math",
            "type": "CLUB",
            "gradeRange": "4th–6th",
            "minAge": 9,
            "maxAge": 11,
            "price": 190.0,
            "nextSessionDate": 1754136000.000000000
        }
    ],
    "page": 0,
    "size": 10,
    "totalPages": 1
}
```

---



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
