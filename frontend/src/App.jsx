import React, { useEffect, useState, useCallback } from "react";
import "./index.css";

import Header from "./components/Header";
import FilterBar from "./components/FilterBar";
import JobCard from "./components/JobCard";
import JobDetailModal from "./components/JobDetailModal";
import ApplyModal from "./components/ApplyModal";
import PostJobModal from "./components/PostJobModal";
import LoginModal from "./components/LoginModal";
import Toast from "./components/Toast";

import { fetchJobs, createJob, applyToJob } from "./api";

const SAVED_KEY = "board_saved_v1";
const THEME_KEY = "board_theme_v1";
const USER_KEY = "board_user";

function loadSaved() {
  try {
    const raw = localStorage.getItem(SAVED_KEY);

    return raw ? new Set(JSON.parse(raw)) : new Set();
  } catch {
    return new Set();
  }
}

function loadUser() {
  try {
    const raw = localStorage.getItem(USER_KEY);

    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export default function App() {
  const [jobs, setJobs] = useState([]);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const [user, setUser] = useState(loadUser);

  const [filters, setFilters] = useState({
    search: "",
    location: "",
    type: "",
    schedule: "",
    mode: "",
    sort: "newest",
  });

  const [saved, setSaved] = useState(loadSaved);

  const [showSavedOnly, setShowSavedOnly] = useState(false);

  const [theme, setTheme] = useState(
    () => localStorage.getItem(THEME_KEY) || "light",
  );

  const [activeJobId, setActiveJobId] = useState(null);

  const [applyJobId, setApplyJobId] = useState(null);

  const [showPostModal, setShowPostModal] = useState(false);

  const [showLogin, setShowLogin] = useState(false);

  const [toast, setToast] = useState("");

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);

    localStorage.setItem(THEME_KEY, theme);
  }, [theme]);

  const loadJobs = useCallback(() => {
    setLoading(true);

    setError("");

    fetchJobs(filters)
      .then(setJobs)

      .catch((err) => {
        setError(err.message || "Server unavailable.");
      })

      .finally(() => {
        setLoading(false);
      });
  }, [filters]);

  useEffect(() => {
    const timer = setTimeout(() => {
      loadJobs();
    }, 250);

    return () => clearTimeout(timer);
  }, [loadJobs]);

  function toggleSave(id) {
    setSaved((prev) => {
      const next = new Set(prev);

      if (next.has(id)) next.delete(id);
      else next.add(id);

      localStorage.setItem(
        SAVED_KEY,

        JSON.stringify(Array.from(next)),
      );

      return next;
    });
  }

  function showToast(msg) {
    setToast(msg);

    setTimeout(() => {
      setToast("");
    }, 2500);
  }

  async function handlePostJob(payload) {
    await createJob(payload);

    setShowPostModal(false);

    showToast("🎉 Job posted successfully");

    loadJobs();
  }

  async function handleApply(id, payload) {
    await applyToJob(id, payload);

    setApplyJobId(null);

    showToast("🚀 Application submitted");
  }

  function handleLogin(data) {
    localStorage.setItem("token", data.token);

    localStorage.setItem(
      USER_KEY,

      JSON.stringify(data),
    );

    setUser(data);

    showToast("✅ Login successful");
  }

  function handleLogout() {
    localStorage.removeItem("token");

    localStorage.removeItem(USER_KEY);

    setUser(null);
  }

  const visibleJobs = showSavedOnly
    ? jobs.filter((j) => saved.has(j.id))
    : jobs;

  const activeJob = jobs.find((j) => j.id === activeJobId);

  const applyJob = jobs.find((j) => j.id === applyJobId);

  return (
    <div className="app">
      <Header
        theme={theme}
        savedCount={saved.size}
        showSavedOnly={showSavedOnly}
        onToggleSaved={() => setShowSavedOnly((s) => !s)}
        onToggleTheme={() => setTheme((t) => (t === "dark" ? "light" : "dark"))}
        onPostJob={() => setShowPostModal(true)}
        onLogin={() => setShowLogin(true)}
      />

      <section className="hero">
        <h2>Find your next opportunity</h2>

        <p>Discover startups, remote jobs and exciting engineering roles.</p>

        <FilterBar filters={filters} onChange={setFilters} />
      </section>

      <section className="jobs-section">
        <div className="section-head">
          <h3>Pinned Jobs</h3>

          <span>{visibleJobs.length} Jobs</span>
        </div>

        {loading && <div className="loading">Loading jobs...</div>}

        {error && <div className="error-box">{error}</div>}

        <div className="board-grid">
          {visibleJobs.map((job) => (
            <JobCard
              key={job.id}
              job={job}
              isSaved={saved.has(job.id)}
              onOpen={setActiveJobId}
              onToggleSave={toggleSave}
            />
          ))}
        </div>
      </section>

      <footer className="footer">
        Built with ❤️ using React + Spring Boot
      </footer>

      {activeJob && (
        <JobDetailModal
          job={activeJob}
          isSaved={saved.has(activeJob.id)}
          onClose={() => setActiveJobId(null)}
          onApply={() => {
            if (!user) {
              showToast("Please login first");

              setShowLogin(true);

              return;
            }

            setApplyJobId(activeJob.id);

            setActiveJobId(null);
          }}
          onToggleSave={toggleSave}
        />
      )}

      {applyJob && (
        <ApplyModal
          job={applyJob}
          onClose={() => setApplyJobId(null)}
          onSubmit={handleApply}
        />
      )}

      {showPostModal && (
        <PostJobModal
          onClose={() => setShowPostModal(false)}
          onSubmit={handlePostJob}
        />
      )}

      {showLogin && (
        <LoginModal onClose={() => setShowLogin(false)} onLogin={handleLogin} />
      )}

      <Toast message={toast} />
    </div>
  );
}
