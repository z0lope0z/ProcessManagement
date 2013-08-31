package actors;

import actors.exceptions.DishFinishedException;
import logger.HTMLLogger;
import models.Dish;

import java.util.ArrayList;
import java.util.List;

/**
 * Assistants cannot cook
 * User: lemano
 * Date: 8/26/13
 * Time: 8:21 AM
 * TODO: Brief description of the class
 */
public class Assistants {
    List<Dish> dishes = new ArrayList<Dish>();

    public Assistants() {

    }

    public Assistants(List<Dish> dishes) {
        this.dishes.addAll(dishes);
    }

    public void addDish(Dish dish) {
        this.dishes.add(dish);
        HTMLLogger.addAssistantDish(dish);
    }

    public void removeDish(Dish dish) {
        this.dishes.remove(dish);
    }

    public Boolean hasDishes() {
        return !dishes.isEmpty();
    }

    /**
     * Prepares food if recipetask is not in a cook state
     *
     * @return list of dishes that are in a cook state
     */
    public List<Dish> prepareFood() throws DishFinishedException {
        List<Dish> cookStateDishes = new ArrayList<Dish>();
        if (dishes.isEmpty()) {
            return cookStateDishes;
        }
        System.out.println("Assistants are currently preparing the dishes : " + dishes);
        for (Dish dish : dishes) {
            if (!dish.isCook()) {
                if (dish.hasTime()) {
                    Integer timeLeft = dish.currentTask().work();
                    if (timeLeft == 0) {
                        dish.finishTask();
                        if (dish.isDone()) {
                            //dishes.remove(dish);
                            System.out.println("Assistants say this dish is finished and ready to serve : " + dish);
                            HTMLLogger.addRemarks(dish.name + " done");
                            throw new DishFinishedException(dish);
                        } else {
                            System.out.println("There are more tasks for this dish!" + dish);
                            if (dish.isCook()) {
                                System.out.println("Assistants say this dish is ready for cooking : " + dish);
                                cookStateDishes.add(dish);
                            } else {
                                HTMLLogger.addAssistantDish(dish);
                            }
                        }
                    } else {
                        HTMLLogger.addAssistantDish(dish);
                    }
                    System.out.println("Assistants worked on this dish : " + dish);
                } else {
                    System.out.println("Assistants say the task for this dish is finished : " + dish + ", removing ..");
                    dish.finishTask();
                }
            } else {
                System.out.println("Assistants say this dish is ready for cooking : " + dish);
                cookStateDishes.add(dish);
                //dishes.remove(dish);
            }
        }
        return cookStateDishes;
    }

    @Override
    public String toString() {
        return "Assistants{" +
                "dishes=" + dishes +
                '}';
    }
}
