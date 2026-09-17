import { FormEvent, useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";

export default function AppLayout() {
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  function submitSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const query = search.trim();
    navigate(query ? `/?q=${encodeURIComponent(query)}#restaurants` : "/#restaurants");
  }

  return (
    <div className="app-frame">
      <aside className="side-nav">
        <NavLink className="brand brand--vertical" to="/" aria-label="Food Delivery home">
          <span className="brand-mark" aria-hidden="true">F</span>
          <span className="brand-name">Foodie</span>
        </NavLink>

        <nav className="side-nav__links" aria-label="Primary navigation">
          <NavLink to="/" end>
            <span aria-hidden="true">⌂</span>
            <span>Discover</span>
          </NavLink>
          <a href="/#restaurants">
            <span aria-hidden="true">◫</span>
            <span>Restaurants</span>
          </a>
          <span className="nav-disabled" aria-disabled="true">
            <span aria-hidden="true">♡</span>
            <span>Orders soon</span>
          </span>
        </nav>

        <NavLink className="side-nav__login" to="/login">
          <span aria-hidden="true">◎</span>
          <span>Log in</span>
        </NavLink>
      </aside>

      <div className="app-workspace">
        <header className="top-bar">
          <div>
            <p className="top-bar__eyebrow">Food Delivery</p>
            <strong>Discover good food</strong>
          </div>

          <form className="global-search" role="search" onSubmit={submitSearch}>
            <label>
              <span className="visually-hidden">Search restaurants</span>
              <input
                type="search"
                placeholder="Search restaurants…"
                value={search}
                onChange={(event) => setSearch(event.target.value)}
              />
            </label>
            <button type="submit">Search</button>
          </form>

          <NavLink className="account-pill" to="/login">
            <span className="account-pill__avatar" aria-hidden="true">G</span>
            <span>
              <strong>Guest</strong>
              <small>Log in</small>
            </span>
          </NavLink>
        </header>

        <main className="app-content">
          <Outlet />
        </main>
      </div>

      <nav className="mobile-nav" aria-label="Mobile navigation">
        <NavLink to="/" end><span aria-hidden="true">⌂</span><small>Home</small></NavLink>
        <a href="/#restaurants"><span aria-hidden="true">◫</span><small>Browse</small></a>
        <NavLink to="/login"><span aria-hidden="true">◎</span><small>Account</small></NavLink>
      </nav>
    </div>
  );
}
