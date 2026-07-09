import React, { useState } from "react";
import "./PostJobModal.css";

const initialState = {
  title: "",
  company: "",
  location: "",
  type: "Full-time",
  mode: "Onsite",
  schedule: "Day shift",
  salary: "",
  tags: "",
  description: "",
};

export default function PostJobModal({ onClose, onSubmit }) {
  const [form, setForm] = useState(initialState);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const set = (key) => (e) => setForm({ ...form, [key]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSubmitting(true);
    try {
      await onSubmit({ ...form, salary: form.salary ? Number(form.salary) : null });
      setForm(initialState);
    } catch (err) {
      setError(err.message || "Could not post job. Please try again.");
    } finally {
      setSubmitting(false);
    }
  };

return (
  <div
    className="modal-backdrop"
    onClick={(e) => e.target === e.currentTarget && onClose()}
  >
    <div
      className="modal"
      role="dialog"
      aria-modal="true"
    >
      <button
        className="modal-close"
        onClick={onClose}
      >
        ×
      </button>

      <h2>📌 Pin a New Role</h2>

      {error && (
        <div className="status-banner error">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="job-form">

        <div className="grid-2">

          <div className="form-group">
            <label>Job Title</label>
            <input
              type="text"
              value={form.title}
              onChange={set("title")}
              placeholder="Frontend Developer"
              required
            />
          </div>

          <div className="form-group">
            <label>Company</label>
            <input
              type="text"
              value={form.company}
              onChange={set("company")}
              placeholder="Google"
              required
            />
          </div>

        </div>

        <div className="form-group">
          <label>Location</label>

          <input
            type="text"
            value={form.location}
            onChange={set("location")}
            placeholder="Hyderabad, India"
            required
          />
        </div>

        <div className="grid-3">

          <div className="form-group">
            <label>Type</label>

            <select
              value={form.type}
              onChange={set("type")}
            >
              <option>Full-time</option>
              <option>Part-time</option>
              <option>Contract</option>
              <option>Internship</option>
            </select>
          </div>

          <div className="form-group">
            <label>Mode</label>

            <select
              value={form.mode}
              onChange={set("mode")}
            >
              <option>Onsite</option>
              <option>Remote</option>
              <option>Hybrid</option>
            </select>
          </div>

          <div className="form-group">
            <label>Schedule</label>

            <select
              value={form.schedule}
              onChange={set("schedule")}
            >
              <option>Day shift</option>
              <option>Night shift</option>
              <option>Flexible</option>
            </select>
          </div>

        </div>

        <div className="grid-2">

          <div className="form-group">
            <label>Salary</label>

            <input
              type="number"
              value={form.salary}
              onChange={set("salary")}
              placeholder="₹ 12,00,000"
            />
          </div>

          <div className="form-group">
            <label>Tags</label>

            <input
              type="text"
              value={form.tags}
              onChange={set("tags")}
              placeholder="React, SQL, Java"
            />
          </div>

        </div>

        <div className="form-group">
          <label>Description</label>

          <textarea
            rows="6"
            value={form.description}
            onChange={set("description")}
            placeholder="Describe the job role..."
            required
          />
        </div>

        <button
          className="btn-submit"
          disabled={submitting}
        >
          {submitting ? "Posting..." : "🚀 Pin this Job"}
        </button>

      </form>

    </div>
  </div>
);
}
