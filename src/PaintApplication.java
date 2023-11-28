import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicReference;

public class PaintApplication extends Application implements ContactDirectory {

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
        // Give the list of names to the ListView
        ListView<String> contactListView = new ListView<>(listOfNamesBox);
        contactListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Set action to listView
        contactListView.getSelectionModel().selectedItemProperty().addListener(e->displayContact(contactListView));


        // Create the HashTable Map
        HashTableMap hashTableMap = new HashTableMap();
        // Search the table
        TextField searchMapField = new TextField();
        Label searchLabel = new Label("Search: ", searchMapField);
        searchLabel.setContentDisplay(ContentDisplay.RIGHT);

        // Search when entered
        searchMapField.setOnAction(e -> {
            String searchedName = searchMapField.getText();
            if (!hashTableMap.search(searchedName))
                searchMapField.setText("No results found.");
            else {
                // Since it was found in the list, the contact must exist. Find it, and display it.
                try {
                    Contact chosenContact = getSelectedContact(searchedName);
                    // Set the fields
                    nameField.setText(chosenContact.getName());
                    addressField.setText(chosenContact.getAddress());
                    birthdayField.setText(chosenContact.getBirthdayFileFormat());
                    emailField.setText(chosenContact.getEmail());
                    phoneNumberField.setText(String.valueOf(chosenContact.getPhoneNumber()));
                    notesArea.setText(chosenContact.getNotes());
                } catch (Exception e1) {
                    searchMapField.setText("No results found.");
//                    e1.printStackTrace();
                }
            }
        });

        // Create the pane for the comboBox and the search field
        VBox searchOptionsPane = new VBox(5);
        searchOptionsPane.getChildren().add(searchLabel);
        searchOptionsPane.getChildren().add(contactListView);

        Button newContactBT = new Button("New Contact");
        newContactBT.setOnAction(e-> {
            contactListView.getSelectionModel().clearSelection();
            clearFields();
        });

        // Create Edit Button.
        Button edit = new Button("Edit Contact");
        // Set text color to black by default
        edit.setTextFill(Paint.valueOf("black"));
        edit.setOnAction(e -> {
            // Fields can be edited
            if (edit.getTextFill().equals(Paint.valueOf("black"))) {
                edit.setTextFill(Paint.valueOf("red"));
                nameField.setEditable(true);
                addressField.setEditable(true);
                birthdayField.setEditable(true);
                emailField.setEditable(true);
                phoneNumberField.setEditable(true);
                notesArea.setEditable(true);
//                edit.setText("Edit Contact\n(Enabled)");
            } else {
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
        saveContact.setOnAction(e -> {
            // Do not do anything unless a contact is selected or one is being created
            if (!nameField.getText().equals("Choose a contact!") && isFieldsFull()) {

                try {
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
                    if (!contactListView.getItems().contains(nameField.getText())) {
                        contactListView.getItems().add(nameField.getText()); // Add name to the combo box.
                        hashTableMap.add(nameField.getText()); // Add name to the search map.
                    }
                } catch (Exception e1) {

                    // If there is incorrect formatting in the Contact fields, an error window will display.
                    getErrorFormattingStage().show();
                    e1.printStackTrace();
                }
            }
        });

        // Create the delete button
        Button deleteContact = new Button("Delete Contact");
        deleteContact.setOnAction(e -> {
            // Do not do anything unless a contact is selected
            if (!nameField.getText().equals("Choose a contact!")) {
                File file = new File("./src/ContactSaves/" + nameField.getText() + ".dat");
                System.out.println("File deletion: " + file.delete()); // Print true or false.
                contactListView.getItems().remove(nameField.getText()); // Remove name from combo box
                hashTableMap.remove(nameField.getText()); // Remove name from the search map
                contactListView.getSelectionModel().clearSelection();
            }
        });
        // Create pane for the buttons
        HBox mainButtonPane = new HBox(5);
        mainButtonPane.setStyle("-fx-border-color: orange");
        mainButtonPane.setPadding(new Insets(5, 5, 5, 5));
        mainButtonPane.getChildren().add(newContactBT);
        mainButtonPane.getChildren().add(edit);
        mainButtonPane.getChildren().add(saveContact);
        mainButtonPane.getChildren().add(deleteContact);
        Button togglePanesBT = new Button("More Options");
        mainButtonPane.getChildren().add(togglePanesBT);


        FlowPane rightPane = new FlowPane();
        rightPane.setHgap(5);
        rightPane.setVgap(5);
        // The first item in the rightPane should be the button Pane. The other items are added with the toggle button.
        rightPane.getChildren().add(mainButtonPane);


        /* This creates the toggle button.
         Initialize toggleBT as false since it hasn't been toggled yet. */
        AtomicReference<Boolean> toggleBT = new AtomicReference<>(false);
        EmailStackPane emailStackPane = new EmailStackPane(this.emailField);
        LargestBirthdayPane largestBirthdayPane = new LargestBirthdayPane(this.nameField);
        FindPhoneNumberPane findPhoneNumberPane = new FindPhoneNumberPane();
        TransferContactPane transferContactPane = new TransferContactPane(contactListView);

        // This makes it so that when the More Options button is pressed, it either displays or removes the extra panes.
        togglePanesBT.setOnAction(e->
            toggleBT.set(toggleOptionPanes(toggleBT.get(), rightPane, emailStackPane, largestBirthdayPane, findPhoneNumberPane, transferContactPane))
        );


        // Create the left side pane & the contactPane by adding it to the mainPane
        mainPane.getChildren().add(searchOptionsPane);
        mainPane.getChildren().add(contactPane);
        mainPane.getChildren().add(rightPane);


        Scene scene = new Scene(mainPane, 1200, 750);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Phone Book");
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image("Thumbnails/Phone Book Thumbnail 2.jpg"));
        primaryStage.show();

        // Control the height of the ListView. Make sure it's the length of the window at all times.
        contactListView.setPrefHeight(primaryStage.getHeight() - 100);
        primaryStage.heightProperty().addListener(
                e->contactListView.setPrefHeight(primaryStage.getHeight() - 100));

        /* The receiveContact button, when pressed, releases a thread that will keep the program running
        when everything is closed. So, this makes sure that the program closes when the primaryStage closes.
        */
        primaryStage.setOnCloseRequest(e->System.exit(0));
    }

    private static Stage getErrorFormattingStage() {
        Text error = new Text("Error. Incorrect Contact Formatting");
        BorderPane errorPane = new BorderPane();
        errorPane.setCenter(error);
        Scene errorScene = new Scene(errorPane, 300, 300);
        Stage errorStage = new Stage();
        errorStage.setScene(errorScene);
        errorStage.setTitle("Error");
        errorStage.getIcons().add(new Image("Thumbnails/Phone Book Thumbnail 2.jpg"));
        return errorStage;
    }

    /**
     * Find the file and return the serialized Contact.
     */
    public static Contact getSelectedContact(String name) throws Exception {
        // Input the file name
        try (ObjectInputStream inputStream = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream("./src/ContactSaves/" + name + ".dat")))) {
            // There is only one Contact object per file, which is the searched object. Return it.
            return (Contact) inputStream.readObject();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (Exception ee) {
            System.out.println("Strange error in getSelectedContact method.");
            System.out.println(ee.getMessage());
            throw new Exception();
        }
    }


