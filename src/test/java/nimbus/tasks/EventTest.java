package nimbus.tasks;

import nimbus.exceptions.NimbusException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventTest {

    @Test
    void testValidEventCreation() throws NimbusException {
        Event event = new Event("Team meeting", "2023-11-01 1000", "2023-11-01 1200");
        assertEquals("[E][ ] Team meeting (from: Nov 01 2023, 10:00 am to: Nov 01 2023, 12:00 pm)", event.toString());
    }

    @Test
    void testInvalidDateFormat() {
        NimbusException exception = assertThrows(NimbusException.class, () -> {
            new Event("Invalid event", "2023/11/01 1000", "2023/11/01 1200"); // Invalid format
        });
        assertEquals("Oops! Invalid date format! Try examples like:\n"
                + " - 2023-10-15 1800\n"
                + " - 15/10/2023 1800\n"
                + " - Oct 15 2023 1800\n"
                + " - 15 10 2023 1800", exception.getMessage());
    }

    @Test
    void testToFileString() throws NimbusException {
        Event event = new Event("Conference", "2023-12-05 0900", "2023-12-05 1700");
        assertEquals("E | 0 | Conference | 2023-12-05 0900 | 2023-12-05 1700", event.toFileString());
    }

    @Test
    void testIsOnDate() throws NimbusException {
        Event event = new Event("Workshop", "2023-11-10 0900", "2023-11-11 1700");
        LocalDateTime date = LocalDateTime.parse("2023-11-11T15:00");
        assertEquals(true, event.isOnDate(date));
    }
}