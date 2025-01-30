import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Nimbus {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static void main(String[] args) {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Nimbus, your assistant. ☁️");
        System.out.println(" How can I make your day brighter?");
        System.out.println("____________________________________________________________");

        ArrayList<Task> tasks = new ArrayList<>(Storage.loadTasks());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String userInput = scanner.nextLine().trim();

                // Handle empty input
                if (userInput.isEmpty()) {
                    throw new NimbusException("Oops! It seems like you entered nothing. Try typing a command.");
                }

                // Parse command
                Command command = Command.parseCommand(userInput);

                // Execute command
                switch (command) {
                    case BYE -> {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Stay awesome, and I’ll see you soon! 👋");
                        System.out.println("____________________________________________________________");
                        Storage.saveTasks(tasks); // Save before exiting
                        return; // Exit program
                    }
                    case LIST -> printTasks(tasks);
                    case TODO -> addTodoTask(userInput, tasks);
                    case DEADLINE -> addDeadlineTask(userInput, tasks);
                    case EVENT -> addEventTask(userInput, tasks);
                    case MARK -> markTask(userInput, tasks, true);
                    case UNMARK -> markTask(userInput, tasks, false);
                    case DELETE -> deleteTask(userInput, tasks);
                    case FIND_DATE -> findTasksByDate(userInput, tasks);
                    case CLEAR -> clearAllTasks(tasks);
                    default -> throw new NimbusException("Oops! I don't recognize that command.");
                }

                Storage.saveTasks(tasks); // Auto-save after modification
            } catch (NimbusException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" " + e.getMessage());
                System.out.println("____________________________________________________________");
            } catch (Exception e) {
                System.out.println("____________________________________________________________");
                System.out.println(" Something went wrong! Please try again. If the problem persists, contact support.");
                System.out.println("____________________________________________________________");
            }
        }
    }

    // Enum for commands
    enum Command {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, FIND_DATE, CLEAR;

        public static Command parseCommand(String input) throws NimbusException {
            String command = input.split(" ")[0].toUpperCase();
            try {
                return Command.valueOf(command);
            } catch (IllegalArgumentException e) {
                throw new NimbusException("Oops! I don't recognize that command.");
            }
        }
    }

    // Helper methods for tasks
    private static void printTasks(ArrayList<Task> tasks) {
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

    private static void addTodoTask(String input, ArrayList<Task> tasks) throws NimbusException {
        if (input.length() <= 5) {
            throw new NimbusException("Oops! The description of a todo cannot be empty.");
        }
        String description = input.substring(5).trim();
        tasks.add(new Todo(description));
        confirmTaskAdded(tasks);
    }

    private static void addDeadlineTask(String input, ArrayList<Task> tasks) throws NimbusException {
        if (input.length() <= 9 || !input.contains("/by")) {
            throw new NimbusException("Oops! Deadlines need a description and a '/by' date.");
        }
        String[] parts = input.substring(9).split(" /by ");
        try {
            tasks.add(new Deadline(parts[0].trim(), parts[1].trim()));
        } catch (IllegalArgumentException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" Oops! " + e.getMessage());
            System.out.println("____________________________________________________________");
            return;
        }
        confirmTaskAdded(tasks);
    }

    private static void addEventTask(String input, ArrayList<Task> tasks) throws NimbusException {
        if (input.length() <= 6 || (!input.contains("/from") || !input.contains("/to"))) {
            throw new NimbusException("Oops! Events need a description, '/from' time, and '/to' time.");
        }
        String[] parts = input.substring(6).split(" /from | /to ");
        try {
            tasks.add(new Event(parts[0].trim(), parts[1].trim(), parts[2].trim()));
        } catch (IllegalArgumentException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" Oops! " + e.getMessage());
            System.out.println("____________________________________________________________");
            return;
        }
        confirmTaskAdded(tasks);
    }

    private static void markTask(String input, ArrayList<Task> tasks, boolean isDone) throws NimbusException {
        int taskNumber = parseTaskNumber(input, tasks.size());
        if (isDone) {
            tasks.get(taskNumber).markAsDone();
            System.out.println("____________________________________________________________");
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            tasks.get(taskNumber).unmark();
            System.out.println("____________________________________________________________");
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + tasks.get(taskNumber));
        System.out.println("____________________________________________________________");
    }

    private static void deleteTask(String input, ArrayList<Task> tasks) throws NimbusException {
        int taskNumber = parseTaskNumber(input, tasks.size());
        Task removedTask = tasks.remove(taskNumber);
        System.out.println("____________________________________________________________");
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + removedTask);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private static int parseTaskNumber(String input, int taskCount) throws NimbusException {
        try {
            int taskNumber = Integer.parseInt(input.split(" ")[1]) - 1;
            if (taskNumber < 0 || taskNumber >= taskCount) {
                throw new NimbusException("Oops! That task number doesn't exist. Please check your list.");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new NimbusException("Oops! Please provide a valid task number.");
        }
    }

    private static void confirmTaskAdded(ArrayList<Task> tasks) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks.get(tasks.size() - 1));
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    private static void findTasksByDate(String input, ArrayList<Task> tasks) {
        try {
            String dateStr = input.split(" ", 2)[1];
            LocalDate searchDate = LocalDate.parse(dateStr, DATE_FORMAT);

            System.out.println(" Tasks on " + searchDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + ":");
            for (Task task : tasks) {
                if (task instanceof Deadline deadline && deadline.isOnDate(searchDate.atStartOfDay())) {
                    System.out.println(" " + task);
                }
                if (task instanceof Event event && event.isOnDate(searchDate.atStartOfDay())) {
                    System.out.println(" " + task);
                }
            }

        } catch (ArrayIndexOutOfBoundsException | DateTimeParseException e) {
            System.out.println("____________________________________________________________");
            System.out.println("Invalid date format! Try examples like:\n"
                    + " - 2023-10-15 1800\n"
                    + " - 15/10/2023 1800\n"
                    + " - Oct 15 2023 1800\n"
                    + " - 15 10 2023 1800");
            System.out.println("____________________________________________________________");
        }
    }

    private static void clearAllTasks(ArrayList<Task> tasks) {
        tasks.clear(); // Remove all tasks
        Storage.saveTasks(tasks); // Save the empty task list to file
        System.out.println("____________________________________________________________");
        System.out.println(" All tasks have been cleared.");
        System.out.println("____________________________________________________________");
    }
}