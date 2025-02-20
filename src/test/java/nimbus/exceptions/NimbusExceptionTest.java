package nimbus.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NimbusExceptionTest {

    @Test
    void testExceptionMessage() {
        NimbusException exception = new NimbusException("This is a test error message.");
        assertEquals("This is a test error message.", exception.getMessage());
    }

    @Test
    void testThrowNimbusException() {
        Exception exception = assertThrows(NimbusException.class, () -> {
            throw new NimbusException("Something went wrong!");
        });

        assertEquals("Something went wrong!", exception.getMessage());
    }

    @Test
    void testEmptyMessage() {
        NimbusException exception = new NimbusException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    void testNullMessage() {
        NimbusException exception = new NimbusException(null);
        assertNull(exception.getMessage());
    }
}