package actors;

import models.Dish;
import models.DishOrder;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Dish producer
 * User: lemano
 * Date: 8/26/13
 * Time: 8:07 AM
 * Check this every minute
 */
public class Costumer {
    List<DishOrder> orderList = new ArrayList<DishOrder>();

    public Costumer(List<DishOrder> orderList) {
        this.orderList = orderList;
    }

    public List<Dish> takeOrder(Integer time){
        List<Dish> currentlyOrdered = new ArrayList<Dish>();
        for (DishOrder dishOrder: this.orderList){
            if (dishOrder.when == time){
                currentlyOrdered.add(dishOrder.dish);
            }
        }
        return currentlyOrdered;
    }
}
