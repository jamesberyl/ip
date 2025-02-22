package nimbus.tasklist;

import nimbus.exceptions.NimbusException;
import nimbus.storage.Storage;
import nimbus.tasklist.TaskList;
import nimbus.ui.UI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskListTest {

    private TaskList taskList;
    private UI ui;
    private Storage storage;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws NimbusException {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        storage = new Storage("test_tasks.txt");
        ui = new UI();
        taskList = new TaskList(storage, ui);
    }

    @Test
    void testAddTodoTask_validInput() throws NimbusException {
        taskList.addTodoTask("todo Read book");
        assertEquals(1, taskList.getTasks().size());
        assertEquals("[T][ ] Read book", taskList.getTasks().get(0).toString());
    }

    @Test
    void testAddTodoTask_emptyInput() {
        NimbusException exception = assertThrows(NimbusException.class, () -> {
            taskList.addTodoTask("todo ");
        });
        assertEquals("Oops! The description of a todo cannot be empty.", exception.getMessage());
    }

    @Test
    void testDeleteTask_validInput() throws NimbusException {
        taskList.addTodoTask("todo Read book");
        taskList.deleteTask("delete 1");
        assertEquals(0, taskList.getTasks().size());
    }

    @Test
    void testDeleteTask_invalidIndex() {
        NimbusException exception = assertThrows(NimbusException.class, () -> {
            taskList.deleteTask("delete 5");
        });
        assertEquals("Oops! That task number doesn't exist. Please check your list.", exception.getMessage());
    }

    @Test
    void testMarkTask_validInput() throws NimbusException {
        taskList.addTodoTask("todo Read book");
        taskList.markTask("mark 1", true);
        assertEquals("[T][X] Read book", taskList.getTasks().get(0).toString());
    }

    @Test
    void testUnmarkTask_validInput() throws NimbusException {
        taskList.addTodoTask("todo Read book");
        taskList.markTask("mark 1", true);
        taskList.markTask("unmark 1", false);
        assertEquals("[T][ ] Read book", taskList.getTasks().get(0).toString());
    }
}
