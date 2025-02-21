package nimbus.parser;

import nimbus.exceptions.NimbusException;
import nimbus.storage.Storage;
import nimbus.tasklist.TaskList;
import nimbus.ui.UI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private Parser parser;
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
        parser = new Parser(taskList, ui, storage);
    }

    @Test
    void testParseCommand_validCommand() throws NimbusException {
        assertEquals(Parser.Command.LIST, Parser.Command.parseCommand("list"));
        assertEquals(Parser.Command.TODO, Parser.Command.parseCommand("todo Read book"));
    }

    @Test
    void testParseCommand_invalidCommand() {
        NimbusException exception = assertThrows(NimbusException.class, () -> {
            Parser.Command.parseCommand("invalidCommand");
        });
        assertEquals("Oops! I don't recognize that command.", exception.getMessage());
    }

    @Test
    void testProcessCommand_emptyInput() {
        NimbusException exception = assertThrows(NimbusException.class, () -> {
            parser.processCommand("");
        });
        assertEquals("Oops! It seems like you entered nothing.", exception.getMessage());
    }
}