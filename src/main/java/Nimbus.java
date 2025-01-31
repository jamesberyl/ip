public class Nimbus {
    private UI ui;
    private Storage storage;
    private TaskList taskList;
    private Parser parser;

    public Nimbus(String filepath) {
        this.ui = new UI();
        this.storage = new Storage(filepath);
        this.taskList = new TaskList(storage, ui);
        this.parser = new Parser(taskList, ui, storage);
    }

    public void run() {
        ui.showWelcomeMessage();
        boolean isRunning = true;

        while (isRunning) {
            try {
                String input = ui.readCommand().trim();
                if (input.equalsIgnoreCase("bye")) {
                    ui.showExitMessage();
                    break;
                }
                parser.processCommand(input);
            } catch (NimbusException e) {
                ui.showErrorMessage(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Nimbus("./data/nimbus.txt").run();
    }
}