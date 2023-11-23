import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class TransferContactPane extends javafx.scene.layout.BorderPane {
    private TransferContactPane() {

    }
    public TransferContactPane(ListView<String> listView) {
            this.setPadding(new Insets(5, 5, 5, 5));
            this.setStyle("-fx-border-color: green");

            VBox sendPane = new VBox(5);
            sendPane.setPadding(new Insets(5, 5, 5, 5));
            sendPane.setAlignment(Pos.CENTER);
            sendPane.setStyle("-fx-border-color: blue");
            TextField IPField = new TextField();
            Label IPLabel = new Label("Input IP Here", IPField);
            IPLabel.setContentDisplay(ContentDisplay.BOTTOM);
            Button sendContactBT = new Button("Send Contact");

            sendPane.getChildren().add(IPLabel);
            sendPane.getChildren().add(sendContactBT);

            sendContactBT.setOnAction(e->{
                if (!IPField.getText().isEmpty()) {
                    ContactSender contactSender = new ContactSender(IPField.getText());
                    try {
                        Contact contact = PaintApplication.getSelectedContact(listView.getSelectionModel().getSelectedItem());
                        contactSender.sendContact(contact);
                    } catch (Exception ex) {
                        // Do nothing. This means that no contact has yet been selected.
                    }

                }
            });

            this.setLeft(sendPane);

            VBox receivePane = new VBox(5);
            receivePane.setAlignment(Pos.CENTER);
            receivePane.setPadding(new Insets(5, 5, 5, 5));

            receivePane.setStyle("-fx-border-color: yellow");
            Button receiveContactButton = new Button("Receive Contact");

            receivePane.getChildren().add(receiveContactButton);

            receiveContactButton.setOnAction(e->ContactReceiver.receiveContact(receiveContactButton, listView));

            this.setRight(receivePane);
    }
}
