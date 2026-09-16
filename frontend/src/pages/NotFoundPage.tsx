import { Link } from "react-router-dom";

export default function NotFoundPage() {
  return (
    <section className="page-section compact-page">
      <div className="placeholder-card">
        <p className="eyebrow">404</p>
        <h1>That page is not on the menu.</h1>
        <p>The address may be incorrect or the page may have moved.</p>
        <Link className="button" to="/">
          Return home
        </Link>
      </div>
    </section>
  );
}
