import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class PaintApplication extends Application {

    private static final File CONTACT_NAMES_FOLDER = new File("./src/ContactSaves");

    private final ContactPane contactPane = new ContactPane(40); // Set gap 40
    private ObservableList<Node> listOfNodes = contactPane.getChildren();

    // You need to access the Label before accessing the node, unfortunately. So, it creates ugly statements.
    // These statements retrieve the nodes from the contactPane class.
    private TextField nameField = ((TextField) ((Label) contactPane.getChildren().get(0)).getGraphic());

    private TextField addressField = ((TextField) ((Label) contactPane.getChildren().get(1)).getGraphic());

    private TextField birthdayField = ((TextField) ((Label) contactPane.getChildren().get(2)).getGraphic());

    private TextField emailField = ((TextField) ((Label) contactPane.getChildren().get(3)).getGraphic());

    private TextField phoneNumberField = ((TextField) ((Label) contactPane.getChildren().get(4)).getGraphic());

    private TextArea notesArea = ((TextArea) ((Label) contactPane.getChildren().get(5)).getGraphic());

    public void start(Stage primaryStage) {

        HBox panePane = new HBox(5);
        panePane.setPadding(new Insets(10, 3, 0, 3));
        // This contains the list of names
        ObservableList<String> listOfNamesBox = getFileNames();
        // Give the list of names to the ComboBox
        ComboBox<String> contactComboBox = new ComboBox<>(listOfNamesBox);

        // Set action to combo box
        contactComboBox.setOnAction(e->{
            //contactComboBox.getItems().add("Ham burger");

            String contactName = contactComboBox.getValue();
            // Find the Contact Object based on the chosen name
            Contact chosenContact = getSelectedContact(contactName);
            // Set the fields
            nameField.setText(chosenContact.getName());
            addressField.setText(chosenContact.getAddress());
            birthdayField.setText(chosenContact.getBirthDayFormat());
            emailField.setText(chosenContact.getEmail());
            phoneNumberField.setText(String.valueOf(chosenContact.getPhoneNumber()));
            notesArea.setText(chosenContact.getNotes());



        });

        // Add nodes to pane
        panePane.getChildren().add(contactComboBox);
        panePane.getChildren().add(contactPane);









        Scene scene = new Scene(panePane, 800, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Phone Book");
//        primaryStage.setFullScreenExitHint("Press Esc to exit full screen!\n(You also smell weird)");
//        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    // Using the selected String name from the Combo Box, find the file and return the serialized Contact.
    private Contact getSelectedContact(String name) {

    }



    // This method is used to build the list of names for the ComboBox
    private static ObservableList<String> getFileNames() {
        try {
            File[] files = CONTACT_NAMES_FOLDER.listFiles();
            String[] namesOfFiles = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                int lengthOfString = files[i].getName().length();
                // Get the substring of the name because I don't want the ".dat" part of the name.
                namesOfFiles[i] = files[i].getName().substring(0, lengthOfString - 1 - 3);
            }
            return FXCollections.observableArrayList(namesOfFiles);
        }
        catch (NullPointerException e)
        {
            System.err.println("listOfNames array object points to nothing.");
            System.exit(-111);
        }
        // This return statement is unreachable because if there is an error here, the program closes.
        return null;
    }


}
