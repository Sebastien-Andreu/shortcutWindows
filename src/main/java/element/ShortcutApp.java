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
import singleton.SingletonShortcut;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;

public class ShortcutApp {

    public String url,text;
    public int id, pos;
    public AnchorPane view;

    private static boolean nodeIsInDrag = false;
    private static ShortcutApp viewWantToMove;


    public ShortcutApp(int id, int pos, String url, String text) {
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
                SingletonShortcut.shortcutAppController.resetColorOfItem();
                if (event.getButton().equals(MouseButton.SECONDARY)){
                    SingletonShortcut.shortcutAppController.shortcutAppSelected = this;
                    view.setStyle("-fx-background-color: #23272a;");
                    SingletonShortcut.shortcutAppController.buttonDelApp.setVisible(true);

                    view.setOnDragDetected( e -> {
                        nodeIsInDrag = true;
                        viewWantToMove = this;
                    });
                }
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    try {
                        SingletonShortcut.shortcutAppController.shortcutAppSelected = null;
                        SingletonShortcut.shortcutAppController.buttonDelApp.setVisible(false);

                        Node source = (Node) event.getSource();
                        Stage stage = (Stage) source.getScene().getWindow();
                        stage.setHeight(0.05);

                        Platform.runLater(() -> {
                            try {
                                url =  url.replace("\\", "\\\\");
                                String extension = url.substring(url.lastIndexOf(".") + 1);
                                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", url);
                                pb.start();
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
                    SingletonShortcut.shortcutAppController.updateView(this.pos, viewWantToMove.pos);
                    SingletonShortcut.shortcutAppController.database.setPositionOfShortcutApp(viewWantToMove, this, SingletonShortcut.shortcutAppController.listShortcutApp);

                    SingletonShortcut.shortcutAppController.listShortcutApp.sort(Comparator.comparingInt((ShortcutApp s) -> s.pos));

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

    public void setValue (String text) throws FileNotFoundException {
        Platform.runLater(() -> {
            BufferedImage image = JIconExtract.getIconForFile(40,40,url);
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
