import { Navigate } from "react-router-dom";
import type { PropsWithChildren } from "react";
import { useAuth } from "./AuthProvider";

type ProtectedProps = PropsWithChildren & {
  role?: "ADMIN" | "STUDENT";
};

export default function ProtectedRoute({ children, role }: ProtectedProps) {
  const { token, user } = useAuth();

  if (!token) return <Navigate to="/login" replace />;
  if (role && user?.role !== role) {
    return (
      <Navigate to={user?.role === "ADMIN" ? "/admin" : "/landing"} replace />
    );
  }
  return <>{children}</>;
}
