import { useState } from "react";
import type { Company } from "../types/company";
import { createQuiz } from "../api/exam";
import "../styles/adminExam.css";
import { QuizAdminRequest, QuizQuestionAdmin } from "types/exam";

type Props = {
  company: Company;
  onClose: () => void;
  onCreated?: () => void;
};

export default function AdminExamModal({ company, onClose, onCreated }: Props) {
  const [title, setTitle] = useState("");
  const [jobRole, setJobRole] = useState("");
  const [description, setDescription] = useState("");
  const [durationMinutes, setDurationMinutes] = useState<string>("");
  const [questions, setQuestions] = useState<QuizQuestionAdmin[]>([
    {
      questionText: "",
      optionA: "",
      optionB: "",
      optionC: "",
      optionD: "",
      correctOption: "A",
    },
  ]);

  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function updateQuestion(
    index: number,
    field: keyof QuizQuestionAdmin,
    value: string
  ) {
    setQuestions((prev) =>
      prev.map((q, i) => (i === index ? { ...q, [field]: value } : q))
    );
  }

  function addQuestion() {
    setQuestions((prev) => [
      ...prev,
      {
        questionText: "",
        optionA: "",
        optionB: "",
        optionC: "",
        optionD: "",
        correctOption: "A",
      },
    ]);
  }

  function removeQuestion(index: number) {
    if (questions.length === 1) return;
    setQuestions((prev) => prev.filter((_, i) => i !== index));
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    const trimmedTitle = title.trim();
    const trimmedRole = jobRole.trim();

    if (!trimmedTitle || !trimmedRole) {
      setError("Title and job role are required.");
      return;
    }

    const payload: QuizAdminRequest = {
      title: trimmedTitle,
      jobRole: trimmedRole,
      description: description.trim() || undefined,
      durationMinutes: durationMinutes ? Number(durationMinutes) : undefined,
      active: true,
      questions,
    };

    try {
      setSaving(true);
      await createQuiz(company.id!, payload);
      if (onCreated) onCreated();
      onClose();
    } catch (err) {
      console.error(err);
      setError("Failed to create exam. Please try again.");
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="exam-modal-backdrop">
      <div className="exam-modal">
        <div className="exam-modal-header">
          <div>
            <h2 className="exam-modal-title">Add Exam</h2>
            <p className="exam-modal-subtitle">
              Company: <strong>{company.name}</strong>
            </p>
          </div>
          <button className="exam-modal-close" onClick={onClose}>
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit} className="exam-form">
          <section className="exam-section">
            <h3 className="exam-section-title">Exam details</h3>
            <div className="exam-grid">
              <div className="exam-field">
                <label className="exam-label">Exam title *</label>
                <input
                  type="text"
                  className="exam-input"
                  placeholder="Eg: Online Test"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                />
              </div>

              <div className="exam-field">
                <label className="exam-label">Job role *</label>
                <input
                  type="text"
                  className="exam-input"
                  placeholder="Eg: Software Engineer Intern"
                  value={jobRole}
                  onChange={(e) => setJobRole(e.target.value)}
                />
              </div>

              <div className="exam-field exam-field--small">
                <label className="exam-label">Duration (minutes)</label>
                <input
                  type="number"
                  min={1}
                  className="exam-input"
                  placeholder="e.g. 30"
                  value={durationMinutes}
                  onChange={(e) => setDurationMinutes(e.target.value)}
                />
              </div>
            </div>

            <div className="exam-field">
              <label className="exam-label">Description</label>
              <textarea
                className="exam-textarea"
                placeholder="Short description about this exam "
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>
          </section>
          <section className="exam-section">
            <h3 className="exam-section-title">Questions</h3>
            <p className="exam-section-hint">
              Add questions with four options and mark the correct answer.
            </p>

            <div className="exam-questions-wrapper">
              {questions.map((q, index) => (
                <div key={index} className="exam-question-card">
                  <div className="exam-question-header">
                    <span className="exam-question-title">
                      Question {index + 1}
                    </span>
                    {questions.length > 1 && (
                      <button
                        type="button"
                        className="exam-question-remove"
                        onClick={() => removeQuestion(index)}
                      >
                        Remove
                      </button>
                    )}
                  </div>

                  <textarea
                    className="exam-textarea exam-question-text"
                    placeholder="Type the question here"
                    value={q.questionText}
                    onChange={(e) =>
                      updateQuestion(index, "questionText", e.target.value)
                    }
                  />

                  <div className="exam-options-grid">
                    {(
                      ["optionA", "optionB", "optionC", "optionD"] as const
                    ).map((field, idx) => (
                      <div key={field} className="exam-field">
                        <label className="exam-label">
                          Option {String.fromCharCode(65 + idx)}
                        </label>
                        <input
                          type="text"
                          className="exam-input"
                          value={q[field] as string}
                          onChange={(e) =>
                            updateQuestion(index, field, e.target.value)
                          }
                        />
                      </div>
                    ))}
                  </div>

                  <div className="exam-field exam-correct-option">
                    <label className="exam-label">Correct option</label>
                    <select
                      className="exam-select"
                      value={q.correctOption}
                      onChange={(e) =>
                        updateQuestion(index, "correctOption", e.target.value)
                      }
                    >
                      <option value="A">A</option>
                      <option value="B">B</option>
                      <option value="C">C</option>
                      <option value="D">D</option>
                    </select>
                  </div>
                </div>
              ))}
            </div>

            <button
              type="button"
              className="exam-add-question-btn"
              onClick={addQuestion}
            >
              + Add another question
            </button>
          </section>

          {error && <p className="exam-error">{error}</p>}

          <div className="exam-actions">
            <button
              type="button"
              className="pager-btn pager-btn--ghost"
              onClick={onClose}
            >
              Cancel
            </button>
            <button type="submit" className="pager-btn" disabled={saving}>
              {saving ? "Saving…" : "Create exam"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
