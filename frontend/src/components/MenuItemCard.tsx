import type { MenuItem } from "../types/menuItem";

type MenuItemCardProps = {
  item: MenuItem;
};

export default function MenuItemCard({ item }: MenuItemCardProps) {
  return (
    <article className="menu-card">
      <div className="menu-card__visual" aria-hidden="true">
        <span>{item.name.charAt(0).toUpperCase()}</span>
      </div>
      <div className="menu-card__body">
        <div>
          <p className="menu-card__category">{item.category}</p>
          <h3>{item.name}</h3>
          <p className="menu-card__description">{item.description}</p>
        </div>
        <div className="menu-card__footer">
          <strong>{item.price.toFixed(2)} RON</strong>
          <span className="menu-card__status">Available</span>
        </div>
      </div>
    </article>
  );
}
