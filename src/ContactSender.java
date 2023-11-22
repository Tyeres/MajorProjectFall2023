import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ObjectOutputStream;
import java.net.*;

public class ContactSender {
    private String IP;

    private ContactSender() {
        // Private because I do not want this to be used.
    }

    public ContactSender(String IP) {
        this.IP = IP;
    }

    public void sendContact(Contact contact) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(
                    new Socket(IP, ContactReceiver.PORT).getOutputStream());
            output.writeObject(contact);
        } catch (java.io.IOException e) {
            getErrorFormattingStage();
        }
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    protected static void getErrorFormattingStage() {
        Text error = new Text("Error. The IP is invalid or there is no user to receive data.");
        BorderPane errorPane = new BorderPane();
        errorPane.setCenter(error);
        Scene errorScene = new Scene(errorPane, 300, 300);
        Stage errorStage = new Stage();
        errorStage.setScene(errorScene);
        errorStage.setTitle("Error");
        errorStage.getIcons().add(new Image("Thumbnails/Phone Book Thumbnail 2.jpg"));
        errorStage.show();
    }
}