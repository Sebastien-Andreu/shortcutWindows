package element;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import singleton.SingletonShortcut;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class ShortcutElement {

    public String url,text;
    public int id, pos;
    public AnchorPane view;


    public ShortcutElement(int id, int pos, String text, String url) {
        this.id = id;
        this.text = text;
        this.url = url;
        this.pos = pos;
    }

    public void resetColor () {
        view.setStyle("-fx-background-color: #2c2f33;");
    }

    public AnchorPane getView() {
        try {
            view = (AnchorPane) new FXMLLoader(getClass().getResource("/shortcutElement.fxml")).load();

            view.setOnMouseClicked(event -> {
                SingletonShortcut.shortcutInternetController.resetColorOfItem();
                if (event.getButton().equals(MouseButton.SECONDARY)){
                    SingletonShortcut.shortcutInternetController.shortcutElementSelected = this;
                    view.setStyle("-fx-background-color: #23272a;");
                    SingletonShortcut.shortcutInternetController.buttonDelShortcut.setVisible(true);
                    SingletonShortcut.shortcutInternetController.buttonEditShortcut.setVisible(true);
                }
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        SingletonShortcut.shortcutInternetController.database.setPositionOfShortcut(this, SingletonShortcut.shortcutInternetController.listShortcut);
                        SingletonShortcut.shortcutInternetController.setToFirstPlace(this);
                        SingletonShortcut.shortcutMostUsed.showListOfShortcutInternetMostUsed();
                        SingletonShortcut.shortcutInternetController.shortcutElementSelected = null;
                        SingletonShortcut.shortcutInternetController.buttonDelShortcut.setVisible(false);
                        SingletonShortcut.shortcutInternetController.buttonEditShortcut.setVisible(false);

                        Node source = (Node) event.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
                        stage.setHeight(0.05);

                        Platform.runLater(() -> {
                            try {
                                Desktop.getDesktop().browse(new URL (this.url).toURI());
                            } catch (IOException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            setValue(text);

            return view;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void setValue (String text) {
        Label label = (Label) view.lookup("#label");
        label.setText(text);

        Image image;
        if (text.equals("Github")) {
            image = new Image("https://github.com/fluidicon.png");
        } else {
            image = new Image("https://www.google.com/s2/favicons?sz=64&domain_url=" + url);

        }

        ImageView imgView = (ImageView) view.lookup("#picture");
        imgView.setImage(image);
    }

}
