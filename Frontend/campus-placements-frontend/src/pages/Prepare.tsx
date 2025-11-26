import { useParams } from 'react-router-dom';

export default function Prepare() {
  const { companyId } = useParams();
  return (
    <div style={{ maxWidth:900, margin:'0 auto', padding:'24px 16px' }}>
      <h2 style={{ fontWeight:800 }}>Prepare for Tests</h2>
      <p>Company ID: <b>{companyId}</b></p>
      {}
    </div>
  );
}
