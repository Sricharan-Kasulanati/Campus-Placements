import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/auth.css';
import { signup } from '../api/auth';
import { useAuth } from '../auth/AuthProvider';
import type { Role, SignupRequest } from '../types/auth';
import { HttpError } from '../api/http';

export default function Signup() {
  const nav = useNavigate();
  const { setSession } = useAuth();

  const [form, setForm] = useState<SignupRequest>({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    role: 'STUDENT',
  });

  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  function onChange(
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value } as SignupRequest));
  }

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      const res = await signup(form);
      setSession(res);
      nav('/');
    } catch (err) {
      if (err instanceof HttpError && err.status === 409) {
        setError('Email already exists');
      } else {
        setError((err as Error).message || 'Signup failed');
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1 className="auth-title">SIGN UP</h1>

        <form onSubmit={onSubmit}>
          {error && <div className="form-error">{error}</div>}

          <div className="row">
            <div className="field" style={{ flex: 1 }}>
              <span className="field-icon" aria-hidden>👤</span>
              <input
                className="input"
                name="firstName"
                placeholder="First name"
                value={form.firstName}
                onChange={onChange}
                required
              />
            </div>

            <div className="field" style={{ flex: 1 }}>
              <span className="field-icon" aria-hidden>👤</span>
              <input
                className="input"
                name="lastName"
                placeholder="Last name"
                value={form.lastName}
                onChange={onChange}
                required
              />
            </div>
          </div>

          <div className="field">
            <span className="field-icon" aria-hidden>📧</span>
            <input
              className="input"
              type="email"
              name="email"
              placeholder="Email"
              autoComplete="email"
              value={form.email}
              onChange={onChange}
              required
            />
          </div>

          <div className="field">
            <span className="field-icon" aria-hidden>🔒</span>
            <input
              className="input"
              type="password"
              name="password"
              placeholder="Password"
              autoComplete="new-password"
              value={form.password}
              onChange={onChange}
              required
            />
          </div>
          <div className="field">
            <span className="field-icon" aria-hidden>🎓</span>
            <select
              className="input"
              name="role"
              value={form.role}
              onChange={onChange}
            >
              <option value="STUDENT">Student</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>

          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? 'Creating account…' : 'CREATE ACCOUNT'}
          </button>
        </form>

        <div className="auth-footer">
          Already have an account?{' '}
          <Link to="/login" className="auth-link">Log in</Link>
        </div>
      </div>
    </div>
  );
}
