# Task Manager API

REST API for managing caseworker tasks. Java 21, Spring Boot, PostgreSQL.

## Running locally

Needs Java 21 and Docker.

```
docker compose up -d
./gradlew bootRun
```

Runs on `http://localhost:4000`. Database settings can be overridden with `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER_NAME` and `DB_PASSWORD`; the defaults match `compose.yaml`.

## API endpoints

Swagger UI is at `/swagger-ui/index.html` once the app is running, with the spec at `/v3/api-docs`.

| Method | Path          | Description                        | Success |
| ------ | ------------- | ---------------------------------- | ------- |
| POST   | `/tasks`      | Create a task                      | 201     |
| GET    | `/tasks`      | Get all tasks, ordered by due date | 200     |
| GET    | `/tasks/{id}` | Get a task by ID                   | 200     |
| PATCH  | `/tasks/{id}` | Update a task's status             | 200     |
| DELETE | `/tasks/{id}` | Delete a task                      | 204     |

Missing tasks return 404, invalid input returns 400.

### Task fields

| Field         | Type                                             | Required |
| ------------- | ------------------------------------------------ | -------- |
| `title`       | string                                           | Yes      |
| `description` | string                                           | No       |
| `status`      | `TODO`, `IN_PROGRESS` or `COMPLETED`             | Yes      |
| `dueDate`     | ISO date-time, for example `2026-11-10T09:00:00` | Yes      |

```
curl -X POST http://localhost:4000/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Review case file","status":"TODO","dueDate":"2026-11-10T09:00:00"}'
```

Validation failures come back as `{"errors": ["Title is required"]}`.

## Tests

```
./gradlew test
```

`@WebMvcTest` with a mocked repository, covering all five endpoints plus the 404 and validation cases. The integration source set still only holds the starter's root-endpoint test:

```
./gradlew integration
```

## Changes to the starter

Two files needed adjusting: an H2 test profile so the integration set runs without Postgres, and the root-endpoint test scoped to `RootController` once the task endpoints existed.

## Notes

I hadn't used Java or Spring Boot commercially before this, so I used AI as a learning aid for conventions and debugging. All code was written and reviewed by me.
