import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.LinkedList;

public class LargestBirthdayPane extends BorderPane {
    /**
     * This is used to add Contacts to an linkedList and to display the oldest birthday
     */
    public LargestBirthdayPane(TextField nameField) {
        this.setStyle("-fx-border-color: blue");
        HBox buttonLargestBirthdayPane = new HBox(5);
        buttonLargestBirthdayPane.setPadding(new Insets(5, 5, 5, 5));
        LinkedList<Contact> contactLinkedList = new LinkedList<>();
        TextArea birthdayDisplay = new TextArea();
        birthdayDisplay.setPrefColumnCount(8);
        birthdayDisplay.setEditable(false);

        Button addContactButton = new Button("Add Contact");
        addContactButton.setOnAction(e-> {
            if (!nameField.getText().equals(ContactPane.DEFAULT_MESSAGE)) {
                try {
                    // Add the selected Contact
                    Contact selectedContact = PaintApplication.getSelectedContact(nameField.getText());
                    contactLinkedList.addLast(selectedContact);
                    birthdayDisplay.setText(birthdayDisplay.getText() + "\n" + selectedContact.getName());
                } catch (Exception eea) {
                    System.out.println(eea.getMessage());
                }
            }
        });

        Button removeContactButton = new Button("Remove Last");
        removeContactButton.setOnAction(e-> {
            if (!nameField.getText().equals(ContactPane.DEFAULT_MESSAGE) && !contactLinkedList.isEmpty()) {
                contactLinkedList.removeLast();

                int indexOfLastWhiteSpace = birthdayDisplay.getText().lastIndexOf("\n");
                String updatedText = birthdayDisplay.getText().substring(0, indexOfLastWhiteSpace);
                birthdayDisplay.setText(updatedText);
            }

        });

        Button displayLargestBirthday = new Button("Display Largest DOB");
        displayLargestBirthday.setOnAction(e-> {
            if (!contactLinkedList.isEmpty()) {
                // Initially assume it's the first element
                Contact largestContact = contactLinkedList.getFirst();
                // No need to start at index 0 if assuming index 0 is the largest.
                // Find the largest Contact. It's found by comparing birthdays.
                for (int i = 1; i < contactLinkedList.size(); i++) {
                    // Use > instead of < because, for example, 1995 is a greater age than 2005, but it's a small number
                    if (largestContact.compareTo(contactLinkedList.get(i)) > 0)
                        largestContact = contactLinkedList.get(i);
                }
                Text largestDOBText = new Text(largestContact.getName() + ": " + largestContact.getBirthdayFormat());
                BorderPane largestDOBContactPane = new BorderPane();
                largestDOBContactPane.setCenter(largestDOBText);
                Scene largestDOBScene = new Scene(largestDOBContactPane, 300, 300);
                Stage largestDOBStage = new Stage();
                largestDOBStage.setScene(largestDOBScene);
                largestDOBStage.setTitle("Largest Age");
                largestDOBStage.getIcons().add(new Image("Thumbnails/Phone Book Thumbnail 2.jpg"));
                largestDOBStage.show();
            }
        });
        buttonLargestBirthdayPane.getChildren().add(addContactButton);
        buttonLargestBirthdayPane.getChildren().add(removeContactButton);
        buttonLargestBirthdayPane.getChildren().add(displayLargestBirthday);
        this.setCenter(birthdayDisplay);
        this.setBottom(buttonLargestBirthdayPane);
    }
}
