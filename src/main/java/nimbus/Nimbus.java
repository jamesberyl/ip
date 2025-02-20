package nimbus;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;


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
    private final UI ui;
    private final Storage storage;
    private final TaskList taskList;
    private final Parser parser;

    /**
     * Constructs a Nimbus chatbot instance with the specified file path for storage.
     *
     * @param filepath The path to the file where tasks are stored.
     */
    public Nimbus(String filepath) throws NimbusException {
        this.ui = new UI();
        this.storage = new Storage(filepath);
        this.taskList = new TaskList(storage, ui);
        this.parser = new Parser(taskList, ui, storage);
    }

    /**
     * Default constructor for JavaFX GUI.
     * Uses the default storage file path.
     */
    public Nimbus() throws NimbusException {
        this("./data/nimbus.txt");
    }

    /**
     * Processes user input and returns Nimbus's response.
     * This method is used by the GUI to interact with the chatbot.
     *
     * @param input The user input string.
     * @return The chatbot's response.
     */
    public String getResponse(String input) {
        try {
            if (input.equalsIgnoreCase("bye")) {
                String exitMessage = ui.showExitMessage();

                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(event -> Platform.exit());
                delay.play();

                return exitMessage;
            }

            String[] commands = input.split("\\r?\\n|;");
            StringBuilder responseBuilder = new StringBuilder();

            for (String command : commands) {
                if (!command.trim().isEmpty()) {
                    responseBuilder.append(parser.processCommand(command.trim())).append("\n");
                }
            }

            return responseBuilder.toString().trim();
        } catch (NimbusException e) {
            return ui.showErrorMessage(e.getMessage());
        }
    }
}