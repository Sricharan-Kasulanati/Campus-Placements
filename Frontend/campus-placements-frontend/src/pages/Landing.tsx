import { useEffect, useState } from 'react';
import SearchBar from '../components/SearchBar';
import CompanyCard from '../components/CompanyCard';
import { searchCompanies } from '../api/company';
import { getMyRegistrationIds, registerForCompany } from '../api/registrations';
import type { Company } from '../types/company';
import '../styles/landing.css';

export default function Landing() {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [registeredIds, setRegisteredIds] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);

  async function load(query = '') {
    setLoading(true);
    const [list, ids] = await Promise.all([
      searchCompanies(query),
      getMyRegistrationIds()
    ]);

    const idsSet = new Set(ids);
    const sorted = list.length
      ? [...list].sort((a, b) => Number(!idsSet.has(a.id)) - Number(!idsSet.has(b.id)))
      : [];

    setCompanies(sorted);
    setRegisteredIds(ids);
    setLoading(false);
  }

  useEffect(() => { load(); }, []);

  async function handleRegister(id: number) {
    await registerForCompany(id);
    const ids = await getMyRegistrationIds();
    const idsSet = new Set(ids);
    const sorted = [...companies].sort(
      (a, b) => Number(!idsSet.has(a.id)) - Number(!idsSet.has(b.id))
    );
    setRegisteredIds(ids);
    setCompanies(sorted);
  }

  return (
    <div className="landing">
      <SearchBar onSearch={(q) => load(q)} />

      {loading ? (
        <p className="state">Loading companies…</p>
      ) : companies.length === 0 ? (
        <p className="state">No companies found.</p>
      ) : (
        <div className="company-grid">
          {companies.map((c) => (
            <CompanyCard
              key={c.id}
              company={c}
              isRegistered={registeredIds.includes(c.id)}
              onRegister={handleRegister}
            />
          ))}
        </div>
      )}
    </div>
  );
}
