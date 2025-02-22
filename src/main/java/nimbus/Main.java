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
        assert stage != null : "Primary stage should not be null";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            assert fxmlLoader.getLocation() != null : "FXML file not found at the specified location";

            AnchorPane ap = fxmlLoader.load();
            assert ap != null : "Failed to load AnchorPane from FXML";

            Scene scene = new Scene(ap);
            stage.setTitle("Nimbus - Your friendly TaskManager");
            stage.setScene(scene);

            assert fxmlLoader.getController() != null : "FXML controller is not initialized";
            fxmlLoader.<MainWindow>getController().setNimbus(nimbus);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}