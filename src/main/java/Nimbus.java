import java.util.Scanner;

public class Nimbus {
    public static void main(String[] args) {
        // Greet the user
        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Nimbus, your assistant. ☁️");
        System.out.println(" How can I make your day brighter?");
        System.out.println("____________________________________________________________");

        Task[] tasks = new Task[100];
        int taskCount = 0;
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
                    if (taskCount == 0) {
                        System.out.println(" Hmm... Your task list is empty. Ready to add something?");
                    } else {
                        System.out.println(" Here are the tasks in your list:");
                        for (int i = 0; i < taskCount; i++) {
                            System.out.println(" " + (i + 1) + ". " + tasks[i]);
                        }
                    }
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("todo")) {
                    if (userInput.length() <= 5) {
                        throw new NimbusException("Oops! The description of a todo cannot be empty.");
                    }
                    String description = userInput.substring(5).trim();
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("deadline")) {
                    if (userInput.length() <= 9 || !userInput.contains("/by")) { // Validate format and presence of `/by`
                        throw new NimbusException("Oops! Deadlines need a description and a '/by' date. Example: deadline Submit report /by Monday.");
                    }
                    String[] parts = userInput.substring(9).split(" /by ");
                    tasks[taskCount] = new Deadline(parts[0].trim(), parts[1].trim());
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("event")) {
                    if (userInput.length() <= 6 || (!userInput.contains("/from") || !userInput.contains("/to"))) { // Validate format and presence of `/from` and `/to`
                        throw new NimbusException("Oops! Events need a description, '/from' time, and '/to' time. Example: event Team meeting /from 2pm /to 4pm.");
                    }
                    String[] parts = userInput.substring(6).split(" /from | /to ");
                    tasks[taskCount] = new Event(parts[0].trim(), parts[1].trim(), parts[2].trim());
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("mark")) {
                    int taskNumber = parseTaskNumber(userInput, taskCount);
                    tasks[taskNumber].markAsDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[taskNumber]);
                    System.out.println("____________________________________________________________");
                } else if (userInput.startsWith("unmark")) {
                    int taskNumber = parseTaskNumber(userInput, taskCount);
                    tasks[taskNumber].unmark();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[taskNumber]);
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