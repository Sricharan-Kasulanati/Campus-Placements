import { useEffect, useState } from "react";
import SearchBar from "../components/SearchBar";
import CompanyCard from "../components/CompanyCard";
import { searchCompaniesPage } from "../api/company";
import { getMyRegistrationIds, registerForCompany } from "../api/registrations";
import type { Company } from "../types/company";
import type { Page } from "../types/page";
import "../styles/landing.css";

export default function Landing() {
  const [registeredIds, setRegisteredIds] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState("");
  const [topMatch, setTopMatch] = useState<Company | null>(null);
  const [noExactMsg, setNoExactMsg] = useState<string | null>(null);
  const [allCompanies, setAllCompanies] = useState<Company[]>([]);
  const [page, setPage] = useState(0);
  const [size] = useState(12);
  const [totalPages, setTotalPages] = useState(1);
  const [last, setLast] = useState(true);

  function hasRegistered(id: number) {
    return registeredIds.includes(id);
  }

  function sortRegisteredFirst(list: Company[], regIds: number[]) {
    const reg = new Set(regIds.map(Number));
    return [...list].sort((a, b) => {
      const aReg = reg.has(Number(a.id)) ? 0 : 1;
      const bReg = reg.has(Number(b.id)) ? 0 : 1;
      if (aReg !== bReg) return aReg - bReg;
      return (a.name || "").localeCompare(b.name || "");
    });
  }

  async function loadInitial() {
    setLoading(true);
    setQuery("");
    setTopMatch(null);
    setNoExactMsg(null);

    const [pgAll, ids] = await Promise.all([
      searchCompaniesPage("", 0, size, "name,asc"),
      getMyRegistrationIds(),
    ]);

    const all = pgAll.content ?? [];
    setAllCompanies(sortRegisteredFirst(all, ids));
    setRegisteredIds(ids);
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
    const [pgQ, ids, pgAll] = await Promise.all([
      searchCompaniesPage(q, 0, size, "name,asc"),
      getMyRegistrationIds(),
      searchCompaniesPage("", 0, size, "name,asc"),
    ]);
    setRegisteredIds(ids);

    const exact =
      (pgQ.content ?? []).find(
        (c) => c.name.trim().toLowerCase() === q.trim().toLowerCase()
      ) || null;

    if (q.trim() && !exact) {
      setNoExactMsg(`No companies named “${q}”.`);
    } else {
      setNoExactMsg(null);
    }
    setTopMatch(exact);

    const all = pgAll.content ?? [];
    const dedup = exact ? all.filter((c) => c.id !== exact.id) : all;
    setAllCompanies(sortRegisteredFirst(dedup, ids));
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
      ? (pgAll.content ?? []).filter((c) => c.id !== topMatch.id)
      : pgAll.content ?? [];
    setAllCompanies((prev) =>
      sortRegisteredFirst([...prev, ...extra], registeredIds)
    );
    setPage(next);
    setLast(pgAll.last);
    setTotalPages(pgAll.totalPages);
    setLoading(false);
  }

  async function handleRegister(id: number) {
    await registerForCompany(id);
    const ids = await getMyRegistrationIds();
    setRegisteredIds(ids);
    setAllCompanies((prev) => sortRegisteredFirst(prev, ids));
  }

  return (
    <div className="landing">
      <SearchBar onSearch={onSearch} />

      {loading && allCompanies.length === 0 && !topMatch ? (
        <p className="state">Loading companies…</p>
      ) : (
        <>
          {query.trim() ? (
            topMatch ? (
              <section style={{ marginBottom: 18 }}>
                <h4 className="section-title">Top match</h4>
                <div className="company-grid">
                  <CompanyCard
                    key={topMatch.id}
                    company={topMatch}
                    isRegistered={hasRegistered(topMatch.id)}
                    onRegister={handleRegister}
                  />
                </div>
              </section>
            ) : (
              <p className="state" style={{ marginTop: 6 }}>
                {noExactMsg}
              </p>
            )
          ) : null}

          <section>
            <h4 className="section-title">All companies</h4>

            {allCompanies.length === 0 ? (
              <p className="state">No companies to display.</p>
            ) : (
              <>
                <div className="company-grid">
                  {allCompanies.map((c) => (
                    <CompanyCard
                      key={c.id}
                      company={c}
                      isRegistered={hasRegistered(c.id)}
                      onRegister={handleRegister}
                    />
                  ))}
                </div>

                <div
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    margin: "18px 0",
                  }}
                >
                  {!last ? (
                    <button
                      className="pager-btn"
                      onClick={loadMoreAll}
                      disabled={loading}
                    >
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
    </div>
  );
}
