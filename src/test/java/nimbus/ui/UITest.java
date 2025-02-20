package nimbus.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import nimbus.exceptions.NimbusException;
import nimbus.tasks.Deadline;
import nimbus.tasks.Event;
import nimbus.tasks.Task;
import nimbus.tasks.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UITest {

    private UI ui;
    private ArrayList<Task> taskList;

    @BeforeEach
    void setUp() {
        ui = new UI();
        taskList = new ArrayList<>();
    }

    @Test
    void testShowExitMessage() {
        String expected = "Stay awesome, and I’ll see you soon! 👋";
        assertEquals(expected, ui.showExitMessage());
    }

    @Test
    void testShowErrorMessage() {
        String expected = "⚠ ERROR: File not found!";
        assertEquals(expected, ui.showErrorMessage("File not found!"));
    }

    @Test
    void testShowEmptyTaskList() {
        String expected = "Hmm... Your task list is empty. Ready to add something?";
        assertEquals(expected, ui.showTaskList(taskList));
    }

    @Test
    void testShowTaskListWithTasks() throws NimbusException {
        Task task1 = new Todo("Read book");
        Task task2 = new Deadline("Submit report", "Feb 28 2025");

        taskList.add(task1);
        taskList.add(task2);

        String expected = "Here are the tasks in your list:\n" +
                "1. [T][ ] Read book\n" +
                "2. [D][ ] Submit report (by: Feb 28 2025)";
        assertEquals(expected, ui.showTaskList(taskList));
    }

    @Test
    void testShowTaskAdded() {
        Task task = new Todo("Read book");

        String expected = "Got it. I've added this task:\n" +
                "  [T][ ] Read book\n" +
                "Now you have 1 tasks in the list.";
        assertEquals(expected, ui.showTaskAdded(task, 1));
    }

    @Test
    void testShowTaskMarked() {
        Task task = new Todo("Read book");

        String expectedDone = "Nice! I've marked this task as done:\n  [T][X] Read book";
        task.markAsDone();
        assertEquals(expectedDone, ui.showTaskMarked(task, true));

        task.unmark();
        String expectedNotDone = "OK, I've marked this task as not done yet:\n  [T][ ] Read book";
        assertEquals(expectedNotDone, ui.showTaskMarked(task, false));
    }

    @Test
    void testShowTaskDeleted() {
        Task task = new Todo("Read book");

        String expected = "Noted. I've removed this task:\n" +
                "  [T][ ] Read book\n" +
                "Now you have 0 tasks in the list.";
        assertEquals(expected, ui.showTaskDeleted(task, 0));
    }

    @Test
    void testShowAllTasksCleared() {
        String expected = "✅ All tasks have been cleared.";
        assertEquals(expected, ui.showAllTasksCleared());
    }

    @Test
    void testShowTasksOnDate_NoTasks() {
        LocalDate searchDate = LocalDate.of(2025, 2, 20);
        String expected = "Tasks on Feb 20 2025:\n  No tasks found on this date.";
        assertEquals(expected, UI.showTasksOnDate(searchDate, taskList));
    }

    @Test
    void testShowTasksOnDate_WithTasks() throws NimbusException {
        LocalDate searchDate = LocalDate.of(2025, 2, 20);
        Deadline deadline = new Deadline("Submit report", "20/02/2025");
        Event event = new Event("Project Meeting", "20/02/2025 1000", "20/02/2025 1200");

        taskList.add(deadline);
        taskList.add(event);

        String expected = "Tasks on Feb 20 2025:\n" +
                "  [D][ ] Submit report (by: Feb 20 2025)\n" +
                "  [E][ ] Project Meeting (from: Feb 20 2025 10:00 to: Feb 20 2025 12:00)";
        assertEquals(expected, UI.showTasksOnDate(searchDate, taskList));
    }

    @Test
    void testShowMatchingTasks_NoMatches() {
        String expected = "Here are the matching tasks for \"report\":\n  No matching tasks found.";
        assertEquals(expected, ui.showMatchingTasks(taskList, "report"));
    }

    @Test
    void testShowMatchingTasks_WithMatches() throws NimbusException {
        Task task = new Deadline("Submit report", "Feb 28 2025");
        taskList.add(task);

        String expected = "Here are the matching tasks for \"report\":\n" +
                "1. [D][ ] Submit report (by: Feb 28 2025)";
        assertEquals(expected, ui.showMatchingTasks(taskList, "report"));
    }

    @Test
    void testShowSortedTasks() throws NimbusException {
        Task task1 = new Todo("Read book");
        Task task2 = new Deadline("Submit report", "Feb 28 2025");

        ArrayList<Task> sortedTasks = new ArrayList<>();
        sortedTasks.add(task1);
        sortedTasks.add(task2);

        String expected = "Tasks sorted successfully:\n" +
                "1. [T][ ] Read book\n" +
                "2. [D][ ] Submit report (by: Feb 28 2025)";
        assertEquals(expected, ui.showSortedTasks(sortedTasks));
    }
}