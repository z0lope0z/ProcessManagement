package models;

/**
 * Created with IntelliJ IDEA.
 * User: lemano
 * Date: 8/26/13
 * Time: 8:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class RecipeTask {
    public String name;
    public Integer time;

    public RecipeTask(String name, Integer time) {
        this.name = name;
        this.time = time;
    }

    public Boolean hasTime(){
        return time > 0;
    }

    public Integer work(){
        this.time = this.time - 1;
        return this.time;
    }

    public Boolean isCook(){
        return name == "cook";
    }

    @Override
    public String toString() {
        return "RecipeTask{" +
                "name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}

