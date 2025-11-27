import { createContext, useContext, useMemo, useState, type PropsWithChildren } from 'react';
import { getAuthToken, setAuthToken } from '../api/http';
import type { AuthResponse, Role } from '../types/auth';

type UserSummary = { fullName: string; role: Role } | null;
type AuthContextValue = {
  token: string | null;
  user: UserSummary;
  setSession: (a: AuthResponse) => void;
  logout: () => void;
};

const AuthCtx = createContext<AuthContextValue | null>(null);
export const useAuth = () => {
  const ctx = useContext(AuthCtx);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};

export default function AuthProvider({ children }: PropsWithChildren) {
  const [token, setToken] = useState<string | null>(getAuthToken());
  const [user, setUser] = useState<UserSummary>(null);

  const value = useMemo<AuthContextValue>(() => ({
    token, user,
    setSession: ({ token, fullName, role }) => {
      setToken(token); setUser({ fullName, role }); setAuthToken(token);
    },
    logout: () => { setToken(null); setUser(null); setAuthToken(); }
  }), [token, user]);

  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}
