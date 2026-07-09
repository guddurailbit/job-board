import React, { useState } from "react";
import "./ApplyModal.css";

export default function ApplyModal({ job, onClose, onSubmit }) {
  const [form, setForm] = useState({ name: "", email: "", resumeLink: "", note: "" });
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  if (!job) return null;

  const set = (key) => (e) => setForm({ ...form, [key]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSubmitting(true);
    try {
      await onSubmit(job.id, form);
      setForm({ name: "", email: "", resumeLink: "", note: "" });
    } catch (err) {
      setError(err.message || "Could not submit application. Please try again.");
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
      className="modal apply-modal"
      role="dialog"
      aria-modal="true"
    >
      <button
        className="modal-close"
        onClick={onClose}
      >
        ×
      </button>

      <div className="apply-header">
        <div className="company-icon">💼</div>

        <div>
          <h2>Apply for {job.title}</h2>
          <p className="modal-sub">
            {job.company} • {job.location}
          </p>
        </div>
      </div>

      {error && (
        <div className="status-banner error">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="job-form">

        <div className="grid-2">

          <div className="form-group">
            <label>Full Name</label>

            <input
              type="text"
              placeholder="John Doe"
              value={form.name}
              onChange={set("name")}
              required
            />
          </div>

          <div className="form-group">
            <label>Email Address</label>

            <input
              type="email"
              placeholder="john@gmail.com"
              value={form.email}
              onChange={set("email")}
              required
            />
          </div>

        </div>

        <div className="form-group">
          <label>Resume Link</label>

          <input
            type="url"
            placeholder="https://drive.google.com/..."
            value={form.resumeLink}
            onChange={set("resumeLink")}
            required
          />
        </div>

        <div className="form-group">
          <label>Cover Letter</label>

          <textarea
            rows="6"
            placeholder="Tell the hiring team why you're the best fit for this role..."
            value={form.note}
            onChange={set("note")}
          />
        </div>

        <button
          className="btn-submit"
          disabled={submitting}
        >
          {submitting ? "Sending..." : "🚀 Submit Application"}
        </button>

      </form>
    </div>
  </div>
);
}
