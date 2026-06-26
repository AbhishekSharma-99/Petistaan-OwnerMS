# Petistaan-OwnerMS

Owner microservice for the [Petistaan-MS](https://github.com/AbhishekSharma-99/Petistaan-MS)
pet management system. Handles owner registration, profile management, and pet lifecycle —
and fires lifecycle emails to MailMS on every state change via Spring's `RestClient`.

> **To run the full system** (all services + MySQL + Eureka + Config Server), see the
> [Petistaan-MS](https://github.com/AbhishekSharma-99/Petistaan-MS) hub repo.
> The `docker-compose.yml` lives there.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [API Reference](#api-reference)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Running Locally](#running-locally)
- [Branch Strategy](#branch-strategy)
- [Part of Petistaan-MS](#part-of-petistaan-ms)

---

## Overview

OwnerMS is a standalone Spring Boot microservice responsible for:

- Registering owners and their pets (domestic or wild) in a single request
- Serving owner profiles with full pet details
- Patching a pet's name and notifying the owner via email
- Deleting an owner and their associated pet (cascaded), with a farewell email
- Paginated listing of all owners

Every write operation triggers an inter-service call to **MailMS** (`POST /mails`)
carrying a `MailDTO` with recipient details and a `MailType` enum that resolves to a
FreeMarker template on the mail side.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.1.0 |
| Persistence | Spring Data JPA · Hibernate · MySQL |
| Mapping | MapStruct 1.6.3 + Lombok binding |
| HTTP Client | Spring `RestClient` (inter-service) |
| Build | Maven 3.9.x (Maven Wrapper) |
| Service Discovery | Eureka Client (via Petistaan-MS) |

---

## API Reference

Base URL: `http://localhost:8081`

| Method | Endpoint | Request Body | Response | Description |
|---|---|---|---|---|
| `POST` | `/owners` | `OwnerDTO` | `201 ownerId` | Register a new owner + pet |
| `GET` | `/owners/{ownerId}` | — | `200 OwnerDTO` | Fetch owner by ID |
| `PATCH` | `/owners/{ownerId}` | `UpdatePetDTO` | `204` | Rename the owner's pet |
| `DELETE` | `/owners/{ownerId}` | — | `204` | Delete owner and their pet |
| `GET` | `/owners?page=0&size=10` | — | `200 Page<OwnerDTO>` | Paginated owner list |

### Sample request — register owner

```json
POST /owners
{
  "firstName": "Arjun",
  "lastName": "Sharma",
  "gender": "M",
  "city": "Jaipur",
  "state": "Rajasthan",
  "mobileNumber": "9876543210",
  "emailId": "arjun@example.com",
  "petDTO": {
    "category": "Domestic",
    "name": "Bruno",
    "gender": "M",
    "petType": "Dog",
    "birthDate": "2021-04-15"
  }
}
```

> `category` is the Jackson polymorphic discriminator — `"Domestic"` or `"Wild"`.
> Domestic pets carry `birthDate`; wild pets carry `birthPlace`.

### Error response shape

```json
{
  "message": "Can't find owner with ownerId 99.",
  "httpStatus": "NOT_FOUND",
  "value": 404,
  "now": "2025-09-01T14:32:00"
}
```

---

## Project Structure

```
src/main/java/com/abhishek/
├── config/
│   └── RestClientConfig.java          # Default RestClient bean for MailMS calls
├── controller/
│   ├── advice/
│   │   └── ExceptionControllerHandler.java   # 404 / 405 / 500 global handler
│   └── OwnerController.java
├── dto/
│   ├── PetDTO.java                    # Abstract base with Jackson discriminator
│   ├── DomesticPetDTO.java            # Adds birthDate
│   ├── WildPetDTO.java                # Adds birthPlace
│   ├── OwnerDTO.java
│   ├── UpdatePetDTO.java              # PATCH payload — record(String name)
│   ├── MailDTO.java                   # Inter-service mail payload
│   └── ErrorDTO.java                  # Uniform error body
├── entity/
│   ├── Base.java                      # @MappedSuperclass — auto-increment id
│   ├── Pet.java                       # Abstract — JOINED inheritance on pet_table
│   ├── DomesticPet.java               # Final leaf — domestic_pet_table
│   ├── WildPet.java                   # Final leaf — wild_pet_table
│   └── Owner.java                     # owner_table — @OneToOne(cascade=ALL) to Pet
├── enums/
│   ├── Gender.java                    # M, F
│   ├── PetType.java                   # Bird, Cat, Dog, Fish, Rabbit
│   └── MailType.java                  # WELCOME, MODIFY, EXIT — with subject + template
├── exception/
│   └── OwnerNotFoundException.java
├── repository/
│   └── OwnerRepository.java           # JpaRepository<Owner, Integer>
├── service/
│   ├── impl/
│   │   ├── OwnerServiceImpl.java      # Orchestrates repo + mapper + mail
│   │   └── MailServiceImpl.java       # POSTs to MailMS via RestClient
│   ├── OwnerService.java
│   └── MailService.java
├── utils/
│   └── OwnerMapper.java               # MapStruct — sealed switch dispatch on Pet subtypes
└── PetistaanOwnerMsApplication.java
```

---

## Configuration

All sensitive values are externalized. Copy `.env.example` to `.env` and fill in your values:

```dotenv
MY_SQL_URL=jdbc:mysql://localhost:3306/petistaan
MY_SQL_USERNAME=root
MY_SQL_PASSWORD=yourpassword
```

`application.properties` resolves these at runtime via `${MY_SQL_URL}` etc. The `.env`
file is gitignored and never committed.

Key properties:

| Property | Value |
|---|---|
| `server.port` | `8081` |
| `spring.jpa.hibernate.ddl-auto` | `update` |
| `mail.service.url` | `http://localhost:8083/mails` |

`database.sql` in `src/main/resources/` is a **DDL reference only** — the schema is
managed by Hibernate at startup.

---

## Running Locally

**Prerequisites:** Java 25, MySQL running and accessible, MailMS running on port 8083.

> To spin up the full environment in one command, use the `docker-compose.yml` in the
> [Petistaan-MS](https://github.com/AbhishekSharma-99/Petistaan-MS) hub repo.

To run this service in isolation:

```bash
# 1. Clone the repo
git clone https://github.com/AbhishekSharma-99/Petistaan-OwnerMS.git
cd Petistaan-OwnerMS

# 2. Set up environment
cp .env.example .env
# Edit .env with your MySQL credentials

# 3. Run
./mvnw spring-boot:run
```

Service starts on `http://localhost:8081`.

---

## Branch Strategy

```
main  ●──────────────────────────────────────────● (merge via PR)
       \                                         /
dev     ●──●──●──●──●──●──●──●──●──●──●──●─────
```

- `main` — stable, production-ready commits only; no direct pushes
- `dev` — integration branch; all feature work merges here first
- `feature/*` — short-lived branches off `dev` for individual concerns

---

## Part of Petistaan-MS

Petistaan-OwnerMS is one service in the broader Petistaan microservices ecosystem:

| Service | Port | Responsibility |
|---|---|---|
| [Petistaan-EurekaServer](https://github.com/AbhishekSharma-99/Petistaan-EurekaServer) | 8761 | Service discovery |
| [Petistaan-ConfigServer](https://github.com/AbhishekSharma-99/Petistaan-ConfigServer) | 8888 | Centralized config |
| [Petistaan-APIGateway](https://github.com/AbhishekSharma-99/Petistaan-APIGateway) | 8080 | Single entry point |
| **Petistaan-OwnerMS** | **8081** | **Owner + pet management** |
| [Petistaan-PetMS](https://github.com/AbhishekSharma-99/Petistaan-PetMS) | 8082 | Pet statistics |
| [Petistaan-MailMS](https://github.com/AbhishekSharma-99/Petistaan-MailMS) | 8083 | Email dispatch |

See [Petistaan-MS](https://github.com/AbhishekSharma-99/Petistaan-MS) for system
architecture, build order, and Docker Compose setup.
