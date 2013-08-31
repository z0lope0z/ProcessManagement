package reader;

import models.Dish;
import models.RecipeTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Auto-generated header
 * User: lemano
 * Date: 8/31/13
 * Time: 1:40 PM
 * TODO: Brief description of the class
 */
public class RecipeReader {

    public List<Dish> read() {
        List<Dish> dishList = new ArrayList<Dish>();
        List<String> fileList = getFileList();
        for (String file : fileList) {
            Dish dish = new Dish();
            int position = file.lastIndexOf(".");
            dish.name = file.substring(0, position);
            dish.recipeTaskList = extractTaskList(file);
            dishList.add(dish);
        }
        return dishList;
    }

    public List<RecipeTask> extractTaskList(String fileName) {
        List<RecipeTask> taskList = new ArrayList<RecipeTask>();
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("recipes/" + fileName));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println("line = " + line);
                RecipeTask recipeTask = new RecipeTask();
                recipeTask.name = line.split(" ")[0];
                System.out.println("recipeTask.name = " + recipeTask.name);
                System.out.println("recipeTask.name.equals() = " + recipeTask.name.equals("cook"));
                recipeTask.time = Integer.parseInt(line.split(" ")[1]);
                taskList.add(recipeTask);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    public List<String> getFileList() {
        List<String> fileList = new ArrayList<String>();
        String path = "recipes/";
        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files = listOfFiles[i].getName();
                if (files.endsWith(".txt") || files.endsWith(".TXT")) {
                    fileList.add(files);
                }
            }
        }
        return fileList;
    }
}
