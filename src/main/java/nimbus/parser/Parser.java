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
    private final TaskList taskList;
    private final UI ui;
    private final Storage storage;

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
            String commandKeyword = extractCommandKeyword(input);
            try {
                return Command.valueOf(commandKeyword);
            } catch (IllegalArgumentException e) {
                throw new NimbusException("Oops! I don't recognize that command.");
            }
        }

        private static String extractCommandKeyword(String input) {
            return input.split(" ")[0].toUpperCase();
        }
    }

    /**
     * Processes the user input by identifying the command and executing the corresponding action.
     *
     * @param input The user input command string.
     * @return A string containing the response message.
     * @throws NimbusException If the input is invalid, unrecognized, or causes an error during processing.
     */
    public String processCommand(String input) throws NimbusException {
        if (input.isEmpty()) {
            throw new NimbusException("Oops! It seems like you entered nothing.");
        }

        Command command = Command.parseCommand(input);
        String response;

        switch (command) {
        case BYE -> {
            response = ui.showExitMessage();
        }
        case LIST -> {
            response = ui.showTaskList(taskList.getTasks());
        }
        case TODO -> {
            response = taskList.addTodoTask(input);
        }
        case DEADLINE -> {
            response = taskList.addDeadlineTask(input);
        }
        case EVENT -> {
            response = taskList.addEventTask(input);
        }
        case MARK -> {
            response = taskList.markTask(input, true);
        }
        case UNMARK -> {
            response = taskList.markTask(input, false);
        }
        case DELETE -> {
            response = taskList.deleteTask(input);
        }
        case FIND_DATE -> {
            response = taskList.findTasksByDate(input);
        }
        case FIND -> {
            response = taskList.findTasksByKeyword(input);
        }
        case CLEAR -> {
            response = taskList.clearAllTasks(ui);
        }
        default -> throw new NimbusException("Oops! I don't recognize that command.");
        }

        storage.saveTasks(taskList.getTasks());
        return response;
    }
}
