# The Board — Job Listing App (Spring Boot + MySQL + React)

A full-stack job board:
- **Backend:** Java 17, Spring Boot 3, Spring Data JPA, MySQL
- **Frontend:** React 18 + Vite
- Search, filters, sorting, saved jobs, job applications, and posting new roles

## ⚠️ Important: Vercel only hosts the frontend

Vercel runs static sites and serverless (Node/Edge) functions — it **cannot run a long-lived Spring Boot + MySQL server**. So this project deploys as two pieces:

| Piece | Where it goes |
|---|---|
| React frontend | **Vercel** (as originally required) |
| Spring Boot + MySQL backend | A host that runs Java, e.g. **Render** or **Railway** (both have free tiers) |

The frontend talks to the backend over a plain REST API (`VITE_API_BASE_URL`), so once both are deployed, this works exactly like a single app to whoever uses it — you'll just have two links: the Vercel URL (what you submit as the "demo") and the backend URL (which the frontend calls behind the scenes).

If your assessment strictly requires everything on Vercel with zero other services, say so and I'll help you adapt this to Vercel serverless functions + a hosted MySQL (e.g. PlanetScale/Aiven) instead — it's a bigger rewrite of the backend, so flagging it now is cheaper than redoing it later.

## Project structure

```
theboard/
├── backend/                     # Spring Boot REST API
│   ├── src/main/java/com/theboard/
│   │   ├── model/                # Job, JobApplication (JPA entities)
│   │   ├── repository/           # Spring Data JPA repositories
│   │   ├── service/               # JobService — filtering/sorting/business logic
│   │   ├── controller/           # JobController, ApplicationController (REST endpoints)
│   │   ├── dto/                   # request payloads (JobRequest, ApplyRequest)
│   │   ├── config/                # CorsConfig, DataSeeder (seed data on first run)
│   │   └── exception/             # GlobalExceptionHandler
│   ├── src/main/resources/application.properties   # MySQL config (env-var driven)
│   ├── src/test/java/...          # smoke test used by CI
│   ├── Dockerfile                 # for Render/Railway deploys
│   └── pom.xml
├── frontend/                     # React + Vite app
│   ├── src/
│   │   ├── components/            # Header, FilterBar, JobCard, modals, Toast
│   │   ├── api.js                 # fetch wrapper for the backend REST API
│   │   ├── utils.js               # formatting helpers
│   │   ├── App.jsx
│   │   └── index.css              # design system (pinboard theme)
│   ├── vercel.json
│   └── package.json
└── .github/workflows/deploy.yml  # CI/CD: tests backend, builds frontend, deploys to Vercel
```

## Run it locally

### 1. Backend

```bash
cd backend
# Create the database once:
mysql -u root -p -e "CREATE DATABASE theboard;"

# Set your local MySQL credentials (or edit application.properties directly):
export DB_URL="jdbc:mysql://localhost:3306/theboard?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
export DB_USERNAME=root
export DB_PASSWORD=Rohit@8540

mvn spring-boot:run
# API now running at http://localhost:8080
```

On first startup, `DataSeeder` inserts six sample jobs (guarded so it never duplicates on restart).

### 2. Frontend

```bash
cd frontend
cp .env.example .env     # defaults to http://localhost:8080
npm install
npm run dev
# App running at http://localhost:5173
```

## API reference

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/api/jobs?search=&location=&type=&schedule=&mode=&sort=` | list jobs, filtered/sorted |
| `GET` | `/api/jobs/{id}` | job detail |
| `POST` | `/api/jobs` | create a job |
| `POST` | `/api/jobs/{id}/applications` | submit an application to a job |
| `GET` | `/api/jobs/{id}/applications` | list applications for a job |

Full request/response shapes are in `DOCUMENTATION.md`.

## Deploying

### Backend → Render (recommended, has a generous free tier)

1. Push this repo to GitHub (see below).
2. On [render.com](https://render.com): **New → Web Service** → connect your repo → set **Root Directory** to `backend` → Render will detect the `Dockerfile` and build it automatically.
3. Add a managed MySQL database: **New → MySQL** on Render (or use Railway/PlanetScale/Aiven if you prefer) and copy its connection details.
4. Set these environment variables on the Render web service:
   - `DB_URL` (JDBC URL from your MySQL provider)
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `FRONTEND_ORIGIN` (your Vercel URL, added after step below — e.g. `https://the-board.vercel.app`)
5. Deploy. Note the resulting backend URL, e.g. `https://the-board-api.onrender.com`.

### Frontend → Vercel

1. On [vercel.com](https://vercel.com): **Add New → Project** → import the repo → set **Root Directory** to `frontend` → framework auto-detects as Vite.
2. Add environment variable `VITE_API_BASE_URL` = your Render backend URL.
3. Deploy once manually so the project exists in your Vercel account.

### Wire up GitHub Actions CI/CD

The workflow in `.github/workflows/deploy.yml`:
- Runs backend tests (`mvn verify`, using an in-memory H2 database — no real MySQL needed in CI)
- Builds the frontend
- Deploys the frontend to Vercel production on every push to `main`

To let it deploy, add these repo secrets (**GitHub repo → Settings → Secrets and variables → Actions**):

| Secret | Where to get it |
|---|---|
| `VERCEL_TOKEN` | Vercel → Settings → Tokens → Create Token |
| `BACKEND_URL` | your deployed Render backend URL, e.g. `https://the-board-api.onrender.com` |

You also need `.vercel/project.json` linked once locally so Vercel knows which project to build against:

```bash
cd frontend
npm i -g vercel
vercel login
vercel link
```

This creates `.vercel/project.json` (gitignored) locally — the `vercel pull` step in CI re-fetches the equivalent config automatically using `VERCEL_TOKEN`, as long as you've linked and deployed manually at least once from your machine first.

### Push to GitHub

```bash
git init
git add .
git commit -m "Initial commit: The Board (Spring Boot + MySQL + React)"
git branch -M main
git remote add origin https://github.com/<your-username>/<your-repo>.git
git push -u origin main
```

Watch the pipeline run under your repo's **Actions** tab.

## Notes for the reviewer

- Backend persistence is real (MySQL via JPA) — jobs and applications survive restarts.
- "Saved" jobs are kept client-side in `localStorage` since there's no login/user system in this build; every other piece of data (jobs, applications) lives in MySQL.
- See `DOCUMENTATION.md` for a full feature-by-feature writeup.
