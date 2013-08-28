package scheduler;

import actors.*;
import actors.exceptions.DishFinishedException;
import event.TimeListener;
import logger.HTMLLogger;
import models.Dish;
import models.RecipeTask;
import scheduler.models.FCFSDish;

import java.util.*;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/26/13
 * Time: 8:58 AM
 * TODO: Brief description of the class
 */
public class FCFSScheduler implements Scheduler, TimeListener {
    PriorityQueue<FCFSDish> readyToCookFood = new PriorityQueue<FCFSDish>();
    Costumer costumer;
    Assistants assistants;
    FCFSOrderLookup lookupTable;

    public FCFSScheduler(Costumer costumer, Assistants assistants) {
        this.costumer = costumer;
        this.assistants = assistants;
        this.readyToCookFood = new PriorityQueue<FCFSDish>(10, new FCFSComparator());
        this.lookupTable = new FCFSOrderLookup();
    }

    @Override
    public Dish whatIsNext(Dish currentDish, Vector<Dish> dishesQueue) {
        System.out.println("Whatisnext was called, size of readyQueue : " + readyToCookFood.size());
        System.out.println("Current dish parameter : " + currentDish);
        if (!readyToCookFood.isEmpty())
            System.out.println("Top of queue : " + readyToCookFood.peek().name + " with order : " + readyToCookFood.peek().order);
        if (currentDish != null){
            if (currentDish.isDone()){
                System.out.println("This dish is done and is ready to serve!" + currentDish);
                return dequeue();
            }
            if (currentDish.isCook()){
                if (currentDish.hasTime()){
                    // if there's still time remaining, do current dish
                    System.out.println("There's still time remaining on the dish, returning to chef : " + currentDish);
                    HTMLLogger.cook = currentDish.toHTMLString();
                    return currentDish;
                } else {
                    // current dish's time is 0
                    // this should not happen since the chef deletes the dish, but just in case!
                    currentDish.finishTask();
                    if (currentDish.nextTask() != null){
                        if (currentDish.nextTask().isCook()){
                            System.out.println("The next task for this dish is cooking, returning dish : " + currentDish);
                            HTMLLogger.cook = currentDish.toHTMLString();
                            return currentDish;
                        } else {
                            System.out.println("The next task for this dish is not a cooking task, returning this to assistants : " + currentDish);
                            sendDishToAssistants(currentDish);
                        }
                    }
                    return dequeue();
                }
            } else {
                System.out.println("The current task for this dish is not a cooking task, returning this to assistants : " + currentDish);
                sendDishToAssistants(currentDish);
                return dequeue();
            }
        }
        return dequeue();
    }

    public Dish dequeue(){
        Dish returnDish = readyToCookFood.peek();
        if (returnDish != null)
            HTMLLogger.cook = returnDish.toHTMLString();
            readyToCookFood.remove(returnDish);
        HTMLLogger.ready = HTMLLogger.convertFCFSReadyQueue(readyToCookFood);
        return returnDish;
    }

    public void sendDishToAssistants(Dish dish){
        assistants.addDish(dish);
        readyToCookFood.remove(dish);
    }

    public Boolean isEmpty(Integer currentTime){
        if ((!costumer.hasMoreOrders(currentTime)) && (!assistants.hasDishes())){
            System.out.println("Costumer and Assistants are empty!");
            return true;
        }
        return false;
    }


    @Override
    public void time(Integer currentTime) {
        // assistants prepare the food
        System.out.println("Assistants are preparing the food: " + assistants);
        List<Dish> readyToCookDishes = null;
        try {
            readyToCookDishes = assistants.prepareFood();
        } catch (DishFinishedException e) {
            assistants.removeDish(e.dish);
            readyToCookDishes = new ArrayList<Dish>();
        }
        if (readyToCookDishes.size() > 0){
            System.out.println("readyToCookDishes.size() = " + readyToCookDishes.size());
            lookupTable.addDishes(readyToCookDishes);
            for (Dish dish: readyToCookDishes){
                readyToCookFood.add(lookupTable.lookUp(dish));
                HTMLLogger.ready = HTMLLogger.convertFCFSReadyQueue(readyToCookFood);
                assistants.removeDish(dish);
            }
        }

        // new order comes in
        List<Dish> newOrders = this.costumer.takeOrder(currentTime);
        if (newOrders.size() > 0){
            System.out.println("New orders come in : " + newOrders.get(0).name);
            System.out.println("Task name : " + newOrders.get(0).recipeTaskList.get(0).name);
            System.out.println("Time needed : " + newOrders.get(0).recipeTaskList.get(0).time);
            // store the sequence
            lookupTable.addDishes(newOrders);
            // examine nature of order
            for(Dish dish: newOrders){
                HTMLLogger.addRemarks(dish.name + " arrives");
                RecipeTask firstDishTask = dish.recipeTaskList.get(0);
                if (firstDishTask.name == "cook"){
                    System.out.println("Added to ready queue : " + newOrders.get(0).name);
                    readyToCookFood.add(lookupTable.lookUp(dish));
                    HTMLLogger.ready = HTMLLogger.convertFCFSReadyQueue(readyToCookFood);
                    System.out.println("No. of items in ready queue : " + readyToCookFood.size());
                } else {
                    assistants.addDish(dish);
                }
            }
        }
    }
}

class FCFSOrderLookup {
    Map<String, FCFSDish> dishMap = new LinkedHashMap<String, FCFSDish>();

    public FCFSOrderLookup(){
    }

    public FCFSDish addDish(Dish dish){
        Integer order = dishMap.size();
        FCFSDish checkDish = dishMap.get(dish.id);
        if (checkDish != null){
            dishMap.put(checkDish.id, new FCFSDish(order, dish));
            return dishMap.get(checkDish.id);
        }
        FCFSDish orderedDish = new FCFSDish(order, dish);
        dishMap.put(orderedDish.id, orderedDish);
        return orderedDish;
    }

    public List<FCFSDish> addDishes(List<Dish> dishes){
        List<FCFSDish> returnDishes = new ArrayList<FCFSDish>();
        for (Dish dish : dishes){
            returnDishes.add(addDish(dish));
        }
        return returnDishes;
    }

    public Integer getOrder(Dish dish){
        return dishMap.get(dish.id).order;
    }

    public FCFSDish lookUp(Dish dish){
        return dishMap.get(dish.id);
    }

}

class FCFSComparator implements Comparator<FCFSDish>{

    @Override
    public int compare(FCFSDish dish, FCFSDish dish2) {
        if (dish.order < dish2.order)
            return -1;
        if (dish.order > dish2.order)
            return 1;
        return 0;
    }
}
