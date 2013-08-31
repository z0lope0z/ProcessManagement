package models;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/26/13
 * Time: 3:00 PM
 * TODO: Brief description of the class
 */
public class DishOrder{
    public Dish dish;
    public Integer when;

    public DishOrder(Dish dish, Integer when) {
        this.dish = dish;
        this.when = when;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Dish dishOrder = (Dish) o;
        if (!dish.id.equals(dishOrder.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return dish.hashCode();
    }

    @Override
    public String toString() {
        return "DishOrder{" +
                "dish=" + dish.name +
                ", when=" + when +
                '}';
    }
}