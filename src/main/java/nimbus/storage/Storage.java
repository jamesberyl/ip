package nimbus.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import nimbus.exceptions.NimbusException;
import nimbus.tasks.Deadline;
import nimbus.tasks.Event;
import nimbus.tasks.Task;
import nimbus.tasks.Todo;

/**
 * Handles the storage and retrieval of tasks from a local file.
 * This class manages reading from and writing to the file system,
 * ensuring that tasks persist between sessions.
 */
public class Storage {
    private final String filePath;

    /**
     * Constructs a Storage object with the specified file path.
     *
     * @param filePath The path to the file where tasks will be saved and loaded.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the list of tasks to the storage file.
     * Ensures the parent directory exists before saving.
     *
     * @param tasks The list of tasks to be saved to the file.
     * @throws NimbusException If there is an error while saving.
     */
    public void saveTasks(ArrayList<Task> tasks) throws NimbusException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (!created) {
                throw new NimbusException("Error: Could not create directory for storage file.");
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(task.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new NimbusException("Error saving tasks to file: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the storage file.
     * If the file does not exist, returns an empty ArrayList.
     *
     * @return A list of tasks loaded from the file.
     * @throws NimbusException If there is an error while loading tasks.
     */
    public ArrayList<Task> loadTasks() throws NimbusException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    tasks.add(parseTask(line));
                } catch (Exception e) {
                    System.err.println("Skipping corrupted task in storage: " + line);
                }
            }
        } catch (IOException e) {
            throw new NimbusException("Error loading tasks from file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Parses a task line from storage and converts it into a Task object.
     *
     * @param line The stored task string in format: TYPE | STATUS | DESCRIPTION | (OPTIONAL: DATE/TIME)
     * @return The corresponding Task object.
     * @throws NimbusException if the format is invalid.
     */
    private Task parseTask(String line) throws NimbusException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new NimbusException("Invalid task data format: " + line);
        }

        String type = parts[0].trim();
        String status = parts[1].trim();
        String description = parts[2].trim();
        Task task;

        try {
            switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new NimbusException("Invalid deadline format: " + line);
                }
                String deadline = parts[3].trim();
                task = new Deadline(description, deadline);
                break;
            case "E":
                if (parts.length < 5) {
                    throw new NimbusException("Invalid event format: " + line);
                }
                String startTime = parts[3].trim();
                String endTime = parts[4].trim();
                task = new Event(description, startTime, endTime);
                break;
            default:
                throw new NimbusException("Unknown task type: " + line);
            }
        } catch (Exception e) {
            throw new NimbusException("Error parsing task: " + e.getMessage());
        }

        if (status.equals("1")) {
            task.markAsDone();
        }

        return task;
    }
}
