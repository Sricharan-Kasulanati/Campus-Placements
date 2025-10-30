import { useState } from 'react';
import TextField from '../components/TextField';
import Button from '../components/Button';
import FormError from '../components/FormError';
import { login } from '../api/auth';
import { useAuth } from '../auth/AuthProvider';
import { Link, useNavigate } from 'react-router-dom';
import type { LoginRequest } from '../types/auth';
import { HttpError } from '../api/http';
import '../styles/auth.css';


export default function Login() {
  const nav = useNavigate();
  const { setSession } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [err, setErr] = useState('');
  const [loading, setLoading] = useState(false);

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault(); setErr(''); setLoading(true);
    try { const res = await login({ email, password }); setSession(res); nav('/'); }
    catch (e) { setErr(e instanceof HttpError ? (e.status===401?'Invalid email or password': e.message) : 'Login failed'); }
    finally { setLoading(false); }
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1 className="auth-title">LOGIN</h1>

        <form onSubmit={onSubmit}>
          {err && <div className="form-error">{err}</div>}

          <div className="field">
            <span className="field-icon">📧</span>
            <input
              className="input"
              type="email"
              placeholder="Email"
              autoComplete="email"
              value={email}
              onChange={e=>setEmail(e.target.value)}
              required
            />
          </div>

          <div className="field">
            <span className="field-icon">🔒</span>
            <input
              className="input"
              type="password"
              placeholder="Password"
              autoComplete="current-password"
              value={password}
              onChange={e=>setPassword(e.target.value)}
              required
            />
          </div>

          <div className="row">
            <label className="auth-subtle" style={{display:'flex',alignItems:'center',gap:8}}>
              <input type="checkbox" /> Remember me
            </label>
            <a className="auth-link" href="#">Forgot password?</a>
          </div>

          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? 'Logging in…' : 'LOGIN'}
          </button>
        </form>

        <div className="auth-footer">
          Not a member? <Link to="/signup" className="auth-link">Sign up now</Link>
        </div>
      </div>
    </div>
  );
}