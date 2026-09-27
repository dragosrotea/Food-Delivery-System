export type UserRole = "CUSTOMER" | "DRIVER" | "ADMIN";
export type AuthUser = { email: string; role: UserRole };
export type AuthResponse = { accessToken: string; tokenType: string; expiresIn: number };
export type RegisterResponse = { id: number; email: string; role: UserRole };
