# Job Application Tracker Architecture

## Phase 1 baseline

The repository currently contains a Spring Boot 3.5.3 / Java 21 Maven WAR project under `src/main/java/com/pratice/Job_Application_Tracker`. Existing code includes user, application, tracking, and dashboard entities with controllers and repositories. There is no frontend, API contract, schema script, or README yet. `application.properties` uses local database credentials and Hibernate schema updates; `docker-compose.yml` configures a different database name and hardcoded credentials. These are existing project conditions, not the target setup.

Keep the existing implementation intact while new modules are developed. Before replacing legacy entities or schema behavior, map existing data and provide an explicit migration. The current `applications` and `track` records should not be silently dropped or treated as the final normalized model.

## Target repository layout

```text
backend/                         # Spring Boot application (migration target)
  src/main/java/com/jobtracker/
    config/                      # CORS, security, Jackson, scheduling
    controller/                  # REST endpoints; request/response only
    dto/                          # Validated request and response records/classes
    entity/                       # JPA persistence models
    enums/                        # Persisted status/type enums
    exception/                    # Domain exceptions and API error handling
    mapper/                       # Entity/DTO mapping
    repository/                   # Spring Data repositories and projections
    security/                     # JWT filter, principal, token service
    service/                      # Interfaces and implementations
    util/
  src/main/resources/
    application.properties
    db/migration/                 # Versioned migrations (Flyway target)
frontend/                         # React + Vite application
  src/{assets,components,context,hooks,layouts,pages,services,styles,utils}/
database/schema.sql              # Fresh MySQL schema reference
docker-compose.yml
```

During the transition, the existing root Maven project remains the runnable backend. Move it to `backend/` only as a deliberate, separately verifiable repository change; do not maintain two competing backend builds. The target Java base package is `com.jobtracker`, with lowercase package segments and conventional Spring layering.

## Responsibilities and request flow

```text
React page/component -> service module -> centralized Axios client
  -> REST controller -> service interface/implementation
  -> repository -> MySQL
```

Controllers perform HTTP binding and validation only. Services enforce business rules and ownership using the authenticated principal; repositories scope reads and mutations by both record ID and owner ID. DTOs are the only API boundary, so JPA entities and password hashes never leave the backend. Dashboard and analytics data are derived from database queries/projections scoped to the current user.

Authentication uses BCrypt password hashing, short-lived signed JWT access tokens, and a stateless Spring Security filter chain. Private requests identify the user from the verified token, never from a client-supplied user ID. CORS is restricted to configured frontend origins. Secrets and database credentials are supplied through environment variables.

## Data model

The initial relational model is in `database/schema.sql`:

- `users` owns all private records and has unique email and mobile values.
- `job_applications` belongs to one user and stores the job, progress, and deadline data.
- `interviews` belongs to an application; ownership is enforced through the application’s user.
- `reminders` belongs to a user and may optionally reference an application or interview. It supports deadline, interview, and follow-up reminders, including completion state.

Use `BIGINT` generated identifiers, `VARCHAR` enum values (mapped as enum names in JPA), `DATE` for calendar dates, and `DATETIME` for event/audit timestamps. Foreign keys use `ON DELETE CASCADE` only where child rows have no independent meaning (user-owned applications/reminders and application interviews). Add indexes for owner/status, date-driven views, and foreign keys. Persist timestamps in UTC and format them at the client boundary.

The dashboard is a read model, not a stored user-editable table. Interview dates are represented by interview records, not duplicated in application records as the source of truth. Application status history can be added as a separate audit table if historical funnel analysis is required; current status alone cannot reconstruct prior transitions.

## API conventions

- JSON REST endpoints are rooted at `/api`.
- Validation failures return `400`; unauthenticated requests `401`; forbidden requests `403`; missing owned records `404`; conflicts such as duplicate email `409`.
- List endpoints accept bounded `page` and `size`, `sort`, search, status, priority, and date filters; responses include pagination metadata.
- Consistent error responses contain a timestamp, HTTP status, stable error code, message, and field errors where relevant.
- `PATCH /api/applications/{id}/status` changes only status and still applies ownership checks.

## Migration sequence

1. Keep the present project buildable; inventory its existing controllers, entity fields, and data before schema changes.
2. Introduce the target package/modules and DTO/service boundaries without deleting legacy code prematurely.
3. Use a versioned migration to add the normalized tables and indexes, then migrate useful rows from `applications`/`track` after defining deterministic mapping rules. Do not copy dashboard aggregates; recalculate them.
4. Switch configuration from `ddl-auto=update` to migration-managed schema and environment-backed credentials when the migration is in place.
5. Add API and frontend slices against the same contract, then remove obsolete paths only after data and callers are accounted for.

## Phase boundaries

Phase 1 establishes this inventory, target structure, and database design. Backend implementation, schema migration, security, frontend initialization, Docker changes, and test suites belong to later phases and should be reviewed as separate increments.

## Legacy UI cleanup

The legacy JSP views and their MVC controllers, repositories, and entities have been removed after the React REST API replaced them. This source cleanup does not delete rows from any existing MySQL database. The original `applications`, `track`, and legacy user rows still require a separately reviewed data migration before they can be used by the normalized API.
