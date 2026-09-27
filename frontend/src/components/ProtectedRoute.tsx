import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import type { UserRole } from "../types/auth";

export default function ProtectedRoute({ allowedRoles }: { allowedRoles?: UserRole[] }) {
  const { user } = useAuth();
  const location = useLocation();
  if (!user) return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  if (allowedRoles && !allowedRoles.includes(user.role)) return <Navigate to="/forbidden" replace />;
  return <Outlet />;
}
