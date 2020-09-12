package shortcut;

import database.Database;
import element.ShortcutApp;
import iconExtract.JIconExtract;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import singleton.SingletonShortcut;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ShortcutAppController {

    @FXML
    AnchorPane anchorPaneViewAddApp;

    @FXML
    ScrollPane scrollPaneViewApp;

    @FXML
    public FlowPane showApp;

    @FXML
    public Button buttonAddApp, buttonDelApp, buttonValidAddApp;

    @FXML
    public ImageView picture;

    @FXML
    public Label label;

    public ShortcutApp shortcutAppSelected;
    private boolean shortcutAddClicked = false;
    public boolean setInFirstPlace = false;


    public Database database;

    public ObservableList<ShortcutApp> listShortcutApp = FXCollections.observableArrayList();

    public static List<File> filesAdd;

    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutAppController = this;

            this.listShortcutApp.addListener(this::eventListenerShortcut);

            database = new Database();

            showPictureButton(buttonAddApp, "picture/add.png");
            showPictureButton(buttonDelApp, "picture/delete.png");

            initializeListOfShortCutApp();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventListenerShortcut(ListChangeListener.Change<? extends ShortcutApp> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                final Node[] toFirstPlace = new Node[1];
                toFirstPlace[0] = null;
                if (setInFirstPlace){
                    showApp.getChildren().forEach(e -> {
                        if (e.equals(shortcutAppSelected.view)){
                            toFirstPlace[0] = e;
                        }
                    });
                    toFirstPlace[0].toBack();
                } else {
                    this.showApp.getChildren().add(listShortcutApp.get(listShortcutApp.size()-1).getView());
                }
            }
            if (change.wasRemoved()) {
                if (!listShortcutApp.isEmpty()){
                    if (!setInFirstPlace) {
                        this.showApp.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                } else {
                    this.showApp.getChildren().clear();
                }
            }
        }
    }

    private void showPictureButton (Button btn, String str) {
        btn.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource(str)).toString())));
    }

    public void initializeListOfShortCutApp () {
        showApp.getChildren().clear();
        for (ShortcutApp element: database.getListOfShortcutApp()) {
            listShortcutApp.add(element);
        }
    }

    public void resetColorOfItem () {
        listShortcutApp.forEach(ShortcutApp::resetColor);
    }

    private boolean verifyIfEntryAddShortcutIsNotEmpty () {
        return filesAdd.size() > 0;
    }

    public void onUserWantToAddApp (ActionEvent event) {
        if (verifyIfEntryAddShortcutIsNotEmpty()) {
            Path source = Paths.get(filesAdd.get(0).getAbsolutePath());
            Path target = Paths.get("shortcut\\application\\" + filesAdd.get(0).getName());

            try {
                Files.copy(source, target);
                resetColorOfItem();
                shortcutAddClicked = false;
                final int lastPeriodPos = filesAdd.get(0).getName().lastIndexOf('.');
                String name = filesAdd.get(0).getName().substring(0, lastPeriodPos);

                if (listShortcutApp.isEmpty()) {
                    database.addNewShortcutApp(filesAdd.get(0).getAbsolutePath(), name, 1);
                } else {
                    database.addNewShortcutApp(filesAdd.get(0).getAbsolutePath(), name, listShortcutApp.get(listShortcutApp.size() - 1).pos + 1);
                }

                listShortcutApp.add(database.getLastShortcutApp());

                showPictureButton(buttonAddApp, "picture/add.png");
                shortcutAddClicked = false;

                anchorPaneViewAddApp.setVisible(false);
                scrollPaneViewApp.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onUserClickToShowAddApp ( ActionEvent event) {
        if (!shortcutAddClicked) {
            buttonValidAddApp.setVisible(false);
            label.setText("Glisser et d\u00e9poser l'application");
            picture.setImage(null);
            anchorPaneViewAddApp.setVisible(true);
            buttonDelApp.setVisible(false);
            scrollPaneViewApp.setVisible(false);
            shortcutAddClicked = true;
            showPictureButton(buttonAddApp, "picture/cancel.png");
        } else {
            resetColorOfItem();
            anchorPaneViewAddApp.setVisible(false);
            scrollPaneViewApp.setVisible(true);
            shortcutAddClicked = false;
            showPictureButton(buttonAddApp, "picture/add.png");
        }
    }

    public void onUserClickToDelShortcut (ActionEvent event) {
        Platform.runLater(() -> {
            if (shortcutAppSelected != null) {
                try {
                    database.deleteShortcutApp(shortcutAppSelected);
                    listShortcutApp.removeIf(shortcutApp -> shortcutApp.equals(shortcutAppSelected));
                    String extension = shortcutAppSelected.url.substring(shortcutAppSelected.url.lastIndexOf(".") + 1);
                    Files.delete(Paths.get("shortcut\\application\\" + shortcutAppSelected.text + "." + extension));
                    shortcutAppSelected = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonDelApp.setVisible(false);
    }

    public void onMouseClickedHead (MouseEvent event) {
        buttonDelApp.setVisible(false);
        resetColorOfItem();
    }

    public void setToFirstPlace (ShortcutApp shortcutApp) {
        shortcutAppSelected = shortcutApp;
        setInFirstPlace = true;
        listShortcutApp.remove(shortcutApp);
        listShortcutApp.add(0,shortcutApp);
        setInFirstPlace = false;
    }

    public void onDragDropped(DragEvent e) throws FileNotFoundException {
        buttonValidAddApp.setVisible(true);
        filesAdd = e.getDragboard().getFiles();
        BufferedImage image = JIconExtract.getIconForFile(60,60,filesAdd.get(0));
        Platform.runLater(() -> {
            Image img = SwingFXUtils.toFXImage(image, null);
            label.setText(filesAdd.get(0).getName());
            picture.setImage(img);
        });

    }

    public void onDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            final boolean[] dragPossible = {true};
            if (!event.getDragboard().getFiles().get(0).isDirectory()){
                listShortcutApp.forEach(e -> {
                    final int lastPeriodPos = event.getDragboard().getFiles().get(0).getName().lastIndexOf('.');
                    String name = event.getDragboard().getFiles().get(0).getName().substring(0, lastPeriodPos);
                    if (e.text.equals(name)){
                        dragPossible[0] = false;
                    }
                });
                if (dragPossible[0]) {
                    event.acceptTransferModes(TransferMode.ANY);
                }
            }
        }
        event.consume();
    }
}
