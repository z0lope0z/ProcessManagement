package models;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: lemano
 * Date: 8/26/13
 * Time: 8:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecipeTask {
    public String id;
    public String name;
    public Integer time;

    public RecipeTask(){
        this.id = String.valueOf(UUID.randomUUID());
    }

    public RecipeTask(RecipeTask recipeTask){
        this.id = String.valueOf(UUID.randomUUID());
        this.name = recipeTask.name;
        this.time = recipeTask.time;
    }

    public RecipeTask(String name, Integer time) {
        this.name = name;
        this.time = time;
        this.id = String.valueOf(UUID.randomUUID());
    }

    public Boolean hasTime(){
        return time > 0;
    }

    public Integer work(){
        this.time = this.time - 1;
        return this.time;
    }

    public Boolean isCook(){
        return name.equals("cook");
    }

    public void regen(){
        this.id = String.valueOf(UUID.randomUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeTask)) return false;

        RecipeTask that = (RecipeTask) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RecipeTask{" +
                "name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}

