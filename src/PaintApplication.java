import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class PaintApplication extends Application {

    private static final File CONTACT_NAMES_FOLDER = new File("./src/ContactSaves");

    public void start(Stage primaryStage) {


        ComboBox<String> contactComboBox = new ComboBox<>(getFileNames());

        HBox comboAndContactPane = new HBox();
        comboAndContactPane.getChildren().add(contactComboBox);
        comboAndContactPane.getChildren().add(new ContactPane());

        Scene scene = new Scene(comboAndContactPane, 700, 650);
        primaryStage.setScene(scene);
        primaryStage.show();

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
