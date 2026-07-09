import React from "react";
import "./JobDetailModal.css";
import { formatSalary, tagsToArray } from "../utils";

export default function JobDetailModal({
  job,
  isSaved,
  onClose,
  onApply,
  onToggleSave,
}) {
  if (!job) return null;

  const tags = tagsToArray(job.tags);

  return (
    <div
      className="modal-backdrop"
      onClick={(e) => e.target === e.currentTarget && onClose()}
    >
      <div className="job-detail-modal">
        <button className="detail-close" onClick={onClose}>
          ✕
        </button>

        <div className="detail-header">
          <div className="company-logo">{job.company.charAt(0)}</div>

          <div>
            <h2>{job.title}</h2>

            <p>
              {job.company} • {job.location}
            </p>
          </div>
        </div>

        <div className="detail-info">
          <span>{job.type}</span>

          <span>{job.mode}</span>

          <span>{job.schedule}</span>

          <span className="salary">{formatSalary(job.salary)}</span>
        </div>

        <div className="detail-tags">
          {tags.map((tag) => (
            <span key={tag}>{tag}</span>
          ))}
        </div>

        <div className="detail-section">
          <h3>Job Description</h3>

          <p>{job.description}</p>
        </div>

        <div className="detail-footer">
          <button className="save-job" onClick={() => onToggleSave(job.id)}>
            {isSaved ? "★ Saved" : "☆ Save"}
          </button>

          <button className="apply-job" onClick={onApply}>
            🚀 Apply Now
          </button>
        </div>
      </div>
    </div>
  );
}
