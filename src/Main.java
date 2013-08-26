import actors.Assistants;
import actors.Chef;
import actors.Costumer;
import event.Time;
import models.Dish;
import models.DishOrder;
import models.RecipeTask;
import scheduler.FCFSScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/26/13
 * Time: 10:06 AM
 * TODO: Brief description of the class
 */
public class Main {
    public static void main(String[] args) {
        Time time = new Time();
        Assistants assistants = new Assistants();
        Costumer costumer = createCostumer();
        FCFSScheduler scheduler = new FCFSScheduler(costumer, assistants);
        Chef chef = new Chef(scheduler);
        Scanner input = new Scanner(System.in);
        int num = 0;
        while ((num = input.nextInt()) >= 0) {
            scheduler.time(num);
            chef.time(num);
        }
    }

    public static Costumer createCostumer(){
        List<DishOrder> orderList = new ArrayList<DishOrder>();
        orderList.add(new DishOrder(createDish(), 0));
        orderList.add(new DishOrder(createDish(), 3));
        return new Costumer(orderList);
    }

    public static Dish createDish(){
        return new Dish("tinola", createRecipeTaskList());
    }

    public static List<RecipeTask> createRecipeTaskList(){
        List<RecipeTask> recipeList = new ArrayList<RecipeTask>();
        recipeList.add(new RecipeTask("cook", 1));
        recipeList.add(new RecipeTask("cut", 3));
        recipeList.add(new RecipeTask("mix", 2));
        return recipeList;
    }
}
