package shortcut;

import database.Database;
import element.ShortcutFolder;
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
import javafx.scene.layout.HBox;
import singleton.SingletonColor;
import singleton.SingletonShortcut;

import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ShortcutFolderController {


    @FXML
    public AnchorPane anchorPaneViewAddFolder, viewShortcutFolder;

    @FXML
    ScrollPane scrollPaneViewFolder;

    @FXML
    public FlowPane showFolder;

    @FXML
    public Button buttonAddFolder, buttonDelFolder, buttonValidAddFolder;

    @FXML
    public ImageView picture;

    @FXML
    public Label label;

    @FXML
    public HBox hBoxShortcutFolderTitle;

    public ShortcutFolder shortcutFolderSelected;
    private boolean shortcutAddClicked = false;
    public boolean setInFirstPlace = false;


    public Database database;

    public ObservableList<ShortcutFolder> listShortcutFolder = FXCollections.observableArrayList();

    public static List<File> filesAdd;


    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutFolderController = this;
            setColor();
            this.listShortcutFolder.addListener(this::eventListenerShortcut);

            database = new Database();

            showPictureButton(buttonAddFolder, "picture/add.png");
            showPictureButton(buttonDelFolder, "picture/delete.png");

            initializeListOfShortCutFolder();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventListenerShortcut(ListChangeListener.Change<? extends ShortcutFolder> change) {
        while(change.next()) {
            if (change.wasAdded()) {
                final Node[] toFirstPlace = new Node[1];
                toFirstPlace[0] = null;
                if (setInFirstPlace && listShortcutFolder.size() != 1){
                    showFolder.getChildren().forEach(e -> {
                        if (e.equals(shortcutFolderSelected.view)){
                            toFirstPlace[0] = e;
                        }
                    });
                    toFirstPlace[0].toBack();
                } else {
                    this.showFolder.getChildren().add(listShortcutFolder.get(listShortcutFolder.size()-1).getView());
                }
            }
            if (change.wasRemoved()) {
                if (!listShortcutFolder.isEmpty()){
                    if (!setInFirstPlace) {
                        this.showFolder.getChildren().subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                } else {
                    this.showFolder.getChildren().clear();
                }
            }
        }
    }

    private void showPictureButton (Button btn, String str) {
        btn.setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getClassLoader().getResource(str)).toString())));
    }

    public void initializeListOfShortCutFolder () {
        showFolder.getChildren().clear();
        for (ShortcutFolder element: database.getListOfShortcutFolder()) {
            listShortcutFolder.add(element);
        }
    }

    public void resetColorOfItem () {
        listShortcutFolder.forEach(ShortcutFolder::resetColor);
    }

    private boolean verifyIfEntryAddShortcutIsNotEmpty () {
        return filesAdd.size() > 0;
    }

    public void onUserWantToAddFolder (ActionEvent event) throws IOException {
        if (verifyIfEntryAddShortcutIsNotEmpty()) {
            try {
                resetColorOfItem();
                shortcutAddClicked = false;
                if (listShortcutFolder.isEmpty()) {
                    database.addNewShortcutFolder(filesAdd.get(0).getAbsolutePath(), FileSystemView.getFileSystemView().getSystemDisplayName(filesAdd.get(0)), 1);
                } else {
                    database.addNewShortcutFolder(filesAdd.get(0).getAbsolutePath(), FileSystemView.getFileSystemView().getSystemDisplayName(filesAdd.get(0)), listShortcutFolder.get(listShortcutFolder.size() - 1).pos + 1);
                }

                listShortcutFolder.add(database.getLastShortcutFolderElement());
                showPictureButton(buttonAddFolder, "picture/add.png");
                shortcutAddClicked = false;

                anchorPaneViewAddFolder.setVisible(false);
                scrollPaneViewFolder.setVisible(true);
                SingletonShortcut.freezeApp = false;
            } catch (Exception exp) {
                buttonValidAddFolder.setVisible(false);
                label.setText("Glisser et d\u00e9poser le fichier");
                picture.setImage(null);
                anchorPaneViewAddFolder.setVisible(true);
                buttonDelFolder.setVisible(false);
                scrollPaneViewFolder.setVisible(false);
                shortcutAddClicked = true;
                showPictureButton(buttonAddFolder, "picture/cancel.png");
                System.out.println(exp.getMessage());
            }
        }
    }

    public void onUserClickToShowAddFolder( ActionEvent event) {
        if (!shortcutAddClicked) {
            SingletonShortcut.freezeApp = true;
            buttonValidAddFolder.setVisible(false);
            label.setText("Glisser et d\u00e9poser le fichier");
            picture.setImage(null);
            anchorPaneViewAddFolder.setVisible(true);
            buttonDelFolder.setVisible(false);
            scrollPaneViewFolder.setVisible(false);
            shortcutAddClicked = true;
            showPictureButton(buttonAddFolder, "picture/cancel.png");
        } else {
            SingletonShortcut.freezeApp = false;
            resetColorOfItem();
            anchorPaneViewAddFolder.setVisible(false);
            scrollPaneViewFolder.setVisible(true);
            shortcutAddClicked = false;
            showPictureButton(buttonAddFolder, "picture/add.png");
        }
    }

    public void onUserClickToDelShortcut (ActionEvent event) {
        Platform.runLater(() -> {
            if (shortcutFolderSelected != null) {
                database.deleteShortcutFolder(shortcutFolderSelected, listShortcutFolder);
                listShortcutFolder.removeIf(shortcutFolder -> shortcutFolder.equals(shortcutFolderSelected));
                shortcutFolderSelected = null;
            }
        });

        buttonDelFolder.setVisible(false);
    }

    public void onMouseClickedHead (MouseEvent event) {
        buttonDelFolder.setVisible(false);
        resetColorOfItem();
    }

    public void updateView (int start, int stop) {
        if (start < stop) {
            for (int i = start; i < listShortcutFolder.size(); ++i) {
                if (i != stop) {
                    listShortcutFolder.get(i).view.toFront();
                }
            }
        } else {
            for (int i = start; i >= 0; --i) {
                if (i != stop) {
                    listShortcutFolder.get(i).view.toBack();
                }
            }
        }
    }

    public void onDragDropped(DragEvent e) {
        buttonValidAddFolder.setVisible(true);
        filesAdd = e.getDragboard().getFiles();
        BufferedImage image = JIconExtract.getIconForFile(60,60,filesAdd.get(0));
        Platform.runLater(() -> {
            Image img = SwingFXUtils.toFXImage(image, null);
            label.setText(FileSystemView.getFileSystemView().getSystemDisplayName(filesAdd.get(0)));
            picture.setImage(img);
        });
    }

    public void onDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            if (event.getDragboard().getFiles().get(0).isDirectory()) {
                final boolean[] dragPossible = {true};
                listShortcutFolder.forEach(e -> {
                    if (e.text.equals(event.getDragboard().getFiles().get(0).getName())){
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

    public void setColor () {
        resetColorOfItem();
        hBoxShortcutFolderTitle.setStyle("-fx-background-color: "+ SingletonColor.instance.getBackgroundTitle() + "; -fx-border-color: "+ SingletonColor.instance.getLine() + "; -fx-border-width: 0 0 3 0;");
        buttonAddFolder.setStyle("-fx-background-radius: 20; -fx-background-color: "+ SingletonColor.instance.getButton() + ";");
        buttonDelFolder.setStyle("-fx-background-radius: 20; -fx-background-color: "+ SingletonColor.instance.getButton() + ";");

        scrollPaneViewFolder.setStyle("-fx-background-color: transparent; -fx-border-color: "+ SingletonColor.instance.getBackground() + "; -fx-border-width: 0 0 3 0;");
        showFolder.setStyle("-fx-background-color: "+ SingletonColor.instance.getBackground() + "; -fx-hgap: 20; -fx-vgap: 12;");

        buttonValidAddFolder.setStyle("-fx-background-color: " + SingletonColor.instance.getButton() +";");
    }
}
