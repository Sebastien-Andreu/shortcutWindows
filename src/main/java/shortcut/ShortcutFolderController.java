package shortcut;

import database.Database;
import element.ShortcutFileFolder;
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
import org.springframework.util.FileSystemUtils;
import singleton.SingletonShortcut;
import utils.CopyDirectory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ShortcutFolderController {


    @FXML
    AnchorPane anchorPaneViewAddFolder; // drag and drop

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

    public ShortcutFileFolder shortcutFolderSelected;
    private boolean shortcutAddClicked = false;
    public boolean setInFirstPlace = false;


    public Database database;

    public ObservableList<ShortcutFileFolder> listShortcutFolder = FXCollections.observableArrayList();

    public static List<File> filesAdd;


    @FXML
    public void initialize () {
        try {
            SingletonShortcut.shortcutFolderController = this;

            this.listShortcutFolder.addListener(this::eventListenerShortcut);

            database = new Database();

            showPictureButton(buttonAddFolder, "picture/add.png");
            showPictureButton(buttonDelFolder, "picture/delete.png");

            initializeListOfShortCutFolder();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventListenerShortcut(ListChangeListener.Change<? extends ShortcutFileFolder> change) {
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
        for (ShortcutFileFolder element: database.getListOfShortcutFolder()) {
            listShortcutFolder.add(element);
        }
    }

    public void resetColorOfItem () {
        listShortcutFolder.forEach(ShortcutFileFolder::resetColor);
    }

    private boolean verifyIfEntryAddShortcutIsNotEmpty () {
        return filesAdd.size() > 0;
    }

    public void onUserWantToAddFolder (ActionEvent event) throws IOException {
        if (verifyIfEntryAddShortcutIsNotEmpty()) {
            Path source  = Paths.get(filesAdd.get(0).getAbsolutePath().toString());
            Path target = Paths.get("shortcut\\folder\\" + filesAdd.get(0).getName());

            new Thread(() -> {
                try {
                    copyDirectory(source, target);
                    resetColorOfItem();
                    shortcutAddClicked = false;
                    if (listShortcutFolder.isEmpty()) {
                        database.addNewShortcutFolder(filesAdd.get(0).getAbsolutePath(), filesAdd.get(0).getName(), 1);
                    } else {
                        database.addNewShortcutFolder(filesAdd.get(0).getAbsolutePath(), filesAdd.get(0).getName(), listShortcutFolder.get(listShortcutFolder.size() - 1).pos + 1);
                    }

                    listShortcutFolder.add(database.getLastShortcutFolderElement());
                    showPictureButton(buttonAddFolder, "picture/add.png");
                    shortcutAddClicked = false;

                    anchorPaneViewAddFolder.setVisible(false);
                    scrollPaneViewFolder.setVisible(true);
                } catch (Exception exp) {
                    FileSystemUtils.deleteRecursively(new File("shortcut\\folder\\" + filesAdd.get(0).getName()));
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
            }).start();
        }
    }


    public static void copyDirectory(Path source, Path target) throws IOException {
        CopyDirectory copy = new CopyDirectory(source, target);
        Files.walkFileTree(source, copy);

    }

    public void onUserClickToShowAddFolder( ActionEvent event) {
        if (!shortcutAddClicked) {
            buttonValidAddFolder.setVisible(false);
            label.setText("Glisser et d\u00e9poser le fichier");
            picture.setImage(null);
            anchorPaneViewAddFolder.setVisible(true);
            buttonDelFolder.setVisible(false);
            scrollPaneViewFolder.setVisible(false);
            shortcutAddClicked = true;
            showPictureButton(buttonAddFolder, "picture/cancel.png");
        } else {
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
                try {
                    database.deleteShortcutFolder(shortcutFolderSelected);
                    listShortcutFolder.removeIf(shortcutFileFolder -> shortcutFileFolder.equals(shortcutFolderSelected));
                    Files.delete(Paths.get("shortcut\\folder\\" + shortcutFolderSelected.text));
                    shortcutFolderSelected = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonDelFolder.setVisible(false);
    }

    public void onMouseClickedHead (MouseEvent event) {
        buttonDelFolder.setVisible(false);
        resetColorOfItem();
    }

    public void setToFirstPlace (ShortcutFileFolder shortcutFile) {
        shortcutFolderSelected = shortcutFile;
        setInFirstPlace = true;
        listShortcutFolder.remove(shortcutFile);
        listShortcutFolder.add(0,shortcutFile);
        setInFirstPlace = false;
    }

    public void onDragDropped(DragEvent e) {
        buttonValidAddFolder.setVisible(true);
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
}
