package nimbus.tasks;

import java.time.LocalDateTime;

/**
 * Represents an abstract Task in the Nimbus Chatbot application.
 * A Task has a description and a completion status. This class serves as the
 * base class for specific task types such as Todo, Deadline, and Event.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected LocalDateTime createdAt;

    /**
     * Constructs a Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the status icon of the task.
     * Displays "X" if the task is marked as done, or a blank space if not done.
     *
     * @return The status icon representing the task's completion status.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks the task as done by setting its status to true.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Unmarks the task, setting its status to not done.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Converts the task into a string suitable for file storage.
     * This method is abstract and must be implemented by subclasses.
     *
     * @return The formatted string representing the task for file storage.
     */
    public abstract String toFileString();

    /**
     * Returns a string representation of the task.
     * The format includes the task's status icon and description.
     *
     * @return The formatted string representation of the task.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}

