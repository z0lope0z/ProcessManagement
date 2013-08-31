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
 * Time: 10:51 PM
 * TODO: Brief description of the class
 */
public class RRScheduler extends AbstractScheduler {
    Queue<Dish> readyToCookFood = new LinkedList<Dish>();
    TimeQuantum timeQuantum;
    ContextSwitch contextSwitch;

    public RRScheduler(Costumer costumer, Assistants assistants, Integer timeQuantumLimit, Integer contextSwitchLimit) {
        super(costumer, assistants);
        this.name = "Round Robin";
        this.readyToCookFood = new LinkedList<Dish>();
        this.timeQuantum = new TimeQuantum(timeQuantumLimit);
        this.contextSwitch = new ContextSwitch(contextSwitchLimit);
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

    public Dish renewAndReturn(Dish dish){
        Dish returnDish = readyToCookFood.peek();
        if (returnDish != null){
            readyToCookFood.remove();
            readyToCookFood.add(dish);
            return returnDish;
        }
        return null;
    }

    @Override
    public Dish whatIsNext(Dish currentDish, Vector<Dish> dishesQueue) {
        if (contextSwitch.isContextSwitch){
            contextSwitch.incrementAndCheckLimit();
            HTMLLogger.addRemarks("context switch");
            return null;
        }
        if (currentDish == null) {
            return dequeueReadyToCook();
        } else {
            if (currentDish.isDone()) {
                return dequeueReadyToCook();
            } else {
                if (currentDish.currentTask().isCook()) {
                    if (currentDish.hasTime()) {
                        if (!timeQuantum.incrementAndCheckLimit())
                            //TODO
                            return currentDish;
                        else {
                            if (getReadyToCookDishes().isEmpty()){
                                System.out.println("Only one dish left, no need to context switch: " + currentDish);
                                return currentDish;
                            }
                            System.out.println("Time quantum maxed for dish: " + currentDish);
                            addReadyToCook(currentDish);
                            contextSwitch.isContextSwitch = true;
                            // offset
                            contextSwitch.incrementAndCheckLimit();
                            HTMLLogger.addRemarks("context switch");
                            return null;
                        }
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
        HTMLLogger.ready = HTMLLogger.convertDishes(readyToCookFood);
    }

    @Override
    public Dish dequeueReadyToCook() {
        Dish returnDish = readyToCookFood.peek();
        if (returnDish != null)
            readyToCookFood.remove();
        HTMLLogger.ready = HTMLLogger.convertDishes(readyToCookFood);
        return returnDish;
    }

}

class TimeQuantum {
    Integer limit;
    Integer currentCount;

    public TimeQuantum(Integer limit) {
        this.limit = limit;
        this.currentCount = 0;
    }

    public Boolean incrementAndCheckLimit() {
        increment();
        return isLimit();
    }

    public Boolean isLimit() {
        if (currentCount == limit) {
            reset();
            return true;
        }
        return false;
    }

    public Integer increment() {
        return currentCount += 1;
    }

    public void reset() {
        currentCount = 0;
    }
}

class ContextSwitch {
    Integer limit;
    Integer currentCount;
    Boolean isContextSwitch;

    public ContextSwitch(Integer limit) {
        this.limit = limit;
        this.currentCount = 0;
        this.isContextSwitch = false;
    }

    public Boolean incrementAndCheckLimit(){
        increment();
        if (currentCount >= limit){
            reset();
            isContextSwitch = false;
            return true;
        }
        return false;
    }

    public Integer increment(){
        return currentCount += 1;
    }

    public void reset(){
        currentCount = 0;
    }
}

