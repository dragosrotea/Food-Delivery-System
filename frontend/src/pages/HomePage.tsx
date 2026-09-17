import { useEffect, useState } from "react";
import { getRestaurants } from "../api/restaurants";
import RestaurantCard from "../components/RestaurantCard";
import type { Restaurant } from "../types/restaurant";

type LoadState = "loading" | "success" | "error";

export default function HomePage() {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loadState, setLoadState] = useState<LoadState>("loading");
  const [errorMessage, setErrorMessage] = useState("");

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

  return (
    <>
      <section className="hero page-section">
        <div className="hero__content">
          <p className="eyebrow">Dinner, without the detour</p>
          <h1>Good food from local places, delivered simply.</h1>
          <p className="hero__copy">
            Browse active restaurants now. Ordering, live status updates and
            delivery tracking will join this experience step by step.
          </p>
          <a className="button" href="#restaurants">
            Browse restaurants
          </a>
        </div>

        <div className="hero__visual" aria-hidden="true">
          <div className="plate">
            <span className="plate__leaf plate__leaf--one" />
            <span className="plate__leaf plate__leaf--two" />
            <span className="plate__center">Fresh</span>
          </div>
          <span className="hero__shape hero__shape--one" />
          <span className="hero__shape hero__shape--two" />
        </div>
      </section>

      <section className="page-section restaurant-section" id="restaurants">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Available near you</p>
            <h2>Choose a restaurant</h2>
          </div>
          <p>Loaded directly from the Spring Boot API.</p>
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

        {loadState === "success" && restaurants.length > 0 && (
          <div className="restaurant-grid">
            {restaurants.map((restaurant) => (
              <RestaurantCard key={restaurant.id} restaurant={restaurant} />
            ))}
          </div>
        )}
      </section>
    </>
  );
}
