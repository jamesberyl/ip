package nimbus.ui;

import nimbus.tasks.Task;
import nimbus.tasks.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UITest {

    private UI ui;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        ui = new UI();
    }

    @Test
    void testShowWelcomeMessage() {
        ui.showWelcomeMessage();
        String output = outputStream.toString();
        assertEquals(true, output.contains("Hey there! I'm Nimbus, your assistant."));
    }

    @Test
    void testShowExitMessage() {
        ui.showExitMessage();
        String output = outputStream.toString();
        assertEquals(true, output.contains("Stay awesome, and I’ll see you soon!"));
    }

    @Test
    void testShowErrorMessage() {
        ui.showErrorMessage("This is an error!");
        String output = outputStream.toString();
        assertEquals(true, output.contains("This is an error!"));
    }

    @Test
    void testShowTaskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("Read a book"));

        ui.showTaskList(tasks);
        String output = outputStream.toString();
        assertEquals(true, output.contains("1. [T][ ] Read a book"));
    }

    @Test
    void testShowEmptyTaskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        ui.showTaskList(tasks);
        String output = outputStream.toString();
        assertEquals(true, output.contains("Hmm... Your task list is empty"));
    }

    @Test
    void testShowTaskAdded() {
        Task task = new Todo("Complete assignment");
        ui.showTaskAdded(task, 1);
        String output = outputStream.toString();
        assertEquals(true, output.contains("Got it. I've added this task:"));
        assertEquals(true, output.contains("[T][ ] Complete assignment"));
    }

    @Test
    void testReadCommand() {
        String simulatedInput = "test command\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        UI inputUI = new UI();
        String command = inputUI.readCommand();
        assertEquals("test command", command);
    }
}