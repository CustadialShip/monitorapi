# Monitor API

A Spring Boot RESTful API service

---

## Tech Stack

| Technology              | Purpose                            |
|-------------------------|------------------------------------|
| Spring Boot             | REST API framework                 |
| MariaDB                 | Relational database                |
| Flyway                  | Database version control           |
| Keycloak                | Identity and access management     |
| Spring Security (OAuth) | Token-based authentication         |
| MapStruct               | Entity ↔ DTO mapping               |
| Lombok                  | Boilerplate code reduction         |
| Docker Compose          | Local development infrastructure   |

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- Docker & Docker Compose

---

## Running with Docker Compose

This setup includes both MariaDB and Keycloak:

```bash
docker-compose up -d