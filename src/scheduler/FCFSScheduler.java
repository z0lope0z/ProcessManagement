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
public class FCFSScheduler extends AbstractScheduler {
    PriorityQueue<FCFSDish> readyToCookFood = new PriorityQueue<FCFSDish>();
    FCFSOrderLookup lookupTable;

    public FCFSScheduler(Costumer costumer, Assistants assistants) {
        super(costumer, assistants);
        this.readyToCookFood = new PriorityQueue<FCFSDish>(10, new FCFSComparator());
        this.lookupTable = new FCFSOrderLookup();
    }

    /**
     * places a dish back into the priority queue and returns
     * @return
     */
    public Dish requeueDequeue(Dish dish){
        addReadyToCook(dish);
        return dequeueReadyToCook();
    }

    @Override
    public Dish whatIsNext(Dish currentDish, Vector<Dish> dishesQueue) {
        if (currentDish == null){
            return dequeueReadyToCook();
        } else {
            if (currentDish.isDone()){
                return dequeueReadyToCook();
            } else {
                if (currentDish.currentTask().isCook()){
                    if (currentDish.hasTime()){
                        return requeueDequeue(currentDish);
                    } else {
                        // offset
                        currentDish.finishTask();
                        // check if next task is okay
                        if (currentDish.nextTask().isCook()){
                            return requeueDequeue(currentDish);
                        } else {
                            sendDishToAssistants(currentDish);
                            return dequeueReadyToCook();
                        }
                    }
                } else {
                    sendDishToAssistants(currentDish);
                    return dequeueReadyToCook();
                }
            }
        }
    }

    @Override
    public Dish dequeueReadyToCook() {
        Dish returnDish = readyToCookFood.peek();
        if (returnDish != null)
            readyToCookFood.remove();
        HTMLLogger.ready = HTMLLogger.convertFCFSReadyQueue(readyToCookFood);
        return returnDish;
    }

    @Override
    public List<Dish> callAssistants() {
        List<Dish> readyToCookDishes = super.callAssistants();
        if (!readyToCookDishes.isEmpty()){
            for (Dish dish: readyToCookDishes){
                readyToCookFood.add(lookupTable.lookUp(dish));
            }
        }
        return readyToCookDishes;
    }

    @Override
    public List<Dish> checkAndSortCostumerOrders(Integer currentTime) {
        List<Dish> newOrders = super.checkAndSortCostumerOrders(currentTime);
        if (!newOrders.isEmpty()){
            lookupTable.addDishes(newOrders);
        }
        return newOrders;
    }

    @Override
    public List<Dish> getReadyToCookDishes() {
        Iterator it = readyToCookFood.iterator();
        List<Dish> dishes;
        dishes = new ArrayList<Dish>();
        while (it.hasNext()) {
            dishes.add((Dish) it.next());
        }
        return dishes;
    }

    @Override
    public void addReadyToCook(Dish dish) {
        readyToCookFood.add(lookupTable.getOrCreateDish(dish));
        HTMLLogger.ready = HTMLLogger.convertFCFSReadyQueue(readyToCookFood);
    }

}

class FCFSOrderLookup {
    Map<String, FCFSDish> dishMap = new LinkedHashMap<String, FCFSDish>();

    public FCFSOrderLookup(){
    }

    public FCFSDish getOrCreateDish(Dish dish){
        Integer order = dishMap.size();
        FCFSDish checkDish = dishMap.get(dish.id);
        if (checkDish != null){
            return dishMap.get(checkDish.id);
        }
        FCFSDish orderedDish = new FCFSDish(order, dish);
        dishMap.put(orderedDish.id, orderedDish);
        return orderedDish;
    }

    public List<FCFSDish> addDishes(List<Dish> dishes){
        List<FCFSDish> returnDishes = new ArrayList<FCFSDish>();
        for (Dish dish : dishes){
            returnDishes.add(getOrCreateDish(dish));
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