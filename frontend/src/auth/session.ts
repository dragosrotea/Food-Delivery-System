import type { AuthUser, UserRole } from "../types/auth";

const TOKEN_KEY = "food-delivery-token";
type TokenPayload = { sub?: string; role?: string; exp?: number };

function decodePayload(token: string): TokenPayload | null {
  try {
    const encodedPayload = token.split(".")[1];
    if (!encodedPayload) return null;
    const base64 = encodedPayload.replace(/-/g, "+").replace(/_/g, "/");
    const padded = base64.padEnd(Math.ceil(base64.length / 4) * 4, "=");
    return JSON.parse(window.atob(padded)) as TokenPayload;
  } catch {
    return null;
  }
}

function isRole(value: string | undefined): value is UserRole {
  return value === "CUSTOMER" || value === "DRIVER" || value === "ADMIN";
}

export function getStoredToken(): string | null { return sessionStorage.getItem(TOKEN_KEY); }
export function storeToken(token: string): void { sessionStorage.setItem(TOKEN_KEY, token); }
export function clearStoredSession(): void { sessionStorage.removeItem(TOKEN_KEY); }

export function readUserFromToken(token: string): AuthUser | null {
  const payload = decodePayload(token);
  if (!payload?.sub || !isRole(payload.role) || !payload.exp) return null;
  if (payload.exp * 1000 <= Date.now()) return null;
  return { email: payload.sub, role: payload.role };
}
