package logger;

import models.Dish;
import scheduler.models.FCFSDish;

import java.io.*;
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
    public static String title = "FCFS";
    public static String cook = "none";
    public static String ready = "none";
    public static String assistants = "none";
    public static String remarks = "none";
    public static List<HTMLRow> rows = new ArrayList<HTMLRow>();

    public static void addAssistantDish(Dish dish) {
        if (assistants == "none") {
            assistants = "";
        }
        assistants += dish.toHTMLString();
    }

    public static void addReadyToCook(Dish dish) {
        if (ready == "none") {
            ready = "";
        }
        ready += dish.toHTMLString();
    }

    public static void addRemarks(String newRemark) {
        if (remarks == "none") {
            remarks = "";
        }
        remarks += newRemark + "\n";
    }

    public static void print() {
        System.out.println("time = " + time);
        System.out.println("cook = " + cook);
        System.out.println("ready = " + ready);
        System.out.println("assistants = " + assistants);
        System.out.println("remarks = " + remarks);
        HTMLRow htmlRow = new HTMLRow();
        htmlRow.time = time;
        htmlRow.cook = cook;
        htmlRow.ready = ready;
        htmlRow.assistants = assistants;
        htmlRow.remarks = remarks;
        rows.add(htmlRow);
    }

    public static String convertReadyQueue(PriorityQueue<Dish> readyToCookFood) {
        Iterator it = readyToCookFood.iterator();
        List<Dish> dishes = new ArrayList<Dish>();
        while (it.hasNext()) {
            dishes.add((Dish) it.next());
        }
        return HTMLLogger.convertDishes(dishes);
    }

    public static String convertFCFSReadyQueue(PriorityQueue<FCFSDish> readyToCookFood) {
        Iterator it = readyToCookFood.iterator();
        List<Dish> dishes = new ArrayList<Dish>();
        while (it.hasNext()) {
            dishes.add((Dish) it.next());
        }
        return HTMLLogger.convertDishes(dishes);
    }

    public static String convertDishes(Collection<Dish> dishes) {
        if (dishes.isEmpty()) {
            return "none";
        }
        String returnString = "";
        for (Dish dish : dishes) {
            returnString += dish.toHTMLString();
        }
        return returnString;
    }

    public static void refresh() {
        cook = "none";
        ready = "none";
        assistants = "none";
        remarks = "none";
        time++;
    }

    public static void read() throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("file.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append('\n');
                line = br.readLine();
            }
            String everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            br.close();
        }
    }

    public static void write() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>" + title);
        sb.append("</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<table border=\"1\" bordercolor=\"#000066\" style=\"background-color:#FFFF66\" width=\"100%\" cellpadding=\"3\" cellspacing=\"3\">");
        sb.append("<tr>");
        sb.append("<th>Time</th>");
        sb.append("<th>Cook</th>");
        sb.append("<th>Ready</th>");
        sb.append("<th>Assistants</th>");
        sb.append("<th>Remarks</th>");
        sb.append("</tr>");
        for (HTMLRow row : rows){
            sb.append("<tr>");
            sb.append("<td>" + row.time + "</td>");
            sb.append("<td>" + row.cook + "</td>");
            sb.append("<td>" + row.ready + "</td>");
            sb.append("<td>" + row.assistants + "</td>");
            sb.append("<td>" + row.remarks + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("</body>");
        sb.append("</html>");
        FileWriter fstream = null;
        try {
            fstream = new FileWriter("output.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter out = new BufferedWriter(fstream);
        try {
            out.write(sb.toString());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class HTMLRow {
    Integer time = 0;
    String cook = "none";
    String ready = "none";
    String assistants = "none";
    String remarks = "none";

    public HTMLRow() {

    }
}
