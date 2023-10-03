import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

// This is used to hold all the data for the Contact
public class ContactPane extends VBox {
    protected static final String DEFAULT_MESSAGE = "Choose a contact!";


    /** If you want two nodes side by side going down,
     make an HBox to hold the two nodes, and then add them to this.getChildren
     */
    public ContactPane() {

        this.setStyle("-fx-border-color: blue");

        TextField nameField = new TextField(DEFAULT_MESSAGE); // This message will be updated when choosing a contact.
        nameField.setEditable(false); // Cannot be edited by default.
        Label nameLabel = new Label("Name: ", nameField);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);
        this.getChildren().add(nameLabel);






    }
}
