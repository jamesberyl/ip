import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the list of tasks to the storage file.
     * Ensures the parent directory exists before saving.
     */
    public void saveTasks(ArrayList<Task> tasks) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        // Ensure the directory exists before writing
        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                System.out.println("Warning: Could not create directory for storage file.");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the storage file.
     * If the file does not exist, returns an empty ArrayList.
     */
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        // If file does not exist, return empty list
        if (!file.exists()) {
            System.out.println("No previous tasks found. Starting with an empty list.");
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    tasks.add(parseTask(line));
                } catch (Exception e) {
                    System.out.println("Skipping corrupted line in storage: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Parses a task line from storage and converts it into a Task object.
     *
     * @param line The stored task string in format: TYPE | STATUS | DESCRIPTION | (OPTIONAL: DATE/TIME)
     * @return The corresponding Task object.
     * @throws Exception if the format is invalid.
     */
    private Task parseTask(String line) throws Exception {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new Exception("Invalid task data format: " + line);
        }

        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String description = parts[2].trim();

        switch (type) {
            case "T": // Todo Task
                return new Todo(description);

            case "D": // Deadline Task
                if (parts.length < 4) {
                    throw new Exception("Invalid deadline format: " + line);
                }
                String deadline = parts[3].trim();
                return new Deadline(description, deadline);

            case "E": // Event Task
                if (parts.length < 5) {
                    throw new Exception("Invalid event format: " + line);
                }
                String startTime = parts[3].trim();
                String endTime = parts[4].trim();
                return new Event(description, startTime, endTime);

            default:
                throw new Exception("Unknown task type: " + line);
        }
    }
}