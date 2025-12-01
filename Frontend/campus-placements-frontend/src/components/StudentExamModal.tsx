import { useEffect, useState } from "react";
import "../styles/studentExam.css";
import { fetchQuizForTaking, submitQuiz } from "../api/exam";
import { QuizForTaking, QuizResult } from "types/exam";

type Props = {
  quizId: number;
  onClose: () => void;
};

type AnswersState = Record<number, string>;

export default function StudentExamModal({ quizId, onClose }: Props) {
  const [quiz, setQuiz] = useState<QuizForTaking | null>(null);
  const [answers, setAnswers] = useState<AnswersState>({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [remainingSeconds, setRemainingSeconds] = useState<number | null>(
    null
  );
  const [result, setResult] = useState<QuizResult | null>(null);
  const [autoSubmitted, setAutoSubmitted] = useState(false);

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await fetchQuizForTaking(quizId);
        setQuiz(data);
        const mins = data.durationMinutes ?? 30;
        setRemainingSeconds(mins * 60);
      } catch (err) {
        console.error(err);
        setError("Failed to load exam.");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [quizId]);

  useEffect(() => {
    if (!quiz || result || remainingSeconds === null) return;
    if (remainingSeconds <= 0) return;

    const id = window.setInterval(() => {
      setRemainingSeconds((prev) => (prev !== null ? prev - 1 : prev));
    }, 1000);

    return () => window.clearInterval(id);
  }, [quiz, result, remainingSeconds]);

  useEffect(() => {
    if (!quiz || result || remainingSeconds === null) return;
    if (remainingSeconds > 0) return;

    const doAutoSubmit = async () => {
      try {
        setAutoSubmitted(true);
        await handleSubmitInternal();
      } catch {
      }
    };
    doAutoSubmit();
  }, [remainingSeconds, quiz, result]);

  function handleChange(questionId: number, opt: string) {
    setAnswers((prev) => ({ ...prev, [questionId]: opt }));
  }

  function formatTime(sec: number | null): string {
    if (sec === null) return "--:--";
    const s = Math.max(sec, 0);
    const m = Math.floor(s / 60);
    const r = s % 60;
    const mm = m.toString().padStart(2, "0");
    const ss = r.toString().padStart(2, "0");
    return `${mm}:${ss}`;
  }

  async function handleSubmitInternal() {
    if (!quiz) return;
    try {
      setSubmitting(true);
      setError(null);

      const payload = quiz.questions.map((q) => ({
        questionId: q.id,
        selectedOption: answers[q.id] || "",
      }));

      const res = await submitQuiz(quiz.quizId, payload);
      setResult(res);
    } catch (err) {
      console.error(err);
      setError("Failed to submit exam.");
    } finally {
      setSubmitting(false);
    }
  }

  async function handleSubmitClick() {
    if (result) return;
    await handleSubmitInternal();
  }

  const isTimeLow =
    remainingSeconds !== null && remainingSeconds <= 60 && !result;

  return (
    <div className="student-exam-backdrop">
      <div className="student-exam-modal">
        <div className="student-exam-header">
          <div>
            <h2 className="student-exam-title">
              {quiz ? quiz.title : "Exam"}
            </h2>
            {quiz?.jobRole && (
              <p className="student-exam-subtitle">
                Job role: <strong>{quiz.jobRole}</strong>
              </p>
            )}
          </div>

          <div className="student-exam-header-right">
            <div
              className={
                "student-exam-timer" +
                (isTimeLow ? " student-exam-timer--low" : "")
              }
            >
              ⏱️ {formatTime(remainingSeconds)}
            </div>
            <button
              className="student-exam-close"
              onClick={onClose}
              disabled={submitting}
            >
              ×
            </button>
          </div>
        </div>

        <div className="student-exam-body">
          {loading && <div className="student-exam-state">Loading…</div>}
          {error && <div className="student-exam-error">{error}</div>}

          {!loading && !error && quiz && !result && (
            <>
              <p className="student-exam-instructions">
                Answer all questions. The exam will auto-submit when
                time is over.
              </p>

              {quiz.questions.map((q, idx) => (
                <div key={q.id} className="student-exam-question-card">
                  <div className="student-exam-question-head">
                    <span className="student-exam-question-index">
                      Q{idx + 1}
                    </span>
                    <p className="student-exam-question-text">
                      {q.questionText}
                    </p>
                  </div>

                  <div className="student-exam-options">
                    {["A", "B", "C", "D"].map((opt) => {
                      const text =
                        opt === "A"
                          ? q.optionA
                          : opt === "B"
                          ? q.optionB
                          : opt === "C"
                          ? q.optionC
                          : q.optionD;
                      return (
                        <label
                          key={opt}
                          className="student-exam-option"
                        >
                          <input
                            type="radio"
                            name={`q-${q.id}`}
                            value={opt}
                            checked={answers[q.id] === opt}
                            onChange={() => handleChange(q.id, opt)}
                          />
                          <span className="student-exam-option-letter">
                            {opt}.
                          </span>
                          <span>{text}</span>
                        </label>
                      );
                    })}
                  </div>
                </div>
              ))}
            </>
          )}
          {!loading && !error && quiz && result && (
            <div className="student-exam-results">
              <div className="student-exam-results-summary">
                <h3>Result</h3>
                <p>
                  Score:{" "}
                  <strong>
                    {result.score} / {result.totalQuestions}
                  </strong>
                  {autoSubmitted && (
                    <span className="student-exam-auto">
                      &nbsp; (auto-submitted when time ended)
                    </span>
                  )}
                </p>
              </div>

              <div className="student-exam-results-list">
                {result.questions.map((qr, idx) => {
                  const isCorrect = qr.correct;
                  const selected = qr.selectedOption;

                  const options = [
                    { key: "A", text: qr.optionA },
                    { key: "B", text: qr.optionB },
                    { key: "C", text: qr.optionC },
                    { key: "D", text: qr.optionD },
                  ];

                  return (
                    <div
                      key={qr.questionId}
                      className={
                        "student-exam-result-card" +
                        (isCorrect
                          ? " student-exam-result-card--correct"
                          : " student-exam-result-card--wrong")
                      }
                    >
                      <div className="student-exam-result-head">
                        <span className="student-exam-question-index">
                          Q{idx + 1}
                        </span>
                        <p className="student-exam-question-text">
                          {qr.questionText}
                        </p>
                      </div>

                      <div className="student-exam-options student-exam-options--result">
                        {options.map((opt) => {
                          const isOptCorrect =
                            opt.key === qr.correctOption;
                          const isOptSelected =
                            opt.key === selected;

                          let cls = "student-exam-option-tag";
                          if (isOptCorrect) {
                            cls += " student-exam-option-tag--correct";
                          } else if (isOptSelected && !isOptCorrect) {
                            cls += " student-exam-option-tag--wrong";
                          }

                          return (
                            <div key={opt.key} className={cls}>
                              <span className="student-exam-option-letter">
                                {opt.key}.
                              </span>
                              <span>{opt.text}</span>
                              {isOptCorrect && (
                                <span className="student-exam-chip">
                                  Correct
                                </span>
                              )}
                              {isOptSelected && !isOptCorrect && (
                                <span className="student-exam-chip student-exam-chip--wrong">
                                  Your answer
                                </span>
                              )}
                              {isOptSelected && isOptCorrect && (
                                <span className="student-exam-chip">
                                  Your answer
                                </span>
                              )}
                            </div>
                          );
                        })}
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          )}
        </div>

        <div className="student-exam-footer">
          {!result && (
            <button
              className="pager-btn"
              onClick={handleSubmitClick}
              disabled={submitting || loading || !quiz}
            >
              {submitting ? "Submitting…" : "Submit exam"}
            </button>
          )}
          {result && (
            <button className="pager-btn" onClick={onClose}>
              Close
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
