package element;

import iconExtract.JIconExtract;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import singleton.SingletonShortcut;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShortcutFileFolder {

    public String url,text;
    public int id, pos;
    public AnchorPane view;

    public ShortcutFileFolder(int id, int pos, String url, String text) {
        this.id = id;
        this.pos = pos;
        this.url = url;
        this.text = text;
    }

    public void resetColor () {
        view.setStyle("-fx-background-color: #2c2f33;");
    }

    public AnchorPane getView() {
        try {
            view = new FXMLLoader(getClass().getResource("/shortcutElement.fxml")).load();

            view.setOnMouseClicked(event -> {
                SingletonShortcut.shortcutFolderController.resetColorOfItem();

                if (event.getButton().equals(MouseButton.SECONDARY)){
                    SingletonShortcut.shortcutFolderController.shortcutFolderSelected = this;
                    view.setStyle("-fx-background-color: #23272a;");
                    SingletonShortcut.shortcutFolderController.buttonDelFolder.setVisible(true);
                }

                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        SingletonShortcut.shortcutFolderController.database.setPositionOfShortcutFolder(this, SingletonShortcut.shortcutFolderController.listShortcutFolder);

                        SingletonShortcut.shortcutFolderController.setToFirstPlace(this);


                        SingletonShortcut.shortcutFolderController.shortcutFolderSelected = null;
                        SingletonShortcut.shortcutFolderController.buttonDelFolder.setVisible(false);

                        Platform.runLater(() -> {
                            try {
                                Desktop.getDesktop().open(new File("shortcut\\folder\\" + text));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            setValue();

            return view;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void setValue () {
        BufferedImage image = JIconExtract.getIconForFile(128,128,url);
        Platform.runLater(() -> {
            Image img = SwingFXUtils.toFXImage(image, null);

            Label label = (Label) view.lookup("#label");
            label.setText(text);

            ImageView imgView = (ImageView) view.lookup("#picture");
            imgView.setImage(img);
        });
    }
}
