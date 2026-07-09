export function timeAgo(isoOrArray) {
  const ts = toTimestamp(isoOrArray);
  const diffMs = Date.now() - ts;
  const hours = Math.floor(diffMs / (1000 * 60 * 60));
  if (hours < 1) return "just now";
  if (hours < 24) return `${hours}h ago`;
  const days = Math.floor(hours / 24);
  if (days < 30) return `${days}d ago`;
  return `${Math.floor(days / 30)}mo ago`;
}

export function toTimestamp(isoOrArray) {
  if (Array.isArray(isoOrArray)) {
    // Jackson can serialize LocalDateTime as [yyyy, MM, dd, HH, mm, ss]
    const [y, mo, d, h = 0, mi = 0, s = 0] = isoOrArray;
    return new Date(y, mo - 1, d, h, mi, s).getTime();
  }
  return new Date(isoOrArray).getTime();
}

export function formatSalary(n) {
  if (!n) return "Not disclosed";
  if (n >= 100000) return `₹${(n / 100000).toFixed(1)} L / yr`;
  return `₹${Number(n).toLocaleString("en-IN")} / yr`;
}

export function tagsToArray(tags) {
  if (!tags) return [];
  return tags.split(",").map((t) => t.trim()).filter(Boolean);
}

// Stable pseudo-random rotation per job id, so pinboard cards look
// hand-placed but don't jitter on re-render.
export function rotationFor(id) {
  const str = String(id);
  let hash = 0;
  for (let i = 0; i < str.length; i++) hash = (hash * 31 + str.charCodeAt(i)) % 1000;
  return ((hash % 5) - 2) * 0.6;
}
