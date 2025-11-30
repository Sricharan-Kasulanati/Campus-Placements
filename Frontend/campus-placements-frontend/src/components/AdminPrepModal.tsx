import { useState } from "react";
import type { Company } from "../types/company";
import { uploadPracticeTest } from "../api/practiceTest";
import "../styles/adminPrepPapers.css";

type Props = {
  company: Company;
  onClose: () => void;
  onUploaded?: () => void;
};

export default function AdminPrepModal({ company, onClose, onUploaded }: Props) {
  const [file, setFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    if (!file) {
      setError("Please choose a PDF file.");
      return;
    }

    try {
      setUploading(true);
      const title = file.name.replace(/\.pdf$/i, "") || `${company.name} practice test`;
      await uploadPracticeTest(company.id!, title, file);
      if (onUploaded) onUploaded();
      onClose();
    } catch (err) {
      console.error(err);
      setError("Upload failed. Please try again.");
    } finally {
      setUploading(false);
    }
  }

  return (
    <div className="prep-modal-backdrop">
      <div className="prep-modal">
        <div className="prep-modal-header">
          <h2 className="prep-modal-title">Add practice paper</h2>
          <button className="prep-modal-close" onClick={onClose}>
            ×
          </button>
        </div>

        <p className="prep-modal-subtitle">
          Company: <strong>{company.name}</strong>
        </p>

        <form onSubmit={handleSubmit}>
          <div className="prep-field">
            <label className="prep-label">PDF file</label>
            <input
              type="file"
              accept="application/pdf"
              onChange={(e) =>
                setFile(e.target.files && e.target.files[0] ? e.target.files[0] : null)
              }
            />
          </div>

          {error && <p className="prep-error">{error}</p>}

          <div className="prep-actions">
            <button type="button" className="pager-btn" onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className="pager-btn" disabled={uploading}>
              {uploading ? "Uploading…" : "Upload"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
