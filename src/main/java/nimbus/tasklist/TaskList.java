package nimbus.tasklist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import nimbus.exceptions.NimbusException;
import nimbus.storage.Storage;
import nimbus.tasks.Deadline;
import nimbus.tasks.Event;
import nimbus.tasks.Task;
import nimbus.tasks.Todo;
import nimbus.ui.UI;


/**
 * Manages the list of tasks in the Nimbus Chatbot application.
 * Handles operations such as adding, marking, deleting, and searching tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    private final Storage storage;
    private final UI ui;

    /**
     * Constructs a TaskList with the specified storage and UI components.
     * Loads existing tasks from storage if available.
     *
     * @param storage The storage component to persist tasks.
     * @param ui The UI component to display messages to the user.
     */
    public TaskList(Storage storage, UI ui) throws NimbusException {
        this.storage = storage;
        this.ui = ui;
        this.tasks = storage.loadTasks();
    }

    /**
     * Returns the list of tasks.
     *
     * @return The list of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Adds a Todo task to the task list.
     *
     * @param input The user input containing the task description.
     * @throws NimbusException If the description is empty.
     */
    public String addTodoTask(String input) throws NimbusException {
        assert input != null : "Input for Todo task should not be null";

        if (input.length() <= 5) {
            throw new NimbusException("Oops! The description of a todo cannot be empty.");
        }
        String description = input.substring(5).trim();
        Task task = new Todo(description);
        tasks.add(task);
        return ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Adds a Deadline task to the task list.
     *
     * @param input The user input containing the task description and deadline.
     * @throws NimbusException If the input format is invalid.
     */
    public String addDeadlineTask(String input) throws NimbusException {
        assert input != null : "Input for Deadline task should not be null";

        if (!input.contains("/by")) {
            throw new NimbusException("Oops! Deadlines need a description and a '/by' date.");
        }
        String[] parts = input.substring(9).split(" /by ");
        Task task = new Deadline(parts[0].trim(), parts[1].trim());
        tasks.add(task);
        return ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Adds an Event task to the task list.
     *
     * @param input The user input containing the task description, start time, and end time.
     * @throws NimbusException If the input format is invalid.
     */
    public String addEventTask(String input) throws NimbusException {
        assert input != null : "Input for Event task should not be null";

        if (!input.contains("/from") || !input.contains("/to")) {
            throw new NimbusException("Oops! Events need a description, '/from' time, and '/to' time.");
        }
        String[] parts = input.substring(6).split(" /from | /to ");
        Task task = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
        tasks.add(task);
        return ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Marks or unmarks a task as done based on the user input.
     *
     * @param input The user input containing the task number.
     * @param isDone True to mark the task as done, false to unmark it.
     * @throws NimbusException If the task number is invalid.
     */
    public String markTask(String input, boolean isDone) throws NimbusException {
        int taskNumber = parseTaskNumber(input);

        Task task = tasks.get(taskNumber);
        if (isDone) {
            task.markAsDone();
        } else {
            task.unmark();
        }
        return ui.showTaskMarked(task, isDone);
    }

    /**
     * Deletes a task from the task list based on the user input.
     *
     * @param input The user input containing the task number to be deleted.
     * @throws NimbusException If the task number is invalid.
     */
    public String deleteTask(String input) throws NimbusException {
        int taskNumber = parseTaskNumber(input);

        Task removedTask = tasks.remove(taskNumber);
        return ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Clears all tasks from the task list.
     *
     * @param ui The UI component to handle user interaction.
     */
    public String clearAllTasks(UI ui) throws NimbusException {
        tasks.clear();
        storage.saveTasks(tasks);
        return ui.showAllTasksCleared();
    }

    /**
     * Finds tasks that match a specific date based on user input.
     *
     * @param input The user input containing the date to search for tasks.
     */
    public String findTasksByDate(String input) {
        try {
            String dateStr = input.split(" ", 2)[1].trim();
            LocalDate searchDate = null;

            DateTimeFormatter[] formats = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                    DateTimeFormatter.ofPattern("MMM dd yyyy"),
                    DateTimeFormatter.ofPattern("dd MM yyyy")
            };

            for (DateTimeFormatter format : formats) {
                try {
                    searchDate = LocalDate.parse(dateStr, format);
                    break;
                } catch (DateTimeParseException ignored) {
                }
            }

            if (searchDate == null) {
                throw new NimbusException("Oops! Invalid date format! Try examples like:\n"
                        + " - 2023-10-15\n"
                        + " - 15/10/2023\n"
                        + " - Oct 15 2023\n"
                        + " - 15 10 2023");
            }

            return UI.showTasksOnDate(searchDate, tasks);
        } catch (ArrayIndexOutOfBoundsException e) {
            return ui.showErrorMessage("Oops! Please enter a date after 'find_date'. Example: find_date 2023-12-01");
        } catch (NimbusException e) {
            return ui.showErrorMessage(e.getMessage());
        }
    }

    /**
     * Parses the task number from user input.
     *
     * @param input The user input containing the task number.
     * @return The zero-based index of the task.
     * @throws NimbusException If the task number is invalid or out of range.
     */
    private int parseTaskNumber(String input) throws NimbusException {
        assert input != null : "Task number input should not be null";
        assert input.split(" ").length > 1 : "Task number input should contain a number";

        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
            if (taskNumber < 0 || taskNumber >= tasks.size()) {
                throw new NimbusException("Oops! That task number doesn't exist. Please check your list.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new NimbusException("Oops! Please provide a valid task number.");
        }
    }

    /**
     * Finds tasks that contain the specified keyword in their description.
     *
     * @param input The user input containing the keyword to search for.
     */
    public String findTasksByKeyword(String input) {
        try {
            String keyword = input.split(" ", 2)[1].trim();
            ArrayList<Task> matchingTasks = new ArrayList<>();

            for (Task task : tasks) {
                if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                    matchingTasks.add(task);
                }
            }
            return ui.showMatchingTasks(matchingTasks, keyword);
        } catch (ArrayIndexOutOfBoundsException e) {
            return ui.showErrorMessage("Oops! Please enter a keyword after 'find'. Example: find book");
        }
    }
}

