package scheduler.models;

import models.Dish;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/27/13
 * Time: 1:44 AM
 * TODO: Brief description of the class
 */
public class FCFSDish extends Dish {
    public Integer order;
    public String name;

    public FCFSDish(Integer order, Dish dish){
        super(dish.name, dish.recipeTaskList);
        this.id = dish.id;
        this.name = dish.name;
        this.order = order;
    }

    @Override
    public String toString() {
        return "FCFSDish{" +
                "order=" + order +
                ", name='" + name + '\'' +
                ", tasks='" + recipeTaskList + '\'' +
                '}';
    }
}