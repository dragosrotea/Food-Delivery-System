import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import { clearStoredSession, getStoredToken, readUserFromToken, storeToken } from "./session";
import type { AuthUser } from "../types/auth";

type AuthContextValue = { user: AuthUser | null; signIn: (token: string) => AuthUser | null; signOut: () => void };
const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const token = getStoredToken();
    return token ? readUserFromToken(token) : null;
  });

  function signOut() { clearStoredSession(); setUser(null); }
  function signIn(token: string) {
    const authenticatedUser = readUserFromToken(token);
    if (!authenticatedUser) return null;
    storeToken(token);
    setUser(authenticatedUser);
    return authenticatedUser;
  }

  useEffect(() => {
    if (!user && getStoredToken()) clearStoredSession();
    window.addEventListener("auth:unauthorized", signOut);
    return () => window.removeEventListener("auth:unauthorized", signOut);
  }, [user]);

  const value = useMemo(() => ({ user, signIn, signOut }), [user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used inside AuthProvider");
  return context;
}
