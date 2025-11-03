import { NavLink, Link } from 'react-router-dom';
import '../styles/site.css';
import { useAuth } from '../auth/AuthProvider';

export default function NavBar() {
  const { user, logout } = useAuth();

  return (
    <nav className="nav">
      <div className="container nav-inner">
        <Link to="/" className="brand">
          <span className="brand-logo" aria-hidden />
          <span className="brand-title">Campus Placements</span>
        </Link>

        <div className="nav-links">
          <NavLink to="/" end className={({isActive}) => `link ${isActive ? 'active' : ''}`}>
            Home
          </NavLink>

          {!user ? (
            <>
              <NavLink to="/login" className={({isActive}) => `link ${isActive ? 'active' : ''}`}>
                Login
              </NavLink>
              <NavLink to="/signup" className="cta">Sign up</NavLink>
            </>
          ) : (
            <>
              <span className="link" style={{cursor:'default'}}>Hi, {user.fullName}</span>
              <button className="link" onClick={logout} style={{border:'none', background:'transparent', cursor:'pointer'}}>
                Logout
              </button>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
