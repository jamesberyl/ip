package nimbus.parser;

import nimbus.exceptions.NimbusException;
import nimbus.storage.Storage;
import nimbus.tasklist.TaskList;
import nimbus.ui.UI;

/**
 * Handles the parsing and processing of user commands in the Nimbus Chatbot application.
 * This class interprets user input, identifies commands, and invokes corresponding actions
 * on the task list, UI, and storage components.
 */
public class Parser {
    private TaskList taskList;
    private UI ui;
    private Storage storage;

    /**
     * Constructs a Parser with the specified task list, UI, and storage components.
     *
     * @param taskList The task list to manage user tasks.
     * @param ui The UI component to display messages to the user.
     * @param storage The storage component to persist tasks.
     */
    public Parser(TaskList taskList, UI ui, Storage storage) {
        this.taskList = taskList;
        this.ui = ui;
        this.storage = storage;
    }

    /**
     * Represents the list of supported commands in the Nimbus application.
     */
    public enum Command {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, FIND_DATE, FIND, CLEAR;

        /**
         * Parses the user input and returns the corresponding command.
         *
         * @param input The raw user input string.
         * @return The corresponding Command enum value.
         * @throws NimbusException If the command is unrecognized.
         */
        public static Command parseCommand(String input) throws NimbusException {
            String command = input.split(" ")[0].toUpperCase();
            try {
                return Command.valueOf(command);
            } catch (IllegalArgumentException e) {
                throw new NimbusException("Oops! I don't recognize that command.");
            }
        }
    }

    /**
     * Processes the user input by identifying the command and executing the corresponding action.
     *
     * @param input The user input command string.
     * @throws NimbusException If the input is invalid, unrecognized, or causes an error during processing.
     */
    public void processCommand(String input) throws NimbusException {
        if (input.isEmpty()) {
            throw new NimbusException("Oops! It seems like you entered nothing.");
        }

        Command command = Command.parseCommand(input);

        switch (command) {
            case BYE -> {
                ui.showExitMessage();
                return;
            }
            case LIST -> ui.showTaskList(taskList.getTasks());
            case TODO -> taskList.addTodoTask(input);
            case DEADLINE -> taskList.addDeadlineTask(input);
            case EVENT -> taskList.addEventTask(input);
            case MARK -> taskList.markTask(input, true);
            case UNMARK -> taskList.markTask(input, false);
            case DELETE -> taskList.deleteTask(input);
            case FIND_DATE -> taskList.findTasksByDate(input);
            case FIND -> taskList.findTasksByKeyword(input);
            case CLEAR -> taskList.clearAllTasks(ui);
            default -> throw new NimbusException("Oops! I don't recognize that command.");
        }

        storage.saveTasks(taskList.getTasks());
    }
}
