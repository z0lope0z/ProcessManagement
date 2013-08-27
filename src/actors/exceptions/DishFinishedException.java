package actors.exceptions;

import models.Dish;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/26/13
 * Time: 1:42 PM
 * TODO: Brief description of the class
 */
public class DishFinishedException extends Exception {
    public Dish dish;

    public DishFinishedException(Dish dish) {
        this.dish = dish;
    }
}
