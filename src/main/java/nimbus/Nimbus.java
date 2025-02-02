package nimbus;

import nimbus.ui.UI;
import nimbus.storage.Storage;
import nimbus.tasklist.TaskList;
import nimbus.parser.Parser;
import nimbus.exceptions.NimbusException;

/**
 * The main class for the Nimbus Chatbot application.
 * Initializes the user interface, storage, task list, and parser,
 * and manages the application's main execution flow.
 */
public class Nimbus {
    private UI ui;
    private Storage storage;
    private TaskList taskList;
    private Parser parser;

    /**
     * Constructs a Nimbus chatbot instance with the specified file path for storage.
     *
     * @param filepath The path to the file where tasks are stored.
     */
    public Nimbus(String filepath) {
        this.ui = new UI();
        this.storage = new Storage(filepath);
        this.taskList = new TaskList(storage, ui);
        this.parser = new Parser(taskList, ui, storage);
    }

    /**
     * Starts the Nimbus chatbot, displaying the welcome message and
     * continuously processing user commands until the exit command is received.
     */
    public void run() {
        ui.showWelcomeMessage();
        boolean isRunning = true;

        while (isRunning) {
            try {
                String input = ui.readCommand().trim();
                if (input.equalsIgnoreCase("bye")) {
                    ui.showExitMessage();
                    break;
                }
                parser.processCommand(input);
            } catch (NimbusException e) {
                ui.showErrorMessage(e.getMessage());
            }
        }
    }

    /**
     * The entry point of the Nimbus application.
     * Initializes the Nimbus chatbot with the default storage file path and starts it.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        new Nimbus("./data/nimbus.txt").run();
    }
}
