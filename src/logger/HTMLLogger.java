package logger;

import models.Dish;
import scheduler.models.FCFSDish;

import java.util.*;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/27/13
 * Time: 1:05 AM
 * TODO: Brief description of the class
 */
public class HTMLLogger {
    public static Integer time = 0;
    public static String cook = "none";
    public static String ready = "none";
    public static String assistants = "none";
    public static String remarks = "none";

    public static void addAssistantDish(Dish dish){
        if (assistants == "none"){
            assistants = "";
        }
        assistants += dish.toHTMLString();
    }


    public static void addRemarks(String newRemark){
        if (remarks == "none"){
            remarks = "";
        }
        remarks += newRemark + "\n";
    }

    public static void print(){
        System.out.println("time = " + time);
        System.out.println("cook = " + cook);
        System.out.println("ready = " + ready);
        System.out.println("assistants = " + assistants);
        System.out.println("remarks = " + remarks);
        refresh();
    }

    public static String convertReadyQueue(PriorityQueue<FCFSDish> readyToCookFood){
        Iterator it = readyToCookFood.iterator();
        List<Dish> dishes = new ArrayList<Dish>();
        while (it.hasNext()){
            dishes.add((Dish) it.next());
        }
        return HTMLLogger.convertDishes(dishes);
    }

    public static String convertDishes(Collection<Dish> dishes){
        if (dishes.isEmpty()){
            return "none";
        }
        String returnString = "";
        for (Dish dish : dishes){
            returnString += dish.toHTMLString();
        }
        return returnString;
    }

    public static void refresh(){
        cook = "none";
        ready = "none";
        assistants = "none";
        remarks = "none";
        time++;
    }
}
