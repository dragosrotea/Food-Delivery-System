import { useState, type FormEvent } from "react";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import { login } from "../api/auth";
import { ApiError } from "../api/client";
import { useAuth } from "../auth/AuthContext";
import type { UserRole } from "../types/auth";

const roleDestinations: Record<UserRole, string> = {
  CUSTOMER: "/account", DRIVER: "/driver", ADMIN: "/admin",
};

export default function LoginPage() {
  const { user, signIn } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [submitting, setSubmitting] = useState(false);

  if (user) return <Navigate to={roleDestinations[user.role]} replace />;

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);
    setErrorMessage("");
    try {
      const response = await login({ email: email.trim(), password });
      const authenticatedUser = signIn(response.accessToken);
      if (!authenticatedUser) {
        setErrorMessage("The server returned an invalid access token.");
        return;
      }
      const destination = (location.state as { from?: string } | null)?.from;
      navigate(destination ?? roleDestinations[authenticatedUser.role], { replace: true });
    } catch (error) {
      setErrorMessage(
        error instanceof ApiError && error.status === 401
          ? "The email or password is incorrect."
          : error instanceof Error ? error.message : "Login could not be completed.",
      );
    } finally {
      setSubmitting(false);
    }
  }

  return <section className="auth-page"><div className="auth-card">
    <p className="eyebrow">Welcome back</p><h1>Log in to continue</h1>
    <p className="auth-card__intro">Use the account connected to your customer, driver or admin role.</p>
    {(location.state as { registered?: boolean } | null)?.registered &&
      <div className="form-notice form-notice--success" role="status">Account created. You can now log in.</div>}
    {errorMessage && <div className="form-notice form-notice--error" role="alert">{errorMessage}</div>}
    <form className="auth-form" onSubmit={submit}>
      <label>Email<input type="email" value={email} onChange={(event) => setEmail(event.target.value)} autoComplete="email" required maxLength={254} /></label>
      <label>Password<input type="password" value={password} onChange={(event) => setPassword(event.target.value)} autoComplete="current-password" required minLength={8} maxLength={72} /></label>
      <button className="button" type="submit" disabled={submitting}>{submitting ? "Logging in…" : "Log in"}</button>
    </form>
    <p className="auth-card__switch">New to Foodie? <Link to="/register">Create a customer account</Link></p>
  </div></section>;
}
