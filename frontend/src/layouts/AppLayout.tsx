import { NavLink, Outlet } from "react-router-dom";

export default function AppLayout() {
  return (
    <div className="app-shell">
      <header className="site-header">
        <NavLink className="brand" to="/" aria-label="Food Delivery home">
          <span className="brand-mark" aria-hidden="true">
            F
          </span>
          <span>Food Delivery</span>
        </NavLink>

        <nav className="main-nav" aria-label="Main navigation">
          <NavLink to="/">Restaurants</NavLink>
          <NavLink className="button button-small" to="/login">
            Log in
          </NavLink>
        </nav>
      </header>

      <main>
        <Outlet />
      </main>

      <footer className="site-footer">
        <p>Built with React and Spring Boot.</p>
      </footer>
    </div>
  );
}
