import { Link } from "react-router-dom";

export default function AccessDeniedPage() {
  return <section className="page-section compact-page"><div className="placeholder-card">
    <p className="eyebrow">403</p><h1>This area is not available to your account.</h1>
    <p>You are logged in, but your role does not have permission to open this page.</p>
    <Link className="button" to="/">Return home</Link>
  </div></section>;
}
