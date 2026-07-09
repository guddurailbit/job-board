import React from "react";
import "./JobCard.css";
import { formatSalary, tagsToArray, timeAgo } from "../utils";

export default function JobCard({ job, isSaved, onOpen, onToggleSave }) {
  const tags = tagsToArray(job.tags);

  return (
    <article className="job-card" onClick={() => onOpen(job.id)}>
      <div className="card-header">
        <div className="company-logo">
          {job.company.charAt(0).toUpperCase()}
        </div>

        <button
          className={`bookmark ${isSaved ? "saved" : ""}`}
          onClick={(e) => {
            e.stopPropagation();

            onToggleSave(job.id);
          }}
        >
          {isSaved ? "★" : "☆"}
        </button>
      </div>

      <h2 className="job-title">{job.title}</h2>

      <p className="company-name">{job.company}</p>

      <div className="meta">
        <span>{job.location}</span>

        <span>{job.type}</span>
      </div>

      <div className="meta">
        <span>{job.mode}</span>

        <span>{job.schedule}</span>
      </div>

      <div className="tags">
        {tags.slice(0, 4).map((tag) => (
          <span key={tag}>{tag}</span>
        ))}
      </div>

      <p className="description">{job.description}</p>

      <div className="salary">{formatSalary(job.salary)}</div>

      <div className="card-footer">
        <span>{timeAgo(job.postedAt)}</span>

        <button>Apply →</button>
      </div>
    </article>
  );
}
