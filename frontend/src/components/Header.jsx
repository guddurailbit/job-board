import React from "react";
import "./Header.css";

export default function Header({
  theme,
  onToggleTheme,
  savedCount,
  showSavedOnly,
  onToggleSaved,
  onPostJob,
  onLogin,
}) {
  return (
    <header className="header">
      <div className="header-container">
        <div className="logo-section">
          <div className="logo-dot"></div>

          <div>
            <h1>🟠 GCC Job Board</h1>
            <span>Find your next opportunity</span>
          </div>
        </div>

        <div className="header-right">
          <button className="icon-btn" onClick={onToggleTheme}>
            {theme === "dark" ? "☀" : "🌙"}
          </button>

          <button
            className={`saved-btn ${showSavedOnly ? "active" : ""}`}
            onClick={onToggleSaved}
          >
            ⭐ Saved
            <span>{savedCount}</span>
          </button>

          <button className="login-btn" onClick={onLogin}>
            🔐 Login
          </button>

          <button className="post-btn" onClick={onPostJob}>
            + Post a Job
          </button>
        </div>
      </div>
    </header>
  );
}
