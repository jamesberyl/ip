package nimbus.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import nimbus.exceptions.NimbusException;

/**
 * Represents an Event task in the Nimbus Chatbot application.
 * An Event task has a description, a start date and time, and an end date and time.
 */
public class Event extends Task {
    private LocalDateTime fromDateTime;
    private LocalDateTime toDateTime;
    private static final List<DateTimeFormatter> INPUT_FORMATS = Arrays.asList(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"),
            DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"),
            DateTimeFormatter.ofPattern("dd MM yyyy HHmm")
    );
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    /**
     * Constructs an Event task with the specified description, start time, and end time.
     *
     * @param description The description of the event.
     * @param from The start date and time in a supported format.
     * @param to The end date and time in a supported format.
     * @throws NimbusException If the date/time format is invalid.
     */
    public Event(String description, String from, String to) throws NimbusException {
        super(description);
        this.fromDateTime = parseDateTime(from);
        this.toDateTime = parseDateTime(to);
    }

    public LocalDateTime getFromDateTime() {
        return fromDateTime;
    }

    public LocalDateTime getToDateTime() {
        return toDateTime;
    }

    /**
     * Parses the date and time string into a LocalDateTime object.
     * Supports multiple date/time formats.
     *
     * @param dateTimeString The date and time string toDateTime parse.
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
     * Returns a string representation of the Event task.
     * The format includes the task type, status, description, and date/time range.
     *
     * @return The formatted string representation of the Event task.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + fromDateTime.format(OUTPUT_FORMAT) + " toDateTime: " + toDateTime.format(OUTPUT_FORMAT) + ")";
    }

    /**
     * Converts the Event task into a string suitable for file storage.
     *
     * @return The formatted string representing the task for file storage.
     */
    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + fromDateTime.format(INPUT_FORMATS.get(0)) + " | " + toDateTime.format(INPUT_FORMATS.get(0));
    }

    /**
     * Checks if the Event task occurs on the specified date.
     *
     * @param date The date toDateTime check against the event's start and end dates.
     * @return True if the event occurs on the specified date, false otherwise.
     */
    public boolean isOnDate(LocalDateTime date) {
        return fromDateTime.toLocalDate().equals(date.toLocalDate()) || toDateTime.toLocalDate().equals(date.toLocalDate());
    }
}
