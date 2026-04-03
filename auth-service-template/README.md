# Шаблон OAuth2 Gateway Service на Spring Boot 3.5

## Описание
Шаблон REST API шлюза с OAuth2 Resource Server для Art Gallery приложения.
Шлюз валидирует JWT токены, полученные от rococo-auth сервиса.

## Архитектура (rococo-gateway)

### Контроллеры (`controller`)
- **CountryRestController** - GET /api/country с пагинацией и фильтрацией
- **MuseumRestController** - GET/PUT /api/museum, GET /museum/{id} с GeoDTO
- **ArtistRestController** - GET /api/artist с search по name, PATCH обновления
- **PaintingRestController** - GET paintings, GET by artist, PATCH картин
- **UserRestController** - GET/PATCH /api/user из JWT claims
- **SessionController** - GET /api/session с информацией о сессии

### DTO и модели (`dto`)
- **UserDTO** - id, username, email из JWT
- **SessionDTO** - id, userId, createdAt, expiresAt
- **CountryDTO** - id, name
- **MuseumDTO** - id, title, description, photo, geo (GeoDTO: city + Country)
- **ArtistDTO** - id, name, biography, photo
- **PaintingDTO** - id, title, description, content, museum (MuseumDTO)
- **PageableResponse<T>** - универсальный пагинированный ответ
- *Patch DTOs:* ArtistPatchDTO, MuseumPatchDTO, PaintingPatchDTO, UserPatchDTO

### Entity классы (`data`)
- **UserEntity** - id, username, firstname, lastname, avatar (longblob)
- **CountryEntity** - id, name
- **MuseumEntity** - id, title, description, city, photo, country_id (FK -> Country)
- **ArtistEntity** - id, name, biography, photo
- **PaintingEntity** - id, title, description, artist_id (FK -> Artist), museum_id (FK -> Museum), content (longblob)

### Репозитории (`data/repository`)
- **CountryRepository** - findAllOrdered(sorted by name)
- **MuseumRepository** - findByTitleContainingIgnoreCase, findWithCountryId
- **ArtistRepository** - findByNameContainingIgnoreCase
- **PaintingRepository** - findAll, findByArtistId (UUID)
- **UserRepository** - findByUsernameIgnoreCase

### Сервисы (`service`)
- **CountryService** - getList с пагинацией и маппинг CountryEntity->CountryDTO
- **MuseumService** - getList с фильтром по title/country, UPDATE + GeoDTO маппинг
- **ArtistService** - поиск artists, PATCH обновление с кэшированием
- **PaintingService** - findAll paintings, findByArtistId, UPDATE картины
- **UserService** - extraction JWT claims -> UserDTO for /api/user endpoint

