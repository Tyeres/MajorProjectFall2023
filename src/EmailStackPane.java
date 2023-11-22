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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Stack;

public class EmailStackPane extends BorderPane {
    private EmailStackPane() {

    }

    /**
     * This here creates the stack on the right side. It adds email addresses to a text area and to a
     * stack. It is used to export the email addresses into a text file in the Downloads folder.
     */
    public EmailStackPane(TextField emailField) {

        this.setStyle("-fx-border-color: red");
        HBox buttonStackPane = new HBox(5);
        buttonStackPane.setPadding(new Insets(5, 5, 5, 5));
        Stack<String> stack = new Stack<>();
        TextArea stackDisplay = new TextArea();
        stackDisplay.setEditable(false);
        Button addToStackButton = new Button("Add Email");
        addToStackButton.setOnAction(e-> {
            // Add the Contact's email address to the stack only if a contact has been selected/created.
            if (!emailField.getText().equals(ContactPane.DEFAULT_MESSAGE)) {
                stack.push(emailField.getText());
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

                Text downloadsText = new Text("File added to Downloads!");
                BorderPane downloadsPane = new BorderPane();
                downloadsPane.setCenter(downloadsText);
                Scene downloadsScene = new Scene(downloadsPane, 300, 300);
                Stage downloadsStage = new Stage();
                downloadsStage.setScene(downloadsScene);
                downloadsStage.setTitle("Emails");
                downloadsStage.getIcons().add(new Image("Thumbnails/Phone Book Thumbnail 2.jpg"));
                downloadsStage.show();
            }
        });

        buttonStackPane.getChildren().add(new Text("Use this to export email\naddresses into a file."));
        // Add the button pane to the stack pane. This is done because we want the
        // buttons in a HBox (holds the buttons) all under the text area.
        buttonStackPane.getChildren().add(addToStackButton);
        buttonStackPane.getChildren().add(removeFromStackButton);
        buttonStackPane.getChildren().add(exportButton);
        this.setCenter(stackDisplay);
        this.setBottom(buttonStackPane);
    }
}
