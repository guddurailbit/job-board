# The Board — Feature Documentation

## Architecture

- **Frontend:** React 18 (Vite), talking to the backend only through `fetch` calls in `src/api.js`. No server-side rendering — a single-page app.
- **Backend:** Spring Boot 3 REST API. Layers: `controller` (HTTP) → `service` (business logic: filtering, sorting) → `repository` (Spring Data JPA) → MySQL.
- **Database:** two tables — `jobs` and `job_applications` (foreign key `job_id`). Schema is created/updated automatically by Hibernate (`ddl-auto=update`); no manual migrations needed for this scope.

---

## 1. Job listings

`GET /api/jobs` returns every job matching the current filters, already sorted. The React `App` component calls this on load and whenever any filter changes (debounced by 250ms so typing in the search box doesn't fire a request per keystroke).

Each job record: `title`, `company`, `location`, `type`, `mode`, `schedule`, `salary` (nullable, annual INR), `tags` (comma-separated string), `description`, `postedAt`.

Cards are rendered with a small deterministic rotation (derived from the job's ID) so the board reads like an actual pinned corkboard rather than a rigid table.

## 2. Search

The `search` query param matches (case-insensitively) against title, company, or tags. Filtering happens in `JobService.findJobs()` using in-memory stream filtering over the job list — simple and fast at this data scale, and easy to extend to a proper SQL `WHERE`/`LIKE` clause or a search index later if the dataset grows.

## 3. Filters

Combined with AND logic, all sent as query params on `GET /api/jobs`:

| Filter | Values |
|---|---|
| `location` | free text, substring match |
| `type` | Full-time / Part-time / Contract / Internship |
| `schedule` | Day shift / Night shift / Flexible |
| `mode` | Onsite / Remote / Hybrid |
| `sort` | newest (default) / oldest / salary-high / salary-low |

## 4. Job detail view

Clicking a card opens `JobDetailModal`, showing the full description (line breaks preserved), all tags, and two actions: **Apply now** and **Save**.

## 5. Apply flow

`ApplyModal` collects name, email, resume link, and an optional note, then calls `POST /api/jobs/{id}/applications`. The backend validates the payload (`@NotBlank`, `@Email`) via `ApplyRequest` + Spring's Bean Validation; invalid submissions get a 400 with field-level error messages, which the frontend surfaces inline. On success, the application is persisted to `job_applications` and the user sees a confirmation toast.

## 6. Post a job

`PostJobModal` collects all job fields and calls `POST /api/jobs`. On success, the job is saved to MySQL, the modal closes, a toast confirms it, and the board re-fetches so the new listing appears immediately (sorted to the top under "Newest").

## 7. Save / bookmark jobs

Because there's no login system in this build, "saved" jobs are kept in the browser's `localStorage` (`board_saved_v1`) rather than the database — this is a deliberate scope decision, documented here rather than left implicit. Adding real per-user saved jobs later just means adding a `User` entity and a join table, and swapping the `localStorage` calls in `App.jsx` for API calls.

## 8. Sorting

Handled server-side via the `sort` query param, applied after filtering so the ordering always reflects exactly what matched.

## 9. Theming (light/dark)

A toggle in the header swaps a `data-theme` attribute on `<html>`, which switches a set of CSS custom properties (colors) defined in `index.css`. The choice persists in `localStorage`.

## 10. Empty states

If a filter combination returns nothing, the grid is replaced with a plain-language message and a direct "post a role" link.

## 11. Validation & error handling

- Backend: `@Valid` on controller methods triggers Bean Validation; `GlobalExceptionHandler` converts validation failures into a clean `{field: message}` JSON body, and catches unexpected errors into a generic 500 response instead of leaking stack traces.
- Frontend: every API call is wrapped so failures surface as an inline error banner (job list) or inline modal error (apply/post forms), never a silent failure.

## 12. CORS

`CorsConfig` restricts `/api/**` to the frontend origin(s) listed in the `FRONTEND_ORIGIN` environment variable (comma-separated if you need more than one, e.g. localhost + your Vercel URL). Defaults to `http://localhost:5173` for local dev.

## 13. Seed data

`DataSeeder` (a `CommandLineRunner`) inserts six sample jobs the first time the app starts against an empty database, and is guarded by a row-count check so it never re-seeds (and duplicates) on subsequent restarts — important since free-tier hosts often restart the app periodically.

## 14. Testing & CI

`BoardApplicationTests` is a Spring context-load smoke test, run against an in-memory H2 database (`application-test.properties`) so CI doesn't need a real MySQL instance just to verify the app wires together correctly. `mvn verify` runs this in the GitHub Actions pipeline on every push and pull request.

## 15. Deployment topology

See `README.md` for full steps. Summary: React frontend → Vercel; Spring Boot + MySQL backend → Render (or Railway/any Docker-friendly host), connected via `VITE_API_BASE_URL` (frontend → backend) and `FRONTEND_ORIGIN` (backend CORS allow-list → frontend).
