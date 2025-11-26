import { Link } from 'react-router-dom';
import type { Company } from '../types/company';
import '../styles/landing.css';

type Props = {
  company: Company;
  isRegistered: boolean;
  onRegister: (id: number) => void;
};

export default function CompanyCard({ company, isRegistered, onRegister }: Props) {
  return (
    <div className="company-card">
      <div className="company-card_image">
        
      </div>

      <div className="company-card__head">
        <h3 className="company-card__title">{company.name}</h3>
        {isRegistered && <span className="company-card__badge">Registered</span>}
      </div>

      <p className="company-card__desc">
        {(company.description ?? '').slice(0, 120)}
        {(company.description ?? '').length > 120 ? '…' : ''}
      </p>

      {!isRegistered ? (
        <button className="company-card__btn" onClick={() => onRegister(company.id)}>
          Register
        </button>
      ) : (
        <Link className="company-card__link" to={`/prepare/${company.id}`}>
          Prepare for tests
        </Link>
      )}
    </div>
  );
}
