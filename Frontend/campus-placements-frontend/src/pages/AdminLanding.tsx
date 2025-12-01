import { useEffect, useState } from "react";
import SearchBar from "../components/SearchBar";
import {
  searchCompaniesPage,
  createCompany,
  updateCompany,
} from "../api/company";
import type { Company } from "../types/company";
import type { Page } from "../types/page";
import "../styles/landing.css";
import { useNavigate } from "react-router-dom";
import AdminCompanyCard from "../components/AdminCompanyCard";
import AdminModal from "../components/AdminModal";
import AdminPrepModal from "components/AdminPrepModal";
import AdminExamModal from "components/AdminExamModal";

export default function AdminLanding() {
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState("");
  const [topMatch, setTopMatch] = useState<Company | null>(null);
  const [noExactMsg, setNoExactMsg] = useState<string | null>(null);
  const [allCompanies, setAllCompanies] = useState<Company[]>([]);
  const [page, setPage] = useState(0);
  const [size] = useState(12);
  const [totalPages, setTotalPages] = useState(1);
  const [last, setLast] = useState(true);

  const [showModal, setShowModal] = useState(false);
  const [editCompany, setEditCompany] = useState<Company | null>(null);

  const [prepCompany, setPrepCompany] = useState<Company | null>(null);
  const [examCompany, setExamCompany] = useState<Company | null>(null);

  const navigate = useNavigate();

  async function loadInitial() {
    setLoading(true);
    setQuery("");
    setTopMatch(null);
    setNoExactMsg(null);

    const pgAll = await searchCompaniesPage("", 0, size, "name,asc");

    setAllCompanies(pgAll.content ?? []);
    setTotalPages(pgAll.totalPages);
    setLast(pgAll.last);
    setPage(0);
    setLoading(false);
  }

  useEffect(() => {
    loadInitial();
  }, []);

  async function onSearch(q: string) {
    setLoading(true);
    setQuery(q);
    setTopMatch(null);
    setNoExactMsg(null);

    const pgQ = await searchCompaniesPage(q, 0, size, "name,asc");
    const pgAll = await searchCompaniesPage("", 0, size, "name,asc");

    const exact =
      (pgQ.content ?? []).find(
        (c) => c.name.trim().toLowerCase() === q.trim().toLowerCase()
      ) || null;

    if (q.trim() && !exact) setNoExactMsg(`No companies named “${q}”.`);

    setTopMatch(exact);
    const dedup = exact
      ? pgAll.content.filter((c) => c.id !== exact.id)
      : pgAll.content;
    setAllCompanies(dedup);
    setTotalPages(pgAll.totalPages);
    setLast(pgAll.last);
    setPage(0);
    setLoading(false);
  }

  async function loadMoreAll() {
    if (last) return;
    setLoading(true);
    const next = page + 1;
    const pgAll: Page<Company> = await searchCompaniesPage(
      "",
      next,
      size,
      "name,asc"
    );
    const extra = topMatch
      ? pgAll.content.filter((c) => c.id !== topMatch.id)
      : pgAll.content;
    setAllCompanies((prev) => [...prev, ...extra]);
    setPage(next);
    setLast(pgAll.last);
    setTotalPages(pgAll.totalPages);
    setLoading(false);
  }

  async function saveCompany(values: any) {
    if (editCompany) await updateCompany(editCompany.id, values);
    else await createCompany(values);

    setShowModal(false);
    setEditCompany(null);
    loadInitial();
  }

  return (
    <div className="landing">
      <div className="admin-top-bar">
        <button
          className="admin-add-btn"
          onClick={() => {
            setEditCompany(null);
            setShowModal(true);
          }}
        >
          Add Company
        </button>

        <SearchBar onSearch={onSearch} />
      </div>

      {loading && allCompanies.length === 0 && !topMatch ? (
        <p className="state">Loading companies…</p>
      ) : (
        <>
          {query.trim() ? (
            topMatch ? (
              <section style={{ marginBottom: 18 }}>
                <h4 className="section-title">Top match</h4>
                <div className="company-grid">
                  <AdminCompanyCard
                    company={topMatch}
                    onEdit={() => {
                      setEditCompany(topMatch);
                      setShowModal(true);
                    }}
                    onPrep={() => setPrepCompany(topMatch)}
                    onExam={() => setExamCompany(topMatch)}
                  />
                </div>
              </section>
            ) : (
              <p className="state">{noExactMsg}</p>
            )
          ) : null}

          <section>
            <h4 className="section-title">All companies</h4>

            {allCompanies.length === 0 ? (
              <p className="state">No companies yet</p>
            ) : (
              <>
                <div className="company-grid">
                  {allCompanies.map((c) => (
                    <AdminCompanyCard
                      key={c.id}
                      company={c}
                      onEdit={() => {
                        setEditCompany(c);
                        setShowModal(true);
                      }}
                      onPrep={() => setPrepCompany(c)}
                      onExam={() => setExamCompany(c)}
                    />
                  ))}
                </div>

                <div style={{ textAlign: "center", margin: 18 }}>
                  {!last ? (
                    <button className="pager-btn" onClick={loadMoreAll}>
                      {loading ? "Loading…" : "Load more"}
                    </button>
                  ) : (
                    <span className="state" style={{ fontSize: 13 }}>
                      Showing {allCompanies.length} result
                      {allCompanies.length === 1 ? "" : "s"}
                      {totalPages > 1
                        ? ` (page ${page + 1} of ${totalPages})`
                        : ""}
                    </span>
                  )}
                </div>
              </>
            )}
          </section>
        </>
      )}

      {showModal && (
        <AdminModal
          initial={editCompany}
          onClose={() => {
            setShowModal(false);
            setEditCompany(null);
          }}
          onSubmit={saveCompany}
        />
      )}

      {prepCompany && (
        <AdminPrepModal
          company={prepCompany}
          onClose={() => setPrepCompany(null)}
        />
      )}

      {examCompany && (
        <AdminExamModal
          company={examCompany}
          onClose={() => setExamCompany(null)}
          onCreated={loadInitial}
        />
      )}
    </div>
  );
}
