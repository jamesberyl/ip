package nimbus;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


/**
 * Controller for the main GUI.
 * This class handles user interactions, displays dialog boxes, and manages input processing.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Nimbus nimbus;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private Image nimbusImage = new Image(this.getClass().getResourceAsStream("/images/Nimbus.png"));

    /**
     * Initializes the main window.
     * This method binds the vertical scroll property of the scroll pane to the height property
     * of the dialog container, ensuring that new messages appear at the bottom.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        dialogContainer.getChildren().add(
                DialogBox.getNimbusDialog(" Hey there! I'm Nimbus, your assistant. \n"
                        + "How can I make your day brighter?", nimbusImage)
        );
    }

    /**
     * Injects the Nimbus instance into the controller.
     *
     * @param n The Nimbus chatbot instance.
     */
    public void setNimbus(Nimbus n) {
        nimbus = n;
    }

    /**
     * Handles user input by creating dialog boxes.
     * This method retrieves the user's input, generates a response from the Nimbus chatbot,
     * creates dialog boxes for both the user and the chatbot, appends them to the dialog container,
     * and then clears the user input field.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = nimbus.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getNimbusDialog(response, nimbusImage)
        );
        userInput.clear();
    }
}