### Security (`config`)
- **SecurityConfiguration** @Bean SecurityFilterChain:
  - authorizeHttpRequests() - /api/** requires authorization
  - oauth2ResourceServer().jwt() - validates tokens from issuer-uri: http://localhost:9000
  - JwtAuthenticationConverter - maps JWT claims to Spring Security authorities
- **ApplicationConfig** - @SpringBootApplication entry point

### Валидация (`validation`)
- **@NonEmptyString** annotation + NonEmptyStringValidator - проверяет непустые строки
- **MuseumIdMustExist** - validation museumId существует перед обновлением картины
- **ValidGeo** validator - ensures geo.city field is not empty in MuseumPatch

### Утилиты (`util`)
- **PageableMapper** - маппинг query params (page, size) -> Spring Pageable
- **SearchUtils** - containsIgnoreCase search helpers, createPageable()
- **DateUtils** - работа с timestamps createdAt/expiresAt

## База данных и миграции (`db/migration/rococo-gateway`)

### Таблицы:
```sql
CREATE TABLE `user` (id UUID, username VARCHAR UNIQUE, firstname, lastname, avatar BLOB)
CREATE TABLE `country` (id UUID, name VARCHAR UNIQUE)
CREATE TABLE `museum` (id UUID, title UNIQUE, description, city, photo BLOB, country_id FK -> country)
CREATE TABLE `artist` (id UUID, name UNIQUE, biography, photo BLOB)
CREATE TABLE `painting` (id UUID, title, description, artist_id FK -> artist, museum_id FK -> museum, content BLOB)
```

### Seed данные:
- 200+ стран с русскими названиями в V1__schema_init.sql
- INSERT statements: (Австралия, Австрия ... Япония, Ватикан, Палестина)

## API Эндпоинты (@RequestMapping("/api"))

### Страны GET /api/country [CountryRestController]
```
size=20, page=0 (URL params)
- returns PageableResponse<CountryDTO> sorted by name
```

### Музеи GET/PUT /api/museum [MuseumRestController]
```
GET: ?title=query&countryId=123&page=0&size=4
PATCH: /{id} MuseumPatchDTO with GeoDTO(city, country)
RETURN: MuseumDTO updated
```

### Художники GET/PUT/PATCH /api/artist [ArtistRestController]
```
GET: ?name=query&page=0&size=18
PATCH: /{id} ArtistPatchDTO (biography, photo)
PUT semantics for full list replace via search params
```

### Картины GET/PATCH /api/painting [PaintingRestController]
```
GET /api/painting: ?title=query&museumId=123&page=0&size=9
GET /api/painting/by-artist/{artistId} -> List<PaintingDTO>
PATCH /api/painting/{id}
VALIDATION: museumId (if provided) must exist
```

### Пользователь GET/PATCH /api/user [UserRestController]
```
GET: extracts preferred_username, email from JWT claims
PATCH: partial update username/email from request body
Authentication required via OAuth2 Bearer token
```

### Сессия GET /api/session [SessionController]
```
Returns SessionDTO with:
- id = JWT jti claim (UUID)
- userId = JWT sub claim (UUID)  
- createdAt/current timestamp
- expiresAt = current + 1 hour
Authentication required via OAuth2 Bearer token
```

## Конфигурация (`application.yml`)
```yaml
server:
  port: 9001

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/rococo-gateway
    hikari:
      maximum-pool-size: 30
      minimum-idle: 10
  flyway:
    enabled: true
    baseline-on-migrate: true
  jpa:
    hibernate:
      ddl-auto: none
    database-platform: MySQL8Dialect

logging:
  level:
    org.springframework.security.oauth2: DEBUG
```-

### Build configuration (build.gradle)
Spring Boot 3.5.8 dependencies:
- **oauth2-resource-server** - JWT validation (issuer-uri=http://localhost:9000)
- spring-boot-starter-data-jpa ✓
- flyway-mysql, Thymeleaf, validation
- Lombok @Getter @Setter @RequiredArgsConstructor ✓
- MySQL connector ✓

## Структура проекта
```
src/
├── main/
│   ├── java/io/student/rococo/
│   │   ├── config/
│   │   │   ├── ApplicationConfig.java (@SpringBootApplication)
│   │   │   ├── SecurityConfiguration.java (@EnableWebSecurity + OAuth2ResourceServer)
│   │   ├── controller/
│   │   │   ├── CountryRestController.java
│   │   │   ├── MuseumRestController.java
│   │   │   ├── ArtistRestController.java
│   │   │   ├── PaintingRestController.java
│   │   │   ├── UserRestController.java
│   │   │   └── SessionController.java
│   │   ├── data/
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── CountryRepository.java
│   │   │   │   ├── MuseumRepository.java
│   │   │   │   ├── ArtistRepository.java
│   │   │   │   └── PaintingRepository.java
│   │   │   ├── UserEntity.java (@Entity @Table(name="user"))
│   │   │   ├── CountryEntity.java (@Entity)
│   │   │   ├── MuseumEntity.java (ManyToOne -> CountryEntity)
│   │   │   ├── ArtistEntity.java (@Entity)
│   │   │   └── PaintingEntity.java (@Column(columnDefinition="longblob"))
│   │   ├── dto/
│   │   │   ├── UserDTO.java
│   │   │   ├── SessionDTO.java
│   │   │   ├── PageableResponse<T>.java
│   │   │   ├── CountryDTO.java
│   │   │   ├── GeoDTO.java
│   │   │   ├── MuseumDTO.java (@Builder)
│   │   │   ├── ArtistDTO.java
│   │   │   ├── PaintingDTO.java @EqualsAndHashCode
│   │   │   └── *_PatchDTO/*.java (patch partial updates)
│   │   ├── service/
│   │   │   ├── CountryService.java (@Transactional(readOnly=true))
│   │   │   ├── MuseumService.java
│   │   │   ├── ArtistService.java
│   │   │   ├── PaintingService.java (findByArtistId UUID)
│   │   │   └── UserService.java (Jwt -> DTO mapper)
│   │   └── validation/
│   │       ├── NonEmptyString.java (@Constraint validator annotation)
│   │       ├── NonEmptyStringValidator.java (@Component)
│   │       ├── MuseumIdMustExist.java (
│   │       └── ValidGeo.java (@Validated)
│   ├── resources/
│   │   ├── templates/
│   │   │   └── error.html (HTTP error views)
│   │   └── db/migration/rococo-gateway/
│   │       └── V1__schema_init.sql (full DDL + seed data 200+ countries)
├── test/
├── docs/
│   └── rococo_api_v1.yml (Complete OpenAPI 3.0 specification)
```

## Отличия от auth-service-template

| Feature | Gateway | Auth Service |
|---------|---------|--------------|
| **OAuth2 Role** | Resource Server (validate JWTs) | Authorization Server (issue JWTs, keys) |
| **JWT Handling** | Decodes/validates from issuer-uri | Generates RSA keypair, signs tokens |
| **UI Pages** | None (pure REST API) | Thymeleaf login/register forms |
| **Data Layer** | 5 entity tables + seed data | Users+Authorities only |
| **Pagination** | Full support across all endpoints | No pagination |
| **Search** | containsIgnoreCase filtering | Simple username/password |

## Использование шаблона

### Запуск шлюза:
```bash
cd rococo-gateway
./gradlew bootRun --args=--server.port=9001
```

### API использование:
```typescript
// frontend TypeScript client (docs/rococo_api_v1.yml)
get<T>(endpoint: string, options?: RequestOptions): Promise<T>
patch<T>(endpoint: string, data: object): Promise<T>

// Auth header: Authorization: Bearer <JWT_TOKEN> from rococo-auth
```

### Обновление миграций:
Миграции через Flyway в `src/main/resources/db/migration/rococo-gateway/`:
```
V1__schema_init.sql (existing tables + seed)
V2__feature_name.sql (add columns, new tables)
```