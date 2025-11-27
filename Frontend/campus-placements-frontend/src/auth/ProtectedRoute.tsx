import { Navigate } from 'react-router-dom';
import type { PropsWithChildren } from 'react';
import { useAuth } from './AuthProvider';

export default function ProtectedRoute({ children }: PropsWithChildren) {
  const { token } = useAuth();
  if (!token) return <Navigate to="/login" replace />;
  return <>{children}</>;
}
