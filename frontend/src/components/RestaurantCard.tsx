import { Link } from "react-router-dom";
import type { Restaurant } from "../types/restaurant";

type RestaurantCardProps = {
  restaurant: Restaurant;
};

export default function RestaurantCard({ restaurant }: RestaurantCardProps) {
  return (
    <article className="restaurant-card">
      <div className="restaurant-card__visual" aria-hidden="true">
        <span>{restaurant.name.charAt(0).toUpperCase()}</span>
      </div>
      <div className="restaurant-card__body">
        <p className="eyebrow">Open for orders</p>
        <h3>{restaurant.name}</h3>
        <p className="restaurant-card__address">
          {restaurant.street}, {restaurant.city}
        </p>
        <Link
          className="card-link"
          to={`/restaurants/${restaurant.id}`}
          aria-label={`View ${restaurant.name} menu`}
        >
          View menu <span aria-hidden="true">→</span>
        </Link>
      </div>
    </article>
  );
}
