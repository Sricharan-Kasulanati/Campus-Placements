import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import "../styles/landing.css";
import "../styles/companyPrep.css";
import type { Company } from "../types/company";
import { getCompany } from "../api/company";
import { listPracticeTests } from "../api/practiceTest";
import { PracticeTest } from "types/practiceTest";
import { API_BASE } from "api/http";

type Grouped = {
  jobRole: string;
  description?: string;
  tests: PracticeTest[];
};

function buildPdfUrl(fileUrl?: string | null): string | null {
  if (!fileUrl) return null;
  if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
    return fileUrl;
  }
  return `${API_BASE}${fileUrl}`;
}
export default function CompanyPrepPage() {
  const { id } = useParams<{ id: string }>();
  const companyId = Number(id);

  const [company, setCompany] = useState<Company | null>(null);
  const [groups, setGroups] = useState<Grouped[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [activeTest, setActiveTest] = useState<PracticeTest | null>(null);

  useEffect(() => {
    let cancelled = false;

    async function load() {
      try {
        setLoading(true);
        setError(null);

        const [c, tests] = await Promise.all([
          getCompany(companyId),
          listPracticeTests(companyId),
        ]);
        if (cancelled) return;

        setCompany(c);

        const map = new Map<string, PracticeTest[]>();
        tests.forEach((t) => {
          const key = (t.jobRole || "General").trim() || "General";
          if (!map.has(key)) map.set(key, []);
          map.get(key)!.push(t);
        });

        const grouped: Grouped[] = Array.from(map.entries()).map(
          ([role, arr]) => ({
            jobRole: role,
            description: arr[0]?.description,
            tests: arr,
          })
        );

        setGroups(grouped);
      } catch (e) {
        console.error(e);
        setError("Could not load practice papers.");
      } finally {
        if (!cancelled) setLoading(false);
      }
    }

    if (!Number.isNaN(companyId)) {
      load();
    } else {
      setError("Invalid company.");
      setLoading(false);
    }

    return () => {
      cancelled = true;
    };
  }, [companyId]);

    const pdfUrl = activeTest ? buildPdfUrl(activeTest.fileUrl) : null;


  return (
    <div className="landing company-prep-page">
      <header className="company-prep-header">
        <div>
          <h1 className="company-prep-title">
            Prepare for tests
          </h1>
          <h2 className="company-prep-name">
            {company?.name ?? "Company"}
          </h2>
          {company?.description && (
            <p className="company-prep-description">{company.description}</p>
          )}
          {company?.location && (
            <p className="company-prep-meta">
              {company.location}
              {company.category && <> · {company.category}</>}
            </p>
          )}
        </div>

        <Link to="/landing" className="pager-btn company-prep-back">
          ← Back to companies
        </Link>
      </header>

      {loading && <p className="state">Loading practice papers…</p>}
      {error && <p className="state">{error}</p>}

      {!loading && !error && groups.length === 0 && (
        <p className="state">No practice papers have been uploaded yet.</p>
      )}

      {!loading &&
        !error &&
        groups.map((g) => (
          <section key={g.jobRole} className="job-prep-section">
            <h3 className="job-prep-title">{g.jobRole}</h3>
            {g.description && (
              <p className="job-prep-description">{g.description}</p>
            )}

            <ul className="job-prep-list">
              {g.tests.map((t) => (
                <li key={t.id} className="job-prep-item">
                  <div className="job-prep-item-main">
                    <span className="job-prep-paper-title">
                      {t.title || "Practice paper"}
                    </span>
                    {t.uploadedAt && (
                      <span className="job-prep-paper-meta">
                        Uploaded {new Date(t.uploadedAt).toLocaleDateString()}
                      </span>
                    )}
                  </div>
                  <button
                    type="button"
                    className="pager-btn job-prep-start"
                    onClick={() => setActiveTest(t)}
                  >
                    Start now
                  </button>
                </li>
              ))}
            </ul>
          </section>
        ))}
      {activeTest && (
        <div className="pdf-modal-backdrop">
          <div className="pdf-modal">
            <header className="pdf-modal-header">
              <div className="pdf-modal-header-text">
                <div className="pdf-modal-company">
                  {company?.name ?? "Company"}
                </div>
                <div className="pdf-modal-role">
                  {activeTest.jobRole || "Practice test"}
                </div>
              </div>
              <button
                type="button"
                className="pdf-modal-close"
                onClick={() => setActiveTest(null)}
              >
                ×
              </button>
            </header>
            <section className="pdf-modal-body">
              <div className="pdf-modal-scroll">
                {pdfUrl ? (
                  <iframe
                    src={pdfUrl}
                    title={activeTest.title || "Practice PDF"}
                    className="pdf-frame"
                  />
                ) : (
                  <p className="state">No file URL available for this test.</p>
                )}
              </div>
            </section>
            <footer className="pdf-modal-footer">
              <button
                type="button"
                className="pager-btn"
                onClick={() => setActiveTest(null)}
              >
                Close
              </button>
            </footer>
          </div>
        </div>
      )}
    </div>
  );
}
