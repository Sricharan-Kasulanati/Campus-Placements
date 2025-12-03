import { useEffect, useMemo, useState } from "react";
import "../styles/landing.css";
import "../styles/admin.css";
import SearchBar from "../components/SearchBar";
import type { AdminStudent } from "../types/adminStudent";
import { listAdminStudents } from "../api/admin";
import { Link } from "react-router-dom";

export default function AdminStudentPage() {
  const [students, setStudents] = useState<AdminStudent[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState("");
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    (async () => {
      try {
        setLoading(true);
        const data = await listAdminStudents();
        setStudents(data);
      } catch (e) {
        console.error(e);
        setError("Failed to load students.");
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return students;
    return students.filter((s) => {
      if (s.fullName.toLowerCase().includes(q)) return true;
      if (s.email.toLowerCase().includes(q)) return true;
      return s.registeredCompanies.some((c) =>
        (c.name || "").toLowerCase().includes(q)
      );
    });
  }, [students, query]);

  function handleSearch(q: string) {
    setQuery(q);
  }

  return (
    <div className="landing admin-students-page">
      <div className="admin-students-topbar">
        <h2 className="admin-students-title">Student Information</h2>
        <SearchBar onSearch={handleSearch} />
      </div>

      {loading ? (
        <p className="state">Loading students…</p>
      ) : error ? (
        <p className="state">{error}</p>
      ) : filtered.length === 0 ? (
        <p className="state">No students match your search.</p>
      ) : (
        <div className="student-card-grid">
          {filtered.map((s) => (
            <div key={s.id} className="student-card">
              <h3 className="student-name">{s.fullName}</h3>
              <p className="student-email">{s.email}</p>

              <p className="student-companies-label">Companies Registered:</p>
              {s.registeredCompanies.length === 0 ? (
                <p className="student-companies-empty">No registrations yet.</p>
              ) : (
                <ul className="student-companies-list">
                  {s.registeredCompanies.map((c) => (
                    <li key={c.id}>{c.name}</li>
                  ))}
                </ul>
              )}

              <div className="student-actions">
                <Link
                  className="student-analytics-link"
                  to={`/analytics?studentId=${s.id}&name=${encodeURIComponent(s.fullName)}`}
                >
                  See Analytics →
                </Link>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
