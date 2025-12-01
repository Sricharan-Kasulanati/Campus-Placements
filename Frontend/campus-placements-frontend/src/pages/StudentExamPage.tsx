import { useEffect, useState } from "react";
import { useParams, useLocation } from "react-router-dom";
import "../styles/landing.css";
import { fetchStudentQuizzes} from "../api/exam";
import StudentExamModal from "../components/StudentExamModal";
import { QuizStudentSummary } from "types/exam";

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

export default function StudentExamPage() {
  const { companyId, jobRole } = useParams();
  const query = useQuery();
  const companyNameFromQuery = query.get("companyName") || "";

  const [quizzes, setQuizzes] = useState<QuizStudentSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeQuizId, setActiveQuizId] = useState<number | null>(null);

  useEffect(() => {
    if (!companyId) return;
    const load = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await fetchStudentQuizzes(
          Number(companyId),
          jobRole
        );
        setQuizzes(data);
      } catch (err) {
        console.error(err);
        setError("Failed to load exams.");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [companyId, jobRole]);

  if (!companyId) return <div className="landing">Invalid company</div>;

  return (
    <div className="landing">
      <header style={{ marginBottom: 16 }}>
        <h1 style={{ marginBottom: 4 }}>
          Practice Exams for {" "}
          {companyNameFromQuery || `Company #${companyId}`}
        </h1>
        {jobRole && (
          <p className="state">
            Job Role: <strong>{jobRole}</strong>
          </p>
        )}
      </header>

      {loading && <div className="state">Loading exams…</div>}
      {error && <div className="state">{error}</div>}

      {!loading && !error && quizzes.length === 0 && (
        <div className="state">
          No exams available for this job role yet.
        </div>
      )}

      <div className="company-grid">
        {quizzes.map((q) => (
          <div key={q.id} className="company-card">
            <div className="company-card__head">
              <h3 className="company-card__name">{q.title}</h3>
            </div>

            {q.description && (
              <p className="company-card__desc">{q.description}</p>
            )}

            <div className="company-card__meta">
              {q.durationMinutes && (
                <span className="company-badge">
                  Duration: {q.durationMinutes} min
                </span>
              )}
            </div>

            <div className="company-card__footer">
              <button
                className="pager-btn"
                onClick={() => setActiveQuizId(q.id)}
              >
                Start exam
              </button>
            </div>
          </div>
        ))}
      </div>

      {activeQuizId !== null && (
        <StudentExamModal
          quizId={activeQuizId}
          onClose={() => setActiveQuizId(null)}
        />
      )}
    </div>
  );
}
