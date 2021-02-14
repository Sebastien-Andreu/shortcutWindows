package element;

import iconExtract.JIconExtract;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import singleton.SingletonColor;
import singleton.SingletonShortcut;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

public class ShortcutFolder {

    public String url,text;
    public int id, pos;
    public AnchorPane view;

    private static boolean nodeIsInDrag = false;
    private static ShortcutFolder viewWantToMove;


    public ShortcutFolder(int id, int pos, String url, String text) {
        this.id = id;
        this.pos = pos;
        this.url = url;
        this.text = text;
    }

    public void resetColor () {
        view.setStyle("-fx-background-color: "+ SingletonColor.singletonColor.getBackground() + ";");
    }

    public AnchorPane getView() {
        try {
            view = new FXMLLoader(getClass().getResource("/shortcutElement.fxml")).load();
            resetColor();

            view.setOnMouseClicked(event -> {
                SingletonShortcut.shortcutFolderController.resetColorOfItem();

                if (event.getButton().equals(MouseButton.SECONDARY)){
                    SingletonShortcut.shortcutFolderController.shortcutFolderSelected = this;
                    view.setStyle("-fx-background-color: "+ SingletonColor.singletonColor.getBackgroundTitle() + ";");
                    SingletonShortcut.shortcutFolderController.buttonDelFolder.setVisible(true);

                    view.setOnDragDetected( e -> {
                        nodeIsInDrag = true;
                        viewWantToMove = this;
                    });
                }

                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        SingletonShortcut.shortcutFolderController.shortcutFolderSelected = null;
                        SingletonShortcut.shortcutFolderController.buttonDelFolder.setVisible(false);

                        Node source = (Node) event.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
                        stage.setHeight(0.05);

                        Platform.runLater(() -> {
                            try {
                                Desktop.getDesktop().open(new File(url));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            view.setOnMouseEntered( e -> {
                if (nodeIsInDrag) {
                    SingletonShortcut.shortcutFolderController.updateView(this.pos, viewWantToMove.pos);
                    SingletonShortcut.shortcutFolderController.database.setPositionOfShortcutFolder(viewWantToMove, this, SingletonShortcut.shortcutFolderController.listShortcutFolder);

                    SingletonShortcut.shortcutFolderController.listShortcutFolder.sort(Comparator.comparingInt((ShortcutFolder s) -> s.pos));

                    nodeIsInDrag = false;
                    viewWantToMove = null;
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
            if (image != null) {
                Image img = SwingFXUtils.toFXImage(image, null);

                Label label = (Label) view.lookup("#label");
                label.setText(text);

                ImageView imgView = (ImageView) view.lookup("#picture");
                imgView.setImage(img);
            }
        });
    }
}
