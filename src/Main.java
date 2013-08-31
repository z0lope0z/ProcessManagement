import actors.Assistants;
import actors.Chef;
import actors.Costumer;
import event.Time;
import logger.HTMLLogger;
import models.Dish;
import models.DishOrder;
import models.RecipeTask;
import reader.OrderReader;
import reader.RecipeReader;
import scheduler.*;

import java.util.*;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/26/13
 * Time: 10:06 AM
 * TODO: Brief description of the class
 */
public class Main {
    public static void main(String[] args) {
        Assistants assistants = new Assistants();
        Costumer costumer = createCostumer();
        RecipeReader recipeReader = new RecipeReader();
        List<Dish> dishList = recipeReader.read();
        Map<String, Dish> dishLookUp = new HashMap<String, Dish>();
        for (Dish dish: dishList){
            dish.regen();
            dishLookUp.put(dish.name, dish);
        }
        OrderReader orderReader = new OrderReader(dishLookUp);
        costumer = new Costumer(orderReader.read());
        System.out.println("orderReader.read() = " + costumer);
        System.out.println("createCostumer() = " + createCostumer());
        AbstractScheduler scheduler = getScheduler(orderReader, costumer, assistants);
        Chef chef = new Chef(scheduler);
        Scanner input = new Scanner(System.in);
        int num = 0;
        HTMLLogger.title = scheduler.name;
        while (true) {
            HTMLLogger.time = num;
            scheduler.time(num);
            chef.time(num);
            if (scheduler.isEmpty(num) && (!chef.isCooking())){
                HTMLLogger.addRemarks("Simulation ends.");
                HTMLLogger.print();
                HTMLLogger.write();
                break;
            }
            HTMLLogger.print();
            HTMLLogger.write();
            HTMLLogger.refresh();
            num++;
        }
    }

    public static AbstractScheduler getScheduler(OrderReader orderReader, Costumer costumer, Assistants assistants){

        if (orderReader.schedulerType == OrderReader.FIRST_COME_FIRST_SERVE){
            return new FCFSScheduler(costumer, assistants);
        }
        if (orderReader.schedulerType == OrderReader.SHORTEST_JOB_FIRST){
            return new SJFScheduler(costumer, assistants);
        }
        if (orderReader.schedulerType == OrderReader.PRIORITY){
            return new PriorityScheduler(costumer, assistants);
        }
        if (orderReader.schedulerType == OrderReader.ROUND_ROBIN){
            return new RRScheduler(costumer, assistants, orderReader.RR_TIME_QUANTUM, orderReader.RR_CONTEXT_SWITCH);
        }
        //default
        return new FCFSScheduler(costumer, assistants);
    }

    public static Costumer createCostumer(){
        List<DishOrder> orderList = new ArrayList<DishOrder>();
        orderList.add(new DishOrder(createDish(), 0));
        orderList.add(new DishOrder(createDish2(), 1));
        orderList.add(new DishOrder(createDish3(), 1));
        return new Costumer(orderList);
    }

    public static Dish createDish(){
        return new Dish("adobo1", createRecipeTaskList());
    }

    public static Dish createDish2(){
        return new Dish("adobo2", createRecipeTaskList2());
    }

    public static Dish createDish3(){
        return new Dish("adobo3", createRecipeTaskList2());
    }

    public static List<RecipeTask> createRecipeTaskList(){
        List<RecipeTask> recipeList = new ArrayList<RecipeTask>();
        recipeList.add(new RecipeTask("cook", 4));
        recipeList.add(new RecipeTask("cook", 3));
        recipeList.add(new RecipeTask("cook", 3));
        return recipeList;
    }

    public static List<RecipeTask> createRecipeTaskList2(){
        List<RecipeTask> recipeList = new ArrayList<RecipeTask>();
        recipeList.add(new RecipeTask("cook", 3));
        recipeList.add(new RecipeTask("cook", 4));
        recipeList.add(new RecipeTask("mix", 2));
        return recipeList;
    }
}
