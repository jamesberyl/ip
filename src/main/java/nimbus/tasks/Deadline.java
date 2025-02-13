package nimbus.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import nimbus.exceptions.NimbusException;


/**
 * Represents a Deadline task in the Nimbus Chatbot application.
 * A Deadline task has a description and a specific date and time by which it must be completed.
 */
public class Deadline extends Task {
    private LocalDateTime by;
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
    private static final List<DateTimeFormatter> INPUT_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"),
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"),
            DateTimeFormatter.ofPattern("dd MM yyyy HHmm")
    );

    /**
     * Constructs a Deadline task with the specified description and due date/time.
     *
     * @param description The description of the task.
     * @param by The due date and time in a supported format.
     * @throws NimbusException If the date/time format is invalid.
     */
    public Deadline(String description, String by) throws NimbusException {
        super(description);
        this.by = parseDateTime(by);
    }

    /**
     * Parses the date and time string into a LocalDateTime object.
     * Supports multiple date/time formats.
     *
     * @param dateTimeString The date and time string to parse.
     * @return The parsed LocalDateTime object.
     * @throws NimbusException If the date/time format is invalid.
     */
    private LocalDateTime parseDateTime(String dateTimeString) throws NimbusException {
        for (DateTimeFormatter format : INPUT_FORMATS) {
            try {
                return LocalDateTime.parse(dateTimeString, format);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new NimbusException("Oops! Invalid date format! Try examples like:\n"
                + " - 2023-10-15 1800\n"
                + " - 15/10/2023 1800\n"
                + " - Oct 15 2023 1800\n"
                + " - 15 10 2023 1800");
    }

    /**
     * Returns a string representation of the Deadline task.
     * The format includes the task type, status, description, and due date/time.
     *
     * @return The formatted string representation of the Deadline task.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Converts the Deadline task into a string suitable for file storage.
     *
     * @return The formatted string representing the task for file storage.
     */
    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by.format(INPUT_FORMATS.get(0));
    }

    /**
     * Checks if the Deadline task is scheduled for the specified date.
     *
     * @param date The date to check against the task's due date.
     * @return True if the task is scheduled on the specified date, false otherwise.
     */
    public boolean isOnDate(LocalDateTime date) {
        return by.toLocalDate().equals(date.toLocalDate());
    }
}
