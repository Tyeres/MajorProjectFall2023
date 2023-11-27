import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.File;

public class FindPhoneNumberPane extends javafx.scene.layout.HBox implements ContactDirectory {

    /**
     * This is used to add Contacts to a 2-4 Tree and to display the Contact name when matching number prefix
     */


    public FindPhoneNumberPane() {
        this.setStyle("-fx-border-color: purple");
        this.setPadding(new Insets(5, 5, 5, 5));

        // Build the 2-4 Tree
        Tree24<Long> contactTree = new Tree24<>();
        try {
            buildTree(contactTree);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error with the try statement in FindPhoneNumberPane\n" +
                    "The issue lies in adding contacts to the 2-4 tree.");
        }

        TextField searchNumberField = new TextField();
        searchNumberField.setPrefColumnCount(10);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e-> {
            // The tree should never be empty unless the ContactSaves folder is empty, but technically that's possible
            if (!searchNumberField.getText().isEmpty() && !contactTree.isEmpty()) {
                try {
                    // Find the phone number
                        String phoneNumber = searchNumberField.getText();
                        if (contactTree.contains(Long.valueOf(phoneNumber))) {
                            searchNumberField.setText("A contact contains it!");
                        }
                        else searchNumberField.setText("Number not found.");

                } catch (NumberFormatException eea) {
                    searchNumberField.setText("Invalid number input");
                }
            }
        });


        this.getChildren().add(new Text("Confirms if a contact with a\n" +
                "specified phone number exists."));
        this.getChildren().add(searchNumberField);
        this.getChildren().add(searchButton);
    }

    private void buildTree(Tree24<Long> contactTree) throws Exception {
        File[] files = CONTACT_NAMES_FOLDER.listFiles();
        for (int i = 0; i < files.length; i++) {
            int lengthOfString = files[i].getName().length();
            // Get the substring of the name because the method adds ".dat" onto the end. So, we have to remove it first.
            contactTree.add(PaintApplication.getSelectedContact(files[i].getName().substring(0, lengthOfString - 1 - 3)).getPhoneNumber());
        }
    }
}
