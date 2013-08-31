package reader;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import models.Dish;
import models.DishOrder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/31/13
 * Time: 2:43 PM
 * TODO: Brief description of the class
 */
public class OrderReader {
    // known dishes
    Map<String, Dish> dishLookUp = new HashMap<String, Dish>();
    public static final String DEFAULT = "FCFS";
    public static final String FIRST_COME_FIRST_SERVE = "FCFS";
    public static final String SHORTEST_JOB_FIRST = "SJF";
    public static final String PRIORITY = "PRIORITY";
    public static final String ROUND_ROBIN = "RR";
    public String schedulerType = FIRST_COME_FIRST_SERVE;

    //round robin
    public Integer RR_TIME_QUANTUM = 2;
    public Integer RR_CONTEXT_SWITCH = 2;

    public OrderReader(Map<String, Dish> dishLookUp) {
        this.dishLookUp.putAll(dishLookUp);
    }

    public List<DishOrder> read() {
        List<DishOrder> orderList = new ArrayList<DishOrder>();
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("tasklist.txt"));
            this.schedulerType = getType(in.nextLine());
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String taskName = line.split(" ")[0];
                Integer when = Integer.parseInt(line.split(" ")[1]);
                Pattern pattern = Pattern.compile("(^\\w*)\\d");
                Matcher matcher = pattern.matcher(taskName);
                matcher.find();
                String dishName = matcher.group(1);
                orderList.add(new DishOrder(dishLookUp.get(dishName), when));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    private String getType(String rawLine){
        if (FIRST_COME_FIRST_SERVE.equals(rawLine)){
            return FIRST_COME_FIRST_SERVE;
        }
        if (SHORTEST_JOB_FIRST.equals(rawLine)){
            return SHORTEST_JOB_FIRST;
        }
        if (PRIORITY.equals(rawLine)){
            return PRIORITY;
        }
        // round robin
        if (rawLine.startsWith(ROUND_ROBIN)){
            RR_TIME_QUANTUM = Integer.parseInt(rawLine.split(" ")[1]);
            RR_CONTEXT_SWITCH = Integer.parseInt(rawLine.split(" ")[2]);
            return ROUND_ROBIN;
        }
        return DEFAULT;
    }

}
