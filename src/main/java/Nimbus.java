import java.util.Scanner;

public class Nimbus {
    public static void main(String[] args) {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Nimbus, your assistant. ☁️");
        System.out.println(" How can I make your day brighter?");
        System.out.println("____________________________________________________________");

        Task[] tasks = new Task[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            userInput = scanner.nextLine().trim();

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
            } else if (userInput.startsWith("mark")) {
                try {
                    int taskNumber = Integer.parseInt(userInput.split(" ")[1]) - 1;
                    if (taskNumber >= 0 && taskNumber < taskCount) {
                        tasks[taskNumber].markAsDone();
                        System.out.println("____________________________________________________________");
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[taskNumber]);
                        System.out.println("____________________________________________________________");
                    } else {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Invalid task number. Please try again.");
                        System.out.println("____________________________________________________________");
                    }
                } catch (Exception e) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Please specify a valid task number to mark.");
                    System.out.println("____________________________________________________________");
                }
            } else if (userInput.startsWith("unmark")) {
                try {
                    int taskNumber = Integer.parseInt(userInput.split(" ")[1]) - 1;
                    if (taskNumber >= 0 && taskNumber < taskCount) {
                        tasks[taskNumber].unmark();
                        System.out.println("____________________________________________________________");
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[taskNumber]);
                        System.out.println("____________________________________________________________");
                    } else {
                        System.out.println("____________________________________________________________");
                        System.out.println(" Invalid task number. Please try again.");
                        System.out.println("____________________________________________________________");
                    }
                } catch (Exception e) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Please specify a valid task number to unmark.");
                    System.out.println("____________________________________________________________");
                }
            } else {
                if (taskCount < 100) {
                    tasks[taskCount] = new Task(userInput);
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it! I’ve added: \"" + userInput + "\" to your task list.");
                    System.out.println("____________________________________________________________");
                } else {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Uh oh! Your task list is full.");
                    System.out.println("____________________________________________________________");
                }
            }
        }

        scanner.close();
    }
}