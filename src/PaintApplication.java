import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Stack;

public class PaintApplication extends Application {

    private static final File CONTACT_NAMES_FOLDER = new File("./src/ContactSaves");

    private final ContactPane contactPane = new ContactPane(40); // Set gap 40
    private final ObservableList<Node> listOfNodeLabels = contactPane.getChildren();

    // These statements retrieve the nodes from the contactPane class' Labels.
    private final TextField nameField = ((TextField) ((Label) listOfNodeLabels.get(0)).getGraphic());

    private final TextField addressField = ((TextField) ((Label) listOfNodeLabels.get(1)).getGraphic());

    private final TextField birthdayField = ((TextField) ((Label) listOfNodeLabels.get(2)).getGraphic());

    private final TextField emailField = ((TextField) ((Label) listOfNodeLabels.get(3)).getGraphic());

    private final TextField phoneNumberField = ((TextField) ((Label) listOfNodeLabels.get(4)).getGraphic());

    private final TextArea notesArea = ((TextArea) ((Label) listOfNodeLabels.get(5)).getGraphic());

    public void start(Stage primaryStage) {

        HBox mainPane = new HBox(5);
        mainPane.setPadding(new Insets(10, 3, 0, 3));
        // This contains the list of names
        ObservableList<String> listOfNamesBox = getFileNames();
        // Give the list of names to the ComboBox
        ComboBox<String> contactComboBox = new ComboBox<>(listOfNamesBox);

        // Set action to combo box
        contactComboBox.setOnAction(e-> displayContact(contactComboBox));

        // Create Edit Button.
        Button edit = new Button("Edit Contact");
        // Set text color to black by default
        edit.setTextFill(Paint.valueOf("black"));
        edit.setOnAction(e-> {
            // Fields can be edited
            if (edit.getTextFill().equals(Paint.valueOf("black"))) {
                edit.setTextFill(Paint.valueOf("red"));
                nameField.setEditable(true);
                addressField.setEditable(true);
                birthdayField.setEditable(true);
                emailField.setEditable(true);
                phoneNumberField.setEditable(true);
                notesArea.setEditable(true);
                edit.setText("Edit Contact\n(Enabled)");
            }
            else {
                // Fields cannot be edited
                edit.setTextFill(Paint.valueOf("Black"));
                nameField.setEditable(false);
                addressField.setEditable(false);
                birthdayField.setEditable(false);
                emailField.setEditable(false);
                phoneNumberField.setEditable(false);
                notesArea.setEditable(false);
                edit.setText("Edit Contact");
            }
        });

        // Create the save button
        Button saveContact = new Button("Save Contact/Edits");
        saveContact.setOnAction(e->{
            if (!nameField.getText().equals("Choose a contact!")) {

                Contact contact = new Contact();
                contact.setName(nameField.getText());
                contact.setAddress(addressField.getText());

                // Save birthday
                String birthdayString = birthdayField.getText();
                int[] birthDayInts = getBirthdayValues(birthdayString);
                contact.setBirthDay(new GregorianCalendar(birthDayInts[0], birthDayInts[1], birthDayInts[2]));

                contact.setEmail(emailField.getText());
                contact.setPhoneNumber(Long.parseLong(phoneNumberField.getText()));
                contact.setNotes(notesArea.getText());

                contact.save();
                System.out.println("Saved Successfully");
                if (!contactComboBox.getItems().contains(nameField.getText()))
                    contactComboBox.getItems().add(nameField.getText()); // Add name to the combo box.
            }
        });


        Button deleteContact = new Button("Delete Contact");
        deleteContact.setOnAction(e->{
            File file = new File("./src/ContactSaves/" + nameField.getText() + ".dat");
            System.out.println("File deletion: " + file.delete()); // Print true or false.
            contactComboBox.getItems().remove(nameField.getText());
        });


        // ---------------------------------------------------------
        /**
         * This here creates the stack on the right side. It adds email addresses to a text area and to a
         * stack. It is used to export the email addresses into a text file in the Downloads folder.
         */
        BorderPane stackPane = new BorderPane();
        stackPane.setStyle("-fx-border-color: red");
        HBox buttonStackPane = new HBox(5);
        buttonStackPane.setPadding(new Insets(5, 5, 5, 5));
        Stack<String> stack = new Stack<>();
        TextArea stackDisplay = new TextArea();
        stackDisplay.setPrefColumnCount(8);
        stackDisplay.setEditable(false);
        Button addToStackButton = new Button("Add Email");
        addToStackButton.setOnAction(e-> {
            // Add the Contact's email address to the stack only if a contact has been selected/created.
            if (!emailField.getText().equals(ContactPane.DEFAULT_MESSAGE)) {
                stack.push(this.emailField.getText());
                stackDisplay.setText(stackDisplay.getText() + "\n" + stack.peek());
            }
        });
        Button removeFromStackButton = new Button("Remove Last");
        removeFromStackButton.setOnAction(e-> {
            // The button is not to do anything if a contact hasn't been created/selected and the stack is empty.
            if (!emailField.getText().equals(ContactPane.DEFAULT_MESSAGE) && !stack.isEmpty()) {
                stack.pop(); // Remove head from the actual stack
                // Now display the change
                int indexOfLastWhiteSpace = stackDisplay.getText().lastIndexOf("\n");
                String updatedText = stackDisplay.getText().substring(0, indexOfLastWhiteSpace);
                stackDisplay.setText(updatedText);
            }
        });
        Button exportButton = new Button("Export");
        exportButton.setOnAction(e-> {
            // The button is not to do anything if a contact hasn't been created/selected
            if (!stackDisplay.getText().isEmpty()) {
                // We are saving the file to the user's downloads folder
                String home = System.getProperty("user.home");
                File file = new File(home+"/Downloads/" + "Email Addresses" + ".txt");
                try (PrintWriter exportFile = new PrintWriter(file)){
                    for (String stackString: stack) {
                        exportFile.println(stackString);
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println("Export File not Found (Shouldn't be an issue)");
                }
            }
        });

        // Add the button pane to the stack pane. This is done because we want the
        // buttons in a HBox (holds the buttons) all under the text area.
        buttonStackPane.getChildren().add(addToStackButton);
        buttonStackPane.getChildren().add(removeFromStackButton);
        buttonStackPane.getChildren().add(exportButton);
        stackPane.setCenter(stackDisplay);
        stackPane.setBottom(buttonStackPane);
        // --------------------------------------------------
        /**
         * This is used to add Contacts to an linkedList and to display the oldest birthday
         */

        BorderPane largestBirthdayPane = new BorderPane();
        largestBirthdayPane.setStyle("-fx-border-color: blue");
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
                    Contact selectedContact = getSelectedContact(this.nameField.getText());
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
                Scene largestDOBScene = new Scene(largestDOBContactPane, 200, 200);
                Stage largestDOBStage = new Stage();
                largestDOBStage.setScene(largestDOBScene);
                largestDOBStage.show();
            }
        });
        buttonLargestBirthdayPane.getChildren().add(addContactButton);
        buttonLargestBirthdayPane.getChildren().add(removeContactButton);
        buttonLargestBirthdayPane.getChildren().add(displayLargestBirthday);
        largestBirthdayPane.setCenter(birthdayDisplay);
        largestBirthdayPane.setBottom(buttonLargestBirthdayPane);
        //--------------------------------









        // Add nodes to pane
        mainPane.getChildren().add(contactComboBox);
        mainPane.getChildren().add(contactPane);
        FlowPane rightPane = new FlowPane();
        rightPane.setHgap(5);
        rightPane.setVgap(5);
        rightPane.getChildren().add(edit);
        rightPane.getChildren().add(saveContact);
        rightPane.getChildren().add(deleteContact);
        rightPane.getChildren().add(stackPane);
        rightPane.getChildren().add(largestBirthdayPane);
        mainPane.getChildren().add(rightPane);



        Scene scene = new Scene(mainPane, 950, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Phone Book");
//        primaryStage.setFullScreenExitHint("Press Esc to exit full screen!\n(You also smell weird)");
//        primaryStage.setFullScreen(true);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    // Find the file and return the serialized Contact.
    private Contact getSelectedContact(String name) throws Exception {
        // Input the file name
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new BufferedInputStream( new FileInputStream("./src/ContactSaves/" + name + ".dat"))))
        {
            return (Contact) inputStream.readObject();
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.out.println(e.getMessage());
            throw new FileNotFoundException();
        }
        catch (Exception ee) {
            System.out.println("Strange error in getSelectedContact method.");
            System.out.println(ee.getMessage());
            throw new Exception();
        }
    }



    /** This method is used to build the list of names for the ComboBox.
     It returns the names files in the ContactNames package but without the .dat at the end.
     */
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

    /**
     * This takes a string in the format of "year monthIndex day" and converts it into an int array.
     * This is done so that it can be turned into GregorianCalendar object by the save button.
     */
    public int[] getBirthdayValues(String dateStr) {
        int year = Integer.parseInt(dateStr.substring(0, 4));
        String monthAndDay = dateStr.substring(5);
        int indexOfSpace = monthAndDay.indexOf(' '); // For example: 2003 5 18 ...Gets the space before 18.
        int month = Integer.parseInt(monthAndDay.substring(0, indexOfSpace));
        int day = Integer.parseInt(monthAndDay.substring(indexOfSpace + 1));
        int[] array = new int[3];
        array[0] = year;
        array[1] = month - 1; // Subtract one so that it's an index
        array[2] = day;
        return array;
    }

    private <E extends Comparable<E>> void comparePrint(E e, E r)  {
        if (e.compareTo(r) > 0)
            System.out.println("Larger!");
        else if (e.compareTo(r) == 0)
            System.out.println("Equal!");
        else System.out.println("Smaller!");
    }

    // This method is used to display a contact
    protected void displayContact(ComboBox<String> contactComboBox) {
        String contactName = contactComboBox.getValue();
        // Find the Contact Object based on the chosen name
        Contact chosenContact;

        // This try-catch code is important. When a contact is deleted, the program runs into errors
        // because it was accessing the deleted Contact. So, when an Exception during deletion,
        // the code switches away from that contact to the first one in the ComboBox, but it
        // doesn't have to be the first one; it can be any of them. Point is: it switches away
        // from the deleted contact.
        try {
            chosenContact = getSelectedContact(contactName);
        } catch (Exception ex) {
            String firstName = contactComboBox.getItems().get(0);
            try {
                chosenContact = getSelectedContact(firstName);
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        }
        // Set the fields
        nameField.setText(chosenContact.getName());
        addressField.setText(chosenContact.getAddress());
        birthdayField.setText(chosenContact.getBirthdayFileFormat());
        emailField.setText(chosenContact.getEmail());
        phoneNumberField.setText(String.valueOf(chosenContact.getPhoneNumber()));
        notesArea.setText(chosenContact.getNotes());
    }
}
