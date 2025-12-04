import { useEffect, useState } from "react";
import type { Company } from "../types/company";
import { getCompanyQuiz, listCompanyQuizzes } from "../api/exam";
import { QuizAdminRequest } from "types/exam";
import "../styles/adminExam.css";

type ExistingExam = QuizAdminRequest & { id: number };

type Props = {
  company: Company;
  onClose: () => void;
  onPick: (exam: ExistingExam) => void;
};

type ExamSummary = {
  id: number;
  title: string;
  jobRole?: string;
  active?: boolean;
};

export default function AdminExamListModal({
  company,
  onClose,
  onPick,
}: Props) {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [exams, setExams] = useState<ExamSummary[]>([]);

  useEffect(() => {
    async function load() {
      try {
        setError(null);
        setLoading(true);
        const data = await listCompanyQuizzes(company.id!);
        setExams(data || []);
      } catch (err) {
        console.error(err);
        setError("Failed to load exams. Please try again.");
      } finally {
        setLoading(false);
      }
    }
    load();
  }, [company.id]);

  async function handlePick(id: number) {
    try {
      setError(null);
      setLoading(true);
      const fullExam = await getCompanyQuiz(id);
      onPick(fullExam as ExistingExam);
    } catch (err) {
      console.error(err);
      setError("Failed to load exam details. Please try again.");
      setLoading(false);
    }
  }

  return (
    <div className="exam-modal-backdrop">
      <div className="exam-modal">
        <div className="exam-modal-header">
          <div>
            <h2 className="exam-modal-title">Edit Existing Exam</h2>
            <p className="exam-modal-subtitle">
              Company: <strong>{company.name}</strong>
            </p>
          </div>
          <button className="exam-modal-close" onClick={onClose}>
            ×
          </button>
        </div>

        <div className="exam-form">
          {loading && <p className="state">Loading exams…</p>}
          {error && <p className="exam-error">{error}</p>}

          {!loading && !error && exams.length === 0 && (
            <p className="state">No exams found for this company.</p>
          )}

          {!loading && !error && exams.length > 0 && (
            <ul className="exam-list">
              {exams.map((exam) => (
                <li
                  key={exam.id}
                  className="exam-list-item"
                  onClick={() => handlePick(exam.id)}
                >
                  <div className="exam-list-main">
                    <span className="exam-list-title">{exam.title}</span>
                    {exam.jobRole && (
                      <span className="exam-list-role">{exam.jobRole}</span>
                    )}
                  </div>
                  {exam.active === false && (
                    <span className="exam-list-badge">Inactive</span>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}
