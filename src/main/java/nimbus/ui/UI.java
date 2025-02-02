package nimbus.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import nimbus.tasks.Deadline;
import nimbus.tasks.Event;
import nimbus.tasks.Task;

/**
 * Handles all user interactions in the Nimbus Chatbot application.
 * Responsible for displaying messages, reading user input, and presenting task-related information.
 */
public class UI {

    private Scanner scanner;

    /**
     * Constructs a UI object for handling user interactions.
     * Initializes the scanner to read user input from the console.
     */
    public UI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return The user input as a string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a welcome message when the application starts.
     */
    public void showWelcomeMessage() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Nimbus, your assistant. ☁️");
        System.out.println(" How can I make your day brighter?");
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays an exit message when the application is terminated.
     */
    public void showExitMessage() {
        System.out.println("____________________________________________________________");
        System.out.println(" Stay awesome, and I’ll see you soon! 👋");
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showErrorMessage(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" " + message);
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays the list of tasks.
     *
     * @param tasks The list of tasks to display.
     */
    public void showTaskList(ArrayList<Task> tasks) {
        System.out.println("____________________________________________________________");
        if (tasks.isEmpty()) {
            System.out.println(" Hmm... Your task list is empty. Ready to add something?");
        } else {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + ". " + tasks.get(i));
            }
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a confirmation message when a task is added.
     *
     * @param task The task that was added.
     * @param size The current number of tasks in the list.
     */
    public void showTaskAdded(Task task, int size) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a message when a task is marked as done or not done.
     *
     * @param task The task that was marked or unmarked.
     * @param isDone True if the task was marked as done, false if unmarked.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        System.out.println("____________________________________________________________");
        if (isDone) {
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + task);
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays a message when a task is deleted.
     *
     * @param task The task that was deleted.
     * @param size The current number of tasks in the list after deletion.
     */
    public void showTaskDeleted(Task task, int size) {
        System.out.println("____________________________________________________________");
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
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
    public void showAllTasksCleared() {
        System.out.println("____________________________________________________________");
        System.out.println(" ✅ All tasks have been cleared.");
        System.out.println("____________________________________________________________");
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
    public static void showTasksOnDate(LocalDate searchDate, ArrayList<Task> tasks) {
        System.out.println("____________________________________________________________");
        System.out.println(" Tasks on " + searchDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");

        boolean found = false;
        for (Task task : tasks) {
            if (task instanceof Deadline deadline && deadline.isOnDate(searchDate.atStartOfDay())) {
                System.out.println("   " + task);
                found = true;
            }
            if (task instanceof Event event && event.isOnDate(searchDate.atStartOfDay())) {
                System.out.println("   " + task);
                found = true;
            }
        }

        if (!found) {
            System.out.println("   No tasks found on this date.");
        }

        System.out.println("____________________________________________________________");
    }

    /**
     * Displays tasks that match the given keyword.
     *
     * @param matchingTasks The list of tasks that match the keyword.
     * @param keyword The keyword used for the search.
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks, String keyword) {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the matching tasks in your list for \"" + keyword + "\":");

        if (matchingTasks.isEmpty()) {
            System.out.println("   No matching tasks found.");
        } else {
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + ". " + matchingTasks.get(i));
            }
        }

        System.out.println("____________________________________________________________");
    }
}
