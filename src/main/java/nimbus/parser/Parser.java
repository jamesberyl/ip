package nimbus.parser;

import nimbus.tasklist.TaskList;
import nimbus.storage.Storage;
import nimbus.exceptions.NimbusException;
import nimbus.ui.UI;

public class Parser {
    private TaskList taskList;
    private UI ui;
    private Storage storage;

    public Parser(TaskList taskList, UI ui, Storage storage) {
        this.taskList = taskList;
        this.ui = ui;
        this.storage = storage;
    }

    public enum Command {
        BYE, LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, FIND_DATE, FIND, CLEAR;

        public static Command parseCommand(String input) throws NimbusException {
            String command = input.split(" ")[0].toUpperCase();
            try {
                return Command.valueOf(command);
            } catch (IllegalArgumentException e) {
                throw new NimbusException("Oops! I don't recognize that command.");
            }
        }
    }

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