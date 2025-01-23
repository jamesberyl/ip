import java.util.Scanner;

public class Nimbus {
    public static void main(String[] args) {
        System.out.println("____________________________________________________________");
        System.out.println(" Hey there! I'm Nimbus, your assistant. ☁️");
        System.out.println(" How can I make your day brighter?");
        System.out.println("____________________________________________________________");

        String[] tasks = new String[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        String userInput;

        while (true) {
            userInput = scanner.nextLine().trim(); // Read user input

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
                    System.out.println(" Here’s what you’ve got so far:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                }
                System.out.println("____________________________________________________________");
            } else {
                // Add the task to the array and increment the task counter
                if (taskCount < 100) {
                    tasks[taskCount] = userInput;
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it! I’ve added: \"" + userInput + "\" to your task list. ");
                    System.out.println(" What else do you need?");
                    System.out.println("____________________________________________________________");
                } else {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Uh oh! Your task list is full. ");
                    System.out.println("____________________________________________________________");
                }
            }
        }

        scanner.close(); // Close the scanner
    }
}
