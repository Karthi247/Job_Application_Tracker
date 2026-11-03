# Job Application Tracker

A full-stack job-search workspace for tracking applications, interviews, deadlines, reminders, and outcomes. The React client uses the Spring Boot REST API, and MySQL persists user-owned records.

## Features

- Email signup and login with BCrypt password hashing and signed, short-lived JWT access tokens.
- Private application records with search, status/priority/date filtering, pagination, sorting, details, and editing.
- A Kanban pipeline with persisted status changes.
- Interview scheduling linked to an application, plus calendar views for interviews, deadlines, and reminders.
- Dashboard counts and SQL-backed analytics for status, month, company, interview conversion, offers, and rejections.
- Personal profile and password management.
- Responsive React layout with loading, empty, error, and confirmation states.

## Technology

- Backend: Java 21, Spring Boot 3.5, Spring Web, Spring Data JPA, Spring Security, JWT, Bean Validation, Maven.
- Frontend: React 18, Vite, React Router, Axios, Bootstrap Icons, CSS.
- Database: MySQL 8.
- Tests: JUnit 5, Mockito, Spring Boot Test, Vitest, Testing Library.
- Containers: Docker and Docker Compose.

## Repository structure

The backend remains at the repository root to preserve the existing Maven project. The new frontend and database reference schema are separate.

```text
src/main/java/com/pratice/Job_Application_Tracker/
  config/ controller/ domain/ dto/ enums/ exception/
  security/ service/ Repository/
src/main/resources/application.properties
src/test/java/                         # Spring context and service tests
frontend/src/                          # React pages, components, services, styles
database/schema.sql                    # Fresh MySQL schema reference
docs/architecture.md                   # Architecture and legacy migration notes
Dockerfile                             # Multi-stage Spring Boot WAR image
frontend/Dockerfile                    # Vite build served by Nginx
docker-compose.yml                     # MySQL + backend + frontend
```

The retired JSP views and their MVC handlers have been removed. Historical database rows are not deleted by that cleanup. The security fallback denies non-API routes; only the JWT-secured `/api/**` endpoints are exposed to clients.

## Database

`database/schema.sql` creates the normalized `users`, `job_applications`, `interviews`, and `reminders` tables with foreign keys, unique constraints, and indexes. It is a **fresh-install reference**. It is not a migration for an existing installation: back up and map the legacy `applications`, `track`, and `dashboard` data before changing an existing database. Dashboard aggregates are recalculated and are not stored.

For a local MySQL 8 instance, create the database and apply the reference schema:

```sh
mysql -u root -p < database/schema.sql
```

Schema changes are managed explicitly. Hibernate `ddl-auto` is disabled by default; do not use Hibernate auto-update as a substitute for a reviewed migration.

## Local development

### 1. Start MySQL

Start a local MySQL 8 server and apply `database/schema.sql` as an administrative account. Create a least-privilege local application account (or use the Compose-created account if using its MySQL service):

```sql
CREATE USER 'jobtracker'@'localhost' IDENTIFIED BY 'your-local-database-password';
GRANT ALL PRIVILEGES ON job_application_tracker.* TO 'jobtracker'@'localhost';
```

If you use Compose for MySQL, its host port is `3307`.

### 2. Configure and run the backend

Set a Base64-encoded JWT signing key containing at least 32 random bytes. For PowerShell, generate one with:

```powershell
[Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
```

Set the backend environment variables in the terminal used to start Spring:

```powershell
$env:SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3307/job_application_tracker?serverTimezone=UTC'
$env:SPRING_DATASOURCE_USERNAME = 'jobtracker'
$env:SPRING_DATASOURCE_PASSWORD = 'your-local-database-password'
$env:JWT_SECRET = 'paste-the-generated-base64-key'
$env:APP_CORS_ALLOWED_ORIGINS = 'http://localhost:5173'
mvn spring-boot:run
```

Use a database account with access to the tracker database. Do not commit secrets or use the example values in a shared environment. The backend listens on `http://localhost:8080`.

### 3. Run the frontend

```sh
cd frontend
npm ci
npm run dev
```

Open `http://localhost:5173`. Vite proxies `/api` requests to the local backend. To use another API origin, set `VITE_API_BASE_URL` in `frontend/.env.local` before starting Vite.

