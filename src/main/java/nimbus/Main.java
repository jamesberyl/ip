package nimbus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import nimbus.exceptions.NimbusException;

/**
 * The main entry point for the Nimbus application, providing a graphical user interface (GUI) using JavaFX and FXML.
 */
public class Main extends Application {
    private final Nimbus nimbus = new Nimbus();

    /**
     * Constructs a Main instance and initializes the Nimbus chatbot.
     *
     * @throws NimbusException If there is an error initializing the chatbot.
     */
    public Main() throws NimbusException {
    }

    /**
     * Starts the JavaFX application and sets up the main window.
     *
     * @param stage The primary stage for the application.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setNimbus(nimbus);  // inject the Duke instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}