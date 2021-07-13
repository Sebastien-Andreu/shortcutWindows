package element;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import singleton.SingletonColor;
import singleton.SingletonShortcut;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;

public class ShortcutElement {

    public String url,text;
    public int id, pos;
    public AnchorPane view;

    private static boolean nodeIsInDrag = false;
    private static ShortcutElement viewWantToMove;


    public ShortcutElement(int id, int pos, String text, String url) {
        this.id = id;
        this.text = text;
        this.url = url;
        this.pos = pos;
    }

    public void resetColor () {
        view.setStyle("-fx-background-color: "+ SingletonColor.instance.getBackground() + ";");
    }

    public AnchorPane getView() {
        try {
            view = (AnchorPane) new FXMLLoader(getClass().getResource("/shortcutElement.fxml")).load();
            resetColor();

            view.setOnMouseClicked(event -> {
                SingletonShortcut.shortcutInternetController.resetColorOfItem();
                if (event.getButton().equals(MouseButton.SECONDARY)){
                    SingletonShortcut.shortcutInternetController.shortcutElementSelected = this;
                    view.setStyle("-fx-background-color: "+ SingletonColor.instance.getBackgroundTitle() + ";");
                    SingletonShortcut.shortcutInternetController.buttonDelShortcut.setVisible(true);
                    SingletonShortcut.shortcutInternetController.buttonEditShortcut.setVisible(true);

                    view.setOnDragDetected( e -> {
                        nodeIsInDrag = true;
                        viewWantToMove = this;
                    });
                }
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    try {

                        Platform.runLater(() -> {
                            try {
                                Desktop.getDesktop().browse(new URL (this.url).toURI());
                            } catch (IOException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                        });


                        SingletonShortcut.shortcutInternetController.shortcutElementSelected = null;
                        SingletonShortcut.shortcutInternetController.buttonDelShortcut.setVisible(false);
                        SingletonShortcut.shortcutInternetController.buttonEditShortcut.setVisible(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            view.setOnMouseEntered( e -> {
                if (nodeIsInDrag) {
                    SingletonShortcut.shortcutInternetController.updateView(this.pos, viewWantToMove.pos);
                    SingletonShortcut.shortcutInternetController.database.setPositionOfShortcut(viewWantToMove, this, SingletonShortcut.shortcutInternetController.listShortcut);

                    SingletonShortcut.shortcutInternetController.listShortcut.sort(Comparator.comparingInt((ShortcutElement s) -> s.pos));

                    SingletonShortcut.shortcutMostUsed.refreshView = true;
                    SingletonShortcut.shortcutMostUsed.listShortcutInternet.clear();

                    nodeIsInDrag = false;
                    viewWantToMove = null;
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
        if (text.toUpperCase().equals("GITHUB")) {
            image = new Image("https://github.com/fluidicon.png");
        } else {
            image = new Image("https://www.google.com/s2/favicons?sz=64&domain_url=" + url);

        }

        ImageView imgView = (ImageView) view.lookup("#picture");
        imgView.setImage(image);
    }

}
