import { NavLink, Link, useNavigate } from 'react-router-dom';
import '../styles/site.css';
import { useAuth } from '../auth/AuthProvider';
import { useEffect, useMemo, useRef, useState } from 'react';

function initialsOf(name?: string, email?: string) {
  const n = (name ?? '').trim();
  if (n) {
    const p = n.split(/\s+/);
    return ((p[0]?.[0] ?? '') + (p.length > 1 ? p[p.length - 1][0] : '')).toUpperCase() || 'U';
  }
  return ((email ?? 'u')[0] || 'u').toUpperCase();
}

export default function NavBar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const initials = useMemo(() => initialsOf(user?.fullName), [user]);

  const [open, setOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);
  useEffect(() => {
    function onDoc(e: MouseEvent) {
      if (!menuRef.current?.contains(e.target as Node)) setOpen(false);
    }
    if (open) document.addEventListener('mousedown', onDoc);
    return () => document.removeEventListener('mousedown', onDoc);
  }, [open]);

  return (
    <nav className="nav">
      <div className="container nav-inner">
        <Link to="/" className="brand" aria-label="Campus Placements – Home">
          <span className="brand-logo" aria-hidden />
          <span className="brand-title">Campus Placements</span>
        </Link>

        <div className="nav-links">
          {!user ? (
            <>
              <NavLink to="/" end className={({ isActive }) => `link ${isActive ? 'active' : ''}`}>
                Home
              </NavLink>
              <NavLink to="/login" className={({ isActive }) => `link ${isActive ? 'active' : ''}`}>
                Login
              </NavLink>
              <NavLink to="/signup" className="cta">Sign up</NavLink>
            </>
          ) : (
            <>
              <NavLink to="/landing" end className={({ isActive }) => `link ${isActive ? 'active' : ''}`}>
                Home
              </NavLink>
              <NavLink to="/analytics" className={({ isActive }) => `link ${isActive ? 'active' : ''}`}>
                Analytics
              </NavLink>
              <div className="account" ref={menuRef}>
                <button
                  type="button"
                  className="avatar-btn"
                  aria-haspopup="menu"
                  aria-expanded={open}
                  title={user.fullName}
                  onClick={() => setOpen(v => !v)}
                  onKeyDown={(e) => { if (e.key === 'Enter' || e.key === ' ') setOpen(v => !v); }}
                >
                  {initials}
                </button>

                {open && (
                  <div className="menu" role="menu">
                    <button
                      className="menu-item"
                      role="menuitem"
                      onClick={() => { setOpen(false); navigate('/profile'); }}
                    >
                      Update Profile
                    </button>
                    <div className="menu-sep" />
                    <button
                      className="menu-item danger"
                      role="menuitem"
                      onClick={() => { setOpen(false); logout(); }}
                    >
                      Logout
                    </button>
                  </div>
                )}
              </div>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
