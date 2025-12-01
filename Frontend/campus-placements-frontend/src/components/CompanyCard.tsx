import { Link } from "react-router-dom";
import type { Company } from "../types/company";
import "../styles/landing.css";

type Props = {
  company: Company;
  isRegistered: boolean;
  onRegister: (id: number) => void;
};

function short(text = "", max = 160) {
  const t = text.trim();
  return t.length > max ? t.slice(0, max - 1) + "…" : t;
}

export default function CompanyCard({
  company,
  isRegistered,
  onRegister,
}: Props) {
  const { id, name, location, website, description } = company;

  return (
    <div className="company-card">
      <div className="company-card__head">
        <h3 className="company-card__title">{name}</h3>
        {isRegistered && (
          <span className="company-card__badge">Registered</span>
        )}
      </div>

      <div className="company-card__meta">
        {location && (
          <span className="company-card__meta-item">{location}</span>
        )}
        {website && (
          <a
            className="company-card__meta-item company-card__link-inline"
            href={website}
            target="_blank"
            rel="noopener noreferrer"
            onClick={(e) => e.stopPropagation()}
          >
            {website.replace(/^https?:\/\/(www\.)?/i, "")}
          </a>
        )}
      </div>
      {description && (
        <p className="company-card__desc">{short(description, 160)}</p>
      )}
      {!isRegistered ? (
        <button className="company-card__btn" onClick={() => onRegister(id)}>
          Register
        </button>
      ) : (
        <>
          <Link className="company-card-prepare-tests" to={`/companies/${company.id}/prep`}>
            Prepare for tests
          </Link>

          <Link className="company-card__link" to={`/companies/${id}/exams?companyName=${encodeURIComponent(
        name || ""
      )}`}>
            Take Practice Tests
          </Link>
        </>
      )}
    </div>
  );
}
