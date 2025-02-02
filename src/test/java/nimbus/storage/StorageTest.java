package nimbus.storage;

import nimbus.tasks.Task;
import nimbus.tasks.Todo;
import nimbus.tasks.Deadline;
import nimbus.tasks.Event;
import nimbus.exceptions.NimbusException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StorageTest {

    private Storage storage;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        tempFile = File.createTempFile("nimbus_test", ".txt");
        storage = new Storage(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testSaveAndLoadTodoTask() {
        ArrayList<Task> tasksToSave = new ArrayList<>();
        tasksToSave.add(new Todo("Test Todo"));

        storage.saveTasks(tasksToSave);
        ArrayList<Task> loadedTasks = storage.loadTasks();

        assertEquals(1, loadedTasks.size());
        assertEquals("[T][ ] Test Todo", loadedTasks.get(0).toString());
    }

    @Test
    void testSaveAndLoadDeadlineTask() throws NimbusException {
        ArrayList<Task> tasksToSave = new ArrayList<>();
        tasksToSave.add(new Deadline("Submit report", "2023-12-01 1800"));

        storage.saveTasks(tasksToSave);
        ArrayList<Task> loadedTasks = storage.loadTasks();

        assertEquals(1, loadedTasks.size());
        assertEquals("[D][ ] Submit report (by: Dec 01 2023, 6:00 pm)", loadedTasks.get(0).toString());
    }

    @Test
    void testSaveAndLoadEventTask() throws NimbusException {
        ArrayList<Task> tasksToSave = new ArrayList<>();
        tasksToSave.add(new Event("Team meeting", "2023-11-01 1000", "2023-11-01 1200"));

        storage.saveTasks(tasksToSave);
        ArrayList<Task> loadedTasks = storage.loadTasks();

        assertEquals(1, loadedTasks.size());
        assertEquals("[E][ ] Team meeting (from: Nov 01 2023, 10:00 am to: Nov 01 2023, 12:00 pm)", loadedTasks.get(0).toString());
    }

    @Test
    void testLoadFromNonExistentFile() {
        File nonExistentFile = new File("non_existent_file.txt");
        Storage newStorage = new Storage(nonExistentFile.getAbsolutePath());

        ArrayList<Task> loadedTasks = newStorage.loadTasks();
        assertTrue(loadedTasks.isEmpty());
    }

}