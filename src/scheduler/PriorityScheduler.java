package scheduler;

import actors.Assistants;
import actors.Costumer;
import logger.HTMLLogger;
import models.Dish;

import java.util.*;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/28/13
 * Time: 10:36 PM
 * TODO: Priority with preemption
 */
public class PriorityScheduler extends AbstractScheduler {
    PriorityQueue<Dish> readyToCookFood = new PriorityQueue<Dish>();

    public PriorityScheduler(Costumer costumer, Assistants assistants) {
        super(costumer, assistants);
        this.name = "Priority";
        this.readyToCookFood = new PriorityQueue<Dish>(10, new PriorityComparator());
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

    /**
     * places a dish back into the priority queue and returns
     *
     * @return
     */
    public Dish requeueDequeue(Dish dish) {
        addReadyToCook(dish);
        return dequeueReadyToCook();
    }

    @Override
    public Dish whatIsNext(Dish currentDish, Vector<Dish> dishesQueue) {
        if (currentDish == null) {
            return dequeueReadyToCook();
        } else {
            if (currentDish.isDone()) {
                return dequeueReadyToCook();
            } else {
                if (currentDish.currentTask().isCook()) {
                    if (currentDish.hasTime()) {
                        return requeueDequeue(currentDish);
                    } else {
                        // offset
                        currentDish.finishTask();
                        // check if next task is okay
                        if (currentDish.nextTask().isCook()) {
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
    public List<Dish> callAssistants() {
        List<Dish> readyToCookDishes = super.callAssistants();
        // get ready to cook dishes and sort
        if (!readyToCookDishes.isEmpty()) {
            for (Dish dish : readyToCookDishes) {
                System.out.println("Got new cook state dishes from the assistants! " + dish);
                addReadyToCook(dish);
                assistants.removeDish(dish);
            }
        }
        return readyToCookDishes;
    }

    @Override
    public List<Dish> checkAndSortCostumerOrders(Integer currentTime) {
        List<Dish> newOrders = super.checkAndSortCostumerOrders(currentTime);
        for (Dish dish : newOrders) {
            HTMLLogger.addRemarks(dish.name + " arrives");
        }
        return newOrders;
    }

    @Override
    public void addReadyToCook(Dish dish) {
        readyToCookFood.add(dish);
        HTMLLogger.ready = HTMLLogger.convertReadyQueue(readyToCookFood);
    }

    @Override
    public Dish dequeueReadyToCook() {
        Dish returnDish = readyToCookFood.peek();
        if (returnDish != null)
            readyToCookFood.remove();
        HTMLLogger.ready = HTMLLogger.convertReadyQueue(readyToCookFood);
        return returnDish;
    }

}

class PriorityComparator implements Comparator<Dish> {

    @Override
    public int compare(Dish dish, Dish dish2) {
        return dish.name.compareTo(dish2.name);
    }
}