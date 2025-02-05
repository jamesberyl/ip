package nimbus.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import nimbus.tasks.Deadline;
import nimbus.tasks.Event;
import nimbus.tasks.Task;

/**
 * Handles all user interactions in the Nimbus Chatbot application.
 * Responsible for displaying messages, reading user input, and presenting task-related information.
 */
public class UI {


    /**
     * Displays a welcome message when the application starts.
     */
    public String showWelcomeMessage() {
        return "Hey there! I'm Nimbus, your assistant. ☁️\n"
                + "How can I make your day brighter?";
    }


    /**
     * Displays an exit message when the application is terminated.
     */
    public String showExitMessage() {
        return "Stay awesome, and I’ll see you soon! 👋";
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public String showErrorMessage(String message) {
        return "⚠ ERROR: " + message;
    }

    /**
     * Displays the list of tasks.
     *
     * @param tasks The list of tasks to display.
     */
    public String showTaskList(ArrayList<Task> tasks) {
        StringBuilder output = new StringBuilder();
        if (tasks.isEmpty()) {
            output.append("Hmm... Your task list is empty. Ready to add something?");
        } else {
            output.append("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                output.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
            }
        }
        return output.toString().trim();
    }

    /**
     * Displays a confirmation message when a task is added.
     *
     * @param task The task that was added.
     * @param size The current number of tasks in the list.
     */
    public String showTaskAdded(Task task, int size) {
        return "Got it. I've added this task:\n"
                + "  " + task + "\n"
                + "Now you have " + size + " tasks in the list.";
    }

    /**
     * Displays a message when a task is marked as done or not done.
     *
     * @param task The task that was marked or unmarked.
     * @param isDone True if the task was marked as done, false if unmarked.
     */
    public String showTaskMarked(Task task, boolean isDone) {
        if (isDone) {
            return "Nice! I've marked this task as done:\n  " + task;
        } else {
            return "OK, I've marked this task as not done yet:\n  " + task;
        }
    }

    /**
     * Displays a message when a task is deleted.
     *
     * @param task The task that was deleted.
     * @param size The current number of tasks in the list after deletion.
     */
    public String showTaskDeleted(Task task, int size) {
        return "Noted. I've removed this task:\n"
                + "  " + task + "\n"
                + "Now you have " + size + " tasks in the list.";
    }

    /**
     * Displays a confirmation prompt before clearing all tasks.
     */
    public void showClearConfirmation() {
        System.out.println("____________________________________________________________");
        System.out.println(" ⚠ WARNING: This will delete ALL tasks permanently.");
        System.out.println(" Are you sure you want to proceed? (y/n)");
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a message when all tasks have been cleared.
     */
    public String showAllTasksCleared() {
        return "✅ All tasks have been cleared.";
    }

    /**
     * Displays a message when the task-clearing operation is canceled.
     */
    public void showTaskClearingCancelled() {
        System.out.println("____________________________________________________________");
        System.out.println(" ❌ Task clearing cancelled.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays tasks that occur on a specific date.
     *
     * @param searchDate The date to search for tasks.
     * @param tasks The list of tasks to filter and display.
     */
    public static String showTasksOnDate(LocalDate searchDate, ArrayList<Task> tasks) {
        StringBuilder output = new StringBuilder();
        output.append("Tasks on ").append(searchDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))).append(":\n");

        boolean found = false;
        for (Task task : tasks) {
            if (task instanceof Deadline deadline && deadline.isOnDate(searchDate.atStartOfDay())) {
                output.append("  ").append(task).append("\n");
                found = true;
            }
            if (task instanceof Event event && event.isOnDate(searchDate.atStartOfDay())) {
                output.append("  ").append(task).append("\n");
                found = true;
            }
        }

        if (!found) {
            output.append("  No tasks found on this date.");
        }

        return output.toString().trim();
    }

    /**
     * Displays tasks that match the given keyword.
     *
     * @param matchingTasks The list of tasks that match the keyword.
     * @param keyword The keyword used for the search.
     */
    public String showMatchingTasks(ArrayList<Task> matchingTasks, String keyword) {
        StringBuilder output = new StringBuilder();
        output.append("Here are the matching tasks for \"").append(keyword).append("\":\n");

        if (matchingTasks.isEmpty()) {
            output.append("  No matching tasks found.");
        } else {
            for (int i = 0; i < matchingTasks.size(); i++) {
                output.append((i + 1)).append(". ").append(matchingTasks.get(i)).append("\n");
            }
        }

        return output.toString().trim();
    }
}
