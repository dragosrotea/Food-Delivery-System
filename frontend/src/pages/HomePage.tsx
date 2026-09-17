import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { getRestaurants } from "../api/restaurants";
import RestaurantCard from "../components/RestaurantCard";
import type { Restaurant } from "../types/restaurant";

type LoadState = "loading" | "success" | "error";

const categoryCards = [
  { name: "Pizza", className: "category-card--terracotta", symbol: "P" },
  { name: "Fresh meals", className: "category-card--green", symbol: "F" },
  { name: "Desserts", className: "category-card--gold", symbol: "D" },
];

export default function HomePage() {
  const [searchParams] = useSearchParams();
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loadState, setLoadState] = useState<LoadState>("loading");
  const [errorMessage, setErrorMessage] = useState("");
  const search = searchParams.get("q")?.trim() ?? "";

  async function loadRestaurants() {
    setLoadState("loading");
    setErrorMessage("");

    try {
      const data = await getRestaurants();
      setRestaurants(data);
      setLoadState("success");
    } catch (error) {
      setErrorMessage(
        error instanceof Error ? error.message : "Could not load restaurants.",
      );
      setLoadState("error");
    }
  }

  useEffect(() => {
    void loadRestaurants();
  }, []);

  const visibleRestaurants = useMemo(() => {
    const normalizedSearch = search.toLowerCase();

    if (!normalizedSearch) {
      return restaurants;
    }

    return restaurants.filter(
      (restaurant) =>
        restaurant.name.toLowerCase().includes(normalizedSearch) ||
        restaurant.city.toLowerCase().includes(normalizedSearch),
    );
  }, [restaurants, search]);

  return (
    <div className="home-page">
      <section className="dashboard-hero">
        <div className="dashboard-hero__content">
          <p className="eyebrow eyebrow--light">Dinner, without the detour</p>
          <h1>Good food, delivered simply.</h1>
          <p>
            Discover active local restaurants and explore their freshly
            available menus.
          </p>
          <a className="button button--light" href="#restaurants">
            Explore restaurants
          </a>
        </div>
        <div className="food-art" aria-hidden="true">
          <div className="food-art__plate">
            <span className="food-art__leaf food-art__leaf--one" />
            <span className="food-art__leaf food-art__leaf--two" />
            <strong>Fresh</strong>
          </div>
          <span className="food-art__accent food-art__accent--gold" />
          <span className="food-art__accent food-art__accent--green" />
        </div>
      </section>

      <section className="category-section" aria-labelledby="category-title">
        <div className="section-title-row">
          <div>
            <p className="eyebrow">Browse the mood</p>
            <h2 id="category-title">What sounds good?</h2>
          </div>
          <span>More categories will grow with the menu.</span>
        </div>
        <div className="category-grid">
          {categoryCards.map((category) => (
            <a
              className={`category-card ${category.className}`}
              href="#restaurants"
              key={category.name}
            >
              <span>{category.symbol}</span>
              <strong>{category.name}</strong>
            </a>
          ))}
        </div>
      </section>

      <section className="restaurant-section" id="restaurants">
        <div className="section-title-row">
          <div>
            <p className="eyebrow">Available near you</p>
            <h2>{search ? `Results for “${search}”` : "Choose a restaurant"}</h2>
          </div>
          <span>Live data from the Spring Boot API.</span>
        </div>

        {loadState === "loading" && (
          <div className="status-panel" role="status">
            <span className="spinner" aria-hidden="true" />
            Loading restaurants…
          </div>
        )}

        {loadState === "error" && (
          <div className="status-panel status-panel--error" role="alert">
            <div>
              <strong>Restaurants are unavailable.</strong>
              <p>{errorMessage}</p>
            </div>
            <button className="button button-small" onClick={loadRestaurants}>
              Try again
            </button>
          </div>
        )}

        {loadState === "success" && restaurants.length === 0 && (
          <div className="status-panel">
            <div>
              <strong>No restaurants are active yet.</strong>
              <p>An administrator can add or activate one through the API.</p>
            </div>
          </div>
        )}

        {loadState === "success" &&
          restaurants.length > 0 &&
          visibleRestaurants.length === 0 && (
            <div className="status-panel">
              <div>
                <strong>No matching restaurants.</strong>
                <p>Try searching by another restaurant name or city.</p>
              </div>
            </div>
          )}

        {loadState === "success" && visibleRestaurants.length > 0 && (
          <div className="restaurant-grid">
            {visibleRestaurants.map((restaurant) => (
              <RestaurantCard key={restaurant.id} restaurant={restaurant} />
            ))}
          </div>
        )}
      </section>

      <aside className="cart-preview" aria-label="Future cart">
        <div>
          <p className="eyebrow">Your order</p>
          <h2>Cart coming next</h2>
          <p>
            Choose a restaurant and explore its menu. Adding items and checkout
            will be implemented as a separate, reviewable feature.
          </p>
        </div>
        <div className="cart-preview__empty" aria-hidden="true">
          <span>0</span>
          <small>items</small>
        </div>
      </aside>
    </div>
  );
}