## Docker Compose

1. Copy `.env.example` to `.env`.
2. Replace the MySQL passwords and `JWT_SECRET`. Generate a 32-byte JWT key with the PowerShell command above or `openssl rand -base64 32`.
3. Start the stack:

```sh
docker compose up --build
```

The app is available at `http://localhost:5173`; the backend API is also exposed directly at `http://localhost:8080`. The frontend container serves the built React app and proxies `/api` to the backend. MySQL is available to local tools on port `3307`.

The schema script runs only when MySQL initializes a new data volume. Existing volumes are preserved. To reset a development database intentionally, stop the stack and remove the `mysql_data` volume with `docker compose down -v`; this deletes the database volume.

Production deployments should inject secrets through the deployment platform’s secret manager, use TLS, restrict allowed origins, and use managed database backups. The committed Compose file is a local deployment baseline, not a complete cloud hardening profile.

## Build and tests

Backend:

```sh
mvn test
mvn -DskipTests package
```

The Spring context test uses an isolated in-memory H2 database. Service tests cover registration password handling and owner-scoped application lookup.

Frontend:

```sh
cd frontend
npm ci
npm test
npm run build
npm audit --omit=dev
```

The frontend test suite covers application form validation and API payload shaping.

## API overview

Private endpoints require `Authorization: Bearer <accessToken>`. Signup and login are public. The authenticated user is resolved from the verified JWT; clients do not send owner IDs.

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/auth/signup` | Create an account and return an access token |
| `POST` | `/api/auth/login` | Authenticate and return an access token |
| `GET` | `/api/auth/me` | Return the authenticated profile |
| `GET`, `PUT` | `/api/users/profile` | Read or update personal information |
| `PUT` | `/api/users/change-password` | Change password after verifying the current password |
| `GET`, `POST` | `/api/applications` | Search/list or create applications |
| `GET`, `PUT`, `DELETE` | `/api/applications/{id}` | Read, update, or delete an owned application |
| `PATCH` | `/api/applications/{id}/status` | Update application status |
| `GET`, `POST` | `/api/interviews` | List or schedule interviews |
| `GET`, `PUT`, `DELETE` | `/api/interviews/{id}` | Read, update, or delete an owned interview |
| `GET` | `/api/calendar/events?from=YYYY-MM-DD&to=YYYY-MM-DD` | Read calendar events for a date range |
| `GET`, `POST` | `/api/reminders` | List or create reminders |
| `GET`, `PUT`, `DELETE` | `/api/reminders/{id}` | Read, update, or delete an owned reminder |
| `PATCH` | `/api/reminders/{id}/complete` | Mark a reminder complete |
| `GET` | `/api/dashboard/summary` | Dashboard summary counts |
| `GET` | `/api/dashboard/recent-applications` | Recent owned applications |
| `GET` | `/api/dashboard/upcoming-interviews` | Upcoming interviews |
| `GET` | `/api/dashboard/upcoming-deadlines` | Upcoming application deadlines |
| `GET` | `/api/dashboard/application-statistics` | Status and monthly dashboard statistics |
| `GET` | `/api/analytics/status-distribution` | Counts by application status |
| `GET` | `/api/analytics/monthly-trends` | Twelve-month application trend |
| `GET` | `/api/analytics/interview-conversion` | Applications that have reached interview |
| `GET` | `/api/analytics/companies` | Top companies by application count |
| `GET` | `/api/analytics/outcomes` | Offer and rejection counts and rates |

Application and interview list routes accept bounded pagination. Application lists additionally support `search`, `status`, `priority`, `appliedFrom`, `appliedTo`, `sortBy`, and `direction` query parameters. Validation errors return structured JSON; inaccessible records return `404`.

## Screenshots

Add screenshots of the running dashboard, application list, Kanban board, and calendar after launching the application.

## Future enhancements

- Email delivery for reminder events and password reset flow.
- Refresh-token rotation and revocation.
- Historical application status transitions for funnel analysis.
- File uploads for resumes and cover letters.
- Expanded controller/API and browser end-to-end tests.
- A reviewed migration from the legacy JSP schema to the normalized schema.
