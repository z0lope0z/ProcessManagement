package actors;

import models.Dish;
import models.RecipeTask;

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

    public Assistants(){

    }

    public Assistants(List<Dish> dishes) {
        this.dishes.addAll(dishes);
    }

    public void addDish(Dish dish){
        this.dishes.add(dish);
    }

    /**
     * Prepares food if recipetask is not in a cook state
     * @return list of dishes that are in a cook state
     */
    public List<Dish> prepareFood(){
        List<Dish> cookStateDishes = new ArrayList<Dish>();
        System.out.println("Assistants are currently preparing the dishes : " + dishes);
        for (Dish dish: dishes){
            if (!dish.isCook()){
               if (dish.hasTime()){
                   dish.currentTask().work();
                   System.out.println("Assistants worked on this dish : " + dish);
               } else {
                   System.out.println("Assistants say the task for this dish is finished : " + dish + ", removing ..");
                   dish.finishTask();
               }
            } else {
                System.out.println("Assistants say this dish is ready for cooking : " + dish);
                cookStateDishes.add(dish);
                dishes.remove(dish);
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
