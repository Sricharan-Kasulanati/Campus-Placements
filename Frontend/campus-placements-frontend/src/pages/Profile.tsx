import { useEffect, useState } from 'react';
import '../styles/auth.css';
import { getProfile, updateProfile } from '../api/user';
import type { UserProfile, UpdateProfileRequest } from '../types/user';
import { HttpError } from '../api/http';

export default function Profile() {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [form, setForm] = useState<UpdateProfileRequest>({
    firstName: '',
    lastName: '',
    email: '',
    newPassword: '',
  });
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    let mounted = true;
    setLoading(true);
    getProfile()
      .then((p) => {
        if (!mounted) return;
        setProfile(p);
        setForm({
          firstName: p.firstName,
          lastName: p.lastName,
          email: p.email,
          newPassword: '',
        });
      })
      .catch((e) => {
        if (!mounted) return;
        setError(e instanceof HttpError ? e.message : 'Failed to load profile');
      })
      .finally(() => setLoading(false));
    return () => {
      mounted = false;
    };
  }, []);

  function onChange(
    e: React.ChangeEvent<HTMLInputElement>
  ) {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  }

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError('');
    setSuccess('');
    setSaving(true);
    try {
      const updated = await updateProfile({
        ...form,
        newPassword: form.newPassword?.trim() || undefined,
      });
      setProfile(updated);
      setSuccess('Profile updated successfully.');
      setForm((f) => ({ ...f, newPassword: '' }));
    } catch (e) {
      setError(e instanceof HttpError ? e.message : 'Failed to update profile');
    } finally {
      setSaving(false);
    }
  }

  if (loading && !profile) {
    return (
      <div className="auth-page">
        <div className="auth-card">Loading profile…</div>
      </div>
    );
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <h1 className="auth-title">Update Profile</h1>

        <form onSubmit={onSubmit}>
          {error && <div className="form-error">{error}</div>}
          {success && (
            <div className="form-error" style={{background:'#ecfdf3', borderColor:'#bbf7d0', color:'#166534'}}>
              {success}
            </div>
          )}

          <p>First Name</p>
          <div className="field">
            <span className="field-icon">👤</span>
            <input
              className="input"
              name="firstName"
              placeholder="First name"
              value={form.firstName}
              onChange={onChange}
              required
            />
          </div>

          <p>Last Name</p>
          <div className="field">
            <span className="field-icon">👤</span>
            <input
              className="input"
              name="lastName"
              placeholder="Last name"
              value={form.lastName}
              onChange={onChange}
              required
            />
          </div>

          <p>Email</p>
          <div className="field">
            <span className="field-icon">📧</span>
            <input
              className="input"
              type="email"
              name="email"
              placeholder="Email"
              value={form.email}
              onChange={onChange}
              required
            />
          </div>

          <p>New Password</p>
          <div className="field">
            <span className="field-icon">🔐</span>
            <input
              className="input"
              type="password"
              name="newPassword"
              placeholder="Password"
              value={form.newPassword}
              onChange={onChange}
            />
          </div>
          <button className="btn btn-primary" type="submit" disabled={saving}>
            {saving ? 'Saving…' : 'Save changes'}
          </button>
        </form>
      </div>
    </div>
  );
}
