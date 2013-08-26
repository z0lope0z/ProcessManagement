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
}