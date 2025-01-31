import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class UI {

    private Scanner scanner;

    public UI() {
        scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }
    public void showWelcomeMessage() {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Nimbus, your assistant. ☁️");
        System.out.println(" How can I make your day brighter?");
        System.out.println("____________________________________________________________");
    }

    public void showExitMessage() {
        System.out.println("____________________________________________________________");
        System.out.println(" Stay awesome, and I’ll see you soon! 👋");
        System.out.println("____________________________________________________________");
    }


    public void showErrorMessage(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" " + message);
        System.out.println("____________________________________________________________");
    }

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

    public void showTaskAdded(Task task, int size) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

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

    public void showTaskDeleted(Task task, int size) {
        System.out.println("____________________________________________________________");
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + size + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    public void showClearConfirmation() {
        System.out.println("____________________________________________________________");
        System.out.println(" ⚠ WARNING: This will delete ALL tasks permanently.");
        System.out.println(" Are you sure you want to proceed? (y/n)");
        System.out.println("____________________________________________________________");
    }

    public void showAllTasksCleared() {
        System.out.println("____________________________________________________________");
        System.out.println(" ✅ All tasks have been cleared.");
        System.out.println("____________________________________________________________");
    }

    public void showTaskClearingCancelled() {
        System.out.println("____________________________________________________________");
        System.out.println(" ❌ Task clearing cancelled.");
        System.out.println("____________________________________________________________");
    }

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
}