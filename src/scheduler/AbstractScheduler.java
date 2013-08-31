package scheduler;

import actors.Assistants;
import actors.Costumer;
import actors.exceptions.DishFinishedException;
import event.TimeListener;
import logger.HTMLLogger;
import models.Dish;
import models.RecipeTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/28/13
 * Time: 5:30 AM
 * TODO: Brief description of the class
 */
public abstract class AbstractScheduler implements Scheduler, TimeListener {
    public String name;
    Costumer costumer;
    Assistants assistants;

    public AbstractScheduler(Costumer costumer, Assistants assistants){
        this.costumer = costumer;
        this.assistants = assistants;
    }

    public void sendDishToAssistants(Dish dish){
        assistants.addDish(dish);
    }

    public abstract List<Dish> getReadyToCookDishes();
    public abstract Dish dequeueReadyToCook();
    public abstract void addReadyToCook(Dish dish);

    @Override
    public void time(Integer currentTime) {
        callAssistants();
        checkAndSortCostumerOrders(currentTime);
        HTMLLogger.ready = HTMLLogger.convertDishes(getReadyToCookDishes());
        System.out.println("getReadyToCookDishes() = " + getReadyToCookDishes());
    }

    public List<Dish> callAssistants(){
        System.out.println("Assistants are preparing the food: " + assistants);
        List<Dish> readyToCookDishes = null;
        try {
            readyToCookDishes = assistants.prepareFood();
        } catch (DishFinishedException e) {
            assistants.removeDish(e.dish);
            readyToCookDishes = new ArrayList<Dish>();
        }
        for (Dish dish: readyToCookDishes){
            assistants.removeDish(dish);
        }
        return readyToCookDishes;
    }

    public List<Dish> checkAndSortCostumerOrders(Integer currentTime){
        // new order comes in
        List<Dish> newOrders = this.costumer.takeOrder(currentTime);
        if (!newOrders.isEmpty()){
            System.out.println("Costumer sends out new orders : " + newOrders);
            // examine nature of order
            for(Dish dish: newOrders){
                if (dish.currentTask().isCook()){
                    addReadyToCook(dish);
                } else {
                    sendDishToAssistants(dish);
                }
            }
        }
        return newOrders;
    }

    public Boolean isEmpty(Integer currentTime){
        if ((!costumer.hasMoreOrders(currentTime)) && (!assistants.hasDishes()) && (getReadyToCookDishes().isEmpty())){
            System.out.println("Costumer and Assistants and Ready to Cook Dishes are empty!");
            return true;
        }
        return false;
    }
}
