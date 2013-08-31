package actors;

import actors.exceptions.DishNotCookStateException;
import actors.exceptions.TaskFinishedException;
import event.TimeListener;
import logger.HTMLLogger;
import models.Dish;
import models.RecipeTask;
import scheduler.RRScheduler;
import scheduler.Scheduler;

import java.util.Vector;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/26/13
 * Time: 8:45 AM
 * TODO: Brief description of the class
 */
public class Chef implements TimeListener{
    Scheduler scheduler;
    Dish currentDish;
    //dummy
    Vector<Dish> readyQueueDishes;

    public Chef(){

    }

    public Chef(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void setScheduler(Scheduler scheduler){
        this.scheduler = scheduler;
    }
    /**
     * preemption
     * return current dish and assign null
     *
     * @return
     */
    public Dish takeCurrentDish() {
        Dish returnCurrentDish = currentDish;
        this.currentDish = null;
        return returnCurrentDish;
    }

    public Boolean isCooking(){
        return currentDish != null;
    }

    /**
     * returns a dish once it's finished cooking
     *
     * @return
     * @throws DishNotCookStateException
     * @throws TaskFinishedException
     */
    public Dish cook() throws DishNotCookStateException, TaskFinishedException {
        System.out.println("Chef is currently cooking : " + currentDish);
        if (currentDish != null) {
            System.out.println("Chef asked to cook : " + currentDish.name);
            if (currentDish.recipeTaskList.size() > 0) {
                RecipeTask currentTask = currentDish.recipeTaskList.get(0);
                if (currentTask.isCook()) {
                    System.out.println("11111currentTask = " + ((RRScheduler) scheduler).getReadyToCookDishes());
                    currentTask.time = currentTask.time - 1;
                    System.out.println("Chef cooked for one minute : " + currentDish);
                    if (currentTask.time == 0) {
                        System.out.println("9999999currentTask = " + ((RRScheduler) scheduler).getReadyToCookDishes());
                        System.out.println("000000000000Chef done with current task in dish : " + currentTask + ", removing..");
                        System.out.println("currentDish = " + currentDish.name);
                        currentDish.finishTask();
                        System.out.println("9999999currentTask = " + ((RRScheduler) scheduler).getReadyToCookDishes());
                        currentDish = scheduler.whatIsNext(currentDish, readyQueueDishes);
                        System.out.println("Chef asks scheduler what to do and got this dish : " + currentDish);
                        return currentDish;
                    }
                } else {
                    throw new DishNotCookStateException();
                }
            }
            System.out.println("currentDish = " + currentDish.name);
            currentDish = scheduler.whatIsNext(currentDish, readyQueueDishes);
        } else {
            currentDish = scheduler.whatIsNext(currentDish, readyQueueDishes);
            System.out.println("Current dish was null so got new dish: " + currentDish);
        }
        return currentDish;
    }

    @Override
    public void time(Integer currentTime) {
        try {
            Dish dish = cook();
            if (dish != null){
                HTMLLogger.cook = dish.toHTMLString();
            }
        } catch (DishNotCookStateException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TaskFinishedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}