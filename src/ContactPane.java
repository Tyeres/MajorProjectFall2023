import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

// This is used to hold all the data for the Contact
public class ContactPane extends VBox {
    public static final String DEFAULT_MESSAGE = "Choose a contact!";

    protected static final int PREFERRED_COLUMN_COUNT = 15;



    /** If you want two nodes side by side going down,
     make an HBox to hold the two nodes, and then add them to this.getChildren
     */

    public ContactPane() {
        this(0.0);
    }

    public ContactPane(double vGap) {
        super(vGap);
        this.setPadding(new Insets(7,60,7,7));
        this.setStyle("-fx-border-color: orange");

        TextField nameField = new TextField(DEFAULT_MESSAGE); // This message will be updated when choosing a contact.
        nameField.setPrefColumnCount(PREFERRED_COLUMN_COUNT);
        nameField.setEditable(false); // Cannot be edited by default.
        Label nameLabel = new Label("Name:               ", nameField);
        nameLabel.setContentDisplay(ContentDisplay.RIGHT);
        this.getChildren().add(nameLabel);

        TextField addressField = new TextField(DEFAULT_MESSAGE);
        addressField.setPrefColumnCount(PREFERRED_COLUMN_COUNT);
        addressField.setEditable(false);
        Label addressLabel = new Label("Address:            ", addressField);
        addressLabel.setContentDisplay(ContentDisplay.RIGHT);
        this.getChildren().add(addressLabel);

        TextField birthdayField = new TextField(DEFAULT_MESSAGE);
        birthdayField.setPrefColumnCount(PREFERRED_COLUMN_COUNT);
        birthdayField.setEditable(false);
        Label birthdayLabel = new Label("Date of Birth:     ", birthdayField);
        birthdayLabel.setContentDisplay(ContentDisplay.RIGHT);
        this.getChildren().add(birthdayLabel);

        TextField emailField = new TextField(DEFAULT_MESSAGE);
        emailField.setPrefColumnCount(PREFERRED_COLUMN_COUNT + 7);
        emailField.setEditable(false);
        Label emailLabel = new Label("Email:                 ", emailField);
        emailLabel.setContentDisplay(ContentDisplay.RIGHT);
        this.getChildren().add(emailLabel);

        TextField phoneNumberField = new TextField(DEFAULT_MESSAGE);
        phoneNumberField.setPrefColumnCount(PREFERRED_COLUMN_COUNT);
        phoneNumberField.setEditable(false);
        Label phoneNumberLabel = new Label("Phone Number: ", phoneNumberField);
        phoneNumberLabel.setContentDisplay(ContentDisplay.RIGHT);
        this.getChildren().add(phoneNumberLabel);

        TextArea notesArea = new TextArea(DEFAULT_MESSAGE);
        notesArea.setPrefColumnCount(PREFERRED_COLUMN_COUNT + 7);
        //ScrollPane notesScroll = new ScrollPane(notesArea);
        notesArea.setEditable(false);
        Label notesLabel = new Label("Notes:                ", notesArea); // If you want to use the ScrollPane, replace NotesArea here with notesScroll
        notesLabel.setContentDisplay(ContentDisplay.RIGHT);
        this.getChildren().add(notesLabel);




    }

    /**
     * This method returns an empty string if the field is empty.
     * For example, if the notes TextArea is empty, DEFAULT_MESSAGE is replaced by an empty string.
     *
     */
    public static String getDefaultUpdate() {
        return "";
    }
}
