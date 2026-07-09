import React from "react";
import "./FilterBar.css";

export default function FilterBar({ filters, onChange }) {
  const set = (key) => (e) =>
    onChange({
      ...filters,
      [key]: e.target.value,
    });

  return (
    <section className="filter-bar">
      {/* Search */}

      <div className="filter-search">
        <span className="filter-icon">🔍</span>

        <input
          type="text"
          placeholder="Search jobs, companies, skills..."
          value={filters.search}
          onChange={set("search")}
        />
      </div>

      {/* Location */}

      <div className="filter-item">
        <span>📍</span>

        <input
          type="text"
          placeholder="Location"
          value={filters.location}
          onChange={set("location")}
        />
      </div>

      {/* Type */}

      <div className="filter-item">
        <select value={filters.type} onChange={set("type")}>
          <option value="">All Types</option>
          <option>Full-time</option>
          <option>Part-time</option>
          <option>Contract</option>
          <option>Internship</option>
        </select>
      </div>

      {/* Mode */}

      <div className="filter-item">
        <select value={filters.mode} onChange={set("mode")}>
          <option value="">Work Mode</option>
          <option>Remote</option>
          <option>Hybrid</option>
          <option>Onsite</option>
        </select>
      </div>

      {/* Schedule */}

      <div className="filter-item">
        <select value={filters.schedule} onChange={set("schedule")}>
          <option value="">Schedule</option>
          <option>Day shift</option>
          <option>Night shift</option>
          <option>Flexible</option>
        </select>
      </div>

      {/* Sort */}

      <div className="filter-item">
        <select value={filters.sort} onChange={set("sort")}>
          <option value="newest">Newest</option>
          <option value="oldest">Oldest</option>
          <option value="salary-high">Salary ↑</option>
          <option value="salary-low">Salary ↓</option>
        </select>
      </div>
    </section>
  );
}
