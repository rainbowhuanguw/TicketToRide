package carddeck;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TaskFileReader {

    // class variable
    private static List<String> tasksList = new LinkedList<>();
    private static TaskFileReader reader;

    static {
        try {
            reader = new TaskFileReader("src/file/destinations.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // instance variable
    private static File taskFile;
    private static Scanner fileScanner;

    /**
     * private constructor
     */
    private TaskFileReader(String fileName) throws FileNotFoundException {
        taskFile = new File(fileName);
        fileScanner = new Scanner(taskFile);
    }

    public static List<String> getTasks() {
        createTaskList();
        return tasksList;
    }


    public static TaskFileReader getReaderInstance() {
        return reader;
    }

    /**
     * parse the text file containing the task card info, store a line as a string to tasks list,
     * only parse the file when the list is empty
     * format - "city1|city2|points"
     */
    private static void createTaskList () {
        if (tasksList.isEmpty()) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                tasksList.add(line);
            }
            fileScanner.close();
        }
    }
}
