import { useState, type FormEvent } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { register } from "../api/auth";
import { ApiError } from "../api/client";
import { useAuth } from "../auth/AuthContext";

export default function RegisterPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [submitting, setSubmitting] = useState(false);

  if (user) return <Navigate to="/" replace />;

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setErrorMessage("");
    if (password !== confirmPassword) {
      setErrorMessage("The passwords do not match.");
      return;
    }
    setSubmitting(true);
    try {
      await register({ email: email.trim(), password });
      navigate("/login", { replace: true, state: { registered: true } });
    } catch (error) {
      setErrorMessage(
        error instanceof ApiError && error.status === 409
          ? "An account with this email already exists."
          : error instanceof Error ? error.message : "Registration could not be completed.",
      );
    } finally {
      setSubmitting(false);
    }
  }

  return <section className="auth-page"><div className="auth-card">
    <p className="eyebrow">Join Foodie</p><h1>Create your account</h1>
    <p className="auth-card__intro">Public registration creates a customer account. Driver and admin accounts are managed separately.</p>
    {errorMessage && <div className="form-notice form-notice--error" role="alert">{errorMessage}</div>}
    <form className="auth-form" onSubmit={submit}>
      <label>Email<input type="email" value={email} onChange={(event) => setEmail(event.target.value)} autoComplete="email" required maxLength={254} /></label>
      <label>Password<input type="password" value={password} onChange={(event) => setPassword(event.target.value)} autoComplete="new-password" required minLength={8} maxLength={72} /></label>
      <label>Confirm password<input type="password" value={confirmPassword} onChange={(event) => setConfirmPassword(event.target.value)} autoComplete="new-password" required minLength={8} maxLength={72} /></label>
      <button className="button" type="submit" disabled={submitting}>{submitting ? "Creating account…" : "Create account"}</button>
    </form>
    <p className="auth-card__switch">Already have an account? <Link to="/login">Log in</Link></p>
  </div></section>;
}