    /**
     * This method is used to build the list of names for the ListView.
     * It returns the names files in the ContactNames package but without the .dat at the end.
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
        } catch (NullPointerException e) {
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
        int indexOfFirstSlash = dateStr.indexOf('/');
        int indexOfLastSlash = dateStr.lastIndexOf('/'); // For example: 1999/05/12 ...Gets the / before 12.
        int month = Integer.parseInt(dateStr.substring(indexOfFirstSlash + 1, indexOfLastSlash));
        int day = Integer.parseInt(dateStr.substring(indexOfLastSlash + 1));
        int[] array = new int[3];
        array[0] = year;
        array[1] = month - 1; // Subtract one so that it's an index
        array[2] = day;
        return array;
    }

    public static <E extends Comparable<E>> void comparePrint(E e, E r) {
        if (e.compareTo(r) > 0)
            System.out.println("Larger!");
        else if (e.compareTo(r) == 0)
            System.out.println("Equal!");
        else System.out.println("Smaller!");
    }

    /** This method is used to retrieve and to display a contact in the contact text fields */
    public void displayContact(ListView<String> contactListView) {
        String contactName = contactListView.getSelectionModel().getSelectedItem();
        // Find the Contact Object based on the chosen name
        Contact chosenContact;

        /* This try-catch code is important. When a contact is deleted, the program runs into errors
         because it was accessing the deleted Contact. So, when an Exception during deletion,
         the code switches away from that contact to the first one in the ListView, but it
         doesn't have to be the first one; it can be any of them. Point is: it switches away
         from the deleted contact. */
        try {
            chosenContact = getSelectedContact(contactName);
        } catch (Exception ex) {
            String firstName = contactListView.getItems().get(0);
            try {
                chosenContact = getSelectedContact(firstName);
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        }
        displayContact(chosenContact);
    }

    /** This method displays the contact */
    public void displayContact(Contact chosenContact) {

        this.nameField.setText(chosenContact.getName());
        this.addressField.setText(chosenContact.getAddress());
        this.birthdayField.setText(chosenContact.getBirthdayFileFormat());
        this.emailField.setText(chosenContact.getEmail());
        this.phoneNumberField.setText(String.valueOf(chosenContact.getPhoneNumber()));
        this.notesArea.setText(chosenContact.getNotes());
    }

    /**
     * This method returns true if all the fields are filled in. The notes text area is ignored
     * because that is allowed to be empty.
     */
    public boolean isFieldsFull() {
        return (!this.nameField.getText().isEmpty()) && (!this.addressField.getText().isEmpty())
                && (!this.birthdayField.getText().isEmpty()) && (!emailField.getText().isEmpty())
                && (!this.phoneNumberField.getText().isEmpty());
    }
    public void clearFields() {
        nameField.clear();
        addressField.clear();
        birthdayField.clear();
        emailField.clear();
        phoneNumberField.clear();
        notesArea.clear();
    }
    protected static boolean toggleOptionPanes(Boolean panesToggled, FlowPane rightPane, EmailStackPane emailStackPane, LargestBirthdayPane largestBirthdayPane,
                                   FindPhoneNumberPane findPhoneNumberPane, TransferContactPane transferContactPane) {
        if (!panesToggled) {
            rightPane.getChildren().add(emailStackPane);
            rightPane.getChildren().add(largestBirthdayPane);
            rightPane.getChildren().add(findPhoneNumberPane);
            rightPane.getChildren().add(transferContactPane);
            // The options have been toggled. Flip boolean value. Return true.
            return true;
        }
        else {
            rightPane.getChildren().removeAll(emailStackPane, largestBirthdayPane, findPhoneNumberPane, transferContactPane);
            // The options have been toggled. Flip boolean value. Return false.
            return false;
        }
    }
}
