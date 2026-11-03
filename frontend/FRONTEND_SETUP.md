# BiblioTech Frontend — Setup & Notes

A React 18 + Vite single-page app for the BiblioTech library platform. This build only
touches the frontend — the backend services were **not** modified.

## 1. Run it

```bash
cd frontend
npm install
npm run dev        # http://localhost:5173
```

Production build:
```bash
npm run build       # outputs to dist/
npm run preview     # serve the production build locally
```

Both `npm install` and `npm run build` were verified to complete cleanly in this
environment (116 modules transformed, no errors).

## 2. Pointing it at the backend

The app talks to the API Gateway only, via one env var:

```
VITE_API_BASE_URL=http://localhost:8080
```

Copy `.env.example` to `.env` and adjust if your gateway runs somewhere else. If `.env`
is absent, the app falls back to `http://localhost:8080` automatically (see
`src/services/api.js`), so it works out of the box against the default backend setup
from before.

This project deliberately was **not wired up or started against the backend** as part
of this task — it's a standalone frontend build. Nothing in `auth-service`,
`api-gateway`, etc. was touched.

## 3. What changed from the uploaded zip

- **Removed all Docker-related files**: `Dockerfile`, `.dockerignore`, `nginx.conf`.
  Nothing else in the project referenced them.
- **Fixed a crash-on-error bug** in `Dashboard.jsx` and `AdminDashboard.jsx`: both
  pages loaded their stats into a `stats` state that started as `null`. If the initial
  API calls failed (e.g. backend down, 401 expired token), `loading` still flipped to
  `false` but `stats` stayed `null` — and the component tried to render
  `stats.availableBooks` etc. anyway, which throws and blanks the whole page. Now the
  stats grid only renders once `stats` is actually populated; on error you correctly
  just see the error banner instead of a crash.
- Everything else was already consistent with the backend contracts (endpoints, request/
  response field names, JWT header handling, error response shape) — reviewed every
  page, component, and service file against the corresponding backend controllers/DTOs
  and found no other mismatches.

## 4. A correction to the earlier backend documentation

The backend `SETUP_GUIDE.md` said the demo users' seeded passwords were BCrypt-hashed
and unrecoverable. That's technically true in isolation, but this frontend's `Login.jsx`
actually ships the plaintext demo credentials, and I verified them cryptographically
against the exact hashes in `auth-service`'s `data.sql` — **they match**:

| Email | Password | Role |
|---|---|---|
| admin@bibliotech.com | Admin@123 | ADMIN |
| librarian@bibliotech.com | Librarian@123 | LIBRARIAN |
| student@bibliotech.com | Student@123 | STUDENT |
| faculty@bibliotech.com | Faculty@123 | FACULTY |

So once both the backend and this frontend are running, you can log in immediately with
any of the above — no need to register a new account first.

## 5. Project layout (unchanged, already clean)

```
frontend/
├── index.html
├── package.json
├── vite.config.js
├── .env.example
└── src/
    ├── main.jsx / App.jsx
    ├── components/   (Navbar, ProtectedRoute, Card, BookCard, StatusBadge, ...)
    ├── context/      (AuthContext — login/register/logout, JWT + user in localStorage)
    ├── pages/        (Login, Register, Dashboard, Books, BookDetails, MyRentals,
    │                  Fines, Notifications, AdminDashboard, ManageBooks,
    │                  ManageBookCopies, NotFound)
    ├── services/     (one file per backend service: api.js has the axios instance +
    │                  JWT interceptor + 401 handling; authService/bookService/
    │                  rentalService/fineService/notificationService wrap the endpoints)
    ├── styles/index.css
    └── utils/helpers.js
```
