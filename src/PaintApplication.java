import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.*;
import java.util.GregorianCalendar;

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
        contactComboBox.setOnAction(e->{
            //contactComboBox.getItems().add("Ham burger");

            String contactName = contactComboBox.getValue();
            // Find the Contact Object based on the chosen name
            Contact chosenContact;

            // This code is important. When a contact is deleted, the program runs into errors
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
        });

        // Edit Button.
        Button edit = new Button("Edit Contact");
        // Set text to black by default
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


        // Add nodes to pane
        mainPane.getChildren().add(contactComboBox);
        mainPane.getChildren().add(contactPane);

        FlowPane buttonsPane = new FlowPane();
        buttonsPane.setHgap(5);
        buttonsPane.setVgap(5);
        buttonsPane.getChildren().add(edit);
        buttonsPane.getChildren().add(saveContact);
        buttonsPane.getChildren().add(deleteContact);
        mainPane.getChildren().add(buttonsPane);



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



}
