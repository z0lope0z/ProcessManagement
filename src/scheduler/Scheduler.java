package scheduler;

import models.Dish;

import java.util.Vector;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/26/13
 * Time: 8:28 AM
 * TODO: Brief description of the class
 */
public interface Scheduler {
    public Dish whatIsNext(Dish currentDish, Vector<Dish> dishesQueue);
}
