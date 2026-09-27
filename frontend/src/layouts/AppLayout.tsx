import { FormEvent, useState } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { useCart } from "../cart/CartContext";

const accountPaths = { CUSTOMER: "/account", DRIVER: "/driver", ADMIN: "/admin" } as const;

export default function AppLayout() {
  const [search, setSearch] = useState("");
  const navigate = useNavigate();
  const { user, signOut } = useAuth();
  const { itemCount } = useCart();
  const accountPath = user ? accountPaths[user.role] : "/login";

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
          <NavLink to="/cart"><span aria-hidden="true">▢</span><span>Cart {itemCount > 0 && `(${itemCount})`}</span></NavLink>
          {user?.role === "CUSTOMER" && <NavLink to="/account"><span aria-hidden="true">♡</span><span>Orders</span></NavLink>}
        </nav>

        <NavLink className="side-nav__login" to={accountPath}>
          <span aria-hidden="true">◎</span>
          <span>{user ? "Account" : "Log in"}</span>
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

          <NavLink className="account-pill" to={accountPath}>
            <span className="account-pill__avatar" aria-hidden="true">{user?.email.charAt(0).toUpperCase() ?? "G"}</span>
            <span>
              <strong>{user?.email ?? "Guest"}</strong>
              <small>{user?.role.toLowerCase() ?? "Log in"}</small>
            </span>
          </NavLink>
          {user && <button className="logout-button" type="button" onClick={() => { signOut(); navigate("/"); }}>Log out</button>}
        </header>

        <main className="app-content">
          <Outlet />
        </main>
      </div>

      <nav className="mobile-nav" aria-label="Mobile navigation">
        <NavLink to="/" end><span aria-hidden="true">⌂</span><small>Home</small></NavLink>
        <NavLink to="/cart"><span aria-hidden="true">▢</span><small>Cart {itemCount > 0 && `(${itemCount})`}</small></NavLink>
        <NavLink to={accountPath}><span aria-hidden="true">◎</span><small>Account</small></NavLink>
      </nav>
    </div>
  );
}
