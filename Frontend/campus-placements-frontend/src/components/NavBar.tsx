import { NavLink, Link, useNavigate } from 'react-router-dom';
import '../styles/site.css';
import { useAuth } from '../auth/AuthProvider';

function getInitials(fullName: string): string {
  const trimmed = fullName.trim();
  if (!trimmed) return '';
  const parts = trimmed.split(/\s+/);

  const first = parts[0]?.[0] ?? '';
  const last = parts.length > 1 ? parts[parts.length - 1][0] : '';

  return (first + last).toUpperCase();
}

export default function NavBar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const initials = user ? getInitials(user.fullName) : '';

  function goToProfile() {
    navigate('/profile');
  }

  return (
    <nav className="nav">
      <div className="container nav-inner">
        <Link to="/" className="brand">
          <span className="brand-logo" aria-hidden />
          <span className="brand-title">Campus Placements</span>
        </Link>

        <div className="nav-links">
          <NavLink to="/" end className={({ isActive }) => `link ${isActive ? 'active' : ''}`}>
            Home
          </NavLink>

          {!user ? (
            <>
              <NavLink to="/login" className={({ isActive }) => `link ${isActive ? 'active' : ''}`}>
                Login
              </NavLink>
              <NavLink to="/signup" className="cta">
                Sign up
              </NavLink>
            </>
          ) : (
            <>
              <button
                type="button"
                className="avatar-btn"
                onClick={goToProfile}
                title="View / update profile"
              >
                {initials}
              </button>

              <button
                type="button"
                className="link"
                onClick={logout}
                style={{ border: 'none', background: 'transparent', cursor: 'pointer' }}
              >
                Logout
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
