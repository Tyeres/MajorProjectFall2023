import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class PaintApplication extends Application {

    private static final File CONTACT_NAMES_FOLDER = new File("./src/ContactSaves");

    private final ContactPane contactPane = new ContactPane(40);
    private final ObservableList<Node> listOfNodes = contactPane.getChildren();

    public void start(Stage primaryStage) {

        HBox comboAndContactPane = new HBox(5);
        comboAndContactPane.setPadding(new Insets(10, 3, 0, 3));
        // This contains the list of names
        ComboBox<String> contactComboBox = new ComboBox<>(getFileNames());
        comboAndContactPane.getChildren().add(contactComboBox);
        comboAndContactPane.getChildren().add(contactPane);

        Scene scene = new Scene(comboAndContactPane, 800, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Phone Book");
//        primaryStage.setFullScreenExitHint("Press Esc to exit full screen!\n(You also smell weird)");
//        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
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
