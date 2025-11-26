import { useState } from 'react';
import '../styles/site.css';

type Props = { initial?: string; onSearch: (q: string) => void };

export default function SearchBar({ initial = '', onSearch }: Props) {
  const [q, setQ] = useState(initial);

  return (
    <form
      className="submit-form"
      onSubmit={(e) => {
        e.preventDefault();
        onSearch(q.trim());
      }}
      role="search"
    >
      <input
        className="submit-input"
        value={q}
        onChange={(e) => setQ(e.target.value)}
        placeholder="Search companies by name, role, tech…"
        aria-label="Search companies"
      />
      <button type="submit" className="submit-btn">Search</button>
    </form>
  );
}
