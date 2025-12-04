import { Company } from "types/company";

function AdminCompanyCard({ company, onEdit, onPrep, onExam, onEditExam }:{
  company: Company, onEdit:()=>void, onPrep:()=>void, onExam:()=>void, onEditExam: () => void; 
}) {
  return (
    <div className="company-card admin-company-card">
      <h3>{company.name}</h3>

      <p className="admin-company-meta">
        {company.location} {company.category && <> | {company.category}</>}
      </p>

      <p className="admin-company-description">{company.description}</p>

      <div className="admin-card-actions">
        <button className="pager-btn" onClick={onEdit}>Update</button>
        <button className="pager-btn" onClick={onPrep}>Add Prep Papers</button>
        <button className="pager-btn" onClick={onExam}>Add Practice Exam</button>
        <button className="pager-btn" onClick={onEditExam}>Edit Exam</button>
      </div>
    </div>
  );
}

export default AdminCompanyCard;