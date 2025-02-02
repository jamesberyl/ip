package nimbus.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TodoTest {

    @Test
    void testTodoCreation() {
        Todo todo = new Todo("Read a book");
        assertEquals("[T][ ] Read a book", todo.toString());
    }

    @Test
    void testToFileString() {
        Todo todo = new Todo("Buy groceries");
        assertEquals("T | 0 | Buy groceries", todo.toFileString());
    }
}