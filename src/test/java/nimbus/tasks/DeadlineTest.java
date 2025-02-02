package nimbus.tasks;

import nimbus.exceptions.NimbusException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeadlineTest {

    @Test
    void testValidDeadlineCreation() throws NimbusException {
        Deadline deadline = new Deadline("Submit report", "2023-12-01 1800");
        assertEquals("[D][ ] Submit report (by: Dec 01 2023, 6:00 pm)", deadline.toString());
    }

    @Test
    void testInvalidDateFormat() {
        NimbusException exception = assertThrows(NimbusException.class, () -> {
            new Deadline("Invalid date", "12-01-2023 1800"); // Invalid format
        });
        assertEquals("Oops! Invalid date format! Try examples like:\n"
                + " - 2023-10-15 1800\n"
                + " - 15/10/2023 1800\n"
                + " - Oct 15 2023 1800\n"
                + " - 15 10 2023 1800", exception.getMessage());
    }

    @Test
    void testToFileString() throws NimbusException {
        Deadline deadline = new Deadline("Complete assignment", "2023-11-20 1700");
        assertEquals("D | 0 | Complete assignment | 2023-11-20 1700", deadline.toFileString());
    }

    @Test
    void testIsOnDate() throws NimbusException {
        Deadline deadline = new Deadline("Project deadline", "2023-11-15 0900");
        LocalDateTime date = LocalDateTime.parse("2023-11-15T10:00");
        assertEquals(true, deadline.isOnDate(date));
    }
}