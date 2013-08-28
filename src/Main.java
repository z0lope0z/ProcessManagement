import actors.Assistants;
import actors.Chef;
import actors.Costumer;
import event.Time;
import logger.HTMLLogger;
import models.Dish;
import models.DishOrder;
import models.RecipeTask;
import scheduler.FCFSScheduler;
import scheduler.SJFScheduler;

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
        //SJFScheduler scheduler = new SJFScheduler(costumer, assistants);
        Chef chef = new Chef(scheduler);
        Scanner input = new Scanner(System.in);
        int num = 0;
        while ((num = input.nextInt()) >= 0) {
            HTMLLogger.time = num;
            scheduler.time(num);
            chef.time(num);
            if ((!chef.isCooking()) && scheduler.isEmpty(num)){
                HTMLLogger.addRemarks("Simulation ends.");
            }
            HTMLLogger.print();
        }
    }

    public static Costumer createCostumer(){
        List<DishOrder> orderList = new ArrayList<DishOrder>();
        orderList.add(new DishOrder(createDish(), 0));
        orderList.add(new DishOrder(createDish2(), 3));
        return new Costumer(orderList);
    }

    public static Dish createDish(){
        return new Dish("tinola", createRecipeTaskList());
    }

    public static Dish createDish2(){
        return new Dish("adobo1", createRecipeTaskList2());
    }

    public static List<RecipeTask> createRecipeTaskList(){
        List<RecipeTask> recipeList = new ArrayList<RecipeTask>();
        recipeList.add(new RecipeTask("cook", 5));
        recipeList.add(new RecipeTask("mix", 1));
        recipeList.add(new RecipeTask("cook", 3));
        return recipeList;
    }

    public static List<RecipeTask> createRecipeTaskList2(){
        List<RecipeTask> recipeList = new ArrayList<RecipeTask>();
        recipeList.add(new RecipeTask("cook", 2));
        recipeList.add(new RecipeTask("cook", 2));
        recipeList.add(new RecipeTask("mix", 2));
        return recipeList;
    }
}
