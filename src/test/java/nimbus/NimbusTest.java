package nimbus;

import nimbus.exceptions.NimbusException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NimbusTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final java.io.InputStream originalIn = System.in;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outputStream));
        tempFile = File.createTempFile("nimbus_test", ".txt");
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testWelcomeAndExitMessages() {
        String simulatedInput = "bye\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Nimbus nimbus = new Nimbus(tempFile.getAbsolutePath());
        nimbus.run();

        String output = outputStream.toString();
        assertTrue(output.contains("Hey there! I'm Nimbus, your assistant."));
        assertTrue(output.contains("Stay awesome, and I’ll see you soon!"));
    }

    @Test
    void testAddTodoTask() {
        String simulatedInput = "todo Read book\nbye\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Nimbus nimbus = new Nimbus(tempFile.getAbsolutePath());
        nimbus.run();

        String output = outputStream.toString();
        assertTrue(output.contains("Got it. I've added this task:"));
        assertTrue(output.contains("[T][ ] Read book"));
    }

    @Test
    void testInvalidCommand() {
        String simulatedInput = "invalidCommand\nbye\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Nimbus nimbus = new Nimbus(tempFile.getAbsolutePath());
        nimbus.run();

        String output = outputStream.toString();
        assertTrue(output.contains("Oops! I don't recognize that command."));
    }

    @Test
    void testEmptyInputHandling() {
        String simulatedInput = "\nbye\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Nimbus nimbus = new Nimbus(tempFile.getAbsolutePath());
        nimbus.run();

        String output = outputStream.toString();
        assertTrue(output.contains("Oops! It seems like you entered nothing."));
    }
}