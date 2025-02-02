package nimbus.tasks;

/**
 * Represents a Todo task in the Nimbus Chatbot application.
 * A Todo task contains only a description without any specific date or time.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description The description of the Todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the Todo task.
     * The format includes the task type "[T]", status icon, and description.
     *
     * @return The formatted string representation of the Todo task.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Converts the Todo task into a string suitable for file storage.
     * The format includes the task type, completion status, and description.
     *
     * @return The formatted string representing the Todo task for file storage.
     */
    @Override
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
