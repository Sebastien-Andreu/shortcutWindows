package element;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import singleton.SingletonShortcut;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class ShortcutElementMostUsed extends ShortcutElement{

    public String url,text;
    public AnchorPane view;


    public ShortcutElementMostUsed(String text, String url) {
        super(0,0,text, url);
        this.text = text;
        this.url = url;
    }

    @Override
    public void resetColor () {
        view.setStyle("-fx-background-color: #2c2f33;");
    }

    @Override
    public AnchorPane getView() {
        try {
            view = (AnchorPane) new FXMLLoader(getClass().getResource("/shortcutElement.fxml")).load();

            view.setOnMouseClicked(event -> {
                SingletonShortcut.shortcutMostUsed.resetColorOfItem();
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        for (ShortcutElement element : SingletonShortcut.shortcutInternetController.listShortcut) {
                            if (element.url.equals(url) && element.text.equals(text)) {
                                SingletonShortcut.shortcutMostUsed.refreshView = true;
                                SingletonShortcut.shortcutInternetController.database.setPositionOfShortcut(element, SingletonShortcut.shortcutInternetController.listShortcut);
                                Platform.runLater(() -> {SingletonShortcut.shortcutInternetController.setToFirstPlace(element);});
                                SingletonShortcut.shortcutMostUsed.showListOfShortcutInternetMostUsed();
                                Platform.runLater(() -> {
                                    try {
                                        Desktop.getDesktop().browse(new URL(this.url).toURI());
                                    } catch (IOException | URISyntaxException e) {
                                        e.printStackTrace();
                                    }
                                });
                                break;
                            }
                        }
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

    @Override
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
