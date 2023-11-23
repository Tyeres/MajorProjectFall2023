import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;

public class ContactReceiver {
    protected static final int PORT = 8000;

    /**
     * This method receives and saves a contact as a file.
     * Since this method uses the accept() method, make sure to use
     * the receiveContact method in a Thread to prevent the JavaFX UI
     * from freezing up.
     */
    public static void receiveContact(Button button, ListView<String> listView) {
        new Thread(()->{
            try {
                button.setStyle("-fx-background-color: red");
                button.setTextFill(Paint.valueOf("yellow"));
                ObjectInputStream input = new ObjectInputStream(
                        new ServerSocket(PORT).accept().getInputStream());
                Contact contact = (Contact) input.readObject();
                contact.save();
                button.setStyle(null);
                button.setTextFill(Color.BLACK);

                Platform.runLater(()->{
                    listView.getItems().add(contact.getName());
                    Text downloadsText = new Text("Downloaded a Contact!");
                    BorderPane downloadsPane = new BorderPane();
                    downloadsPane.setCenter(downloadsText);
                    Scene downloadsScene = new Scene(downloadsPane, 300, 300);
                    Stage downloadsStage = new Stage();
                    downloadsStage.setScene(downloadsScene);
                    downloadsStage.setTitle("Emails");
                    downloadsStage.getIcons().add(new Image("Thumbnails/Phone Book Thumbnail 2.jpg"));
                    downloadsStage.show();
                });
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("ContactReceiver.java");
                /* Will produce an error if more than one thread is running. This is great, because
                 it makes sure only one thread runs at a time. I believe the error comes from
                 attempting to create the ServerSocket object from separate threads (I am too lazy to verify).
                */
            }
        }).start();
    }
}