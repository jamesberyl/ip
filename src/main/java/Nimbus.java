import java.util.ArrayList;
import java.util.Scanner;

public class Nimbus {
    public static void main(String[] args) {
        // Greet the user
        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Nimbus, your assistant. ☁️");
        System.out.println(" How can I make your day brighter?");
        System.out.println("____________________________________________________________");

        ArrayList<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String userInput = scanner.nextLine().trim();

                // Handle empty input
                if (userInput.isEmpty()) {
                    throw new NimbusException("Oops! It seems like you entered nothing. Try typing a command.");
                }

                // Exit Nimbus
                if (userInput.equalsIgnoreCase("bye")) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Stay awesome, and I’ll see you soon! 👋");
                    System.out.println("____________________________________________________________");
                    break;
                } else if (userInput.equalsIgnoreCase("list")) {
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
                } else if (userInput.startsWith("todo")) {
                    if (userInput.length() <= 5) {
                        throw new NimbusException("Oops! The description of a todo cannot be empty.");
                    }
                    String description = userInput.substring(5).trim();
                    tasks.add(new Todo(description));
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks.get(tasks.size() - 1));
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("deadline")) {
                    if (userInput.length() <= 9 || !userInput.contains("/by")) {
                        throw new NimbusException("Oops! Deadlines need a description and a '/by' date. Example: deadline Submit report /by Monday.");
                    }
                    String[] parts = userInput.substring(9).split(" /by ");
                    tasks.add(new Deadline(parts[0].trim(), parts[1].trim()));
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks.get(tasks.size() - 1));
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("event")) {
                    if (userInput.length() <= 6 || (!userInput.contains("/from") || !userInput.contains("/to"))) {
                        throw new NimbusException("Oops! Events need a description, '/from' time, and '/to' time. Example: event Team meeting /from 2pm /to 4pm.");
                    }
                    String[] parts = userInput.substring(6).split(" /from | /to ");
                    tasks.add(new Event(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks.get(tasks.size() - 1));
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("mark")) {
                    int taskNumber = parseTaskNumber(userInput, tasks.size());
                    tasks.get(taskNumber).markAsDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(taskNumber));
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("unmark")) {
                    int taskNumber = parseTaskNumber(userInput, tasks.size());
                    tasks.get(taskNumber).unmark();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(taskNumber));
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("delete")) {
                    int taskNumber = parseTaskNumber(userInput, tasks.size());
                    Task removedTask = tasks.remove(taskNumber);
                    System.out.println("____________________________________________________________");
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removedTask);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else {
                    throw new NimbusException("Oops! I don't recognize that command. Try 'todo', 'deadline', or 'event' to start.");
                }
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

        scanner.close();
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
}