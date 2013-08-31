package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: lemano
 * Date: 8/26/13
 * Time: 8:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class Dish {
    public String id;
    public String name;
    public List<RecipeTask> recipeTaskList = new ArrayList<RecipeTask>();

    public Dish() {
    }

    public Dish(String name, List<RecipeTask> recipeTaskList) {
        this.name = name;
        this.recipeTaskList.addAll(recipeTaskList);
        this.id = String.valueOf(UUID.randomUUID());
    }

    public void finishTask(){
        if (!recipeTaskList.isEmpty())
            recipeTaskList.remove(0);
    }

    public RecipeTask currentTask(){
        if (recipeTaskList.size() > 0){
            return recipeTaskList.get(0);
        }
        return null;
    }

    public RecipeTask nextTask(){
        if (recipeTaskList.size() > 1){
            return recipeTaskList.get(1);
        }
        return null;
    }

    public Boolean hasTime(){
        RecipeTask currentTask = currentTask();
        if (currentTask != null){
            return currentTask.hasTime();
        }
        return false;
    }

    public Boolean isDone(){
        return recipeTaskList.isEmpty();
    }

    public Integer work(){
        if (currentTask().hasTime())
            return currentTask().work();
        return null;
    }

    public Boolean isCook(){
        return currentTask().name.equals("cook");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dish)) return false;

        Dish dish = (Dish) o;

        if (id != null ? !id.equals(dish.id) : dish.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Dish{name=" + name + "," +
                "recipeTaskList=" + recipeTaskList +
                '}';
    }

    public String toHTMLString(){
        String recipe = "";
        for (RecipeTask recipeTask : recipeTaskList){
            recipe += "(" + recipeTask.name + "=" + recipeTask.time + ")\n";
            break;
        }
        return name + recipe;
    }
}
