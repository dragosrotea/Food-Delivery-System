import type { Restaurant } from "../types/restaurant";

type RestaurantCardProps = {
  restaurant: Restaurant;
};

export default function RestaurantCard({ restaurant }: RestaurantCardProps) {
  return (
    <article className="restaurant-card">
      <div className="restaurant-card__accent" aria-hidden="true">
        {restaurant.name.charAt(0).toUpperCase()}
      </div>
      <div>
        <p className="eyebrow">Open for orders</p>
        <h3>{restaurant.name}</h3>
        <p className="restaurant-card__address">
          {restaurant.street}, {restaurant.city}
        </p>
      </div>
      <button className="text-button" type="button" disabled>
        Menu coming next
      </button>
    </article>
  );
}
