import { useEffect, useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { ApiError } from "../api/client";
import { getRestaurant, getRestaurantMenu } from "../api/restaurants";
import MenuItemCard from "../components/MenuItemCard";
import type { MenuItem } from "../types/menuItem";
import type { Restaurant } from "../types/restaurant";

type LoadState = "loading" | "success" | "error" | "not-found";

export default function RestaurantPage() {
  const { restaurantId } = useParams();
  const numericId = Number(restaurantId);
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [loadState, setLoadState] = useState<LoadState>("loading");
  const [errorMessage, setErrorMessage] = useState("");
  const [activeCategory, setActiveCategory] = useState("All");
  const [search, setSearch] = useState("");

  async function loadRestaurant() {
    if (!Number.isInteger(numericId) || numericId <= 0) {
      setLoadState("not-found");
      return;
    }

    setLoadState("loading");
    setErrorMessage("");

    try {
      const [restaurantData, menuData] = await Promise.all([
        getRestaurant(numericId),
        getRestaurantMenu(numericId),
      ]);
      setRestaurant(restaurantData);
      setMenuItems(menuData);
      setLoadState("success");
    } catch (error) {
      if (error instanceof ApiError && error.status === 404) {
        setLoadState("not-found");
        return;
      }

      setErrorMessage(
        error instanceof Error ? error.message : "Could not load this menu.",
      );
      setLoadState("error");
    }
  }

  useEffect(() => {
    void loadRestaurant();
  }, [restaurantId]);

  const categories = useMemo(
    () => ["All", ...Array.from(new Set(menuItems.map((item) => item.category)))],
    [menuItems],
  );

  const visibleItems = useMemo(() => {
    const normalizedSearch = search.trim().toLowerCase();

    return menuItems.filter((item) => {
      const matchesCategory =
        activeCategory === "All" || item.category === activeCategory;
      const matchesSearch =
        normalizedSearch.length === 0 ||
        item.name.toLowerCase().includes(normalizedSearch) ||
        item.description.toLowerCase().includes(normalizedSearch);

      return matchesCategory && matchesSearch;
    });
  }, [activeCategory, menuItems, search]);

  if (loadState === "loading") {
    return (
      <section className="content-page">
        <div className="status-panel" role="status">
          <span className="spinner" aria-hidden="true" />
          Loading restaurant and menu…
        </div>
      </section>
    );
  }

  if (loadState === "not-found") {
    return (
      <section className="content-page compact-page">
        <div className="placeholder-card">
          <p className="eyebrow">Unavailable</p>
          <h1>That restaurant is not available.</h1>
          <p>
            It may not exist, or it may currently be inactive and hidden from
            public browsing.
          </p>
          <Link className="button" to="/">
            Browse restaurants
          </Link>
        </div>
      </section>
    );
  }

  if (loadState === "error") {
    return (
      <section className="content-page compact-page">
        <div className="status-panel status-panel--error" role="alert">
          <div>
            <strong>The menu could not be loaded.</strong>
            <p>{errorMessage}</p>
          </div>
          <button className="button button-small" onClick={loadRestaurant}>
            Try again
          </button>
        </div>
      </section>
    );
  }

  if (!restaurant) {
    return null;
  }

  return (
    <section className="content-page restaurant-page">
      <Link className="back-link" to="/">
        <span aria-hidden="true">←</span> All restaurants
      </Link>

      <header className="restaurant-hero">
        <div>
          <p className="eyebrow">Open for orders</p>
          <h1>{restaurant.name}</h1>
          <p>{restaurant.street}, {restaurant.city}</p>
        </div>
        <div className="restaurant-hero__mark" aria-hidden="true">
          {restaurant.name.charAt(0).toUpperCase()}
        </div>
      </header>

      <div className="menu-heading">
        <div>
          <p className="eyebrow">Fresh choices</p>
          <h2>Explore the menu</h2>
        </div>
        <label className="menu-search">
          <span className="visually-hidden">Search this menu</span>
          <input
            type="search"
            placeholder="Search this menu"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
        </label>
      </div>

      {menuItems.length === 0 ? (
        <div className="status-panel">
          <div>
            <strong>No menu items are available right now.</strong>
            <p>Please check this restaurant again later.</p>
          </div>
        </div>
      ) : (
        <>
          <div className="category-tabs" aria-label="Menu categories">
            {categories.map((category) => (
              <button
                className={category === activeCategory ? "active" : ""}
                key={category}
                type="button"
                onClick={() => setActiveCategory(category)}
              >
                {category}
              </button>
            ))}
          </div>

          {visibleItems.length === 0 ? (
            <div className="status-panel">
              <div>
                <strong>No matching dishes.</strong>
                <p>Try another category or search term.</p>
              </div>
            </div>
          ) : (
            <div className="menu-grid">
              {visibleItems.map((item) => (
                <MenuItemCard item={item} key={item.id} />
              ))}
            </div>
          )}
        </>
      )}
    </section>
  );
}
